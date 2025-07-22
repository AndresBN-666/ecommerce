package com.ecommerse.backend.service.impl;

import com.ecommerse.backend.dto.pago.PagoDTO;
import com.ecommerse.backend.model.Carrito;
import com.ecommerse.backend.model.ItemCarrito;
import com.ecommerse.backend.model.Usuario;
import com.ecommerse.backend.repository.CarritoRepository;
import com.ecommerse.backend.repository.UsuarioRepository;
import com.ecommerse.backend.service.PagoService;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;

    @Override
    public String crearPreferencia() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no registrado"));

        try {

            Carrito carrito = carritoRepository.findByUsuario_IdAndActivoTrue(usuario.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Carrito activo no encontrado"));

            List<PreferenceItemRequest> itemRequests = new ArrayList<>();

            for (ItemCarrito item : carrito.getItems()){
                PreferenceItemRequest itemRequest  = PreferenceItemRequest.builder()
                        .id(String.valueOf(item.getProducto().getId()))
                        .title(item.getProducto().getNombre())
                        .quantity(item.getCantidad())
                        .unitPrice(BigDecimal.valueOf(item.getProducto().getPrecio()))
                        .currencyId("PEN")
                        .description(item.getProducto().getDescripcion())
                        .build();
                itemRequests.add(itemRequest);
            }

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("https://cb7dc620478c.ngrok-free.app/pago/exito") // Tu frontend: página de éxito
                    .pending("https://cb7dc620478c.ngrok-free.app/pago/pendiente") // Tu frontend: página de pendiente
                    .failure("https://cb7dc620478c.ngrok-free.app/pago/fallido")   // Tu frontend: página de fallo
                    .build();

            PreferencePayerRequest payerRequest = PreferencePayerRequest.builder()
                    .email(correo)
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(itemRequests)
                    .payer(payerRequest)
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .externalReference(String.valueOf(carrito.getId()))
                    .build();

            PreferenceClient client = new  PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return preference.getInitPoint();


        } catch (MPApiException e) {
            // Errores de la API de Mercado Pago (ej. credenciales inválidas, request mal formado)
            System.err.println("Error de API de Mercado Pago: " + e.getApiResponse().getContent());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al crear la preferencia de pago (MP API): " + e.getApiResponse().getContent());
        } catch (MPException e) {
            // Otros errores del SDK de Mercado Pago (ej. problemas de conexión)
            System.err.println("Error del SDK de Mercado Pago: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al crear la preferencia de pago (MP SDK): " + e.getMessage());
        } catch (Exception e) {
            // Otros errores inesperados
            System.err.println("Error inesperado al crear la preferencia: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error inesperado al crear la preferencia de pago: " + e.getMessage());
        }

    }

      /*  String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no registrado"));

        try {

            Carrito carrito = carritoRepository.findByUsuario_IdAndActivoTrue(usuario.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Carrito activo no encontrado"));

            List<PreferenceItemRequest> itemRequests = new ArrayList<>();

            for (ItemCarrito item : carrito.getItems()){
                PreferenceItemRequest itemRequest  = PreferenceItemRequest.builder()
                        .id(String.valueOf(item.getProducto().getId()))
                        .title(item.getProducto().getNombre())
                        .quantity(item.getCantidad())
                        .unitPrice(BigDecimal.valueOf(item.getProducto().getPrecio()))
                        .currencyId("PEN")
                        .description(item.getProducto().getDescripcion())
                        .build();
                itemRequests.add(itemRequest);
            }

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("https://e2ff5d4b15ed.ngrok-free.app/pago/exito") // Tu frontend: página de éxito
                    .pending("https://e2ff5d4b15ed.ngrok-free.app/pago/pendiente") // Tu frontend: página de pendiente
                    .failure("https://e2ff5d4b15ed.ngrok-free.app/pago/fallido")   // Tu frontend: página de fallo
                    .build();

            PreferencePayerRequest payerRequest = PreferencePayerRequest.builder()
                    .email(correo)
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(itemRequests)
                    .payer(payerRequest)
                    .backUrls(backUrls)
                    //.autoReturn("approved")
                    .externalReference(String.valueOf(carrito.getId())) // ¡Este es el ID de tu carrito/pedido!
                    //.notificationUrl("https://TU_URL_NGROK_BACKEND.ngrok-free.app/api/mercadopago/webhook") // <--- ¡Añade esto!
                    .build();

            PreferenceClient client = new  PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return preference.getInitPoint();


        } catch (MPApiException e) {
            // Errores de la API de Mercado Pago (ej. credenciales inválidas, request mal formado)
            System.err.println("Error de API de Mercado Pago: " + e.getApiResponse().getContent());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al crear la preferencia de pago (MP API): " + e.getApiResponse().getContent());
        } catch (MPException e) {
            // Otros errores del SDK de Mercado Pago (ej. problemas de conexión)
            System.err.println("Error del SDK de Mercado Pago: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al crear la preferencia de pago (MP SDK): " + e.getMessage());
        } catch (Exception e) {
            // Otros errores inesperados
            System.err.println("Error inesperado al crear la preferencia: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error inesperado al crear la preferencia de pago: " + e.getMessage());
        }*/

}
