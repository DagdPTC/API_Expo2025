package OrderlyAPI.Expo2025.Repositories.Empleado;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {
}
