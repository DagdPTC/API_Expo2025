package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class FacturaDTO {

    private Long Id;

    private Long IdPedido;

    @DecimalMin(value = "0.01", message = "El descuento debe ser mayor de 0.01")
    @DecimalMax(value = "99999999.99", message = "El descuento debe ser menor de 99999999.99")
    private double Descuento;

    @DecimalMin(value = "0.01", message = "El total debe ser mayor de 0.01")
    @DecimalMax(value = "99999999.99", message = "El total debe ser menor de 99999999.99")
    private double Total;
}
