package OrderlyAPI.Expo2025.Services.Categoria;

import OrderlyAPI.Expo2025.Entities.Categoria.CategoriaEntity;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.CategoriaDTO;
import OrderlyAPI.Expo2025.Models.DTO.UsuarioDTO;
import OrderlyAPI.Expo2025.Repositories.Categoria.CategoriaRepository;
import OrderlyAPI.Expo2025.Repositories.Usuario.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@CrossOrigin
public class CategoriaService {

    @Autowired
    private CategoriaRepository repo;

    public Page<CategoriaDTO> getAllCategorias(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoriaEntity> roles = repo.findAll(pageable);
        return roles.map(this::convertirACategoriasDTO);
    }

    public CategoriaDTO createCategoria(@Valid CategoriaDTO categoriaDTO){
        if (categoriaDTO == null){
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

    public CategoriaDTO updateCategoria(Long id, @Valid CategoriaDTO categoria){
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
