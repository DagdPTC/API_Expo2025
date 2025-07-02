package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter @NotBlank
public class HistorialPedidosDTO {
    private Long Id;
    private Timestamp FechaPedido;
    private String Descripcion;
    private Long IdEmpleado;
    private Long IdCliente;
    private Long IdPlatillo;
}
