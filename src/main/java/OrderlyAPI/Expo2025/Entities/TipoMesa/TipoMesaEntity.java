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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_Mesa_seq")
    @SequenceGenerator(name = "tipo_Mesa_seq", sequenceName = "tipo_Mesa_seq", allocationSize = 1)
    @Column(name = "IDTIPOMESA")
    private Long Id;
    @Column(name = "NOMBRETIPOMESA")
    private String Nombre;
    @Column(name = "CAPACIDADPERSONAS")
    private Long CapacidadPersonas;

}
