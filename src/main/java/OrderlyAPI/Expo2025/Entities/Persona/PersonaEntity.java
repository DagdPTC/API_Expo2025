package OrderlyAPI.Expo2025.Entities.Persona;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "PERSONA")
@Getter @Setter @ToString @EqualsAndHashCode
public class PersonaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persona_seq")
    @SequenceGenerator(name = "persona_seq", sequenceName = "persona_seq", allocationSize = 1)
    @Column(name = "IDPERSONA")
    private Long Id;

    @NotNull(message = "El Primer nombre no puede ser nulo")
    @NotEmpty(message = "El Primer nombre no puede estar vacío")
    @Size(max = 20, message = "El Primer nombre no puede tener más de 20 caracteres")
    @Column(name = "PRIMERNOMBRE")
    private String Pnombre;

    @NotNull(message = "El Segundo nombre no puede ser nulo")
    @NotEmpty(message = "El Segundo nombre no puede estar vacío")
    @Size(max = 20, message = "El Segundo nombre no puede tener más de 20 caracteres")
    @Column(name = "SEGUNDONOMBRE")
    private String Snombre;

    @NotNull(message = "El Apellido Paterno no puede ser nulo")
    @NotEmpty(message = "El Apellido Paterno no puede estar vacío")
    @Size(max = 20, message = "El Apellido Paterno no puede tener más de 20 caracteres")
    @Column(name = "APELLIDOPATERNO")
    private String apellidoP;

    @NotNull(message = "El Apellido Materno no puede ser nulo")
    @NotEmpty(message = "El Apellido Materno no puede estar vacío")
    @Size(max = 20, message = "El Apellido Materno no puede tener más de 20 caracteres")
    @Column(name = "APELLIDOMATERNO")
    private String apellidoM;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    @Column(name = "FECHANACIMIENTO")
    private Date fechaN;
}
