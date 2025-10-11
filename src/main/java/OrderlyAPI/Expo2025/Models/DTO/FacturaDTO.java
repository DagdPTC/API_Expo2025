package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FacturaDTO {

    private Long Id;

    private Long IdPedido;

    // Ahora 0.00 permitido (porque el descuento real lo calculamos desde porcentaje)
    @DecimalMin(value = "0.00", message = "El descuento no puede ser negativo")
    @DecimalMax(value = "99999999.99", message = "El descuento debe ser menor de 99999999.99")
    private double Descuento;

    @DecimalMin(value = "0.01", message = "El total debe ser mayor de 0.01")
    @DecimalMax(value = "99999999.99", message = "El total debe ser menor de 99999999.99")
    private double Total;

    // NUEVO CAMPO PARA ESTADO FACTURA
    @NotNull(message = "El ID del estado de factura es requerido")
    private Long IdEstadoFactura;

    @NotBlank
    private String EstadoFactura;
}