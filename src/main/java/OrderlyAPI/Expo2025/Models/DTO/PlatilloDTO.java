package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlatilloDTO {
    private Long Id;
    private String NomPlatillo;
    private String Descripcion;
    private double Precio;
    private Long TiempoPreparacion;
}
