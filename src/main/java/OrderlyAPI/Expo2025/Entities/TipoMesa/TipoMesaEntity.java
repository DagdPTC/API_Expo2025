package OrderlyAPI.Expo2025.Entities.TipoMesa;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TIPOMESA")
@Setter @Getter @ToString @EqualsAndHashCode
public class TipoMesaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoMesa_seq")
    @SequenceGenerator(name = "tipoMesa_seq", sequenceName = "tipoMesa_seq", allocationSize = 1)
    @Column(name = "IDTIPOMESA")
    private Long Id;
    @Column(name = "NOMBRETIPOMESA")
    private String Nombre;
    @Column(name = "CAPACIDADPERSONAS")
    private Long CapacidadPersonas;

}
