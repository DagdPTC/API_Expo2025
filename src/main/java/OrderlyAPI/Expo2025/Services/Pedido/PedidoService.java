package OrderlyAPI.Expo2025.Services.Pedido;

import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.PedidoDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.Pedido.PedidoRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PedidoService {
    private PedidoRepository repo;


    public List<PedidoDTO> getAllPedidos(){
        List<PedidoEntity> pedidos = repo.findAll();
        return pedidos.stream()
                .map(this::convertirAPedidosDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO createPedido(PedidoDTO pedidoDTO){
        if (pedidoDTO == null || pedidoDTO.getNombrecliente() == null || pedidoDTO.getNombrecliente().describeConstable().isEmpty()){
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        try{
            PedidoEntity pedidoEntity = convertirAPedidossEntity(pedidoDTO);
            PedidoEntity pedidoGuardado = repo.save(pedidoEntity);
            return convertirAPedidosDTO(pedidoGuardado);
        }catch (Exception e){
            log.error("Error al registrar pedido: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el pedido" + e.getMessage());
        }
    }

    public PedidoDTO updatePedido(Long id, PedidoDTO pedido){
        PedidoEntity pedidoExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Pedido no encontrado"));

        pedidoExistente.setNombrecliente(pedido.getNombrecliente());
        pedidoExistente.setIdMesa(pedido.getIdMesa());
        pedidoExistente.setIdEmpleado(pedido.getIdEmpleado());
        pedidoExistente.setFPedido(pedido.getFPedido());
        pedidoExistente.setHPedido(pedido.getHPedido());
        pedidoExistente.setIdEstadoPedido(pedido.getIdEstadoPedido());
        pedidoExistente.setObservaciones(pedido.getObservaciones());
        pedidoExistente.setCantidad(pedido.getCantidad());
        pedidoExistente.setTotalPedido(pedido.getTotalPedido());
        pedidoExistente.setObservaciones(pedido.getObservaciones());




        PedidoEntity pedidoActualizado = repo.save(pedidoExistente);
        return convertirAPedidosDTO(pedidoActualizado);
    }

    public boolean deletePedido(Long id){
        try{
            PedidoEntity objPedido = repo.findById(id).orElse(null);
            if (objPedido != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Pedido no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro pedido con ID:" + id + " para eliminar.", 1);
        }
    }


    public PedidoEntity convertirAPedidossEntity(PedidoDTO pedido){
        PedidoEntity dto = new PedidoEntity();
        dto.setId(pedido.getId());
        dto.setNombrecliente(pedido.getNombrecliente());
        dto.setIdMesa(pedido.getIdMesa());
        dto.setIdEmpleado(pedido.getIdEmpleado());
        dto.setFPedido(pedido.getFPedido());
        dto.setHPedido(pedido.getHPedido());
        dto.setIdEstadoPedido(pedido.getIdEstadoPedido());
        dto.setObservaciones(pedido.getObservaciones());
        return dto;
    }

    public PedidoDTO convertirAPedidosDTO(PedidoEntity pedido){
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setNombrecliente(pedido.getNombrecliente());
        dto.setIdMesa(pedido.getIdMesa());
        dto.setIdEmpleado(pedido.getIdEmpleado());
        dto.setFPedido(pedido.getFPedido());
        dto.setHPedido(pedido.getHPedido());
        dto.setIdEstadoPedido(pedido.getIdEstadoPedido());
        dto.setObservaciones(pedido.getObservaciones());
        return dto;
    }
}
