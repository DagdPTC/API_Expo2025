// src/main/java/OrderlyAPI/Expo2025/Models/DTO/UsuarioDTO.java
package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {

    private Long Id;

    @NotBlank(message = "El nombre usuario no puede ser nulo")
    private String nombreusuario;

    @NotBlank(message = "La contraseña no puede ser nula")
    @Size(min = 8, max = 128, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasenia;

    @NotBlank(message = "El correo no puede ser nulo")
    @Email(message = "El correo no es válido")
    private String correo;

    private Long rolId;
}
