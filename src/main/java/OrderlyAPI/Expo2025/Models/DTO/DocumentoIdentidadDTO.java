package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class DocumentoIdentidadDTO {
    private Long Id;
    @NotBlank @Size(min = 1, message = "No aceptamos numeros negativos")
    private Long Idpersona;
    @NotBlank @Size(max = 15, message = "El tipo documento no debe tener mas de 15 caracteres")
    private String tipoDoc;
    @NotBlank @Pattern()
    private String numDoc;
}
