package OrderlyAPI.Expo2025.Services.DocumentoIdentidad;

import OrderlyAPI.Expo2025.Entities.DocumentoIdentidad.DocumentoIdentidadEntity;
import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.DocumentoIdentidadDTO;
import OrderlyAPI.Expo2025.Repositories.DocumentoIdentidad.DocumentoIdentidadRepository;
import OrderlyAPI.Expo2025.Repositories.TipoDocumento.TipoDocumentoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@Service
@CrossOrigin
@RequiredArgsConstructor
@Transactional
public class DocumentoIdentidadService {

    private final DocumentoIdentidadRepository repo;
    private final TipoDocumentoRepository tipoRepo;

    // ===== READ paginado (si lo usas en otra vista) =====
    @Transactional(readOnly = true)
    public Page<DocumentoIdentidadDTO> getAllDocumentosIdentidades(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable).map(this::toDTO);
    }

    // ===== CREATE =====
    public DocumentoIdentidadDTO createDocumentoIdentidad(@Valid DocumentoIdentidadDTO dtoIn) {
        if (dtoIn == null) {
            throw new IllegalArgumentException("El Documento Identidad no puede ser nulo");
        }
        if (dtoIn.getIdtipoDoc() == null) {
            throw new IllegalArgumentException("idtipoDoc es obligatorio");
        }
        if (dtoIn.getNumDoc() == null || dtoIn.getNumDoc().isBlank()) {
            throw new IllegalArgumentException("numDoc no debe estar vacío");
        }

        try {
            // Carga segura del TipoDocumento (NO usar getReference con id null)
            TipoDocumentoEntity tipo = tipoRepo.findById(dtoIn.getIdtipoDoc())
                    .orElseThrow(() -> new ExceptionDatoNoEncontrado("TipoDocumento no encontrado"));

            DocumentoIdentidadEntity e = new DocumentoIdentidadEntity();
            // Si tu entidad usa 'setId' para el PK y permites setearlo, mantenlo; si es autogenerado, quítalo.
            if (dtoIn.getId() != null) e.setId(dtoIn.getId());
            e.setNumDoc(dtoIn.getNumDoc());
            e.setTipodocumento(tipo);

            DocumentoIdentidadEntity saved = repo.save(e);
            return toDTO(saved);

        } catch (Exception ex) {
            log.error("Error al registrar documento identidad: {}", ex.getMessage(), ex);
            throw new ExceptionDatoNoEncontrado("Error al registrar el documento identidad: " + ex.getMessage());
        }
    }

    // ===== UPDATE =====
    public DocumentoIdentidadDTO updateDocumentoIdentidad(Long id, @Valid DocumentoIdentidadDTO dtoIn) {
        DocumentoIdentidadEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Documento Identidad no encontrado"));

        // Actualiza tipo solo si viene un id válido
        if (dtoIn.getIdtipoDoc() != null) {
            TipoDocumentoEntity tipo = tipoRepo.findById(dtoIn.getIdtipoDoc())
                    .orElseThrow(() -> new ExceptionDatoNoEncontrado("TipoDocumento no encontrado"));
            existente.setTipodocumento(tipo);
        }

        if (dtoIn.getNumDoc() != null && !dtoIn.getNumDoc().isBlank()) {
            existente.setNumDoc(dtoIn.getNumDoc());
        }

        DocumentoIdentidadEntity actualizado = repo.save(existente);
        return toDTO(actualizado);
    }

    // ===== DELETE =====
    public boolean deleteDocumentoIdentidad(Long id) {
        try {
            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException(
                    "No se encontro documento identidad con ID:" + id + " para eliminar.", 1);
        }
    }

    // ===== Mappers (respetando tus nombres actuales: id, idtipoDoc, numDoc) =====
    private DocumentoIdentidadDTO toDTO(DocumentoIdentidadEntity e) {
        DocumentoIdentidadDTO dto = new DocumentoIdentidadDTO();
        dto.setId(e.getId()); // si tu PK es IdDocumento y el getter es getIdDocumento, ajusta aquí
        dto.setIdtipoDoc(e.getTipodocumento() != null ? e.getTipodocumento().getIdTipoDoc() : null);
        dto.setNumDoc(e.getNumDoc());
        return dto;
    }
}
