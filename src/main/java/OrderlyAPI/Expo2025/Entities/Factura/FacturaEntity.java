package OrderlyAPI.Expo2025.Entities.Factura;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name = "IDFACTURA")
    private Long Id;
    @Column(name = "NUMEROFACTURA")
    private String NumFactura;
    @Column(name = "IDPEDIDO")
    private Long IdPedido;
    @Column(name = "FECHAFACTURA")
    private Date FFactura;
    @Column(name = "SUBTOTAL")
    private double SubTotal;
    @Column(name = "IMPUESTO")
    private double Impuesto;
    @Column(name = "DESCUENTO")
    private double Descuento;
    @Column(name = "PROPINA")
    private double Propina;
    @Column(name = "TOTAL")
    private double Total;
    @Column(name = "IDEMPLEADO")
    private Long IdEmpleado;
}
