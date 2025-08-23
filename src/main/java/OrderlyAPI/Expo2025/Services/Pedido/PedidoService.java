package OrderlyAPI.Expo2025.Services.Pedido;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.EstadoPedido.EstadoPedidoEntity;
import OrderlyAPI.Expo2025.Entities.Mesa.MesaEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.PedidoDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.Pedido.PedidoRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
public class PedidoService {

    @Autowired
    private PedidoRepository repo;

    @PersistenceContext
    EntityManager entityManager;

    public Page<PedidoDTO> getAllPedidos(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<PedidoEntity> pedidos = repo.findAll(pageable);
        return pedidos.map(this::convertirAPedidosDTO);
    }

    public PedidoDTO createPedido(@Valid PedidoDTO pedidoDTO){
        if (pedidoDTO == null){
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

    public PedidoDTO updatePedido(Long id, @Valid PedidoDTO pedido){
        PedidoEntity pedidoExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Pedido no encontrado"));

        pedidoExistente.setNombrecliente(pedido.getNombrecliente());
        pedidoExistente.setMesas(pedidoExistente.getMesas());
        pedidoExistente.setEmpleado(pedidoExistente.getEmpleado());
        pedidoExistente.setFPedido(pedido.getFPedido());
        pedidoExistente.setEstpedido(pedidoExistente.getEstpedido());
        pedidoExistente.setObservaciones(pedido.getObservaciones());
        pedidoExistente.setCantidad(pedido.getCantidad());
        pedidoExistente.setTotalPedido(pedido.getTotalPedido());
        pedidoExistente.setSubtotal(pedido.getSubtotal());
        pedidoExistente.setPropina(pedido.getPropina());
        pedidoExistente.setPlatillo(pedidoExistente.getPlatillo());

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
        dto.setMesas(entityManager.getReference(MesaEntity.class, pedido.getIdMesa()));
        dto.setEmpleado(entityManager.getReference(EmpleadoEntity.class, pedido.getIdEmpleado()));
        dto.setFPedido(pedido.getFPedido());
        dto.setEstpedido(entityManager.getReference(EstadoPedidoEntity.class, pedido.getIdEstadoPedido()));
        dto.setObservaciones(pedido.getObservaciones());
        dto.setObservaciones(pedido.getObservaciones());
        dto.setCantidad(pedido.getCantidad());
        dto.setTotalPedido(pedido.getTotalPedido());
        dto.setSubtotal(pedido.getSubtotal());
        dto.setPropina(pedido.getPropina());
        dto.setPlatillo(entityManager.getReference(PlatilloEntity.class, pedido.getIdPlatillo()));
        return dto;
    }

    public PedidoDTO convertirAPedidosDTO(PedidoEntity pedido){
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setNombrecliente(pedido.getNombrecliente());
        dto.setIdMesa(pedido.getMesas().getId());
        dto.setIdEmpleado(pedido.getEmpleado().getId());
        dto.setFPedido(pedido.getFPedido());
        dto.setIdEstadoPedido(pedido.getEstpedido().getId());
        dto.setObservaciones(pedido.getObservaciones());
        dto.setObservaciones(pedido.getObservaciones());
        dto.setCantidad(pedido.getCantidad());
        dto.setTotalPedido(pedido.getTotalPedido());
        dto.setSubtotal(pedido.getSubtotal());
        dto.setPropina(pedido.getPropina());
        dto.setIdPlatillo(pedido.getPlatillo().getId());
        return dto;
    }
}
