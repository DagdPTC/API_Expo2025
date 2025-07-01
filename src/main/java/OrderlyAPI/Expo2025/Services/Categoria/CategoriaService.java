package OrderlyAPI.Expo2025.Services.Categoria;

import OrderlyAPI.Expo2025.Entities.Categoria.CategoriaEntity;
import OrderlyAPI.Expo2025.Models.DTO.CategoriaDTO;
import OrderlyAPI.Expo2025.Repositories.Categoria.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    private CategoriaRepository repo;

    public List<CategoriaDTO> getAllCategorias(){
        List<CategoriaEntity> roles = repo.findAll();
        return roles.stream()
                .map(this::convertirACategoriasDTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO convertirACategoriasDTO(CategoriaEntity categoria){
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNomCategoria(categoria.getNomCategoria());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }
}
