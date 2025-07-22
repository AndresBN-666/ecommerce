package com.ecommerse.backend.controller;

import com.ecommerse.backend.service.PedidoService;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/mercadopago")
@RequiredArgsConstructor
public class WebhookController {

    private final PedidoService pedidoService;

    @PostMapping("/webhook")
    public ResponseEntity<Map<String, String>> handleWebhook(
            @RequestParam String type,
            @RequestParam String id
    ) {
        try {
            if ("payment".equals(type)) {
                PaymentClient paymentClient = new PaymentClient();
                Payment payment = paymentClient.get(Long.valueOf(id));

                String externalReference = payment.getExternalReference();
                String paymentStatus = payment.getStatus();
                System.out.println("ID de pago MP: " + payment.getId() + ", External Ref: "
                        + externalReference + ", Estado MP: " + paymentStatus);

                if ("approved".equals(paymentStatus) && externalReference != null) {
                    try {
                        pedidoService.finalizarCompraDesdeWebhook(Long.valueOf(externalReference));
                        System.out.println("Pedido con referencia " + externalReference + " finalizado correctamente en BD.");
                    } catch (Exception e) {
                        System.err.println("Error al finalizar compra desde webhook para ref " + externalReference + ": " + e.getMessage());
                        e.printStackTrace();
                        // Aquí podrías loggear el error, marcar el pago para revisión manual, etc.
                    }
                } else if ("rejected".equals(paymentStatus) && externalReference != null) {
                    // Opcional: manejar pagos rechazados, por ejemplo, marcar el pedido como fallido en tu BD
                    System.out.println("Pago rechazado para referencia " + externalReference);
                    // pedidoService.marcarPagoRechazado(Long.valueOf(externalReference));
                } else if ("pending".equals(paymentStatus) && externalReference != null) {
                    // Opcional: manejar pagos pendientes
                    System.out.println("Pago pendiente para referencia " + externalReference);
                    // pedidoService.marcarPagoPendiente(Long.valueOf(externalReference));
                }
            }
            // Mercado Pago espera un 200 OK para no reintentar el webhook
            return ResponseEntity.ok(Map.of("status", "success"));

        } catch (MPApiException e) {
            System.err.println("Error de API de MP al procesar webhook: " + e.getApiResponse().getContent());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", "MP API error"));
        } catch (MPException e) {
            System.err.println("Error de SDK de MP al procesar webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", "MP SDK error"));
        } catch (Exception e) {
            System.err.println("Error inesperado al procesar webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", "Internal server error"));
        }
    }
}

