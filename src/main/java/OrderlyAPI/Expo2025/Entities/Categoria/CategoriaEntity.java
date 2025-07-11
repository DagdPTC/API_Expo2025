package OrderlyAPI.Expo2025.Entities.Categoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CATEGORIA")
@Getter @Setter @ToString @EqualsAndHashCode
public class CategoriaEntity {
    @Id
    @Column(name = "IDCATEGORIA")
    private Long Id;
    @Column(name = "NOMBRECATEGORIA")
    private String NomCategoria;
}
