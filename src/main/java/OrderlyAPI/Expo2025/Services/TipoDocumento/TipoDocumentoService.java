package OrderlyAPI.Expo2025.Services.TipoDocumento;

import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.TipoDocumentoDTO;
import OrderlyAPI.Expo2025.Repositories.TipoDocumento.TipoDocumentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TipoDocumentoService {

    private final TipoDocumentoRepository repo;

    // ====== READ ======
    @Transactional(readOnly = true)
    public Page<TipoDocumentoDTO> getAllTipoDocument(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable).map(this::convertirATipDocumentDTO);
    }

    // Lista NO paginada (ideal para combos del front)
    @Transactional(readOnly = true)
    public java.util.List<TipoDocumentoDTO> getAllTipoDocumentList() {
        return repo.findAll()
                .stream()
                .map(this::convertirATipDocumentDTO)
                .toList();
    }

    // ====== CREATE ======
    public TipoDocumentoDTO createTipoDocument(@Valid TipoDocumentoDTO dto) {
        if (dto == null) throw new IllegalArgumentException("El tipo de documento no puede ser nulo");
        try {
            TipoDocumentoEntity entity = convertirATipDocumentEntity(dto);
            TipoDocumentoEntity guardado = repo.save(entity);
            return convertirATipDocumentDTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar tipo documento: {}", e.getMessage(), e);
            throw new ExceptionDatoNoEncontrado("Error al registrar el tipo de documento: " + e.getMessage());
        }
    }

    // ====== UPDATE ======
    public TipoDocumentoDTO updateTipoDocumento(Long id, @Valid TipoDocumentoDTO dto) {
        TipoDocumentoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Tipo documento no encontrado"));

        existente.setTipoDoc(dto.getTipoDoc());
        TipoDocumentoEntity actualizado = repo.save(existente);
        return convertirATipDocumentDTO(actualizado);
    }

    // ====== DELETE ======
    public boolean deleteTipoDocument(Long id) {
        try {
            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException(
                    "No se encontró TipoDocumento con ID: " + id + " para eliminar.", 1);
        }
    }

    // ====== Mappers ======
    public TipoDocumentoEntity convertirATipDocumentEntity(TipoDocumentoDTO dto) {
        TipoDocumentoEntity entity = new TipoDocumentoEntity();
        entity.setIdTipoDoc(dto.getIdTipoDoc());
        entity.setTipoDoc(dto.getTipoDoc());
        return entity;
    }

    public TipoDocumentoDTO convertirATipDocumentDTO(TipoDocumentoEntity entity) {
        TipoDocumentoDTO dto = new TipoDocumentoDTO();
        dto.setIdTipoDoc(entity.getIdTipoDoc());
        dto.setTipoDoc(entity.getTipoDoc());
        return dto;
    }
}
