package OrderlyAPI.Expo2025.Repositories.Usuario;

import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}
