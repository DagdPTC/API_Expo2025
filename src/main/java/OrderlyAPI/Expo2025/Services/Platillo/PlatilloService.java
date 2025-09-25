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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@CrossOrigin
public class PlatilloService {

    @Autowired
    private PlatilloRepository repo;

    @PersistenceContext
    private EntityManager entityManager;

    // ===================== READ =====================
    public Page<PlatilloDTO> getAllPlatillos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PlatilloEntity> platillos = repo.findAll(pageable);
        return platillos.map(this::convertirAPlatillosDTO);
    }

    // ===================== CREATE =====================
    public PlatilloDTO createPlatillo(@Valid PlatilloDTO platilloDTO) {
        if (platilloDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El platillo no puede ser nulo");
        }
        if (platilloDTO.getIdCate() == null) {
            // En BD: IdCategoria es NOT NULL
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría (IdCate) es requerida");
        }
        try {
            PlatilloEntity entity = convertirAPlatillosEntity(platilloDTO);
            PlatilloEntity guardado = repo.save(entity);
            return convertirAPlatillosDTO(guardado);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al registrar platillo: {}", e.getMessage(), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al registrar el platillo: " + e.getMessage(), e
            );
        }
    }

    // ===================== UPDATE =====================
    public PlatilloDTO updatePlatillo(Long id, @Valid PlatilloDTO platillo) {
        PlatilloEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Platillo no encontrado"));

        // Campos simples (update parcial)
        if (platillo.getNomPlatillo() != null) existente.setNomPlatillo(platillo.getNomPlatillo());
        if (platillo.getDescripcion() != null) existente.setDescripcion(platillo.getDescripcion());

        // Precio: si te interesa permitir null (no enviar), puedes envolverlo con una comprobación adicional
        existente.setPrecio(platillo.getPrecio());

        // Imagen (Cloudinary)
        if (platillo.getImagenUrl() != null) existente.setImagenUrl(platillo.getImagenUrl());
        if (platillo.getPublicId() != null)   existente.setPublicId(platillo.getPublicId());

        // Categoría (si viene, se cambia; si no, se mantiene)
        if (platillo.getIdCate() != null) {
            CategoriaEntity categoria = entityManager.getReference(CategoriaEntity.class, platillo.getIdCate());
            existente.setCategoria(categoria);
        }

        PlatilloEntity actualizado = repo.save(existente);
        return convertirAPlatillosDTO(actualizado);
    }

    // ===================== DELETE =====================
    public boolean deletePlatillo(Long id) {
        try {
            PlatilloEntity obj = repo.findById(id).orElse(null);
            if (obj != null) {
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            // Compatibilidad con comportamiento del código 2
            log.warn("No se encontró platillo con ID {} para eliminar.", id);
            return false;
        } catch (Exception e) {
            log.error("Error al eliminar platillo {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // ===================== MAPEOS =====================
    public PlatilloEntity convertirAPlatillosEntity(PlatilloDTO dto) {
        PlatilloEntity e = new PlatilloEntity();
        e.setId(dto.getId());
        e.setNomPlatillo(dto.getNomPlatillo());
        e.setDescripcion(dto.getDescripcion());
        e.setPrecio(dto.getPrecio());
        e.setImagenUrl(dto.getImagenUrl());
        e.setPublicId(dto.getPublicId());

        // Requerido en CREATE (IdCate no debe ser null); en UPDATE lo maneja el método
        if (dto.getIdCate() != null) {
            e.setCategoria(entityManager.getReference(CategoriaEntity.class, dto.getIdCate()));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría (IdCate) es requerida");
        }
        return e;
    }

    public PlatilloDTO convertirAPlatillosDTO(PlatilloEntity e) {
        PlatilloDTO d = new PlatilloDTO();
        d.setId(e.getId());
        d.setNomPlatillo(e.getNomPlatillo());
        d.setDescripcion(e.getDescripcion());
        d.setPrecio(e.getPrecio());
        d.setImagenUrl(e.getImagenUrl());
        d.setPublicId(e.getPublicId());
        d.setIdCate(e.getCategoria() != null ? e.getCategoria().getId() : null);
        return d;
    }
}
