package OrderlyAPI.Expo2025.Entities.DetallePedido;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "DETALLEPEDIDO")
@Getter @Setter @ToString @EqualsAndHashCode
public class DetallePedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detallePedido_seq")
    @SequenceGenerator(name = "detallePedido_seq", sequenceName = "detallePedido_seq", allocationSize = 1)
    @Column(name = "IDDETALLEPEDIDO")
    private Long Id;
    @Column(name = "IDPEDIDO")
    private Long Idpedido;
    @Column(name = "IDPLATILLO")
    private Long IdPlatillo;
    @Column(name = "CANTIDAD")
    private Long Cantidad;
    @Column(name = "PRECIOUNITARIO")
    private double PrecioUnitario;
    @Column(name = "OBSERVACIONES")
    private String Observaciones;
    @Column(name = "IDESTADOPLATILLO")
    private Long IdEstadoPlatillo;
}
