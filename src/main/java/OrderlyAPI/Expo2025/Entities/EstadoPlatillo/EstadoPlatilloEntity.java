package OrderlyAPI.Expo2025.Entities.EstadoPlatillo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ESTADOPLATILLO")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoPlatilloEntity {
    @Id
    @Column(name = "IDESTADOPLATILLO")
    private Long Id;
    @Column(name = "DESCRIPCION")
    private String Descripcion;
}
