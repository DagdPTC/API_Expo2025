package OrderlyAPI.Expo2025.Services.Usuario;

import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Models.DTO.UsuarioDTO;
import OrderlyAPI.Expo2025.Repositories.Usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private UsuarioRepository repo;

    public List<UsuarioDTO> getAllUsuarios(){
        List<UsuarioEntity> usuarios = repo.findAll();
        return usuarios.stream()
                .map(this::convertirAUsuariosDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO convertirAUsuariosDTO(UsuarioEntity usuario){
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setContrasenia(usuario.getContrasenia());
        return dto;
    }
}
