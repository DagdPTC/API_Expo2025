package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @NotBlank
public class UbicacionRestauranteDTO {
    private Long Id;
    private String NomUbicacion;
    private String Descripcion;
}
