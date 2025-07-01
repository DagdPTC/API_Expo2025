package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @NotBlank
public class IngredienteDTO {
    private Long Id;
    private String NomIngrediente;
    private double Cantidad;
    private Long IdCategoria;
    private Long IdUnidadMedida;
    private double CosteUnidad;
}
