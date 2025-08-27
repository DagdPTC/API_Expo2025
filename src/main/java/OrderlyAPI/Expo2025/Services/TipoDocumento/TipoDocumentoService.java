package OrderlyAPI.Expo2025.Services.TipoDocumento;

import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.TipoDocumentoDTO;
import OrderlyAPI.Expo2025.Repositories.TipoDocumento.TipoDocumentoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@Service
@CrossOrigin
public class TipoDocumentoService {

    private TipoDocumentoRepository repo;


    public Page<TipoDocumentoDTO> getAllTipoDocument(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<TipoDocumentoEntity> tipdocument = repo.findAll(pageable);
        return tipdocument.map(this::convertirATipDocumentDTO);
    }

    public TipoDocumentoDTO createTipoDocument(@Valid TipoDocumentoDTO tipoDocumentoDTO){
        if (tipoDocumentoDTO == null){
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        try{
            TipoDocumentoEntity tipoDocumentoEntity = convertirATipDocumentEntity(tipoDocumentoDTO);
            TipoDocumentoEntity tipdocumentGuardado = repo.save(tipoDocumentoEntity);
            return convertirATipDocumentDTO(tipdocumentGuardado);
        }catch (Exception e){
            log.error("Error al registrar rol: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el rol" + e.getMessage());
        }
    }

    public TipoDocumentoDTO updateTipoDocumento(Long id, @Valid TipoDocumentoDTO
            rol){
        TipoDocumentoEntity tipdocumentExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Tipo documento no encontrado"));

        tipdocumentExistente.setTipoDoc(rol.getTipoDoc());

        TipoDocumentoEntity tipdocumentActualizado = repo.save(tipdocumentExistente);
        return convertirATipDocumentDTO(tipdocumentActualizado);
    }

    public boolean deleteTipoDocument(Long id){
        try{
            TipoDocumentoEntity objTipdocument = repo.findById(id).orElse(null);
            if (objTipdocument != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Rol no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro rol con ID:" + id + " para eliminar.", 1);
        }
    }


    public TipoDocumentoEntity convertirATipDocumentEntity(TipoDocumentoDTO dto){
        TipoDocumentoEntity entity = new TipoDocumentoEntity();
        entity.setIdTipoDoc(dto.getIdTipoDoc());
        entity.setTipoDoc(dto.getTipoDoc());
        return entity;
    }

    public TipoDocumentoDTO convertirATipDocumentDTO(TipoDocumentoEntity entity){
        TipoDocumentoDTO dto = new TipoDocumentoDTO();
        dto.setIdTipoDoc(entity.getIdTipoDoc());
        dto.setTipoDoc(entity.getTipoDoc());
        return dto;
    }
}

