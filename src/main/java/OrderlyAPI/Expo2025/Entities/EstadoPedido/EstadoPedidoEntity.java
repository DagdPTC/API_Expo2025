package OrderlyAPI.Expo2025.Entities.EstadoPedido;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ESTADOPEDIDO")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoPedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_Pedido_seq")
    @SequenceGenerator(name = "estado_Pedido_seq", sequenceName = "estado_Pedido_seq", allocationSize = 1)
    @Column(name = "IDESTADOPEDIDO")
    private Long Id;

    @NotNull(message = "El nombre del estado no puede ser nulo")
    @NotEmpty(message = "El nombre del estado no puede estar vacío")
    @Size(max = 50, message = "El nombre del estado no puede tener más de 50 caracteres")
    @Column(name = "NOMBREESTADO")
    private String NomEstado;
}
