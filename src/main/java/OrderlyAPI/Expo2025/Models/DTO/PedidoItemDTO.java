package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PedidoItemDTO {

    @NotNull
    private Long IdPlatillo;

    @NotNull @Min(1)
    private Integer Cantidad;

    // Opcional: si no lo envía el cliente, se valida contra catálogo
    @DecimalMin(value = "0.00") @DecimalMax(value = "99999999.99")
    private Double PrecioUnitario;
}
