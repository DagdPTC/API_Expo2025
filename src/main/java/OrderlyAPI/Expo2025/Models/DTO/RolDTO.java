package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RolDTO {

    private Long Id;

    @NotBlank(message = "El rol no puede ser nulo")
    @Size(max = 15, message = "El nombre del rol no puede tener mas de 10 caracteres")
    private String rol;
}
