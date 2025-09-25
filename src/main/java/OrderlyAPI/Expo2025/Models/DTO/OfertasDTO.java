package OrderlyAPI.Expo2025.Models.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class OfertasDTO {
    private Long id;
    private String descripcion;
    private Double porcentajeDescuento;
    private Double precioOferta;
    private LocalDate fechaInicio;   // usar LocalDate igual que en Entity
    private LocalDate fechaFin;      // usar LocalDate igual que en Entity
    private Boolean activa;          // mejor Boolean que Integer
    private Long idPlatillo;         // FK
}
