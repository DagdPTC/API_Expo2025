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

    @NotNull(message = "El nombre de la mesa no puede ser nulo")
    @NotEmpty(message = "El nombre de la mesa no puede estar vacío")
    @Size(max = 15, message = "El nombre de la mesa no puede tener más de 15 caracteres")
    @Column(name = "NOMBREMESA")
    private String NomMesa;

    @NotNull(message = "El IdTipoMesa no puede ser nulo")
    @Column(name = "IDTIPOMESA")
    private Long IdTipoMesa;
}
