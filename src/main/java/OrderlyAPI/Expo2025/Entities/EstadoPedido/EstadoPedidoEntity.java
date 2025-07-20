package OrderlyAPI.Expo2025.Entities.EstadoPedido;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ESTADOPEDIDO")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoPedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estadoPedido_seq")
    @SequenceGenerator(name = "estadoPedido_seq", sequenceName = "estadoPedido_seq", allocationSize = 1)
    @Column(name = "IDESTADOPEDIDO")
    private Long Id;
    @Column(name = "NOMBREESTADO")
    private String NomEstado;
}
