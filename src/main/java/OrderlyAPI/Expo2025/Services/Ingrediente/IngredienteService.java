package OrderlyAPI.Expo2025.Services.Ingrediente;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.Ingrediente.IngredienteEntity;
import OrderlyAPI.Expo2025.Models.DTO.EmpleadoDTO;
import OrderlyAPI.Expo2025.Models.DTO.IngredienteDTO;
import OrderlyAPI.Expo2025.Repositories.Empleado.EmpleadoRepository;
import OrderlyAPI.Expo2025.Repositories.Ingrediente.IngredienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredienteService {
    private IngredienteRepository repo;

    public List<IngredienteDTO> getAllIngredientes(){
        List<IngredienteEntity> roles = repo.findAll();
        return roles.stream()
                .map(this::convertirAIngredientesDTO)
                .collect(Collectors.toList());
    }

    public IngredienteDTO convertirAIngredientesDTO(IngredienteEntity ingrediente){
        IngredienteDTO dto = new IngredienteDTO();
        dto.setId(ingrediente.getId());
        dto.setNomIngrediente(ingrediente.getNomIngrediente());
        dto.setCantidad(ingrediente.getCantidad());
        dto.setIdCategoria(ingrediente.getIdCategoria());
        dto.setIdUnidadMedida(ingrediente.getIdUnidadMedida());
        dto.setCosteUnidad(ingrediente.getCosteUnidad());
        return dto;
    }
}
