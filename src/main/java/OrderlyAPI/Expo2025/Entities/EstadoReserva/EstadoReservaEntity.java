package OrderlyAPI.Expo2025.Entities.EstadoReserva;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ESTADORESERVA")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_Reserva_seq")
    @SequenceGenerator(name = "estado_Reserva_seq", sequenceName = "estado_Reserva_seq", allocationSize = 1)
    @Column(name = "IDESTADORESERVA")
    private Long Id;

    @NotNull(message = "El nombre del estado no puede ser nulo")
    @NotEmpty(message = "El nombre del estado no puede estar vacío")
    @Size(max = 50, message = "El nombre del estado no puede tener más de 50 caracteres")
    @Column(name = "NOMBREESTADO")
    private String NomEstado;
}
