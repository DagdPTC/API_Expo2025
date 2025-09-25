package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioDTO {

    private Long Id;

    @NotBlank(message = "El nombre usuario no puede ser nulo")
    private String nombreusuario;

    @NotBlank(message = "La contraseña no puede ser nula")
    @Size(min = 8, max = 8, message = "La contraseña debe tener al menos dos caracteres")
    private String contrasenia;

    private Long rolId;

    @NotBlank(message = "El correo no puede ser nulo")
    @Email(message = "Asegurate que el correo tenga su formato correspondiente, ejemplo: usuario@gmail.com")
    private String correo;


}
