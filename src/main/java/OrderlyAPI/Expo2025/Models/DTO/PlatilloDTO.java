package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlatilloDTO {

    private Long Id;

    @NotBlank(message = "El nombre del platillo no puede ser nulo")
    @Size(max = 100, message = "El nombre del platillo no puede tener más de 100 caracteres")
    private String NomPlatillo;

    @NotBlank(message = "La descripción no puede ser nula")
    @Size(max = 100, message = "La descripción no puede tener más de 100 caracteres")
    private String Descripcion;

    // NUMBER(10,2) en BD -> hasta 8 enteros y 2 decimales
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor o igual a 0.01")
    @DecimalMax(value = "99999999.99", message = "El precio debe ser menor o igual a 99,999,999.99")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener como máximo 8 enteros y 2 decimales")
    private double Precio;

    // En BD: IdCategoria NOT NULL
    @NotNull(message = "La categoría (IdCate) es requerida")
    @Positive(message = "La categoría (IdCate) debe ser un ID válido")
    private Long IdCate;

    @Size(max = 500, message = "La URL de la imagen no puede exceder 500 caracteres")
    private String imagenUrl;

    // Nuevo: para gestionar borrado/transformaciones en Cloudinary
    @Size(max = 255, message = "El publicId no puede exceder 255 caracteres")
    private String publicId;
}
