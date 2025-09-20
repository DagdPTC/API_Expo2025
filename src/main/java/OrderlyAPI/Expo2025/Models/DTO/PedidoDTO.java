package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class PedidoDTO {

    private Long Id;

    @NotBlank
    private String NombreCliente;

    @NotNull
    private Long IdMesa;

    @NotNull
    private Long IdEmpleado;

    @NotNull
    private LocalDateTime FPedido;

    @NotNull
    private Long IdEstadoPedido;

    @NotBlank
    private String Observaciones;

    @DecimalMin("0.00") @DecimalMax("99999999.99")
    private double Subtotal;

    @DecimalMin("0.00") @DecimalMax("99999999.99")
    private double Propina;

    @DecimalMin("0.00") @DecimalMax("99999999.99")
    private double TotalPedido;

    // ---- NUEVO: listado de líneas ----
    @NotNull @Size(min = 1, message = "Debe incluir al menos un platillo")
    private List<PedidoItemDTO> Items;
}
