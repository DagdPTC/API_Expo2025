package OrderlyAPI.Expo2025.Entities.MetodoPago;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "METODOPAGO")
@Getter @Setter @ToString @EqualsAndHashCode
public class MetodoPagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metodoPago_seq")
    @SequenceGenerator(name = "metodoPago_seq", sequenceName = "metodoPago_seq", allocationSize = 1)
    @Column(name = "IDMETODOPAGO")
    private Long Id;
    @Column(name = "NOMBREMETODO")
    private String NomMetodo;
}
