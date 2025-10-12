package OrderlyAPI.Expo2025.Repositories.Factura;

import OrderlyAPI.Expo2025.Entities.Factura.FacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<FacturaEntity, Long> {
    boolean existsByPedido_Id(Long idPedido);
    Optional<FacturaEntity> findByPedido_Id(Long idPedido);
}
