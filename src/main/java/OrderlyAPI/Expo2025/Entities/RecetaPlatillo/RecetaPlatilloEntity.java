package OrderlyAPI.Expo2025.Entities.RecetaPlatillo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "RECETAPLATILLO")
@Getter @Setter @ToString @EqualsAndHashCode
public class RecetaPlatilloEntity {
    @Id
    @Column(name = "IDRECETA")
    private Long Id;
    @Column(name = "IDPLATILLO")
    private Long IdPlatillo;
    @Column(name = "IDINGREDIENTE")
    private Long IdIngrediente;
    @Column(name = "CANTIDAD")
    private String Cantidad;
    @Column(name = "IDUNIDADMEDIDA")
    private Long IdUnidadMedida;
}
