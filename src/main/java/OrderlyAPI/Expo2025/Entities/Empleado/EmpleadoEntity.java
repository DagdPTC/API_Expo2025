package OrderlyAPI.Expo2025.Entities.Empleado;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "EMPLEADO")
@Getter @Setter @ToString @EqualsAndHashCode
public class EmpleadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empleado_seq")
    @SequenceGenerator(name = "empleado_seq", sequenceName = "empleado_seq", allocationSize = 1)
    @Column(name = "IDEMPLEADO")
    private Long Id;

    @NotNull(message = "El IdPersona no puede ser nulo")
    @Column(name = "IDPERSONA")
    private Long IdPersona;

    @NotNull(message = "El IdUsuario no puede ser nulo")
    @Column(name = "USUARIOID")
    private Long IdUsuario;

    @NotNull(message = "La fecha de contratacion no puede ser nulo")
    @Column(name = "FECHACONTRATACION")
    private Date FContratacion;

    @NotNull(message = "La direccion no puede ser nulo")
    @Size(max = 255, message = "La direccion no puede excederse de 255 caracteres")
    @Column(name = "DIRECCION")
    private String direccion;
}
