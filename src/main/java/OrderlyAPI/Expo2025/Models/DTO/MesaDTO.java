package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @NotBlank
public class MesaDTO {
    private Long Id;
    private Long NomMesa;
    private Long IdTipoMesa;
}
