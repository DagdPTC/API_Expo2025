package OrderlyAPI.Expo2025.Entities.EstadoPlatillo;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ESTADOPLATILLO")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoPlatilloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rol_seq")
    @SequenceGenerator(name = "rol_seq", sequenceName = "rol_seq", allocationSize = 1)
    @Column(name = "IDESTADOPLATILLO")
    private Long Id;
    @Column(name = "DESCRIPCION")
    private String Descripcion;
}
