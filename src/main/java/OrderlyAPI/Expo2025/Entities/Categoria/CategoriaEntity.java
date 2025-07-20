package OrderlyAPI.Expo2025.Entities.Categoria;

import jakarta.persistence.*;
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
    @Column(name = "NOMBRECATEGORIA")
    private String NomCategoria;
}
