package OrderlyAPI.Expo2025.Entities.Ofertas;

import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "OFERTA")   // 👈 en mayúsculas como en Oracle
@Getter
@Setter
public class OfertasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oferta_seq")
    @SequenceGenerator(name = "oferta_seq", sequenceName = "oferta_SEQ", allocationSize = 1)
    @Column(name = "IDOFERTA")
    private Long id;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "PORCENTAJEDESCUENTO")
    private Double porcentajeDescuento;

    @Column(name = "PRECIOOFERTA")
    private Double precioOferta;

    @Column(name = "FECHAINICIO")
    private LocalDate fechaInicio;

    @Column(name = "FECHAFIN")
    private LocalDate fechaFin;

    @Column(name = "ACTIVA")
    private Boolean activa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDPLATILLO", nullable = false)  // FK
    private PlatilloEntity platillo;
}
