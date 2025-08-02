package OrderlyAPI.Expo2025.Entities.Mesa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "MESA")
@Getter @Setter @ToString @EqualsAndHashCode
public class MesaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mesa_seq")
    @SequenceGenerator(name = "mesa_seq", sequenceName = "mesa_seq", allocationSize = 1)
    @Column(name = "IDMESA")
    private Long Id;

    @Column(name = "NOMBREMESA", unique = true)
    private String NomMesa;

    @Column(name = "IDTIPOMESA")
    private Long IdTipoMesa;

    @Column(name = "IDESTADOMESA")
    private Long IdEstadoMesa;
}
