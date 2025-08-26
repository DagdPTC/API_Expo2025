package OrderlyAPI.Expo2025.Entities.HistorialPedido;

import OrderlyAPI.Expo2025.Entities.Factura.FacturaEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "HISTORIALPEDIDO")
@Getter @Setter @ToString @EqualsAndHashCode
public class HistorialPedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historial_Pedido_seq")
    @SequenceGenerator(name = "historial_Pedido_seq", sequenceName = "historial_Pedido_seq", allocationSize = 1)
    @Column(name = "IDHISTORIALPEDIDO")
    private Long Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDPEDIDO", referencedColumnName = "IDPEDIDO")
    private PedidoEntity pedidos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDFACTURA", referencedColumnName = "IDFACTURA")
    private FacturaEntity factura;
}
