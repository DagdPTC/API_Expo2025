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
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Column(name = "NOMBRECLIENTE")
    private String nomCliente;

    @Column(name = "TELEFONO")
    private String Telefono;

    @Column(name = "IDMESA")
    private Long IdMesa;

    @Column(name = "FECHARESERVA")
    private LocalDateTime FReserva;

    @Column(name = "HORAINICIO")
    private LocalTime HoraI;

    @Column(name = "HORAFIN")
    private LocalTime HoraF;

    @Column(name = "CANTIDADPERSONAS")
    private Long CantidadPersonas;

    @Column(name = "IDTIPORESERVA")
    private Long idTipoReserva;

    @Column(name = "IDESTADORESERVA")
    private Long IdEstadoReserva;
}
