package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter @NotBlank
public class HistorialEstadoDetallePedidoDTO {
    private Long Id;
    private Long IdDetallePedido;
    private Long IdEstadoPlatillo;
    private Timestamp FCambio;
    private Long IdEmpleado;
    private String Observaciones;
}
