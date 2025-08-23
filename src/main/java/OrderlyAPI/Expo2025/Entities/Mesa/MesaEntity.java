package OrderlyAPI.Expo2025.Entities.Mesa;

import OrderlyAPI.Expo2025.Entities.Categoria.CategoriaEntity;
import OrderlyAPI.Expo2025.Entities.EstadoMesa.EstadoMesaEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Entities.Reserva.ReservaEntity;
import OrderlyAPI.Expo2025.Entities.TipoMesa.TipoMesaEntity;
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
@Table(name = "MESA")
@Getter @Setter @ToString @EqualsAndHashCode
public class MesaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mesa_seq")
    @SequenceGenerator(name = "mesa_seq", sequenceName = "mesa_seq", allocationSize = 1)
    @Column(name = "IDMESA")
    private Long Id;

    @Column(name = "NOMBREMESA", unique = true)
    private String NomMesa;

    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL)
    private List<ReservaEntity> mesa;

    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL)
    private List<PedidoEntity> mesas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDTIPOMESA", referencedColumnName = "IDTIPOMESA")
    private TipoMesaEntity tipmesa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDESTADOMESA", referencedColumnName = "IDESTADOMESA")
    private EstadoMesaEntity estmesa;
}
