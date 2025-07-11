package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @NotBlank
public class TipoMesaDTO {
    private Long Id;
    private String Nombre;
    private Long CapacidadPersonas;
}
