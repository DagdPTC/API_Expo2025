package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.Size;
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

    @Size(max = 500, message = "La URL de la imagen no puede exceder 500 caracteres")
    private String imagenUrl;

    // Nuevo: para gestionar borrado/transformaciones en Cloudinary
    @Size(max = 255, message = "El publicId no puede exceder 255 caracteres")
    private String publicId;
}
