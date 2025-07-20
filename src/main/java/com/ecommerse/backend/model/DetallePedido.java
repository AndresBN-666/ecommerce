package com.ecommerse.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_pedido")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@ToString(exclude = "pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private  Producto producto;
}
