package OrderlyAPI.Expo2025.Repositories.Persona;

import OrderlyAPI.Expo2025.Entities.Persona.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {
}
