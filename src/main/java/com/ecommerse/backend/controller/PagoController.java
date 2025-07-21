package com.ecommerse.backend.controller;

import com.ecommerse.backend.dto.pago.PagoDTO;
import com.ecommerse.backend.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pago")
public class PagoController {

    @Autowired
    private PagoService pagoService;


    @PostMapping("/pagar")
    public ResponseEntity<String> iniciarPago(){
        String mercadoPagoInitPoint = pagoService.crearPreferencia();
        return ResponseEntity.ok(mercadoPagoInitPoint);
    }

    @GetMapping("/exito")
    public ResponseEntity<String> confirmarPago(@RequestParam("payment_id") String paymentId,
                                                @RequestParam("status") String status,
                                                @RequestParam("external_reference") String externalReference){
        return ResponseEntity.ok("Pago exitoso. ID: " + paymentId + ", Estado: " + status + ", Referencia: "
                + externalReference);
    }

    @GetMapping("/fallido")
    public String pagoFallido() {
        return "Pago fallido";
    }

    @GetMapping("/pendiente")
    public String pagoPendiente() {
        return "Pago pendiente";
    }
}
