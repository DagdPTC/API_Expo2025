package OrderlyAPI.Expo2025.Repositories.Reserva;

import OrderlyAPI.Expo2025.Entities.Reserva.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {
}
