package OrderlyAPI.Expo2025.Entities.DocumentoIdentidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "DOCUMENTOIDENTIDAD")
@Getter @Setter @ToString @EqualsAndHashCode
public class DocumentoIdentidadEntity {
    @Id
    @Column(name = "IDDOCUMENTO")
    private Long Id;
    @Column(name = "IDPERSONA")
    private Long Idpersona;
    @Column(name = "TIPODOCUMENTO")
    private String tipoDoc;
    @Column(name = "NUMERODOCUMENTO")
    private String numDoc;
}
