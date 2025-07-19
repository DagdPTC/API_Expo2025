package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter @Setter
public class ReservaDTO {
    private Long Id;
    private String nomCliente;
    private Number Telefono;
    private Long IdMesa;
    private Date FReserva;
    private Timestamp Hora;
    private Long CantidadPersonas;
    private String Eventoespecial;
    private String Mesadiscpo;
    private Long idTipoReserva;
    private Long IdEstadoReserva;
}
