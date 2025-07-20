package OrderlyAPI.Expo2025.Entities.Mesa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "MESA")
@Getter @Setter @ToString @EqualsAndHashCode
public class MesaEntity {
    @Id
    @Column(name = "IDMESA")
    private Long Id;
    @Column(name = "NOMBREMESA")
    private String NomMesa;
    @Column(name = "IDTIPOMESA")
    private Long IdTipoMesa;
}
