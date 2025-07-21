package com.ecommerse.backend.dto.pago;

import lombok.Data;

@Data
public class PagoDTO {

    private String tituloProducto;
    private String descripcion;
    private int cantidad;
    private double precio;
    private String emailComprador;
}
