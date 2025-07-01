package OrderlyAPI.Expo2025.Entities.ContactoPersona;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CONTACTOPERSONA")
@Setter @Getter @ToString @EqualsAndHashCode
public class ContactoPersonaEntity {
    @Id
    @Column(name = "IDCONTACTO")
    private Long Id;
    @Column(name = "IDPERSONA")
    private Long IdPersona;
    @Column(name = "TIPOCONTACTO")
    private String tipoContacto;
    @Column(name = "VALORCONTACTO")
    private String valorContacto;
    @Column(name = "ESPRINCIPAL")
    private boolean esPrincipal;
}
