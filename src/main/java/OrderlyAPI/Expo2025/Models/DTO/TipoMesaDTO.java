package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TipoMesaDTO {

    private Long Id;

    @NotBlank(message = "El nombre del tipo mesa no puede ser nulo")
    @Size(max = 100, message = "El nombre del tipo mesa no puede tener mas 100 caracteres")
    private String Nombre;

    @NotBlank(message = "La capacidad de personas no puede ser nulo")
    @Size(min = 1, max = 20, message = "Solo puede haber un maximo de 20 personas")
    private Long CapacidadPersonas;
}
