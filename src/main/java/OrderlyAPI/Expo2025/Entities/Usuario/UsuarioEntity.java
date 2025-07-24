package OrderlyAPI.Expo2025.Entities.Usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "El nombre de usuario no puede ser nulo")
    @NotEmpty(message = "El nombre de usuario no puede estar vacío")
    @Size(max = 50, message = "El nombre de usuario no puede tener más de 50 caracteres")
    @Column(name = "NOMBREUSUARIO")
    private String nombre;

    @NotNull(message = "La contraseña no puede ser nula")
    @NotEmpty(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 8, message = "La contraseña debe tener 8 caracteres")
    @Column(name = "CONTRASEÑA")
    private String contrasenia;

    @NotNull(message = "El rol ID no puede ser nulo")
    @Column(name = "ROLID")
    private Long rolId;

    @NotNull(message = "El correo no puede ser nulo")
    @NotEmpty(message = "El correo no puede estar vacío")
    @Email(message = "El correo debe tener un formato válido")
    @Column(name = "CORREO")
    private String correo;
}
