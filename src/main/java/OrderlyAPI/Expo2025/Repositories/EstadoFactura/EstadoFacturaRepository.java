package OrderlyAPI.Expo2025.Repositories.EstadoFactura;

import OrderlyAPI.Expo2025.Entities.EstadoFactura.EstadoFacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface EstadoFacturaRepository extends JpaRepository<EstadoFacturaEntity, Long>,
        JpaSpecificationExecutor<EstadoFacturaEntity> {

}