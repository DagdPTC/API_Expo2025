package OrderlyAPI.Expo2025.Models.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
public class EmpleadoDTO {

    private Long Id;
    private Long IdPersona;
    private Long IdUsuario;

    // Persona
    @JsonProperty("firstName")  private String firstName;     // Persona.PrimerNombre
    @JsonProperty("secondName") private String secondName;    // Persona.SegundoNombre
    @JsonProperty("lastNameP")  private String lastNameP;     // Persona.ApellidoPaterno
    @JsonProperty("lastNameM")  private String lastNameM;     // Persona.ApellidoMaterno
    @JsonProperty("birthDate")  @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;                              // Persona.FechaNacimiento
    @JsonProperty("address")    private String address;       // Persona.Direccion
    @JsonProperty("docType")    private String docType;       // TipoDocumento.TipoDocumento
    @JsonProperty("docNumber")  private String docNumber;     // DocumentoIdentidad.NumeroDocumento

    // Usuario/Rol
    @JsonProperty("username")   private String username;      // Usuario.NombreUsuario
    @JsonProperty("email")      private String email;         // Usuario.Correo
    @JsonProperty("role")       private String role;          // Rol.Rol (nombre legible)
    @JsonProperty("rolId")      private Long rolId;           // <-- NUEVO: id de Rol para actualizar FK

    // Empleado
    @JsonProperty("hireDate")   @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime hireDate;                           // Empleado.FechaContratacion
}
