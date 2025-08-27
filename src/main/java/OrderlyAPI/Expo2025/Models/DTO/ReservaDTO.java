package OrderlyAPI.Expo2025.Models.DTO;

import OrderlyAPI.Expo2025.Entities.Mesa.MesaEntity;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter @Setter
public class ReservaDTO {

    private Long Id;

    @NotBlank(message = "El nombre cliente no puede ser nulo")
    @Size(max = 100, message = "El nombre del cliente no puede tener mas de 100 caracteres")
    private String nomCliente;

    @NotBlank(message = "El telefono no puede ser nulo")
    @Pattern(regexp = "\\d{4}-\\d{4}", message = "El telefono debe tener el formato 0000-0000")
    private String Telefono;

    private Long IdMesa;

    private LocalDate FReserva;

    private LocalTime HoraI;

    private LocalTime HoraF;

    @Min(value = 1, message = "Debe haber al menos una persona")
    @Max(value = 20, message = "No puede haber mas de 20 personas")
    private Long CantidadPersonas;

    private Long idTipoReserva;

    private Long IdEstadoReserva;
}
