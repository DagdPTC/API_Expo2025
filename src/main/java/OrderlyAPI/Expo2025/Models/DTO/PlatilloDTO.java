package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @NotBlank
public class PlatilloDTO {
    private Long Id;
    private String NomPlatillo;
    private String Descripcion;
    private Long Precio;
    private Long TiempoPreparacion;
}
