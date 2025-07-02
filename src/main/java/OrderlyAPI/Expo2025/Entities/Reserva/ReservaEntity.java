package OrderlyAPI.Expo2025.Entities.Reserva;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name = "IDRESERVA")
    private Long Id;
    @Column(name = "IDCLIENTE")
    private Long IdCliente;
    @Column(name = "IDMESA")
    private Long IdMesa;
    @Column(name = "FECHARESERVA")
    private Date FReserva;
    @Column(name = "HORAINICIO")
    private Timestamp HInicio;
    @Column(name = "HORAFIN")
    private Timestamp HFin;
    @Column(name = "CANTIDADPERSONAS")
    private Long CantidadPersonas;
    @Column(name = "IDTIPORESERVA")
    private Long idTipoReserva;
    @Column(name = "IDESTADORESERVA")
    private Long IdEstadoReserva;
    @Column(name = "OBSERVACIONES")
    private String Observaciones;
}
