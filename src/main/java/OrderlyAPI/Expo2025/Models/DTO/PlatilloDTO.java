package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlatilloDTO {

    private Long Id;

    @NotBlank(message = "El nombre del platillo no puede ser nulo")
    @Size(max = 100, message = "El nombre platillo no puede tener mas de 100 caracteres")
    private String NomPlatillo;

    @NotBlank(message = "La descripcion no puede ser nula")
    @Size(max = 100, message = "La descripcion no puede tener mas de 100 caracteres")
    private String Descripcion;

    @DecimalMin(value = "0.01", message = "El precio debe ser mayor de 0.01")
    @DecimalMax(value = "99999999.99", message = "El precio debe ser menor de 99999999.99")
    private double Precio;

    private Long IdCategoria;
}
