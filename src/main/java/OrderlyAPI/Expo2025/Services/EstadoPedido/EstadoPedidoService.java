package OrderlyAPI.Expo2025.Services.EstadoPedido;

import OrderlyAPI.Expo2025.Entities.EstadoPedido.EstadoPedidoEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.EstadoPedidoDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.EstadoPedido.EstadoPedidoRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
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

@Service
@Slf4j
@CrossOrigin
public class EstadoPedidoService {

    @Autowired
    private EstadoPedidoRepository repo;

    public Page<EstadoPedidoDTO> getAllEstadoPedidos(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<EstadoPedidoEntity> estados = repo.findAll(pageable);
        return estados.map(this::convertirAEstadoPedidosDTO);
    }

    public EstadoPedidoDTO createEstadoPedido(@Valid EstadoPedidoDTO estadoPedidoDTO){
        if (estadoPedidoDTO == null){
            throw new IllegalArgumentException("El estado pedido no puede ser nulo");
        }
        try{
            EstadoPedidoEntity estadoPedidoEntity = convertirAEstadoPedidosEntity(estadoPedidoDTO);
            EstadoPedidoEntity estadoPedidoGuardado = repo.save(estadoPedidoEntity);
            return convertirAEstadoPedidosDTO(estadoPedidoGuardado);
        }catch (Exception e){
            log.error("Error al registrar estado pedido: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el estado pedido" + e.getMessage());
        }
    }

    public EstadoPedidoDTO updateEstadoPedido(Long id, @Valid EstadoPedidoDTO estadoPedido){
        EstadoPedidoEntity estadoPedidoExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Estado Pedido no encontrado"));

        estadoPedidoExistente.setNomEstado(estadoPedido.getNomEstado());

        EstadoPedidoEntity estadoPedidoActualizado = repo.save(estadoPedidoExistente);
        return convertirAEstadoPedidosDTO(estadoPedidoActualizado);
    }

    public boolean deleteEstadoPedido(Long id){
        try{
            EstadoPedidoEntity objEstadoPedido = repo.findById(id).orElse(null);
            if (objEstadoPedido != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Estado Pedido no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro estado pedido con ID:" + id + " para eliminar.", 1);
        }
    }


    public EstadoPedidoEntity convertirAEstadoPedidosEntity(EstadoPedidoDTO estadoPedido){
        EstadoPedidoEntity dto = new EstadoPedidoEntity();
        dto.setId(estadoPedido.getId());
        dto.setNomEstado(estadoPedido.getNomEstado());
        return dto;
    }

    public EstadoPedidoDTO convertirAEstadoPedidosDTO(EstadoPedidoEntity estadoPedido){
        EstadoPedidoDTO dto = new EstadoPedidoDTO();
        dto.setId(estadoPedido.getId());
        dto.setNomEstado(estadoPedido.getNomEstado());
        return dto;
    }
}
