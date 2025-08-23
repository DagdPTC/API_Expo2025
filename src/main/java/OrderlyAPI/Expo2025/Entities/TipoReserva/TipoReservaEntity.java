package OrderlyAPI.Expo2025.Entities.TipoReserva;

import OrderlyAPI.Expo2025.Entities.Persona.PersonaEntity;
import OrderlyAPI.Expo2025.Entities.Reserva.ReservaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "TIPORESERVA")
@Getter @Setter @ToString @EqualsAndHashCode
public class TipoReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoReserva_seq")
    @SequenceGenerator(name = "tipoReserva_seq", sequenceName = "tipoReserva_seq", allocationSize = 1)
    @Column(name = "IDTIPORESERVA")
    private Long Id;

    @Column(name = "NOMBRETIPO")
    private String NomTipo;

    @OneToMany(mappedBy = "tipreser", cascade = CascadeType.ALL)
    private List<ReservaEntity> tipreser;
}
