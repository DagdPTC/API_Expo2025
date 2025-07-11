package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter @Setter @NotBlank
public class PedidoDTO {
    private Long Id;
    private String Nombrecliente;
    private Long IdMesa;
    private Long IdEmpleado;
    private Date FPedido;
    private Timestamp HPedido;
    private Long IdEstadoPedido;
    private String Observaciones;
}
