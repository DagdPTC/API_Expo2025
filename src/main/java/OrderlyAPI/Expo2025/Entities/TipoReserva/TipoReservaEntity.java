package OrderlyAPI.Expo2025.Entities.TipoReserva;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TIPORESERVA")
@Getter @Setter @ToString @EqualsAndHashCode
public class TipoReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoReserva_seq")
    @SequenceGenerator(name = "tipoReserva_seq", sequenceName = "tipoReserva_seq", allocationSize = 1)
    @Column(name = "IDTIPORESERVA")
    private Long Id;

    @NotNull(message = "El nombre del tipo de reserva no puede ser nulo")
    @NotEmpty(message = "El nombre del tipo de reserva no puede estar vacío")
    @Size(max = 15, message = "El nombre del tipo de reserva no puede tener más de 15 caracteres")
    @Column(name = "NOMBRETIPO")
    private String NomTipo;
}
