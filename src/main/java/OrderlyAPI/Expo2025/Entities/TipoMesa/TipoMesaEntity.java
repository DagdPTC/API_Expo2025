package OrderlyAPI.Expo2025.Entities.TipoMesa;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TIPOMESA")
@Setter @Getter @ToString @EqualsAndHashCode
public class TipoMesaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_Mesa_seq")
    @SequenceGenerator(name = "tipo_Mesa_seq", sequenceName = "tipo_Mesa_seq", allocationSize = 1)
    @Column(name = "IDTIPOMESA")
    private Long Id;

    @NotNull(message = "El nombre del tipo de mesa no puede ser nulo")
    @NotEmpty(message = "El nombre del tipo de mesa no puede estar vacío")
    @Column(name = "NOMBRETIPOMESA")
    private String Nombre;

    @NotNull(message = "La capacidad de personas no puede ser nula")
    @Min(value = 1, message = "La capacidad de personas debe ser al menos 1")
    @Column(name = "CAPACIDADPERSONAS")
    private Long CapacidadPersonas;

}
