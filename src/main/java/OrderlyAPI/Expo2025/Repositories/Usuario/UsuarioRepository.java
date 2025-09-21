package OrderlyAPI.Expo2025.Repositories.Usuario;

import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}
