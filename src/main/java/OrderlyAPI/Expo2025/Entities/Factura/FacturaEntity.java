package OrderlyAPI.Expo2025.Entities.Factura;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.EstadoFactura.EstadoFacturaEntity;
import OrderlyAPI.Expo2025.Entities.HistorialPedido.HistorialPedidoEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
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
    private double Descuento;

    @Column(name = "TOTAL")
    private double Total;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<HistorialPedidoEntity> factura;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDPEDIDO", referencedColumnName = "IDPEDIDO")
    private PedidoEntity pedido;

    // NUEVA RELACIÓN CON ESTADO FACTURA
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDESTADOFACTURA", referencedColumnName = "IDESTADOFACTURA")
    private EstadoFacturaEntity estadoFactura;
}