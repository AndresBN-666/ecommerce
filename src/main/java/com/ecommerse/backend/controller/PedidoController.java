package com.ecommerse.backend.controller;

import com.ecommerse.backend.dto.Pedido.PedidoDTO;
import com.ecommerse.backend.model.enums.EstadoPedido;
import com.ecommerse.backend.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @Operation(
            summary = "Finalizar compra",
            description = "Crea un pedido a partir del carrito activo del usuario autenticado y lo guarda en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado o vacío")
    })
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    @PostMapping("/finalizar")
    public ResponseEntity<PedidoDTO> finalizarCompra(){
        PedidoDTO pedido = pedidoService.finalizarCompra();
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @Operation(
            summary = "Listar mis pedidos",
            description = "Devuelve todos los pedidos realizados por el usuario autenticado"
    )
    @ApiResponse(responseCode = "200", description = "Listado de pedidos exitoso")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoDTO>> listarPedidos(){
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @Operation(
            summary = "Obtener pedido por ID",
            description = "Devuelve un pedido específico si pertenece al usuario autenticado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado al pedido"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
            })
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPedidoPorId(@PathVariable Long id){
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    @Operation(
            summary = "Obtener pedido por ID",
            description = "Devuelve un pedido específico si el usuario tiene el rol ADMIN"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado al pedido"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<PedidoDTO> adminObtenerPedidoPorId(@PathVariable Long id){
        return ResponseEntity.ok(pedidoService.adminObtenerPedidoPorId(id));
    }

    @Operation(
            summary = "Obtener listado completo de pedidos",
            description = "Devuelve todos los pedidos si el usuario tiene el rol ADMIN"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<PedidoDTO>> listarTodosLosPedidosRolAdmin(){
        return ResponseEntity.ok(pedidoService.listarTodosLosPedidosRolAdmin());
    }


    @Operation(
            summary = "Actualiza el estado del pedido",
            description = "Actualiza el estado del pedido segun se requiera"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido con estado modificado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<PedidoDTO> modificarEstado(
            @PathVariable Long id,
            @RequestBody EstadoPedido estadoPedido){
        return ResponseEntity.ok(pedidoService.actualizarEstado(id,estadoPedido));

    }

    @Operation(
            summary = "Buscar pedidos por estado",
            description = "Devuelve todos los pedidos segun el estado que se requiera"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/estado/{estado}")
    public ResponseEntity<List<PedidoDTO>> buscarPorEstado(@PathVariable EstadoPedido estado){
        return ResponseEntity.ok(pedidoService.buscarPorEstado(estado));
    }

    @Operation(
            summary = "Obtener estados",
            description = "Devuelve todos los estados disponibles"
    )
    @ApiResponse(responseCode = "200", description = "Estados encontrados")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/estados-disponibles")
    public ResponseEntity<EstadoPedido[]> listarEstados(){
        return ResponseEntity.ok(pedidoService.listarEstados());
    }



    @Operation(
            summary = "Obtener pedidos por estado",
            description = "Devuelve lista de pedidos dependiendo del estado requerido"
    )
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/cliente/estado/{estado}")
    public ResponseEntity<List<PedidoDTO>> listarPorEstadoyUsuario(
            @PathVariable EstadoPedido estado){
        return ResponseEntity.ok(pedidoService.buscarMisPedidosPorEstado(estado));
    }

/*    @GetMapping("/pedidoId/{id}")
    public ResponseEntity<PedidoDTO> getPedidoById(@PathVariable("id") Long externalReference) {
        PedidoDTO pedido = pedidoService.getPedidoPorExternalReference(externalReference);
        return ResponseEntity.ok(pedido);
    }*/
}
