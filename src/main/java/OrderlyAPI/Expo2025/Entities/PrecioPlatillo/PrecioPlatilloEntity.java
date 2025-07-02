package OrderlyAPI.Expo2025.Entities.PrecioPlatillo;

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
@Table(name = "PRECIOPLATILLO")
@Getter @Setter @ToString @EqualsAndHashCode
public class PrecioPlatilloEntity {
    @Id
    @Column(name = "IDPRECIOPLATILLO")
    private Long Id;
    @Column(name = "IDPLATILLO")
    private Long IdPlatillo;
    @Column(name = "PRECIOUNITARIO")
    private double PrecioUnitario;
    @Column(name = "FECHAINICIO")
    private Date FInicio;
    @Column(name = "FECHAFIN")
    private Date FFin;
}
