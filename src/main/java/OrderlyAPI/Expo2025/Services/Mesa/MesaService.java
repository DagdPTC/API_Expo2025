package OrderlyAPI.Expo2025.Services.Mesa;

import OrderlyAPI.Expo2025.Entities.EstadoMesa.EstadoMesaEntity;
import OrderlyAPI.Expo2025.Entities.Mesa.MesaEntity;
import OrderlyAPI.Expo2025.Entities.TipoMesa.TipoMesaEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.MesaDTO;
import OrderlyAPI.Expo2025.Repositories.Mesa.MesaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional; // <-- importante
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@Slf4j
@CrossOrigin
public class MesaService {

    @Autowired
    private MesaRepository repo;

    @PersistenceContext
    EntityManager entityManager;

    public Page<MesaDTO> getAllMesas(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<MesaEntity> mesas = repo.findAll(pageable);
        return mesas.map(this::convertirAMesasDTO);
    }

    public MesaDTO createMesa(MesaDTO mesaDTO){
        if (mesaDTO == null || mesaDTO.getNomMesa() == null || mesaDTO.getNomMesa().trim().isEmpty()){
            throw new IllegalArgumentException("La mesa no puede ser nula y debe tener nombre.");
        }
        MesaEntity mesaEntity = convertirAMesasEntity(mesaDTO);
        MesaEntity mesaGuardado = repo.save(mesaEntity);
        return convertirAMesasDTO(mesaGuardado);
    }

    @Transactional
    public MesaDTO updateMesa(Long id, MesaDTO mesaDTO){
        MesaEntity mesa = repo.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Mesa no encontrada"));

        // Actualiza únicamente lo que venga en el DTO
        if (mesaDTO.getNomMesa() != null && !mesaDTO.getNomMesa().trim().isEmpty()){
            mesa.setNomMesa(mesaDTO.getNomMesa().trim());
        }

        if (mesaDTO.getIdTipoMesa() != null){
            mesa.setTipmesa(entityManager.getReference(TipoMesaEntity.class, mesaDTO.getIdTipoMesa()));
        }

        if (mesaDTO.getIdEstadoMesa() != null){
            mesa.setEstmesa(entityManager.getReference(EstadoMesaEntity.class, mesaDTO.getIdEstadoMesa()));
        }

        // save() + flush() para forzar escritura inmediata en la DB
        MesaEntity actualizado = repo.save(mesa);
        entityManager.flush();

        return convertirAMesasDTO(actualizado);
    }

    @Transactional
    public MesaDTO actualizarEstado(Long id, Long estadoId) {
        MesaEntity mesa = repo.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Mesa no encontrada"));

        // Solo actualiza el estado
        mesa.setEstmesa(entityManager.getReference(EstadoMesaEntity.class, estadoId));

        MesaEntity actualizado = repo.save(mesa);
        entityManager.flush();

        return convertirAMesasDTO(actualizado);
    }


    public boolean deleteMesa(Long id){
        MesaEntity objMesa = repo.findById(id).orElse(null);
        if (objMesa != null){
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public MesaEntity convertirAMesasEntity(MesaDTO mesa){
        MesaEntity e = new MesaEntity();
        e.setId(mesa.getId());
        e.setNomMesa(mesa.getNomMesa());
        if (mesa.getIdEstadoMesa() != null){
            e.setEstmesa(entityManager.getReference(EstadoMesaEntity.class, mesa.getIdEstadoMesa()));
        }
        if (mesa.getIdTipoMesa() != null){
            e.setTipmesa(entityManager.getReference(TipoMesaEntity.class, mesa.getIdTipoMesa()));
        }
        return e;
    }

    public MesaDTO convertirAMesasDTO(MesaEntity mesa){
        MesaDTO dto = new MesaDTO();
        dto.setId(mesa.getId());
        dto.setNomMesa(mesa.getNomMesa());
        if (mesa.getTipmesa() != null) dto.setIdTipoMesa(mesa.getTipmesa().getId());
        if (mesa.getEstmesa() != null) dto.setIdEstadoMesa(mesa.getEstmesa().getId());
        return dto;
    }
}
