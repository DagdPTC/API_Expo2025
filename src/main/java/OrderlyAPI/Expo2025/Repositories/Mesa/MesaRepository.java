package OrderlyAPI.Expo2025.Repositories.Mesa;

import OrderlyAPI.Expo2025.Entities.Mesa.MesaEntity;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesaRepository extends JpaRepository<MesaEntity, Long> {
}
