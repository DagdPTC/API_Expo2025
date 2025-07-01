package OrderlyAPI.Expo2025.Entities.Platillo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "PLATILLO")
@Getter @Setter @ToString @EqualsAndHashCode
public class PlatilloEntity {
    @Id
    @Column(name = "IDPLATILLO")
    private Long Id;
    @Column(name = "NOMBREPLATILLO")
    private String NomPlatillo;
    @Column(name = "DESCRIPCION")
    private String Descripcion;
    @Column(name = "TIEMPOPREPARACION")
    private Long TiempoPreparacion;
}
