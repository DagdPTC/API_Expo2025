package OrderlyAPI.Expo2025.Entities.Factura;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "FACTURA")
@Getter @Setter @ToString @EqualsAndHashCode
public class FacturaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "factura_seq")
    @SequenceGenerator(name = "factura_seq", sequenceName = "factura_seq", allocationSize = 1)
    @Column(name = "IDFACTURA")
    private Long Id;

    @NotNull(message = "El IdPedido no puede ser nulo")
    @Column(name = "IDPEDIDO")
    private Long IdPedido;

    @Min(value = 0, message = "El subtotal no puede ser negativo")
    @Column(name = "SUBTOTAL")
    private double SubTotal;

    @Min(value = 0, message = "El descuento no puede ser negativo")
    @Column(name = "DESCUENTO")
    private double Descuento;

    @Min(value = 0, message = "La propina no puede ser negativa")
    @Column(name = "PROPINA")
    private double Propina;

    @Min(value = 0, message = "El total no puede ser negativo")
    @Column(name = "TOTAL")
    private double Total;

    @NotNull(message = "El ID del empleado no puede ser nulo")
    @Column(name = "IDEMPLEADO")
    private Long IdEmpleado;
}
