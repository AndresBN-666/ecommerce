package com.ecommerse.backend.controller;

import com.ecommerse.backend.dto.carrito.ActualizarItemDTO;
import com.ecommerse.backend.dto.carrito.AgregarItemDTO;
import com.ecommerse.backend.dto.carrito.CarritoDTO;
import com.ecommerse.backend.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @Operation(
            summary = "Agregar producto al carrito",
            description = "Agrega un producto al carrito activo del usuario autenticado. Si no tiene uno, se crea automáticamente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto agregado correctamente al carrito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado o producto inexistente")
    })
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    @PostMapping
    public ResponseEntity<CarritoDTO> agregarProductoAlCarrito(@RequestBody AgregarItemDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(carritoService.agregarProductoAlCarrito(dto));
    }


    @Operation(
            summary = "Obtener carrito actual",
            description = "Retorna el carrito activo del usuario autenticado, incluyendo los productos y el importe total."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No existe un carrito activo para el usuario actual")
    })
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    @GetMapping
    public ResponseEntity<CarritoDTO> obtenerCarrito(){
        return ResponseEntity.ok(carritoService.obtenerCarrito());
    }


    @Operation(
            summary = "Eliminar producto del carrito",
            description = "Elimina un producto del carrito activo del usuario autenticado y retorna el carrito actualizado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente del carrito"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito o carrito no activo")
    })
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    @DeleteMapping("/{idProducto}")
    public ResponseEntity<CarritoDTO> eliminarItemDelCarrito(@PathVariable Long idProducto){
        return ResponseEntity.ok(carritoService.eliminarItemCarrito(idProducto));
    }

    @Operation(
            summary = "Actualizar cantidad de un producto en el carrito",
            description = "Modifica la cantidad de un producto específico en el carrito activo del usuario autenticado. Si la cantidad es 0, se elimina el producto del carrito."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito o carrito inactivo")
    })
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    @PutMapping("/item")
    public ResponseEntity<CarritoDTO> actualizarCantidad(@RequestBody ActualizarItemDTO dto){
        return ResponseEntity.ok(carritoService.actualizarCantidad(dto));
    }
}
