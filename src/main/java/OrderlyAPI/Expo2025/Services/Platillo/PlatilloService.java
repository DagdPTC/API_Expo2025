package OrderlyAPI.Expo2025.Services.Platillo;

import OrderlyAPI.Expo2025.Entities.Categoria.CategoriaEntity;
import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.PlatilloDTO;
import OrderlyAPI.Expo2025.Repositories.Platillo.PlatilloRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@Service
@CrossOrigin
public class PlatilloService {

    @Autowired
    private PlatilloRepository repo;

    @PersistenceContext
    EntityManager entityManager;

    public Page<PlatilloDTO> getAllPlatillos(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<PlatilloEntity> platillos = repo.findAll(pageable);
        return platillos.map(this::convertirAPlatillosDTO);
    }

    // NUEVO
    public PlatilloDTO getPlatilloById(Long id){
        PlatilloEntity p = repo.findById(id).orElseThrow(
                () -> new ExceptionDatoNoEncontrado("Platillo no encontrado")
        );
        return convertirAPlatillosDTO(p);
    }

    public PlatilloDTO createPlatillo(@Valid PlatilloDTO platilloDTO){
        PlatilloEntity entity = convertirAPlatillosEntity(platilloDTO);
        PlatilloEntity saved = repo.save(entity);
        return convertirAPlatillosDTO(saved);
    }

    public PlatilloDTO updatePlatillo(Long id, @Valid PlatilloDTO platillo){
        PlatilloEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Platillo no encontrado"));
        existente.setNomPlatillo(platillo.getNomPlatillo());
        existente.setDescripcion(platillo.getDescripcion());
        existente.setPrecio(platillo.getPrecio());
        if (platillo.getIdCate() != null) {
            existente.setCategoria(entityManager.getReference(CategoriaEntity.class, platillo.getIdCate()));
        }
        PlatilloEntity actualizado = repo.save(existente);
        return convertirAPlatillosDTO(actualizado);
    }

    public boolean deletePlatillo(Long id){
        if (repo.existsById(id)) { repo.deleteById(id); return true; }
        return false;
    }

    public PlatilloEntity convertirAPlatillosEntity(PlatilloDTO dto){
        PlatilloEntity e = new PlatilloEntity();
        e.setId(dto.getId());
        e.setNomPlatillo(dto.getNomPlatillo());
        e.setDescripcion(dto.getDescripcion());
        e.setPrecio(dto.getPrecio());
        if (dto.getIdCate()!=null) e.setCategoria(entityManager.getReference(CategoriaEntity.class, dto.getIdCate()));
        return e;
    }

    public PlatilloDTO convertirAPlatillosDTO(PlatilloEntity e){
        PlatilloDTO dto = new PlatilloDTO();
        dto.setId(e.getId());
        dto.setNomPlatillo(e.getNomPlatillo());
        dto.setDescripcion(e.getDescripcion());
        dto.setPrecio(e.getPrecio());
        if (e.getCategoria()!=null) dto.setIdCate(e.getCategoria().getId());
        return dto;
    }
}
