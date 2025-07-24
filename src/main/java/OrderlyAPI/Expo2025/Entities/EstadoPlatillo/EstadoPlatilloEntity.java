package OrderlyAPI.Expo2025.Entities.EstadoPlatillo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ESTADOPLATILLO")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoPlatilloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_Platillo_seq")
    @SequenceGenerator(name = "estado_Platillo_seq", sequenceName = "estado_Platillo_seq", allocationSize = 1)
    @Column(name = "IDESTADOPLATILLO")
    private Long Id;

    @NotNull(message = "El Nombre del estado no puede ser nulo")
    @NotEmpty(message = "El nombre del estado no puede estar vacío")
    @Size(max = 15, message = "El nombre del estado no puede tener más de 15 caracteres")
    @Column(name = "NOMBREESTADO")
    private String NomEstado;
}
