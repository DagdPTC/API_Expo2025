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

    @NotNull(message = "El IdPersona no puede ser nula")
    @Column(name = "IDPERSONA")
    private Long Idpersona;

    @NotNull(message = "El TipoDocumento no puede ser nulo")
    @NotEmpty(message = "El TipoDocumento no puede estar vacio")
    @Size(max = 15, message = "El TipoDocumento no puede mas de 15 caracteres")
    @Column(name = "TIPODOCUMENTO")
    private String tipoDoc;

    @NotNull(message = "El NumeroDocumento no puede ser nulo")
    @NotEmpty(message = "El NumeroDocumento no puede estar vacio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El numero de DUI debe contener 9 digitos numericos")
    @Column(name = "NUMERODOCUMENTO")
    private String numDoc;
}
