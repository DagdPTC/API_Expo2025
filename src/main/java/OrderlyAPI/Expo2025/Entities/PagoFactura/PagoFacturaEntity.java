package OrderlyAPI.Expo2025.Entities.PagoFactura;

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
@Table(name = "PAGOFACTURA")
@Getter @Setter @ToString @EqualsAndHashCode
public class PagoFacturaEntity {
    @Id
    @Column(name = "IDPAGOFACTURA")
    private Long Id;
    @Column(name = "IDFACTURA")
    private Long IdFactura;
    @Column(name = "IDMETODOPAGO")
    private Long IdMetodoPago;
    @Column(name = "MONTOPAGADO")
    private double MontoPagado;
    @Column(name = "FECHAPAGO")
    private Timestamp FechaPago;
    @Column(name = "REFERENCIAPAGO")
    private String ReferenciaPago;
}
