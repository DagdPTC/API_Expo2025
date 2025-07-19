package OrderlyAPI.Expo2025.Repositories.TipoReserva;

import OrderlyAPI.Expo2025.Entities.TipoReserva.TipoReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoReservaRepository extends JpaRepository<TipoReservaEntity, Long> {
}
