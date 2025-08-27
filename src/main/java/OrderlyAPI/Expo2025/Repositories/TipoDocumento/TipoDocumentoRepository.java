package OrderlyAPI.Expo2025.Repositories.TipoDocumento;

import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumentoEntity, Long> {
}
