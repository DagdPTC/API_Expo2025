package OrderlyAPI.Expo2025.Entities.UnidadMedida;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "UNIDADMEDIDA")
@Getter @Setter @ToString @EqualsAndHashCode
public class UnidadMedidaEntity {
    @Id
    @Column(name = "IDUNIDADMEDIDA")
    private Long Id;
    @Column(name = "NOMBRE")
    private String Nombre;
    @Column(name = "ABREVIATURA")
    private String Abreviatura;
}
