package OrderlyAPI.Expo2025.Repositories.MetodoPago;

import OrderlyAPI.Expo2025.Entities.MetodoPago.MetodoPagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPagoEntity, Long> {
}
