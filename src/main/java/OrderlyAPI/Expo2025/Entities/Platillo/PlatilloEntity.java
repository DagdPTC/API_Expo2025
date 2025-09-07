package OrderlyAPI.Expo2025.Entities.Platillo;

import OrderlyAPI.Expo2025.Entities.Categoria.CategoriaEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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

    // URL pública de Cloudinary (secure_url)
    @Column(name = "IMAGENURL")
    private String imagenUrl;

    // Nuevo: publicId de Cloudinary para gestionar la imagen (borrar / transformar)
    @Column(name = "PUBLICID")
    private String publicId;

    @OneToMany(mappedBy = "platillo", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PedidoEntity> pedidos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDCATEGORIA", referencedColumnName = "IDCATEGORIA", nullable = false)
    private CategoriaEntity categoria;
}
