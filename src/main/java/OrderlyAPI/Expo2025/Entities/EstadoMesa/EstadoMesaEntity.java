package OrderlyAPI.Expo2025.Entities.EstadoMesa;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ESTADOMESA")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoMesaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_Mesa_seq")
    @SequenceGenerator(name = "estado_Mesa_seq", sequenceName = "estado_Mesa_seq", allocationSize = 1)
    @Column(name = "IDESTADOMESA")
    private Long Id;
    @Column(name = "ESTADOMESA")
    private String EstadoMesa;
    @Column(name = "COLORESTADOMESA")
    private String ColorEstadoMesa;
}
