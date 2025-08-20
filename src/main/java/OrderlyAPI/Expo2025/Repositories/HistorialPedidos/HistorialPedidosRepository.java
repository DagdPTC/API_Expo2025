package OrderlyAPI.Expo2025.Repositories.HistorialPedidos;

import OrderlyAPI.Expo2025.Entities.HistorialPedido.HistorialPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialPedidosRepository extends JpaRepository<HistorialPedidoEntity, Long> {
}
