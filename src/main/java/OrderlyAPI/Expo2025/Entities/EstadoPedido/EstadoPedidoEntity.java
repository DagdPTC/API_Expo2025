package OrderlyAPI.Expo2025.Entities.EstadoPedido;

import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Entities.Reserva.ReservaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "ESTADOPEDIDO")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoPedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_Pedido_seq")
    @SequenceGenerator(name = "estado_Pedido_seq", sequenceName = "estado_Pedido_seq", allocationSize = 1)
    @Column(name = "IDESTADOPEDIDO")
    private Long Id;

    @Column(name = "NOMBREESTADO", unique = true)
    private String NomEstado;

    @OneToMany(mappedBy = "estpedido", cascade = CascadeType.ALL)
        private List<PedidoEntity> estpedido;
}
