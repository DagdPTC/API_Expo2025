package OrderlyAPI.Expo2025.Entities.UbicacionRestaurante;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "UBICACIONRESTAURANTE")
@Getter @Setter @ToString @EqualsAndHashCode
public class UbicacionRestauranteEntity {
    @Id
    @Column(name = "IDUBICACION")
    private Long Id;
    @Column(name = "NOMBREUBICACION")
    private String NomUbicacion;
    @Column(name = "DESCRIPCION")
    private String Descripcion;
}
