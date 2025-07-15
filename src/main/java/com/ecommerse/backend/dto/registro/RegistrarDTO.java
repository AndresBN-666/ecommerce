package com.ecommerse.backend.dto.registro;

import com.ecommerse.backend.model.enums.Rol;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarDTO {


    @NotBlank(message = "El campo no debe estar vacio")
    private String nombre;

    @NotBlank(message = "El campo no debe estar vacio")
    private String correo;

    @NotBlank(message = "El campo no debe estar vacio")
    private String clave;

    private Rol rol;
}
