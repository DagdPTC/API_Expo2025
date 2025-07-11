package OrderlyAPI.Expo2025.Services.Categoria;

import OrderlyAPI.Expo2025.Entities.Categoria.CategoriaEntity;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.CategoriaDTO;
import OrderlyAPI.Expo2025.Models.DTO.UsuarioDTO;
import OrderlyAPI.Expo2025.Repositories.Categoria.CategoriaRepository;
import OrderlyAPI.Expo2025.Repositories.Usuario.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoriaService {
    private CategoriaRepository repo;

    public List<CategoriaDTO> getAllCategorias(){
        List<CategoriaEntity> roles = repo.findAll();
        return roles.stream()
                .map(this::convertirACategoriasDTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO createCategoria(CategoriaDTO categoriaDTO){
        if (categoriaDTO == null || categoriaDTO.getNomCategoria() == null || categoriaDTO.getNomCategoria().isEmpty()){
            throw new IllegalArgumentException("La categoria no puede ser nulo");
        }
        try{
            CategoriaEntity categoriaEntity = convertirACategoriasEntity(categoriaDTO);
            CategoriaEntity categoriaGuardado = repo.save(categoriaEntity);
            return convertirACategoriasDTO(categoriaGuardado);
        }catch (Exception e){
            log.error("Error al registrar categoria: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar la categoria" + e.getMessage());
        }
    }

    public CategoriaDTO updateCategoria(Long id, CategoriaDTO categoria){
        CategoriaEntity categoriaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Categoria no encontrada"));

        categoriaExistente.setNomCategoria(categoria.getNomCategoria());

        CategoriaEntity categoriaActualizado = repo.save(categoriaExistente);
        return convertirACategoriasDTO(categoriaActualizado);
    }

    public boolean deleteCategoria(Long id){
        try{
            CategoriaEntity objCategoria = repo.findById(id).orElse(null);
            if (objCategoria != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Categoria no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro categoria con ID:" + id + " para eliminar.", 1);
        }
    }

    public CategoriaEntity convertirACategoriasEntity(CategoriaDTO categoria){
        CategoriaEntity dto = new CategoriaEntity();
        dto.setId(categoria.getId());
        dto.setNomCategoria(categoria.getNomCategoria());
        return dto;
    }

    public CategoriaDTO convertirACategoriasDTO(CategoriaEntity categoria){
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNomCategoria(categoria.getNomCategoria());
        return dto;
    }
}
