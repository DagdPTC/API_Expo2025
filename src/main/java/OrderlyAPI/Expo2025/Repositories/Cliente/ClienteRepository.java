package OrderlyAPI.Expo2025.Repositories.Cliente;

import OrderlyAPI.Expo2025.Entities.Cliente.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
}
