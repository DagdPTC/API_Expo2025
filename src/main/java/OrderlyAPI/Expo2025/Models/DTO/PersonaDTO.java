package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class PersonaDTO {
    private Long Id;
    private String Pnombre;
    private String Snombre;
    private String apellidoP;
    private String apellidoM;
    private Date fechaN;
}
