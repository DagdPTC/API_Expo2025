package OrderlyAPI.Expo2025.Services.DetallePedido;

import OrderlyAPI.Expo2025.Entities.DetallePedido.DetallePedidoEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.DetallePedidoDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.DetallePedido.DetallePedidoRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DetallePedidoService {
    private DetallePedidoRepository repo;


    public List<DetallePedidoDTO> getAllDetallePedidos(){
        List<DetallePedidoEntity> detalles = repo.findAll();
        return detalles.stream()
                .map(this::convertirADetallePedidosDTO)
                .collect(Collectors.toList());
    }

    public DetallePedidoDTO createDetallePedido(DetallePedidoDTO detallePedidoDTO){
        if (detallePedidoDTO == null || detallePedidoDTO.getIdpedido() == null || detallePedidoDTO.getIdpedido().describeConstable().isEmpty()){
            throw new IllegalArgumentException("El detalle pedido no puede ser nulo");
        }
        try{
            DetallePedidoEntity detallePedidoEntity = convertirADetallePedidosEntity(detallePedidoDTO);
            DetallePedidoEntity detallePedidoGuardado = repo.save(detallePedidoEntity);
            return convertirADetallePedidosDTO(detallePedidoGuardado);
        }catch (Exception e){
            log.error("Error al registrar detalle pedido: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el detalle pedido" + e.getMessage());
        }
    }

    public DetallePedidoDTO updateDetallePedido(Long id, DetallePedidoDTO detallePedido){
        DetallePedidoEntity detallePedidoExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Detalle Pedido no encontrado"));

        detallePedidoExistente.setIdpedido(detallePedido.getIdpedido());
        detallePedidoExistente.setIdPlatillo(detallePedido.getIdPlatillo());
        detallePedidoExistente.setCantidad(detallePedido.getCantidad());
        detallePedidoExistente.setPrecioUnitario(detallePedido.getPrecioUnitario());
        detallePedidoExistente.setObservaciones(detallePedido.getObservaciones());
        detallePedidoExistente.setIdEstadoPlatillo(detallePedido.getIdEstadoPlatillo());

        DetallePedidoEntity detallePedidoActualizado = repo.save(detallePedidoExistente);
        return convertirADetallePedidosDTO(detallePedidoActualizado);
    }

    public boolean deleteDetallePedido(Long id){
        try{
            DetallePedidoEntity objDetallePedido = repo.findById(id).orElse(null);
            if (objDetallePedido != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Detalle Pedido no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro detalle pedido con ID:" + id + " para eliminar.", 1);
        }
    }


    public DetallePedidoEntity convertirADetallePedidosEntity(DetallePedidoDTO detallePedido){
        DetallePedidoEntity dto = new DetallePedidoEntity();
        dto.setId(detallePedido.getId());
        dto.setIdpedido(detallePedido.getIdpedido());
        dto.setIdPlatillo(detallePedido.getIdPlatillo());
        dto.setCantidad(detallePedido.getCantidad());
        dto.setPrecioUnitario(detallePedido.getPrecioUnitario());
        dto.setObservaciones(detallePedido.getObservaciones());
        dto.setIdEstadoPlatillo(detallePedido.getIdEstadoPlatillo());
        return dto;
    }

    public DetallePedidoDTO convertirADetallePedidosDTO(DetallePedidoEntity detallePedido){
        DetallePedidoDTO dto = new DetallePedidoDTO();
        dto.setId(detallePedido.getId());
        dto.setIdpedido(detallePedido.getIdpedido());
        dto.setIdPlatillo(detallePedido.getIdPlatillo());
        dto.setCantidad(detallePedido.getCantidad());
        dto.setPrecioUnitario(detallePedido.getPrecioUnitario());
        dto.setObservaciones(detallePedido.getObservaciones());
        dto.setIdEstadoPlatillo(detallePedido.getIdEstadoPlatillo());
        return dto;
    }
}
