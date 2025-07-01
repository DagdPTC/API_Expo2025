package OrderlyAPI.Expo2025.Entities.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "USUARIO")
@Getter @Setter @ToString @EqualsAndHashCode
public class UsuarioEntity {
    @Id
    @Column(name = "USUARIOID")
    private Long Id;
    @Column(name = "NOMBREUSUARIO")
    private String nombre;
    @Column(name = "CONTRASEÑA")
    private String contrasenia;
    @Column(name = "ROLID")
    private Long rolId;
}
