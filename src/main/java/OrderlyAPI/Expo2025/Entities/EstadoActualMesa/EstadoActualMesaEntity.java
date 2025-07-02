package OrderlyAPI.Expo2025.Entities.EstadoActualMesa;

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
@Table(name = "ESTADOACTUALMESA")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoActualMesaEntity {
    @Id
    @Column(name = "IDESTADOACTUALMESA")
    private Long Id;
    @Column(name = "IDMESA")
    private Long IdMesa;
    @Column(name = "IDESTADOMESA")
    private Long IdEstadoMesa;
    @Column(name = "FECHAACTUALIZACION")
    private Timestamp FActualizacion;
}
