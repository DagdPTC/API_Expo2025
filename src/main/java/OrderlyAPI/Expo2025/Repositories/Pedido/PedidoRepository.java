package OrderlyAPI.Expo2025.Repositories.Pedido;

import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    @Query("SELECT COUNT(p) FROM PedidoEntity p WHERE p.empleado.id = :empleadoId")
    long countByEmpleadoId(@Param("empleadoId") Long empleadoId);
}

