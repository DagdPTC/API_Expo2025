package OrderlyAPI.Expo2025.Entities.Factura;

import OrderlyAPI.Expo2025.Entities.EstadoFactura.EstadoFacturaEntity;
import OrderlyAPI.Expo2025.Entities.HistorialPedido.HistorialPedidoEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "FACTURA")
@Getter @Setter @ToString @EqualsAndHashCode
public class FacturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "factura_seq")
    @SequenceGenerator(name = "factura_seq", sequenceName = "factura_seq", allocationSize = 1)
    @Column(name = "IDFACTURA")
    private Long id;

    @Column(name = "DESCUENTO")
    private Double descuento;

    @Column(name = "TOTAL")
    private Double total;

    // Campos para IDs directos (sin relación)
    @Transient
    private Long idPedido;

    @Transient
    private Long idEstadoFactura;

    // Relación con Pedido
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDPEDIDO", referencedColumnName = "IDPEDIDO", nullable = false)
    private PedidoEntity pedido;

    // Relación con EstadoFactura
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDESTADOFACTURA", referencedColumnName = "IDESTADOFACTURA", nullable = false)
    private EstadoFacturaEntity estadoFactura;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<HistorialPedidoEntity> historialPedidos;
}