package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter @NotBlank
public class HistorialEstadoMesaDTO {
    private Long Id;
    private Long IdMesa;
    private Long IdEstadoMesa;
    private Timestamp FInicio;
    private Timestamp FFin;
    private Long IdEmpleado;
}
