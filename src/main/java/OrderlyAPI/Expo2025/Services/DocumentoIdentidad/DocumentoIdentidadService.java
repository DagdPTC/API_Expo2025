package OrderlyAPI.Expo2025.Services.DocumentoIdentidad;

import OrderlyAPI.Expo2025.Entities.DocumentoIdentidad.DocumentoIdentidadEntity;
import OrderlyAPI.Expo2025.Models.DTO.DocumentoIdentidadDTO;
import OrderlyAPI.Expo2025.Repositories.DocumentoIdentidad.DocumentoIdentidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentoIdentidadService {
    private DocumentoIdentidadRepository repo;

    public List<DocumentoIdentidadDTO> getAllDocumentosIdentidades(){
        List<DocumentoIdentidadEntity> documentos = repo.findAll();
        return documentos.stream()
                .map(this::convertirADocumentosIdentidadesDTO)
                .collect(Collectors.toList());
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
