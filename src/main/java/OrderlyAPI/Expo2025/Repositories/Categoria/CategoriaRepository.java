package OrderlyAPI.Expo2025.Repositories.Categoria;

import OrderlyAPI.Expo2025.Entities.Categoria.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
}
