package OrderlyAPI.Expo2025.Repositories.Oferta;

import OrderlyAPI.Expo2025.Entities.Ofertas.OfertasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfertasRepository extends JpaRepository<OfertasEntity, Long> {
}
