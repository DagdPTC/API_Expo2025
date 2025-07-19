package OrderlyAPI.Expo2025.Repositories.TipoMesa;

import OrderlyAPI.Expo2025.Entities.TipoMesa.TipoMesaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoMesaRepository extends JpaRepository<TipoMesaEntity, Long> {
}
