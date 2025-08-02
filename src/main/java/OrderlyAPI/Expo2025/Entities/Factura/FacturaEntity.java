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

    @Column(name = "IDPEDIDO")
    private Long IdPedido;

    @Column(name = "DESCUENTO")
    private double Descuento;

    @Column(name = "TOTAL")
    private double Total;
}
