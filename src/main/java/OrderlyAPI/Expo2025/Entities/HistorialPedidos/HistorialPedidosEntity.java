package OrderlyAPI.Expo2025.Entities.HistorialPedidos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(name = "HISTORIALPEDIDOS")
@Getter @Setter @ToString @EqualsAndHashCode
public class HistorialPedidosEntity {
    @Id
    @Column(name = "IDHISTORIAL")
    private Long Id;
    @Column(name = "FECHAPEDIDO")
    private Timestamp FechaPedido;
    @Column(name = "DESCRIPCION")
    private String Descripcion;
    @Column(name = "IDEMPLEADO")
    private Long IdEmpleado;
    @Column(name = "IDCLIENTE")
    private Long IdCliente;
    @Column(name = "IDPLATILLO")
    private Long IdPlatillo;
}
