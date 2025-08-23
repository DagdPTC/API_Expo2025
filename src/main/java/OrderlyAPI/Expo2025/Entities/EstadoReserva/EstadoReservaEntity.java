package OrderlyAPI.Expo2025.Entities.EstadoReserva;

import OrderlyAPI.Expo2025.Entities.Persona.PersonaEntity;
import OrderlyAPI.Expo2025.Entities.Reserva.ReservaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "ESTADORESERVA")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_Reserva_seq")
    @SequenceGenerator(name = "estado_Reserva_seq", sequenceName = "estado_Reserva_seq", allocationSize = 1)
    @Column(name = "IDESTADORESERVA")
    private Long Id;

    @Column(name = "NOMBREESTADO", unique = true)
    private String NomEstado;

    @OneToMany(mappedBy = "estreser", cascade = CascadeType.ALL)
    private List<ReservaEntity> estreser;
}
