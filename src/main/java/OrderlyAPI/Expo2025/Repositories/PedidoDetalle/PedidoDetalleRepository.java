package OrderlyAPI.Expo2025.Repositories.PedidoDetalle;

import OrderlyAPI.Expo2025.Entities.PedidoDetalle.PedidoDetalleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalleEntity, Long> {
}
