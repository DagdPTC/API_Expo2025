package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter @Setter
public class EmpleadoDTO {

    private Long Id;

    private Long IdPersona;

    private Long IdUsuario;

    private LocalDateTime FContratacion;
}
