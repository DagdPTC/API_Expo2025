package OrderlyAPI.Expo2025.Repositories.Platillo;

import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatilloRepository extends JpaRepository<PlatilloEntity, Long> {
}
