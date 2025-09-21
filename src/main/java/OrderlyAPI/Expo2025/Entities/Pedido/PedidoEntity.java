package OrderlyAPI.Expo2025.Entities.Pedido;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.EstadoPedido.EstadoPedidoEntity;
import OrderlyAPI.Expo2025.Entities.Mesa.MesaEntity;
import OrderlyAPI.Expo2025.Entities.PedidoDetalle.PedidoDetalleEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "PEDIDO")
@ToString @EqualsAndHashCode
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_seq")
    @SequenceGenerator(
            name = "pedido_seq",
            sequenceName = "pedido_seq",   // <-- tu secuencia en Oracle
            allocationSize = 1
    )
    @Column(name = "IDPEDIDO")
    private Long id;

    @Column(name = "NOMBRECLIENTE", nullable = false, length = 100)
    private String nombreCliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDMESA", referencedColumnName = "IDMESA", nullable = false)
    private MesaEntity mesas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDEMPLEADO", referencedColumnName = "IDEMPLEADO", nullable = false)
    private EmpleadoEntity empleado;

    @Column(name = "FECHAPEDIDO", nullable = false)
    private LocalDateTime fPedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDESTADOPEDIDO", referencedColumnName = "IDESTADOPEDIDO", nullable = false)
    private EstadoPedidoEntity estpedido;

    @Column(name = "OBSERVACIONES", nullable = false, length = 100)
    private String observaciones;

    @Column(name = "SUBTOTAL", nullable = false)
    private Double subtotal;

    @Column(name = "PROPINA", nullable = false)
    private Double propina;

    @Column(name = "TOTALPEDIDO", nullable = false)
    private Double totalPedido;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PedidoDetalleEntity> detalles = new ArrayList<>();
}
