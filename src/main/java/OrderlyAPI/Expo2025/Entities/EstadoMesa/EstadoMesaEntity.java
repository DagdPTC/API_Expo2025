package OrderlyAPI.Expo2025.Entities.EstadoMesa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "El estadoMesa no puede ser nulo")
    @NotEmpty(message = "El estadoMesa no puede estar vacío")
    @Size(max = 50, message = "El estadoMesa no puede tener más de 50 caracteres")
    @Column(name = "ESTADOMESA")
    private String EstadoMesa;

    @NotNull(message = "El color del estadoMesa no puede ser nulo")
    @NotEmpty(message = "El color del estadoMesa no puede estar vacío")
    @Size(max = 50, message = "El color del estadoMesa no puede tener más de 50 caracteres")
    @Column(name = "COLORESTADOMESA")
    private String ColorEstadoMesa;
}
