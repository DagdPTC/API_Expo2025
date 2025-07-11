package OrderlyAPI.Expo2025.Entities.TipoReserva;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TIPORESERVA")
@Getter @Setter @ToString @EqualsAndHashCode
public class TipoReservaEntity {
    @Id
    @Column(name = "IDTIPORESERVA")
    private Long Id;
    @Column(name = "NOMBRETIPO")
    private String NomTipo;
}
