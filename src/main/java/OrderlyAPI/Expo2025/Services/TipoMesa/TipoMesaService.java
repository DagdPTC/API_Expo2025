package OrderlyAPI.Expo2025.Services.TipoMesa;

import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.TipoMesa.TipoMesaEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Models.DTO.TipoMesaDTO;
import OrderlyAPI.Expo2025.Repositories.TipoMesa.TipoMesaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TipoMesaService {

    @Autowired
    private TipoMesaRepository repo;

    public List<TipoMesaDTO> getAllTipoMesas(){
        List<TipoMesaEntity> tipos = repo.findAll();
        return tipos.stream()
                .map(this::convertirTipoMesaDTO)
                .collect(Collectors.toList());
    }

    public TipoMesaDTO createTipomesa(TipoMesaDTO tipoMesaDTO){
        if (tipoMesaDTO == null || tipoMesaDTO.getNombre() == null || tipoMesaDTO.getNombre().isEmpty()){
            throw new IllegalArgumentException("El Tipo mesa no puede ser nulo");
        }
        try{
            TipoMesaEntity tipoMesaEntity = convertirTipoMesaEntity(tipoMesaDTO);
            TipoMesaEntity tipoMesaGuardado = repo.save(tipoMesaEntity);
            return convertirTipoMesaDTO(tipoMesaGuardado);
        }catch (Exception e){
            log.error("Error al registrar tipo mesa: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el tipo mesa" + e.getMessage());
        }
    }

    public TipoMesaDTO updatetipoMesa(Long id, TipoMesaDTO tipoMesa){
        TipoMesaEntity tipomesaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Tipo mesa no encontrado"));

        tipomesaExistente.setNombre(tipoMesa.getNombre());
        tipomesaExistente.setCapacidadPersonas(tipoMesa.getCapacidadPersonas());

        TipoMesaEntity tipomesaActualizado = repo.save(tipomesaExistente);
        return convertirTipoMesaDTO(tipomesaActualizado);
    }

    public boolean deleteTipoMesa(Long id){
        try{
            TipoMesaEntity objTipoMesa = repo.findById(id).orElse(null);
            if (objTipoMesa != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Tipo Mesa no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro tipo mesa con ID:" + id + " para eliminar.", 1);
        }
    }


    public TipoMesaEntity convertirTipoMesaEntity(TipoMesaDTO tipoMesa){
        TipoMesaEntity dto = new TipoMesaEntity();
        dto.setId(tipoMesa.getId());
        dto.setNombre(tipoMesa.getNombre());
        dto.setCapacidadPersonas(tipoMesa.getCapacidadPersonas());
        return dto;
    }

    public TipoMesaDTO convertirTipoMesaDTO(TipoMesaEntity tipoMesa){
        TipoMesaDTO dto = new TipoMesaDTO();
        dto.setId(tipoMesa.getId());
        dto.setNombre(tipoMesa.getNombre());
        dto.setCapacidadPersonas(tipoMesa.getCapacidadPersonas());
        return dto;
    }
}
