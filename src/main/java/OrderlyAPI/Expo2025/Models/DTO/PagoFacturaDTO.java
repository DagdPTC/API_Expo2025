package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter @NotBlank
public class PagoFacturaDTO {
    private Long Id;
    private Long IdFactura;
    private Long IdMetodoPago;
    private double MontoPagado;
    private Timestamp FechaPago;
    private String ReferenciaPago;
}
