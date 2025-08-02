package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EstadoMesaDTO {

    private Long Id;

    @NotBlank(message = "El estado mesa no puede ser nulo")
    @Size(max = 100, message = "El estado mesa no puede tener mas de 100 caracteres")
    private String EstadoMesa;

    @NotBlank(message = "El color estado mesa no puede ser nulo")
    @Size(max = 100, message = "El color estado mesa no puede tener mas de 100 caracteres")
    private String ColorEstadoMesa;
}
