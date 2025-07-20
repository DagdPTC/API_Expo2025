package OrderlyAPI.Expo2025.Entities.Platillo;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "PLATILLO")
@Getter @Setter @ToString @EqualsAndHashCode
public class PlatilloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platillo_seq")
    @SequenceGenerator(name = "platillo_seq", sequenceName = "platillo_seq", allocationSize = 1)
    @Column(name = "IDPLATILLO")
    private Long Id;
    @Column(name = "NOMBREPLATILLO")
    private String NomPlatillo;
    @Column(name = "DESCRIPCION")
    private String Descripcion;
    @Column(name = "PRECIO")
    private Long Precio;
    @Column(name = "TIEMPOPREPARACION")
    private Long TiempoPreparacion;
}
