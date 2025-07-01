package OrderlyAPI.Expo2025.Entities.Persona;

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
@Table(name = "PERSONA")
@Getter @Setter @ToString @EqualsAndHashCode
public class PersonaEntity {
    @Id
    @Column(name = "IDPERSONA")
    private Long Id;
    @Column(name = "PRIMERNOMBRE")
    private String Pnombre;
    @Column(name = "SEGUNDONOMBRE")
    private String Snombre;
    @Column(name = "APELLIDOPATERNO")
    private String apellidoP;
    @Column(name = "APELLIDOMATERNO")
    private String apellidoM;
    @Column(name = "FECHANACIMIENTO")
    private Date fechaN;
}
