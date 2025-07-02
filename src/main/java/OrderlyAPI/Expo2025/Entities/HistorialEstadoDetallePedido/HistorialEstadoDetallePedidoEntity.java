package OrderlyAPI.Expo2025.Entities.HistorialEstadoDetallePedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(name = "HISTORIALESTADODETALLEPEDIDO")
@Getter @Setter @ToString @EqualsAndHashCode
public class HistorialEstadoDetallePedidoEntity {
    @Id
    @Column(name = "IDHISTORIALESTADODETALLEPEDIDO")
    private Long Id;
    @Column(name = "IDDETALLEPEDIDO")
    private Long IdDetallePedido;
    @Column(name = "IDESTADOPLATILLO")
    private Long IdEstadoPlatillo;
    @Column(name = "FECHACAMBIO")
    private Timestamp FCambio;
    @Column(name = "IDEMPLEADO")
    private Long IdEmpleado;
    @Column(name = "OBSERVACIONES")
    private String Observaciones;
}
