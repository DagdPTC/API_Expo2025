package OrderlyAPI.Expo2025.Repositories.EstadoMesa;

import OrderlyAPI.Expo2025.Entities.EstadoMesa.EstadoMesaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoMesaRepository extends JpaRepository<EstadoMesaEntity, Long> {
}
