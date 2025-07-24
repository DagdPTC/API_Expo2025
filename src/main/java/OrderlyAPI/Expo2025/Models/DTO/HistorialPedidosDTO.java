package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter @Setter @NotBlank
public class HistorialPedidosDTO {
    private Long IdHistorial;
    private Long IdPedido;
    private String NombreCliente;
    private Long IdEmpleado;
    private Long IdMesa;
    private Date FechaHistorial;
    private Boolean Reservacion;
    private Long idEstadoReserva;
    private Long IdPlatillo;
    private Number Cantidad;
    private Double PrecioUnitario;
    private Double Subtotal;
    private Double Propina;
    private Double Descuento;
    private Double Total;
}
