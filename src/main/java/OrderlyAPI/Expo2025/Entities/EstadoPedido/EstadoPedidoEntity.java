package OrderlyAPI.Expo2025.Entities.EstadoPedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ESTADOPEDIDO")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoPedidoEntity {
    @Id
    @Column(name = "IDESTADOPEDIDO")
    private Long Id;
    @Column(name = "NOMBREESTADO")
    private String NomEstado;
    @Column(name = "DESCRIPCION")
    private String Descripcion;
}
