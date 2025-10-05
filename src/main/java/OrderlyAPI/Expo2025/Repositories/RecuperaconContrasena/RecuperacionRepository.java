package OrderlyAPI.Expo2025.Repositories.RecuperaconContrasena;

import OrderlyAPI.Expo2025.Entities.RecuperacionContrasena.RecuperacionEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface RecuperacionRepository extends JpaRepository<RecuperacionEntity, Long>{
    @Query(value = """
    SELECT * FROM RecuperacionContrasena r
     WHERE r.UsuarioId = :uid
       AND r.Estado = 'PENDIENTE'
     ORDER BY r.CreadoEn DESC
  """, nativeQuery = true)
    List<RecuperacionEntity> findPendientes(@Param("uid") Long usuarioId);

    @Modifying
    @Query(value = "UPDATE RecuperacionContrasena SET Intentos = Intentos + 1 WHERE IdRecuperacion = :id", nativeQuery = true)
    int incrementarIntentos(@Param("id") Long idRecuperacion);

    @Modifying
    @Query(value = "UPDATE RecuperacionContrasena SET Estado = :estado WHERE IdRecuperacion = :id", nativeQuery = true)
    int actualizarEstado(@Param("id") Long idRecuperacion, @Param("estado") String estado);

    @Query(value = """
    SELECT COUNT(*) FROM RecuperacionContrasena
     WHERE UsuarioId = :uid
       AND CreadoEn > (SYSTIMESTAMP - INTERVAL '1' HOUR)
  """, nativeQuery = true)
    int countUltimaHora(@Param("uid") Long usuarioId);
}
