package OrderlyAPI.Expo2025.Repositories.HistorialPedido;

import OrderlyAPI.Expo2025.Entities.HistorialPedido.HistorialPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialPedidoRepository extends JpaRepository<HistorialPedidoEntity, Long> {
    // ¿ya existe un historial con esa factura?
    boolean existsByFactura_Id(Long idFactura);

    // ¿esa factura está usada por otro historial diferente al que actualizo?
    boolean existsByFactura_IdAndIdNot(Long idFactura, Long idHistorial);
}
