package OrderlyAPI.Expo2025.Entities.Pedido;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.text.DecimalFormat;
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
    private Date FPedido;


    @Column(name = "HORAPEDIDO")
    private Timestamp HPedido;


    @Column(name = "IDESTADOPEDIDO")
    private Long IdEstadoPedido;


    @Column(name = "OBSERVACIONES")
    private String Observaciones;


    @Column(name = "CANTIDAD")
    private Number Cantidad;


    @Column(name = "TOTALPEDIDO")
    private DecimalFormat TotalPedido;


    @Column(name = "SUBTOTAL")
    private DecimalFormat Subtotal;


    @Column(name = "PROPINA")
    private DecimalFormat Propina;


    @Column(name = "DESCUENTO")
    private DecimalFormat Descuento;


    @Column(name = "TOTALFACTURA")
    private DecimalFormat TotalFactura;
}
