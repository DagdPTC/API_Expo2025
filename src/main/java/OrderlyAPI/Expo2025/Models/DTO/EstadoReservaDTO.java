package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EstadoReservaDTO {

    private Long Id;

    @NotBlank(message = "El nombre del estado reserva no puede ser nulo")
    @Size(max = 100, message = "El nombre estado no puede tener mas de 100 caracteres")
    private String NomEstado;
}
