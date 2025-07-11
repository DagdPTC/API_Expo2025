package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @NotBlank
public class EstadoPedidoDTO {
    private Long Id;
    private String NomEstado;
}
