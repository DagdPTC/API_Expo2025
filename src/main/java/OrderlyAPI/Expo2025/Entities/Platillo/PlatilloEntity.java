package OrderlyAPI.Expo2025.Entities.Platillo;

import OrderlyAPI.Expo2025.Entities.Categoria.CategoriaEntity;
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

    @Column(name = "NOMBREPLATILLO", unique = true)
    private String NomPlatillo;

    @Column(name = "DESCRIPCION")
    private String Descripcion;

    @Column(name = "PRECIO")
    private double Precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDCATEGORIA", referencedColumnName = "IDCATEGORIA")
    private CategoriaEntity categoria;
}
