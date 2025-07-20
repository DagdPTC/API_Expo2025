package OrderlyAPI.Expo2025.Entities.EstadoReserva;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ESTADORESERVA")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estadoReserva_seq")
    @SequenceGenerator(name = "estadoReserva_seq", sequenceName = "estadoReserva_seq", allocationSize = 1)
    @Column(name = "IDESTADORESERVA")
    private Long Id;
    @Column(name = "NOMBREESTADO")
    private String NomEstado;
}
