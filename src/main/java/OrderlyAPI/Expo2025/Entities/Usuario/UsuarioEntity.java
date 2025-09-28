package OrderlyAPI.Expo2025.Entities.Usuario;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.Persona.PersonaEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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
    private String nombreusuario;

    @Column(name = "CONTRASENA")
    private String contrasenia;

    @Column(name = "CORREO", unique = true)
    private String correo;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<EmpleadoEntity> usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLID", referencedColumnName = "ROLID")
    private RolEntity rol;
}
