package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EstadoMesaDTO {
    private Long Id;
    private String EstadoMesa;
    private String ColorEstadoMesa;
}
