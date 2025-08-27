package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter @Setter
public class PersonaDTO {

    private Long Id;

    @NotBlank(message = "El primer nombre no puede ser nulo")
    @Size(max = 20, message = "El primer nombre debe tener mas de 20 caracteres")
    private String Pnombre;

    @NotBlank(message = "El segundo nombre no puede ser nulo")
    @Size(max = 20, message = "El segundo nombre debe tener mas de 20 caracteres")
    private String Snombre;

    @NotBlank(message = "El apellido paterno no puede ser nulo")
    @Size(max = 20, message = "El apellido paterno debe tener mas de 20 caracteres")
    private String apellidoP;

    @NotBlank(message = "El apellido materno no puede ser nulo")
    @Size(max = 20, message = "El apellido materno debe tener mas de 20 caracteres")
    private String apellidoM;

    private LocalDate fechaN;

    @NotBlank(message = "La direccion no puede ser nula")
    @Size(max = 200, message = "La direccion no debe tener mas de 200 caracteres")
    private String Direccion;

    private Long IdDoc;
}