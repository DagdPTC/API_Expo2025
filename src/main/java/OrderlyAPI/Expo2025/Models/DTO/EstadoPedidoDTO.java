package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EstadoPedidoDTO {

    private Long Id;

    @NotBlank(message = "El nombre de estado pedido no puede ser nulo")
    @Size(max = 100, message = "El nombre de estado pedido no puede tener mas de 100 caracteres")
    private String NomEstado;
}
