package OrderlyAPI.Expo2025.Services.PrecioIngrediente;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.PrecioIngrediente.PrecioIngredienteEntity;
import OrderlyAPI.Expo2025.Models.DTO.EmpleadoDTO;
import OrderlyAPI.Expo2025.Models.DTO.PrecioIngredienteDTO;
import OrderlyAPI.Expo2025.Repositories.PrecioIngrediente.PrecioIngredienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrecioIngredienteService {
    private PrecioIngredienteRepository repo;

    public List<PrecioIngredienteDTO> getAllPrecioIngredientes(){
        List<PrecioIngredienteEntity> roles = repo.findAll();
        return roles.stream()
                .map(this::convertirAPrecioIngredientesDTO)
                .collect(Collectors.toList());
    }

    public PrecioIngredienteDTO convertirAPrecioIngredientesDTO(PrecioIngredienteEntity precioIngrediente){
        PrecioIngredienteDTO dto = new PrecioIngredienteDTO();
        dto.setId(precioIngrediente.getId());
        dto.setIdIngrediente(precioIngrediente.getIdIngrediente());
        dto.setPrecio(precioIngrediente.getPrecio());
        dto.setFInicio(precioIngrediente.getFInicio());
        dto.setFFin(precioIngrediente.getFFin());
        return dto;
    }
}
