package OrderlyAPI.Expo2025.Entities.Platillo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "PLATILLO")
@Getter @Setter @ToString @EqualsAndHashCode
public class PlatilloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platillo_seq")
    @SequenceGenerator(name = "platillo_seq", sequenceName = "platillo_seq", allocationSize = 1)
    @Column(name = "IDPLATILLO")
    private Long Id;

    @NotNull(message = "El nombre del platillo no puede ser nulo")
    @NotEmpty(message = "El nombre del platillo no puede estar vacío")
    @Size(max = 50, message = "El nombre del platillo no puede tener más de 50 caracteres")
    @Column(name = "NOMBREPLATILLO")
    private String NomPlatillo;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    @Column(name = "DESCRIPCION")
    private String Descripcion;

    @NotNull(message = "El precio no puede ser nulo")
    @Min(value = 1, message = "El precio debe ser mayor que 0")
    @Column(name = "PRECIO")
    private double Precio;

    @NotNull(message = "El tiempo de preparación no puede ser nulo")
    @Min(value = 1, message = "El tiempo de preparación debe ser mayor que 0")
    @Column(name = "TIEMPOPREPARACION")
    private Long TiempoPreparacion;
}
