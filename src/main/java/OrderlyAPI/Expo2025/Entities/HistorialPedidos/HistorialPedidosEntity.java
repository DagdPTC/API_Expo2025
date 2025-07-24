package OrderlyAPI.Expo2025.Entities.HistorialPedidos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "HISTORIALPEDIDOS")
@Getter @Setter @ToString @EqualsAndHashCode
public class HistorialPedidosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historial_Pedidos_seq")
    @SequenceGenerator(name = "historial_Pedidos_seq", sequenceName = "historial_Pedidos_seq", allocationSize = 1)
    @Column(name = " IDHISTORIAL ")
    private Long Id;

    @NotNull(message = "El IdPedido no puede ser nulo")
    @Column(name = " IDPEDIDO ")
    private Long IdPedido;


    @Column(name = " NOMBRECLIENTE ")
    private String NombreCliente;

    @NotNull(message = "El IdEmpleado no puede ser nulo")
    @Column(name = " IDEMPLEADO ")
    private Long IdEmpleado;

    @NotNull(message = "El IdMesa no puede ser nulo")
    @Column(name = " IDMESA ")
    private Long IdMesa;


    @Column(name = " FECHAHISTORIAL ")
    private Date FechaHistorial;


    @Column(name = " RESERVACION ")
    private Boolean Reservacion;

    @NotNull(message = "El IdEstadoReserva no puede ser nulo")
    @Column(name = " IDESTADORESERVA ")
    private Long idEstadoReserva;

    @NotNull(message = "El IdPlatillo no puede ser nulo")
    @Column(name = " IDPLATILLO ")
    private Long IdPlatillo;


    @Column(name = " CANTIDAD ")
    private Number Cantidad;


    @Column(name = " PRECIOUNITARIO ")
    private Double PrecioUnitario;


    @Column(name = " SUBTOTAL ")
    private Double Subtotal;


    @Column(name = " PROPINA ")
    private Double Propina;


    @Column(name = " DESCUENTO ")
    private Double Descuento;


    @Column(name = " TOTAL ")
    private Double Total;

}
