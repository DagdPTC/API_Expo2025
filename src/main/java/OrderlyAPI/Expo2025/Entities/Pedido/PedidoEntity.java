package OrderlyAPI.Expo2025.Entities.Pedido;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "PEDIDO")
@Getter @Setter @ToString @EqualsAndHashCode
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_seq")
    @SequenceGenerator(name = "pedido_seq", sequenceName = "pedido_seq", allocationSize = 1)
    @Column(name = "IDPEDIDO")
    private Long Id;

    @Column(name = "NOMBRECLIENTE")
    private String Nombrecliente;

    @Column(name = "IDMESA")
    private Long IdMesa;

    @Column(name = "IDEMPLEADO")
    private Long IdEmpleado;

    @Column(name = "FECHAPEDIDO")
    private LocalDateTime FPedido;

    @Column(name = "IDESTADOPEDIDO")
    private Long IdEstadoPedido;

    @Column(name = "OBSERVACIONES")
    private String Observaciones;

    @Column(name = "CANTIDAD")
    private Long Cantidad;

    @Column(name = "TOTALPEDIDO")
    private double TotalPedido;

    @Column(name = "SUBTOTAL")
    private double Subtotal;

    @Column(name = "PROPINA")
    private double Propina;

    @Column(name = "IDPLATILLO")
    private Long IdPlatillo;
}
