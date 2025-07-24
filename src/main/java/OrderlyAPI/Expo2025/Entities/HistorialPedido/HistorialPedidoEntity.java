package OrderlyAPI.Expo2025.Entities.HistorialPedido;

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
    @NotBlank(message = "El campo no puede ser nulo")
    private Long Id;

    @Column(name = "IDPLATILLO")
    @NotNull(message = "El IdPlatillo no puede ser nulo")
    private Long IdPlatillo;

    @Column(name = "IDPEDIDO", unique = true)
    @NotNull(message = "El IdPedido no puede ser nulo")
    private Long IdPedido;

    @Column(name = "IDFACTURA", unique = true)
    @NotNull(message = "El IdFactura no puede ser nulo")
    private Long IdFactura;
}
