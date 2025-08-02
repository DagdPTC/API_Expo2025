package OrderlyAPI.Expo2025.Entities.DocumentoIdentidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documento_Identidad_seq")
    @SequenceGenerator(name = "documento_Identidad_seq", sequenceName = "documento_Identidad_seq", allocationSize = 1)
    @Column(name = "IDDOCUMENTO")
    private Long Id;

    @Column(name = "IDTIPODOCUMENTO", unique = true)
    private Long IdtipoDoc;

    @Column(name = "NUMERODOCUMENTO", unique = true)
    private String numDoc;
}
