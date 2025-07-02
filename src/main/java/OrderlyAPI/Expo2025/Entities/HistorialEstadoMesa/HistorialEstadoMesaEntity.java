package OrderlyAPI.Expo2025.Entities.HistorialEstadoMesa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(name = "HISTORIALESTADOMESA")
@Getter @Setter @ToString @EqualsAndHashCode
public class HistorialEstadoMesaEntity {
    @Id
    @Column(name = "IDHISTORIALESTADOMESA")
    private Long Id;
    @Column(name = "IDMESA")
    private Long IdMesa;
    @Column(name = "IDESTADOMESA")
    private Long IdEstadoMesa;
    @Column(name = "FECHAINICIO")
    private Timestamp FInicio;
    @Column(name = "FECHAFIN")
    private Timestamp FFin;
    @Column(name = "IDEMPLEADO")
    private Long IdEmpleado;
}
