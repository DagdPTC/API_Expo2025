package OrderlyAPI.Expo2025.Entities.Persona;

import OrderlyAPI.Expo2025.Entities.Categoria.CategoriaEntity;
import OrderlyAPI.Expo2025.Entities.DocumentoIdentidad.DocumentoIdentidadEntity;
import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PERSONA")
@Getter @Setter @ToString @EqualsAndHashCode
public class PersonaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persona_seq")
    @SequenceGenerator(name = "persona_seq", sequenceName = "persona_seq", allocationSize = 1)
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
    private String fechaN;

    @Column(name = "DIRECCION")
    private String Direccion;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL)
    private List<EmpleadoEntity> persona;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDDOCUMENTO", referencedColumnName = "IDDOCUMENTO")
    private DocumentoIdentidadEntity documento;
}
