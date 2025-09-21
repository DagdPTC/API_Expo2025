package OrderlyAPI.Expo2025.Repositories.Pedido;

import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
}