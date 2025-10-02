package OrderlyAPI.Expo2025.Entities.Rol;

import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "ROL")
@Getter @Setter @ToString @EqualsAndHashCode
public class RolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rol_seq")
    @SequenceGenerator(name = "rol_seq", sequenceName = "rol_seq", allocationSize = 1)
    @Column(name = "ROLID")
    private Long Id;

    @Column(name = "ROL", unique = true)
    private String rol;

    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
    private List<UsuarioEntity> urol;
}
