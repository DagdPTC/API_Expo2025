package OrderlyAPI.Expo2025.Entities.Cliente;

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
@Table(name = "CLIENTE")
@Getter @Setter @ToString @EqualsAndHashCode
public class ClienteEntity {
    @Id
    @Column(name = "IDCLIENTE")
    private Long Id;
    @Column(name = "IDPERSONA")
    private Long IdPersona;
    @Column(name = "FECHAREGISTRO")
    private Date FRegistro;
}
