package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @NotBlank
public class DetallePedidoDTO {
    private Long Id;
    private Long Idpedido;
    private Long IdPlatillo;
    private Long Cantidad;
    private double PrecioUnitario;
    private String Observaciones;
    private Long IdEstadoPlatillo;
}
