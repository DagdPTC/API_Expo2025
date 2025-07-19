package OrderlyAPI.Expo2025.Repositories.EstadoPlatillo;

import OrderlyAPI.Expo2025.Entities.EstadoPlatillo.EstadoPlatilloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoPlatilloRepository extends JpaRepository<EstadoPlatilloEntity, Long> {
}
