package OrderlyAPI.Expo2025.Services.OfertasService;

import OrderlyAPI.Expo2025.Entities.Ofertas.OfertasEntity;
import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.OfertasDTO;
import OrderlyAPI.Expo2025.Repositories.Oferta.OfertasRepository;
import OrderlyAPI.Expo2025.Repositories.Oferta.OfertasRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OfertasService {

    @Autowired
    private OfertasRepository repo;

    @PersistenceContext
    private EntityManager entityManager;

    // ===== GET ALL =====
    public Page<OfertasDTO> getAllOfertas(int page, int size) {
        return repo.findAll(PageRequest.of(page, size))
                .map(this::convertirAOfertasDTO);
    }

    // ===== CREATE =====
    public OfertasDTO createOfertas(@Valid OfertasDTO dto) {
        OfertasEntity entity = convertirAOfertasEntity(dto);
        return convertirAOfertasDTO(repo.save(entity));
    }

    // ===== UPDATE =====
    public OfertasDTO updateOfertas(Long id, @Valid OfertasDTO dto) {
        OfertasEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Oferta no encontrada"));

        existente.setDescripcion(dto.getDescripcion());
        existente.setPorcentajeDescuento(dto.getPorcentajeDescuento());
        existente.setPrecioOferta(dto.getPrecioOferta());
        existente.setFechaInicio(dto.getFechaInicio()); // mismo tipo en DTO y Entity
        existente.setFechaFin(dto.getFechaFin());       // mismo tipo en DTO y Entity
        existente.setActiva(dto.getActiva());
        existente.setImagenUrl(dto.getImagenUrl());
        existente.setPublicId(dto.getPublicId());

        if (dto.getIdPlatillo() != null) {
            existente.setPlatillo(entityManager.getReference(PlatilloEntity.class, dto.getIdPlatillo()));
        } else {
            existente.setPlatillo(null);
        }

        return convertirAOfertasDTO(repo.save(existente));
    }

    // ===== DELETE =====
    public boolean deleteOfertas(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    // ===== CONVERTERS =====
    private OfertasEntity convertirAOfertasEntity(OfertasDTO dto) {
        OfertasEntity entity = new OfertasEntity();
        entity.setId(dto.getId());
        entity.setDescripcion(dto.getDescripcion());
        entity.setPorcentajeDescuento(dto.getPorcentajeDescuento());
        entity.setPrecioOferta(dto.getPrecioOferta());
        entity.setFechaInicio(dto.getFechaInicio()); // ⚠️ asegúrate que en DTO también sea LocalDate
        entity.setFechaFin(dto.getFechaFin());
        entity.setActiva(dto.getActiva());
        entity.setImagenUrl(dto.getImagenUrl());
        entity.setPublicId(dto.getPublicId());

        if (dto.getIdPlatillo() != null) {
            entity.setPlatillo(entityManager.getReference(PlatilloEntity.class, dto.getIdPlatillo()));
        } else {
            entity.setPlatillo(null);
        }

        return entity;
    }

    private OfertasDTO convertirAOfertasDTO(OfertasEntity entity) {
        log.info("Convirtiendo oferta ID={} - Platillo={}",
                entity.getId(),
                (entity.getPlatillo() != null ? entity.getPlatillo().getId() : "NULL"));

        OfertasDTO dto = new OfertasDTO();
        dto.setId(entity.getId());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPorcentajeDescuento(entity.getPorcentajeDescuento());
        dto.setPrecioOferta(entity.getPrecioOferta());
        dto.setFechaInicio(entity.getFechaInicio()); // mismo tipo
        dto.setFechaFin(entity.getFechaFin());
        dto.setActiva(entity.getActiva());
        dto.setImagenUrl(entity.getImagenUrl());
        dto.setPublicId(entity.getPublicId());

        if (entity.getPlatillo() != null) {
            dto.setIdPlatillo(entity.getPlatillo().getId());
        } else {
            dto.setIdPlatillo(null);
        }

        return dto;
    }
}
