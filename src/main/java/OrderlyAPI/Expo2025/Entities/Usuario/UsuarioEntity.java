package OrderlyAPI.Expo2025.Entities.Usuario;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "USUARIO")
@Getter @Setter @ToString @EqualsAndHashCode
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_seq", allocationSize = 1)
    @Column(name = "USUARIOID")
    private Long Id;
    @Column(name = "NOMBREUSUARIO")
    private String nombre;
    @Column(name = "CONTRASEÑA")
    private String contrasenia;
    @Column(name = "ROLID")
    private Long rolId;
    @Column(name = "CORREO")
    private String correo;
}
