package OrderlyAPI.Expo2025.Entities.Rol;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
}
