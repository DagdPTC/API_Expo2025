package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoriaDTO {

    private Long Id;

    @NotBlank @NotNull @Size(max = 15, message = "EL Nombre Categoria no debe tener mas de 15 caracteres")
    private String NomCategoria;
}
