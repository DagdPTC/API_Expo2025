package OrderlyAPI.Expo2025.Entities.Empleado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name = "IDEMPLEADO")
    private Long Id;
    @Column(name = "IDPERSONA")
    private Long IdPersona;
    @Column(name = "IDUSUARIO")
    private Long IdUsuario;
    @Column(name = "FECHACONTRATACION")
    private Date FContratacion;
    @Column(name = "DIRECCION")
    private String direccion;
}
