package OrderlyAPI.Expo2025.Entities.TipoMesa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TIPOMESA")
@Setter @Getter @ToString @EqualsAndHashCode
public class TipoMesaEntity {

    @Id
    @Column(name = "IDTIPOMESA")
    private Long Id;
    @Column(name = "NOMBRETIPOMESA")
    private String Nombre;
    @Column(name = "CAPACIDADPERSONAS")
    private Long CapacidadPersonas;

}
