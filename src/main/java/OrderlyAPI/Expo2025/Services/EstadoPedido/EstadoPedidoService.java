package OrderlyAPI.Expo2025.Services.EstadoPedido;

import OrderlyAPI.Expo2025.Entities.EstadoPedido.EstadoPedidoEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.EstadoPedidoDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.EstadoPedido.EstadoPedidoRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EstadoPedidoService {
    private EstadoPedidoRepository repo;


    public List<EstadoPedidoDTO> getAllEstadoPedidos(){
        List<EstadoPedidoEntity> estados = repo.findAll();
        return estados.stream()
                .map(this::convertirAEstadoPedidosDTO)
                .collect(Collectors.toList());
    }

    public EstadoPedidoDTO createEstadoPedido(EstadoPedidoDTO estadoPedidoDTO){
        if (estadoPedidoDTO == null || estadoPedidoDTO.getNomEstado() == null || estadoPedidoDTO.getNomEstado().isEmpty()){
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

    public EstadoPedidoDTO updateEstadoPedido(Long id, EstadoPedidoDTO estadoPedido){
        EstadoPedidoEntity estadoPedidoExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Estado Pedido no encontrado"));

        estadoPedidoExistente.setNomEstado(estadoPedido.getNomEstado());
        estadoPedidoExistente.setDescripcion(estadoPedido.getDescripcion());

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
        dto.setDescripcion(estadoPedido.getDescripcion());
        return dto;
    }

    public EstadoPedidoDTO convertirAEstadoPedidosDTO(EstadoPedidoEntity estadoPedido){
        EstadoPedidoDTO dto = new EstadoPedidoDTO();
        dto.setId(estadoPedido.getId());
        dto.setNomEstado(estadoPedido.getNomEstado());
        dto.setDescripcion(estadoPedido.getDescripcion());
        return dto;
    }
}
