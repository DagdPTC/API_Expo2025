package OrderlyAPI.Expo2025.Entities.Reserva;

import OrderlyAPI.Expo2025.Entities.EstadoReserva.EstadoReservaEntity;
import OrderlyAPI.Expo2025.Entities.Mesa.MesaEntity;
import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import OrderlyAPI.Expo2025.Entities.TipoReserva.TipoReservaEntity;
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
import java.time.LocalDate;
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

    @Column(name = "FECHARESERVA")
    private LocalDate FReserva;

    @Column(name = "HORAINICIO")
    private LocalTime HoraI;

    @Column(name = "HORAFIN")
    private LocalTime HoraF;

    @Column(name = "CANTIDADPERSONAS")
    private Long CantidadPersonas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDMESA", referencedColumnName = "IDMESA")
    private MesaEntity mesa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDTIPORESERVA", referencedColumnName = "IDTIPORESERVA")
    private TipoReservaEntity tipreser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDESTADORESERVA", referencedColumnName = "IDESTADORESERVA")
    private EstadoReservaEntity estreser;
}
