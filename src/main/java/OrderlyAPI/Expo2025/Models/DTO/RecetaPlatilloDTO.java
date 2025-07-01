package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @NotBlank
public class RecetaPlatilloDTO {
    private Long Id;
    private Long IdPlatillo;
    private Long IdIngrediente;
    private String Cantidad;
    private Long IdUnidadMedida;
}
