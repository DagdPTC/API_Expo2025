package OrderlyAPI.Expo2025.Entities.EstadoMesa;

import OrderlyAPI.Expo2025.Entities.Mesa.MesaEntity;
import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
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
@Table(name = "ESTADOMESA")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadoMesaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_Mesa_seq")
    @SequenceGenerator(name = "estado_Mesa_seq", sequenceName = "estado_Mesa_seq", allocationSize = 1)
    @Column(name = "IDESTADOMESA")
    private Long Id;

    @Column(name = "ESTADOMESA", unique = true)
    private String EstadoMesa;

    @Column(name = "COLORESTADOMESA", unique = true)
    private String ColorEstadoMesa;

    @OneToMany(mappedBy = "estmesa", cascade = CascadeType.ALL)
    private List<MesaEntity> estmesa;
}
