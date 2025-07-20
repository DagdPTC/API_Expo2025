package OrderlyAPI.Expo2025.Entities.Empleado;

import jakarta.persistence.*;
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
    @Column(name = "IDPERSONA")
    private Long IdPersona;
    @Column(name = "USUARIOID")
    private Long IdUsuario;
    @Column(name = "FECHACONTRATACION")
    private Date FContratacion;
    @Column(name = "DIRECCION")
    private String direccion;
}
