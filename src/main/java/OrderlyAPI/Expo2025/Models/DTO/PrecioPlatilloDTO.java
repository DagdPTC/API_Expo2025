package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NotBlank
public class PrecioPlatilloDTO {
    private Long Id;
    private Long IdPlatillo;
    private double PrecioUnitario;
    private Date FInicio;
    private Date FFin;
}
