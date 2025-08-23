package OrderlyAPI.Expo2025.Entities.Pedido;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.EstadoPedido.EstadoPedidoEntity;
import OrderlyAPI.Expo2025.Entities.Factura.FacturaEntity;
import OrderlyAPI.Expo2025.Entities.HistorialPedido.HistorialPedidoEntity;
import OrderlyAPI.Expo2025.Entities.Mesa.MesaEntity;
import OrderlyAPI.Expo2025.Entities.Persona.PersonaEntity;
import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    @Column(name = "FECHAPEDIDO")
    private LocalDateTime FPedido;

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

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<FacturaEntity> pedido;

    @OneToMany(mappedBy = "pedidos", cascade = CascadeType.ALL)
    private List<HistorialPedidoEntity> pedidos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDMESA", referencedColumnName = "IDMESA")
    private MesaEntity mesas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDEMPLEADO", referencedColumnName = "IDEMPLEADO")
    private EmpleadoEntity empleado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDESTADOPEDIDO", referencedColumnName = "IDESTADOPEDIDO")
    private EstadoPedidoEntity estpedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDPLATILLO", referencedColumnName = "IDPLATILLO")
    private PlatilloEntity platillo;
}
