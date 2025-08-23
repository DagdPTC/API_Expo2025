package OrderlyAPI.Expo2025.Entities.Empleado;

import OrderlyAPI.Expo2025.Entities.DocumentoIdentidad.DocumentoIdentidadEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Entities.Persona.PersonaEntity;
import OrderlyAPI.Expo2025.Entities.Reserva.ReservaEntity;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "EMPLEADO")
@Getter @Setter @ToString @EqualsAndHashCode
public class EmpleadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empleado_seq")
    @SequenceGenerator(name = "empleado_seq", sequenceName = "empleado_seq", allocationSize = 1)
    @Column(name = "IDEMPLEADO")
    private Long Id;

    @Column(name = "FECHACONTRATACION")
    private LocalDateTime FContratacion;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
    private List<PedidoEntity> empleado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USUARIOID", referencedColumnName = "USUARIOID")
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDPERSONA", referencedColumnName = "IDPERSONA")
    private PersonaEntity persona;
}
