package OrderlyAPI.Expo2025.Entities.Reserva;

import jakarta.persistence.*;
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
    @Column(name = "NOMBRECLIENTE")
    private String nomCliente;
    @Column(name = "IDMESA")
    private Long IdMesa;
    @Column(name = "FECHARESERVA")
    private Date FReserva;
    @Column(name = "HORA")
    private Timestamp Hora;
    @Column(name = "CANTIDADPERSONAS")
    private Long CantidadPersonas;
    @Column(name = "EVENTOESPECIAL")
    private String Eventoespecial;
    @Column(name = "MESADISPONIBLE")
    private String Mesadiscpo;
    @Column(name = "IDTIPORESERVA")
    private Long idTipoReserva;
    @Column(name = "IDESTADORESERVA")
    private Long IdEstadoReserva;
}
