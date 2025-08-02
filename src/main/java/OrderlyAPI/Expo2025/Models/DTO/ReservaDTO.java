package OrderlyAPI.Expo2025.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
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

    @NotBlank(message = "La fecha reserva no puede ser nula")
    private LocalDateTime FReserva;

    @NotBlank(message = "La hora inicio no puede ser nula")
    private LocalTime HoraI;

    @NotBlank(message = "La hora fin no puede ser nula")
    private LocalTime HoraF;

    @NotBlank(message = "La capacidad de personas no puede ser nula")
    @Min(value = 1, message = "Debe haber al menos una persona")
    private Long CantidadPersonas;

    private Long idTipoReserva;

    private Long IdEstadoReserva;
}
