package OrderlyAPI.Expo2025.Services.DocumentoIdentidad;

import OrderlyAPI.Expo2025.Entities.DocumentoIdentidad.DocumentoIdentidadEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.DocumentoIdentidadDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.DocumentoIdentidad.DocumentoIdentidadRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DocumentoIdentidadService {
    private DocumentoIdentidadRepository repo;

    public List<DocumentoIdentidadDTO> getAllDocumentosIdentidades(){
        List<DocumentoIdentidadEntity> documentos = repo.findAll();
        return documentos.stream()
                .map(this::convertirADocumentosIdentidadesDTO)
                .collect(Collectors.toList());
    }

    public DocumentoIdentidadDTO createDocumentoIdentidad(DocumentoIdentidadDTO documentoIdentidadDTO){
        if (documentoIdentidadDTO == null || documentoIdentidadDTO.getNumDoc() == null || documentoIdentidadDTO.getNumDoc().isEmpty()){
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

    public DocumentoIdentidadDTO updateDocumentoIdentidad(Long id, DocumentoIdentidadDTO documentoIdentidad){
        DocumentoIdentidadEntity documentoIdentidadExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Documento Identidad no encontrado"));

        documentoIdentidadExistente.setIdpersona(documentoIdentidad.getIdpersona());
        documentoIdentidadExistente.setTipoDoc(documentoIdentidad.getTipoDoc());
        documentoIdentidadExistente.setNumDoc(documentoIdentidad.getNumDoc());
        documentoIdentidadExistente.setFechaExpedicion(documentoIdentidad.getFechaExpedicion());

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
        dto.setIdpersona(documentoIdentidad.getIdpersona());
        dto.setTipoDoc(documentoIdentidad.getTipoDoc());
        dto.setNumDoc(documentoIdentidad.getNumDoc());
        dto.setFechaExpedicion(documentoIdentidad.getFechaExpedicion());
        return dto;
    }

    public DocumentoIdentidadDTO convertirADocumentosIdentidadesDTO(DocumentoIdentidadEntity documentoIdentidad){
        DocumentoIdentidadDTO dto = new DocumentoIdentidadDTO();
        dto.setId(documentoIdentidad.getId());
        dto.setIdpersona(documentoIdentidad.getIdpersona());
        dto.setTipoDoc(documentoIdentidad.getTipoDoc());
        dto.setNumDoc(documentoIdentidad.getNumDoc());
        dto.setFechaExpedicion(documentoIdentidad.getFechaExpedicion());
        return dto;
    }
}
