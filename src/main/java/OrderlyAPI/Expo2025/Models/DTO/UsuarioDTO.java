package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @NotBlank
public class UsuarioDTO {
    private Long Id;
    private String nombre;
    private String contrasenia;
    private Long rolId;
    private String correo;
}
