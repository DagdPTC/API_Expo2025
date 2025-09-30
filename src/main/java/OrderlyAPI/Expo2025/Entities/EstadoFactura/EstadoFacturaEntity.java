package OrderlyAPI.Expo2025.Entities.EstadoFactura;

import OrderlyAPI.Expo2025.Entities.Factura.FacturaEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Entity
@Table(name = "ESTADOFACTURA")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoFacturaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_factura_seq")
    @SequenceGenerator(name = "estado_factura_seq", sequenceName = "estado_factura_seq", allocationSize = 1)
    @Column(name = "IDESTADOFACTURA")
    private Long Id;

    @Column(name = "ESTADOFACTURA", nullable = false, unique = true, length = 100)
    private String EstadoFactura;

    @OneToMany(mappedBy = "estadoFactura", cascade = CascadeType.ALL)
    private List<FacturaEntity> facturas;
}
