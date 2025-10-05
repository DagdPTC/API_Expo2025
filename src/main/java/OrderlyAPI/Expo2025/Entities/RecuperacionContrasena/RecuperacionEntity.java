package OrderlyAPI.Expo2025.Entities.RecuperacionContrasena;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "RECUPERACIONCONTRASENA")
public class RecuperacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recuperacion_contrasena_seq")
    @SequenceGenerator(name = "recuperacion_contrasena_seq", sequenceName = "recuperacion_contrasena_seq", allocationSize = 1)
    @Column(name = "IDRECUPERACION")
    private Long idRecuperacion;

    @Column(name = "USUARIOID", nullable = false)
    private Long usuarioId;

    @Column(name = "CODIGOHASH", nullable = false, length = 255)
    private String codigoHash;

    @Column(name = "INTENTOS", nullable = false)
    private Integer intentos = 0;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRA", nullable = false)
    private Date expira;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;  // PENDIENTE | VERIFICADO | USADO | EXPIRADO

    @Column(name = "RESETTOKEN", length = 255)
    private String resetToken;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RESETTOKENEXPIRA")
    private Date resetTokenExpira;

    @Column(name = "CANAL", nullable = false, length = 20)
    private String canal = "EMAIL";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREADOEN", nullable = false)
    private Date creadoEn;

    @Column(name = "IPSOLICITANTE", length = 64)
    private String ipSolicitante;
}