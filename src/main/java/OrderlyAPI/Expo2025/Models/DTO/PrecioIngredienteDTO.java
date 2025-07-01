package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NotBlank
public class PrecioIngredienteDTO {
    private Long Id;
    private Long IdIngrediente;
    private double Precio;
    private Date FInicio;
    private Date FFin;
}
