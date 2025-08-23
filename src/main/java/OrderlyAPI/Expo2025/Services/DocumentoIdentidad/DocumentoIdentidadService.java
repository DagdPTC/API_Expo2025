package OrderlyAPI.Expo2025.Services.DocumentoIdentidad;

import OrderlyAPI.Expo2025.Entities.Categoria.CategoriaEntity;
import OrderlyAPI.Expo2025.Entities.DocumentoIdentidad.DocumentoIdentidadEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.DocumentoIdentidadDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.DocumentoIdentidad.DocumentoIdentidadRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@CrossOrigin
public class DocumentoIdentidadService {

    @Autowired
    private DocumentoIdentidadRepository repo;

    @PersistenceContext
    EntityManager entityManager;


    public Page<DocumentoIdentidadDTO> getAllDocumentosIdentidades(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentoIdentidadEntity> documentos = repo.findAll(pageable);
        return documentos.map(this::convertirADocumentosIdentidadesDTO);
    }

    public DocumentoIdentidadDTO createDocumentoIdentidad(@Valid DocumentoIdentidadDTO documentoIdentidadDTO){
        if (documentoIdentidadDTO == null){
            throw new IllegalArgumentException("El Documento Identidad no puede ser nulo");
        }
        try{
            DocumentoIdentidadEntity documentoIdentidadEntity = convertirADocumentosIdentidadesEntity(documentoIdentidadDTO);
            DocumentoIdentidadEntity documentoIdentidadGuardado = repo.save(documentoIdentidadEntity);
            return convertirADocumentosIdentidadesDTO(documentoIdentidadGuardado);
        }catch (Exception e){
            log.error("Error al registrar documento identidad: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el documento identidad" + e.getMessage());
        }
    }

    public DocumentoIdentidadDTO updateDocumentoIdentidad(Long id, @Valid DocumentoIdentidadDTO documentoIdentidad){
        DocumentoIdentidadEntity documentoIdentidadExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Documento Identidad no encontrado"));

        documentoIdentidadExistente.setIdtipoDoc(documentoIdentidad.getIdtipoDoc());
        documentoIdentidadExistente.setNumDoc(documentoIdentidad.getNumDoc());

        DocumentoIdentidadEntity documentoIdentidadActualizado = repo.save(documentoIdentidadExistente);
        return convertirADocumentosIdentidadesDTO(documentoIdentidadActualizado);
    }

    public boolean deleteDocumentoIdentidad(Long id){
        try{
            DocumentoIdentidadEntity objDocumentoIdentidad = repo.findById(id).orElse(null);
            if (objDocumentoIdentidad != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Documento Identidad no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro documento identidad con ID:" + id + " para eliminar.", 1);
        }
    }


    public DocumentoIdentidadEntity convertirADocumentosIdentidadesEntity(DocumentoIdentidadDTO documentoIdentidad){
        DocumentoIdentidadEntity dto = new DocumentoIdentidadEntity();
        dto.setId(documentoIdentidad.getId());
        dto.setIdtipoDoc(entityManager.getReference(TipoDocumentoEntity.class, documentoIdentidad.getIdtipoDoc()));
        dto.setNumDoc(documentoIdentidad.getNumDoc());
        return dto;
    }

    public DocumentoIdentidadDTO convertirADocumentosIdentidadesDTO(DocumentoIdentidadEntity documentoIdentidad){
        DocumentoIdentidadDTO dto = new DocumentoIdentidadDTO();
        dto.setId(documentoIdentidad.getId());
        dto.setIdtipoDoc(documentoIdentidad.getIdtipoDoc());
        dto.setNumDoc(documentoIdentidad.getNumDoc());
        return dto;
    }
}
