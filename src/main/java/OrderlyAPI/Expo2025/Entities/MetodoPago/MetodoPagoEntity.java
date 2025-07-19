package OrderlyAPI.Expo2025.Entities.MetodoPago;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "METODOPAGO")
@Getter @Setter @ToString @EqualsAndHashCode
public class MetodoPagoEntity {
    @Id
    @Column(name = "IDMETODOPAGO")
    private Long Id;
    @Column(name = "NOMBREMETODO")
    private String NomMetodo;
}
