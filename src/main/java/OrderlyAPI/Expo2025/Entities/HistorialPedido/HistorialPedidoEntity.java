package OrderlyAPI.Expo2025.Entities.HistorialPedido;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "El campo nompuedes ernulo")
    private Long IdPlatillo;
    @Column(name = "IDPEDIDO", unique = true)
    @NotBlank(message = "El campo no puedde ser nulo")
    private Long IdPedido;
    @Column(name = "IDFACTURA", unique = true)
    @NotBlank(message = "El campo no puede ser nulo")
    private Long IdFactura;
}
