package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter @Setter @NotBlank
public class ReservaDTO {
    private Long Id;
    private Long IdCliente;
    private Long IdMesa;
    private Date FReserva;
    private Timestamp HInicio;
    private Timestamp HFin;
    private Long CantidadPersonas;
    private Long idTipoReserva;
    private Long IdEstadoReserva;
    private String Observaciones;
}
