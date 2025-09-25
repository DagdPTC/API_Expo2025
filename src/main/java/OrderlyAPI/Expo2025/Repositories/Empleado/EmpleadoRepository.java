package OrderlyAPI.Expo2025.Repositories.Empleado;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {

    // Buscar por el UsuarioId relacionado (propiedad anidada)
    Optional<EmpleadoEntity> findFirstByUsuario_Id(Long usuarioId);

    // Fallback por correo del usuario relacionado (propiedad anidada)
    Optional<EmpleadoEntity> findFirstByUsuario_CorreoIgnoreCase(String correo);
}
