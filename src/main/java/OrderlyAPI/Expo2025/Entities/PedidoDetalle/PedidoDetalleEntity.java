package OrderlyAPI.Expo2025.Entities.PedidoDetalle;

import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "PEDIDODETALLE")
public class PedidoDetalleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_detalle_seq")
    @SequenceGenerator(
            name = "pedido_detalle_seq",
            sequenceName = "pedido_detalle_seq",   // <-- tu secuencia en Oracle
            allocationSize = 1
    )
    @Column(name = "IDDETALLE")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDPEDIDO", nullable = false)
    private PedidoEntity pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDPLATILLO", nullable = false)
    private PlatilloEntity platillo;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "PRECIOUNITARIO", nullable = false)
    private Double precioUnitario;
}
