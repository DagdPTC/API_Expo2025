package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MesaDTO {

    private Long Id;

    @NotBlank(message = "El nombre de la mesa no puede ser nulo")
    @Size(max = 100, message = "El nombre de la mesa no puede tener mas de 100 caracteres")
    private String NomMesa;

    private Long IdTipoMesa;

    private Long IdEstadoMesa;
}
