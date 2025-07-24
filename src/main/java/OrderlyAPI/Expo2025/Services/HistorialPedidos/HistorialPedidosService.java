package OrderlyAPI.Expo2025.Services.HistorialPedidos;

import OrderlyAPI.Expo2025.Entities.HistorialPedidos.HistorialPedidosEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.HistorialPedidosDTO;
import OrderlyAPI.Expo2025.Repositories.HistorialPedidos.HistorialPedidosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service

public class HistorialPedidosService {

    @Autowired
    private HistorialPedidosRepository repo;


    public java.util.List<HistorialPedidosDTO> getAllhistorialpedido(){
        List<HistorialPedidosEntity> historialpedidos = repo.findAll();
        return historialpedidos.stream()
                .map(this::convertirAHistorialPedidosDTO)
                .collect(Collectors.toList());
    }

    public HistorialPedidosDTO createHistorialPedidos(HistorialPedidosDTO historialpedidosDTO){
        if (historialpedidosDTO == null || historialpedidosDTO.getIdHistorial() == null || historialpedidosDTO.getIdHistorial().describeConstable().isEmpty()){
            throw new IllegalArgumentException("El historil  pedidos no puede ser nulo");
        }
        try{
            HistorialPedidosEntity historialpedidosEntity = convertirAHistorialPedidosEntity(historialpedidosDTO);
            HistorialPedidosEntity historialpedidosGuardado = repo.save(historialpedidosEntity);
            return convertirAHistorialPedidosDTO(historialpedidosGuardado);
        }catch (Exception e){
            log.error("Error al registrar historial pedidos: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el historial pedidos" + e.getMessage());
        }
    }

    public HistorialPedidosDTO updateHistorialpedidos(Long id, HistorialPedidosDTO historialpedidos){
        HistorialPedidosEntity historialpedidosExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Historial pedidos no encontrado"));

        historialpedidosExistente.setIdPedido(historialpedidos.getIdPedido());
        historialpedidosExistente.setNombreCliente(historialpedidos.getNombreCliente());
        historialpedidosExistente.setIdEmpleado(historialpedidos.getIdEmpleado());
        historialpedidosExistente.setIdMesa(historialpedidos.getIdMesa());
        historialpedidosExistente.setFechaHistorial(historialpedidos.getFechaHistorial());
        historialpedidosExistente.setReservacion(historialpedidos.getReservacion());
        historialpedidosExistente.setIdEstadoReserva(historialpedidos.getIdEstadoReserva());
        historialpedidosExistente.setIdPlatillo(historialpedidos.getIdPlatillo());
        historialpedidosExistente.setCantidad(historialpedidos.getCantidad());
        historialpedidosExistente.setPrecioUnitario(historialpedidos.getPrecioUnitario());
        historialpedidosExistente.setSubtotal(historialpedidos.getSubtotal());
        historialpedidosExistente.setPropina(historialpedidos.getPropina());
        historialpedidosExistente.setDescuento(historialpedidos.getDescuento());
        historialpedidosExistente.setTotal(historialpedidos.getTotal());

        HistorialPedidosEntity historialpedidosActualizado = repo.save(historialpedidosExistente);
        return convertirAHistorialPedidosDTO(historialpedidosActualizado);
    }

    public boolean deleteHistorialpedidos(Long id){
        try{
            HistorialPedidosEntity objHistorialpedidos = repo.findById(id).orElse(null);
            if (objHistorialpedidos != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Historial pedidos no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro historial pedidos con ID:" + id + " para eliminar.", 1);
        }
    }


    public HistorialPedidosEntity convertirAHistorialPedidosEntity(HistorialPedidosDTO dto){
        HistorialPedidosEntity entity = new HistorialPedidosEntity();
        entity.setIdPedido(dto.getIdPedido());
        entity.setNombreCliente(dto.getNombreCliente());
        entity.setIdEmpleado(dto.getIdEmpleado());
        entity.setIdMesa(dto.getIdMesa());
        entity.setFechaHistorial(dto.getFechaHistorial());
        entity.setReservacion(dto.getReservacion());
        entity.setIdEstadoReserva(dto.getIdEstadoReserva());
        entity.setIdPlatillo(dto.getIdPlatillo());
        entity.setCantidad(dto.getCantidad());
        entity.setPrecioUnitario(dto.getPrecioUnitario());
        entity.setSubtotal(dto.getSubtotal());
        entity.setPropina(dto.getPropina());
        entity.setDescuento(dto.getDescuento());
        entity.setTotal(dto.getTotal());
        return entity;
    }

    public HistorialPedidosDTO convertirAHistorialPedidosDTO(HistorialPedidosEntity historialpedidos){
        HistorialPedidosDTO dto = new HistorialPedidosDTO();
        dto.setIdPedido(historialpedidos.getIdPedido());
        dto.setNombreCliente(historialpedidos.getNombreCliente());
        dto.setIdEmpleado(historialpedidos.getIdEmpleado());
        dto.setIdMesa(historialpedidos.getIdMesa());
        dto.setFechaHistorial(historialpedidos.getFechaHistorial());
        dto.setReservacion(historialpedidos.getReservacion());
        dto.setIdEstadoReserva(historialpedidos.getIdEstadoReserva());
        dto.setIdPlatillo(historialpedidos.getIdPlatillo());
        dto.setCantidad(historialpedidos.getCantidad());
        dto.setPrecioUnitario(historialpedidos.getPrecioUnitario());
        dto.setSubtotal(historialpedidos.getSubtotal());
        dto.setPropina(historialpedidos.getPropina());
        dto.setDescuento(historialpedidos.getDescuento());
        dto.setTotal(historialpedidos.getTotal());
        return dto;
    }
}



