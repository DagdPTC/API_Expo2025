package OrderlyAPI.Expo2025.Repositories.HistorialPedido;

import OrderlyAPI.Expo2025.Entities.HistorialPedido.HistorialPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialPedidoRepository extends JpaRepository<HistorialPedidoEntity, Long> {
}
