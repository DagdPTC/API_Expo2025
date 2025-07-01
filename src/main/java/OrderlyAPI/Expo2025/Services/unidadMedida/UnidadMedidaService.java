package OrderlyAPI.Expo2025.Services.unidadMedida;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.UnidadMedida.UnidadMedidaEntity;
import OrderlyAPI.Expo2025.Models.DTO.EmpleadoDTO;
import OrderlyAPI.Expo2025.Models.DTO.UnidadMedidaDTO;
import OrderlyAPI.Expo2025.Repositories.Empleado.EmpleadoRepository;
import OrderlyAPI.Expo2025.Repositories.UnidadMedida.UnidadMedidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnidadMedidaService {
    private UnidadMedidaRepository repo;

    public List<UnidadMedidaDTO> getAllUnidadMedidas(){
        List<UnidadMedidaEntity> roles = repo.findAll();
        return roles.stream()
                .map(this::convertirAUnidadMedidasDTO)
                .collect(Collectors.toList());
    }

    public UnidadMedidaDTO convertirAUnidadMedidasDTO(UnidadMedidaEntity unidadmedida){
        UnidadMedidaDTO dto = new UnidadMedidaDTO();
        dto.setId(unidadmedida.getId());
        dto.setNombre(unidadmedida.getNombre());
        dto.setAbreviatura(unidadmedida.getAbreviatura());
        return dto;
    }
}
