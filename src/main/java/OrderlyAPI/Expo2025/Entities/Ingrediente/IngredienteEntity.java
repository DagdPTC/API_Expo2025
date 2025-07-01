package OrderlyAPI.Expo2025.Entities.Ingrediente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "INGREDIENTE")
@Getter @Setter @ToString @EqualsAndHashCode
public class IngredienteEntity {
    @Id
    @Column(name = "IDINGREDIENTE")
    private Long Id;
    @Column(name = "NOMBREINGREDIENTE")
    private String NomIngrediente;
    @Column(name = "CANTIDAD")
    private double Cantidad;
    @Column(name = "IDCATEGORIA")
    private Long IdCategoria;
    @Column(name = "IDUNIDADMEDIDA")
    private Long IdUnidadMedida;
    @Column(name = "COSTEUNIDAD")
    private double CosteUnidad;
}
