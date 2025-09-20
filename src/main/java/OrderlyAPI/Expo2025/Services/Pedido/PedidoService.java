package OrderlyAPI.Expo2025.Services.Pedido;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.EstadoPedido.EstadoPedidoEntity;
import OrderlyAPI.Expo2025.Entities.Mesa.MesaEntity;
import OrderlyAPI.Expo2025.Entities.PedidoDetalle.PedidoDetalleEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.PedidoDTO;
import OrderlyAPI.Expo2025.Models.DTO.PedidoItemDTO;
import OrderlyAPI.Expo2025.Repositories.Empleado.EmpleadoRepository;
import OrderlyAPI.Expo2025.Repositories.EstadoPedido.EstadoPedidoRepository;
import OrderlyAPI.Expo2025.Repositories.Mesa.MesaRepository;
import OrderlyAPI.Expo2025.Repositories.PedidoDetalle.PedidoDetalleRepository;
import OrderlyAPI.Expo2025.Repositories.Pedido.PedidoRepository;
import OrderlyAPI.Expo2025.Repositories.Platillo.PlatilloRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@CrossOrigin
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    // NUEVO (mínimo): repo del detalle
    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    @Autowired
    private PlatilloRepository platilloRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private EstadoPedidoRepository estadoPedidoRepository;

    @PersistenceContext
    EntityManager entityManager;

    // ===================== READ =====================
    public Page<PedidoDTO> getDataPedido(int page, int size) {
        return pedidoRepository.findAll(PageRequest.of(page, size))
                .map(this::convertirAPedidoDTO);
    }

    public PedidoDTO getById(Long id) {
        PedidoEntity p = pedidoRepository.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Pedido no encontrado"));
        return convertirAPedidoDTO(p);
    }

    // ===================== CREATE =====================
    @Transactional
    public PedidoDTO createPedido(@Valid PedidoDTO dto) {
        // Cargar relaciones del encabezado (misma lógica que ya usas)
        MesaEntity mesa = mesaRepository.findById(dto.getIdMesa())
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Mesa no encontrada"));
        EmpleadoEntity emp = empleadoRepository.findById(dto.getIdEmpleado())
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Empleado no encontrado"));
        EstadoPedidoEntity est = estadoPedidoRepository.findById(dto.getIdEstadoPedido())
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Estado de pedido no encontrado"));

        PedidoEntity ped = new PedidoEntity();
        // ID lo asigna trigger/secuencia en Oracle (no setear aquí)
        ped.setNombreCliente(dto.getNombreCliente());
        ped.setMesas(mesa);
        ped.setEmpleado(emp);
        ped.setFPedido(dto.getFPedido() != null ? dto.getFPedido() : LocalDateTime.now());
        ped.setEstpedido(est);
        ped.setObservaciones(dto.getObservaciones());
        if (ped.getDetalles() == null) ped.setDetalles(new ArrayList<>());

        // MINIMO: construir líneas y calcular subtotal desde DTO (nuevo o legado)
        double subtotal = upsertDetallesDesdeDTO(ped, dto);
        ped.setSubtotal(subtotal);

        double propina = dto.getPropina() >= 0 ? dto.getPropina() : 0.0;
        ped.setPropina(propina);
        ped.setTotalPedido(subtotal + propina);

        // Guardar (cascade = ALL persiste también PEDIDODETALLE)
        PedidoEntity saved = pedidoRepository.save(ped);
        return convertirAPedidoDTO(saved);
    }

    // ===================== UPDATE =====================
    @Transactional
    public PedidoDTO modificarPedido(Long id, @Valid PedidoDTO dto) {
        PedidoEntity ped = pedidoRepository.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Pedido no encontrado"));

        // Actualizar encabezado (no tocamos tu estilo)
        if (dto.getNombreCliente() != null) ped.setNombreCliente(dto.getNombreCliente());

        if (dto.getIdMesa() != null) {
            MesaEntity mesa = mesaRepository.findById(dto.getIdMesa())
                    .orElseThrow(() -> new ExceptionDatoNoEncontrado("Mesa no encontrada"));
            ped.setMesas(mesa);
        }
        if (dto.getIdEmpleado() != null) {
            EmpleadoEntity emp = empleadoRepository.findById(dto.getIdEmpleado())
                    .orElseThrow(() -> new ExceptionDatoNoEncontrado("Empleado no encontrado"));
            ped.setEmpleado(emp);
        }
        if (dto.getIdEstadoPedido() != null) {
            EstadoPedidoEntity est = estadoPedidoRepository.findById(dto.getIdEstadoPedido())
                    .orElseThrow(() -> new ExceptionDatoNoEncontrado("Estado de pedido no encontrado"));
            ped.setEstpedido(est);
        }
        if (dto.getObservaciones() != null) ped.setObservaciones(dto.getObservaciones());
        if (ped.getDetalles() == null) ped.setDetalles(new ArrayList<>());

        // Si llegan Items (o formato legado), reconstruimos detalle y totales
        if ((dto.getItems() != null && !dto.getItems().isEmpty()) || tieneValoresLegado(dto)) {
            double subtotal = upsertDetallesDesdeDTO(ped, dto); // limpia y vuelve a armar
            ped.setSubtotal(subtotal);
            ped.setTotalPedido(subtotal + (ped.getPropina() != null ? ped.getPropina() : 0.0));
        }

        // Propina (si viene) y total
        if (dto.getPropina() >= 0) {
            ped.setPropina(dto.getPropina());
            ped.setTotalPedido((ped.getSubtotal() != null ? ped.getSubtotal() : 0.0) + dto.getPropina());
        }

        PedidoEntity actualizado = pedidoRepository.save(ped);
        return convertirAPedidoDTO(actualizado);
    }

    // ===================== DELETE =====================
    @Transactional
    public boolean eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) return false;
        pedidoRepository.deleteById(id);
        return true;
    }

    // ===================== HELPERS =====================

    /**
     * Construye (o reemplaza) las líneas del pedido a partir del DTO.
     * Soporta:
     *  - NUEVO: dto.getItems() con { IdPlatillo, Cantidad, (opcional) PrecioUnitario }
     *  - LEGADO: dto.getIdPlatillo() + dto.getCantidad() (si aún existen esos getters)
     * Devuelve el subtotal calculado (sum(items.cantidad * precioUnitario)).
     */
    private double upsertDetallesDesdeDTO(PedidoEntity ped, PedidoDTO dto) {
        // limpiar detalle previo (orphanRemoval = true en la entidad)
        ped.getDetalles().clear();

        double subtotal = 0.0;

        // Preferir formato NUEVO (Items[])
        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            for (PedidoItemDTO item : dto.getItems()) {
                PlatilloEntity pl = platilloRepository.findById(item.getIdPlatillo())
                        .orElseThrow(() -> new ExceptionDatoNoEncontrado("Platillo no encontrado: " + item.getIdPlatillo()));

                int cant = (item.getCantidad() != null && item.getCantidad() > 0) ? item.getCantidad() : 1;
                double precio = (item.getPrecioUnitario() != null) ? item.getPrecioUnitario() : pl.getPrecio();

                PedidoDetalleEntity det = new PedidoDetalleEntity();
                // id lo pone el trigger de Oracle (no seteamos)
                det.setPedido(ped);
                det.setPlatillo(pl);
                det.setCantidad(cant);
                det.setPrecioUnitario(precio);

                ped.getDetalles().add(det);
                subtotal += precio * cant;
            }
            return subtotal;
        }

        // Compatibilidad LEGADO: IdPlatillo + Cantidad en el DTO de encabezado (si siguen existiendo)
        try {
            Method getIdPlatillo = PedidoDTO.class.getMethod("getIdPlatillo");
            Method getCantidad   = PedidoDTO.class.getMethod("getCantidad");
            Object idPlatilloObj = getIdPlatillo.invoke(dto);
            Object cantidadObj   = getCantidad.invoke(dto);

            if (idPlatilloObj != null) {
                Long idPlatillo = (Long) idPlatilloObj;
                Integer cant = (cantidadObj != null) ? (Integer) cantidadObj : 1;

                PlatilloEntity pl = platilloRepository.findById(idPlatillo)
                        .orElseThrow(() -> new ExceptionDatoNoEncontrado("Platillo no encontrado: " + idPlatillo));

                PedidoDetalleEntity det = new PedidoDetalleEntity();
                det.setPedido(ped);
                det.setPlatillo(pl);
                det.setCantidad((cant != null && cant > 0) ? cant : 1);
                det.setPrecioUnitario(pl.getPrecio());

                ped.getDetalles().add(det);
                subtotal += pl.getPrecio() * det.getCantidad();

                return subtotal;
            }
        } catch (ReflectiveOperationException ignore) {
            // Si ya no existen esos getters, seguimos al error final.
        }

        throw new IllegalArgumentException("Debe incluir Items o (IdPlatillo y Cantidad) en el pedido.");
    }

    /**
     * Detecta si el DTO viene en formato legado con IdPlatillo y Cantidad.
     */
    private boolean tieneValoresLegado(PedidoDTO dto) {
        try {
            Method getIdPlatillo = PedidoDTO.class.getMethod("getIdPlatillo");
            Method getCantidad   = PedidoDTO.class.getMethod("getCantidad");
            return getIdPlatillo.invoke(dto) != null && getCantidad.invoke(dto) != null;
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    /**
     * Mapea entidad → DTO manteniendo tu estilo y nombres.
     * (Si tu proyecto ya tiene un mapper central, puedes seguir usándolo).
     */
    private PedidoDTO convertirAPedidoDTO(PedidoEntity p) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(p.getId());
        dto.setNombreCliente(p.getNombreCliente());
        dto.setIdMesa(p.getMesas().getId());
        dto.setIdEmpleado(p.getEmpleado().getId());
        dto.setFPedido(p.getFPedido());
        dto.setIdEstadoPedido(p.getEstpedido().getId());
        dto.setObservaciones(p.getObservaciones());
        dto.setSubtotal(p.getSubtotal() != null ? p.getSubtotal() : 0.0);
        dto.setPropina(p.getPropina() != null ? p.getPropina() : 0.0);
        dto.setTotalPedido(p.getTotalPedido() != null ? p.getTotalPedido() : (dto.getSubtotal() + dto.getPropina()));

        // Items desde el detalle
        List<PedidoItemDTO> items = new ArrayList<>();
        if (p.getDetalles() != null) {
            for (PedidoDetalleEntity det : p.getDetalles()) {
                PedidoItemDTO it = new PedidoItemDTO();
                it.setIdPlatillo(det.getPlatillo().getId());
                it.setCantidad(det.getCantidad());
                it.setPrecioUnitario(det.getPrecioUnitario()); // útil para auditoría/ofertas
                items.add(it);
            }
        }
        dto.setItems(items);

        return dto;
    }
}
