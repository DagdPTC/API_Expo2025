package OrderlyAPI.Expo2025.Entities.Empleado;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
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

    @Column(name = "IDPERSONA", unique = true)
    private Long IdPersona;

    @Column(name = "USUARIOID", unique = true)
    private Long IdUsuario;

    @Column(name = "FECHACONTRATACION")
    private LocalDateTime FContratacion;
}
