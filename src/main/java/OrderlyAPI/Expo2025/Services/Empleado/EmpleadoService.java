package OrderlyAPI.Expo2025.Services.Empleado;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Models.DTO.EmpleadoDTO;
import OrderlyAPI.Expo2025.Repositories.Empleado.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {
    private EmpleadoRepository repo;

    public List<EmpleadoDTO> getAllEmpleados(){
        List<EmpleadoEntity> roles = repo.findAll();
        return roles.stream()
                .map(this::convertirAEmpleadosDTO)
                .collect(Collectors.toList());
    }

    public EmpleadoDTO convertirAEmpleadosDTO(EmpleadoEntity empleado){
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setId(empleado.getId());
        dto.setIdPersona(empleado.getIdPersona());
        dto.setIdUsuario(empleado.getIdUsuario());
        dto.setFContratacion(empleado.getFContratacion());
        return dto;
    }
}
