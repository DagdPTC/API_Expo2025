package OrderlyAPI.Expo2025.Entities.Mesa;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "MESA")
@Getter @Setter @ToString @EqualsAndHashCode
public class MesaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mesa_seq")
    @SequenceGenerator(name = "mesa_seq", sequenceName = "mesa_seq", allocationSize = 1)
    @Column(name = "IDMESA")
    private Long Id;
    @Column(name = "NOMBREMESA")
    private String NomMesa;
    @Column(name = "IDTIPOMESA")
    private Long IdTipoMesa;
}
