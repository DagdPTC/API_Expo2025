package OrderlyAPI.Expo2025.Entities.EstadoReserva;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ESTADORESERVA")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoReservaEntity {
    @Id
    @Column(name = "IDESTADORESERVA")
    private Long Id;
    @Column(name = "NOMBREESTADO")
    private String NomEstado;
    @Column(name = "DESCRIPCION")
    private String Descripcion;
}
