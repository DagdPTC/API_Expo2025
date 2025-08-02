package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class DocumentoIdentidadDTO {

    private Long Id;

    private Long IdtipoDoc;

    @NotBlank
    @Pattern(regexp = "^\\d{8}-\\d$", message = "Formato de DUI invalido, ejemplo: 12345678-9")
    private String numDoc;
}
