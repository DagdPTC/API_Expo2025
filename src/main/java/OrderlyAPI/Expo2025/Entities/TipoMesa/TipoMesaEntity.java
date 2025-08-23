package OrderlyAPI.Expo2025.Entities.TipoMesa;

import OrderlyAPI.Expo2025.Entities.Mesa.MesaEntity;
import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "TIPOMESA")
@Setter @Getter @ToString @EqualsAndHashCode
public class TipoMesaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_Mesa_seq")
    @SequenceGenerator(name = "tipo_Mesa_seq", sequenceName = "tipo_Mesa_seq", allocationSize = 1)
    @Column(name = "IDTIPOMESA")
    private Long Id;

    @Column(name = "NOMBRETIPOMESA")
    private String Nombre;

    @Column(name = "CAPACIDADPERSONAS")
    private Long CapacidadPersonas;

    @OneToMany(mappedBy = "tipmesa", cascade = CascadeType.ALL)
    private List<MesaEntity> tipmesa;
}
