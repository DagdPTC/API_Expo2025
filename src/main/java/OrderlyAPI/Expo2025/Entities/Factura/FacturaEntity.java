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
    private Long Id;

    @Column(name = "DESCUENTO")
    private Double descuento;

    @Column(name = "TOTAL")
    private Double total;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<HistorialPedidoEntity> historialPedidos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDPEDIDO", referencedColumnName = "IDPEDIDO")
    private PedidoEntity pedido;

    // RELACIÓN CON ESTADO FACTURA - CORREGIDA
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDESTADOFACTURA", referencedColumnName = "IDESTADOFACTURA", nullable = false)
    private EstadoFacturaEntity estadoFactura;
}