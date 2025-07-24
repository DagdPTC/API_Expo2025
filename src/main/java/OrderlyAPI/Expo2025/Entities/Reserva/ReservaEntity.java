package OrderlyAPI.Expo2025.Entities.Reserva;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "RESERVA")
@Getter @Setter @ToString @EqualsAndHashCode
public class ReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reserva_seq")
    @SequenceGenerator(name = "reserva_seq", sequenceName = "reserva_seq", allocationSize = 1)
    @Column(name = "IDRESERVA")
    private Long Id;

    @NotNull(message = "El nombre del cliente no puede ser nulo")
    @NotEmpty(message = "El nombre del cliente no puede estar vacío")
    @Size(max = 50, message = "El nombre del cliente debe un maximo de 50 caracteres")
    @Column(name = "NOMBRECLIENTE")
    private String nomCliente;

    @NotNull(message = "El IdMesa no puede ser nulo")
    @Column(name = "IDMESA")
    private Long IdMesa;

    @NotNull(message = "La fecha de la reserva no puede ser nula")
    @Column(name = "FECHARESERVA")
    private Date FReserva;

    @NotNull(message = "La hora no puede ser nula")
    @Column(name = "HORA")
    private Timestamp Hora;

    @NotNull(message = "La cantidad de personas no puede ser nula")
    @Min(value = 1, message = "La cantidad de personas debe ser al menos 1")
    @Column(name = "CANTIDADPERSONAS")
    private Long CantidadPersonas;

    @NotNull(message = "El IdTipoReserva no puede ser nulo")
    @Column(name = "IDTIPORESERVA")
    private Long idTipoReserva;

    @NotNull(message = "El IdEstadoReserva no puede ser nulo")
    @Column(name = "IDESTADORESERVA")
    private Long IdEstadoReserva;
}
