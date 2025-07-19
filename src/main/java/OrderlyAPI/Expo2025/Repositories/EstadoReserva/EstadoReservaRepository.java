package OrderlyAPI.Expo2025.Repositories.EstadoReserva;

import OrderlyAPI.Expo2025.Entities.EstadoReserva.EstadoReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoReservaRepository extends JpaRepository<EstadoReservaEntity, Long> {
}
