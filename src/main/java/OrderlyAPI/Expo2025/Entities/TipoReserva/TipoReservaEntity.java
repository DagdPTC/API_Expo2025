package OrderlyAPI.Expo2025.Entities.TipoReserva;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TIPORESERVA")
@Getter @Setter @ToString @EqualsAndHashCode
public class TipoReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoReserva_seq")
    @SequenceGenerator(name = "tipoReserva_seq", sequenceName = "tipoReserva_seq", allocationSize = 1)
    @Column(name = "IDTIPORESERVA")
    private Long Id;
    @Column(name = "NOMBRETIPO")
    private String NomTipo;
}
