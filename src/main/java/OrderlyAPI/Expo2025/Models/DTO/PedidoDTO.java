package OrderlyAPI.Expo2025.Models.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter @Setter
public class PedidoDTO {

    private Long Id;

    @NotBlank(message = "El nombre cliente no puede ser nulo")
    @Size(max = 100, message = "El nombre del cliente no puede tener mas de 100 caracteres")
    private String Nombrecliente;

    private Long IdMesa;

    private Long IdEmpleado;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate FPedido;

    private Long IdEstadoPedido;

    @NotBlank(message = "Las observacion no puede ser nula")
    @Size(max = 100, message = "Las observaciones no pueden tener mas de 100 caracteres")
    private String Observaciones;

    @NotNull(message = "La cantidad no puede ser nula") // ← CAMBIA @NotBlank por @NotNull
    @Min(value = 1, message = "La cantidad no puede ser cero o negativa")
    private Long Cantidad;

    @DecimalMin(value = "0.01", message = "El total pedido debe ser mayor de 0.01")
    @DecimalMax(value = "99999999.99", message = "El total pedido debe ser menor de 99999999.99")
    private double TotalPedido;

    @DecimalMin(value = "0.01", message = "El subtotal debe ser mayor de 0.01")
    @DecimalMax(value = "99999999.99", message = "El subtotal debe ser menor de 99999999.99")
    private double Subtotal;

    @PositiveOrZero   // permite 0
    @DecimalMax(value = "99999999.99", message = "La propina debe ser menor de 99999999.99")
    private double Propina;

    private Long IdPlatillo;
}
