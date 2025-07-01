package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NotBlank
public class EmpleadoDTO {
    private Long Id;
    private Long IdPersona;
    private Long IdUsuario;
    private Date FContratacion;
}
