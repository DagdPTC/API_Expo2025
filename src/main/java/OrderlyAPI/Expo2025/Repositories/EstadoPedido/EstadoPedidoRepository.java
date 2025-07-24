package OrderlyAPI.Expo2025.Repositories.EstadoPedido;

import OrderlyAPI.Expo2025.Entities.EstadoPedido.EstadoPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoPedidoRepository extends JpaRepository<EstadoPedidoEntity, Long> {
    List<EstadoPedidoEntity> findAll();  // Esto debería devolver todos los registros

}
