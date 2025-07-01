package OrderlyAPI.Expo2025.Entities.PrecioIngrediente;

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
@Table(name = "PRECIOINGREDIENTE")
@Getter @Setter @ToString @EqualsAndHashCode
public class PrecioIngredienteEntity {
    @Id
    @Column(name = "IDPRECIOINGREDIENTE")
    private Long Id;
    @Column(name = "IDINGREDIENTE")
    private Long IdIngrediente;
    @Column(name = "PRECIO")
    private double Precio;
    @Column(name = "FECHAINICIO")
    private Date FInicio;
    @Column(name = "FECHAFIN")
    private Date FFin;
}
