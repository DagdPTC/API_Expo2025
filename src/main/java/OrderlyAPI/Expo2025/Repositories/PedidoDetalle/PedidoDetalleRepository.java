package OrderlyAPI.Expo2025.Repositories.PedidoDetalle;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import OrderlyAPI.Expo2025.Entities.PedidoDetalle.PedidoDetalleEntity;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalleEntity, Long> {

}
