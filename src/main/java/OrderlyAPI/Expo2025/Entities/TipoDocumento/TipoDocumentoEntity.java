package OrderlyAPI.Expo2025.Entities.TipoDocumento;

import OrderlyAPI.Expo2025.Entities.DocumentoIdentidad.DocumentoIdentidadEntity;
import OrderlyAPI.Expo2025.Entities.Persona.PersonaEntity;
import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "TIPODOCUMENTO")
@Getter
@Setter@ToString
public class TipoDocumentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_Document_seq")
    @SequenceGenerator(name = "tipo_Document_seq", sequenceName = "tipo_Document_seq", allocationSize = 1)
    @Column(name = "IDTIPODOCUMENTO")
    private Long IdTipoDoc;

    @Column(name = "TIPODOCUMENTO", unique = true)
    private String TipoDoc;

    @OneToMany(mappedBy = "tipodocumento", cascade = CascadeType.ALL)
    private List<DocumentoIdentidadEntity> documento;
}
