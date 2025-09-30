package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EstadoFacturaDTO {

    private Long Id;

    @NotBlank(message = "El estado de factura no puede estar vacío")
    @Size(min = 1, max = 100, message = "El estado de factura debe tener entre 1 y 100 caracteres")
    private String EstadoFactura;
}