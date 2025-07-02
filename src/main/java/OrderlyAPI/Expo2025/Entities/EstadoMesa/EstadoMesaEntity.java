package OrderlyAPI.Expo2025.Entities.EstadoMesa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ESTADOMESA")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoMesaEntity {
    @Id
    @Column(name = "IDESTADOMESA")
    private Long Id;
    @Column(name = "ESTADOMESA")
    private String EstadoMesa;
    @Column(name = "COLORESTADOMESA")
    private String ColorEstadoMesa;
}
