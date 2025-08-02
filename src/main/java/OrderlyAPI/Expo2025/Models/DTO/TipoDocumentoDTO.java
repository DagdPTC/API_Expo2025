package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TipoDocumentoDTO {

    private Long IdTipoDoc;

    @NotBlank(message = "El nombre del Tipo Documento no puede estar vacio")
    @Size(max = 100, message = "El tipo documento debe tener un maximo de 100 caracteres")
    private String TipoDoc;
}
