package OrderlyAPI.Expo2025.Entities.Categoria;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CATEGORIA")
@Getter @Setter @ToString @EqualsAndHashCode
public class CategoriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria_seq")
    @SequenceGenerator(name = "categoria_seq", sequenceName = "categoria_seq", allocationSize = 1)
    @Column(name = "IDCATEGORIA")
    private Long Id;

    @NotNull(message = "El nombre de la categoria no puede ser nulo")
    @NotEmpty(message = "El nombre de la categoria no puede estar vacio")
    @Size(min = 3, max = 50, message = "El nombre de la categoria debe tener entre 3 a 50 caracteres")
    @Column(name = "NOMBRECATEGORIA")
    private String NomCategoria;
}
