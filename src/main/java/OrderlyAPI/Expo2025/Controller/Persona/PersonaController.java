package OrderlyAPI.Expo2025.Controller.Persona;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatosDuplicados;
import OrderlyAPI.Expo2025.Models.DTO.PersonaDTO;
import OrderlyAPI.Expo2025.Services.Persona.PersonaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/apiPersona")
@CrossOrigin
public class PersonaController {

    @Autowired
    private PersonaService service;

    @GetMapping("/getDataPersona")
    public ResponseEntity<Page<PersonaDTO>> getData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        if (size <= 0 || size > 50){
            return ResponseEntity.badRequest().build();
        }
        Page<PersonaDTO> datos = service.getAllPersonas(page, size);
        return ResponseEntity.ok(datos);
    }

    /**
     * Normalizamos el JSON de entrada para NO depender del case exacto.
     * Aceptamos: Pnombre|pnombre|firstName, Snombre|snombre|secondName, etc.
     */
    @PostMapping("/createPersona")
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Map<String, Object> body, HttpServletRequest request){
        try{
            PersonaDTO dto = mapToPersonaDTO(body);

            // Validaciones mínimas (las de @Valid viven en el DTO, pero aquí prevenimos 400 “raros”)
            List<String> faltantes = new ArrayList<>();
            if (isBlank(dto.getPnombre()))   faltantes.add("Pnombre");
            if (isBlank(dto.getSnombre()))   faltantes.add("Snombre");
            if (isBlank(dto.getApellidoP())) faltantes.add("ApellidoP");
            if (isBlank(dto.getApellidoM())) faltantes.add("ApellidoM");
            if (dto.getFechaN() == null)     faltantes.add("FechaN (yyyy-MM-dd)");
            if (isBlank(dto.getDireccion())) faltantes.add("Direccion");
            if (dto.getIdDoc() == null)      faltantes.add("IdDoc");

            if (!faltantes.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "status", "VALIDATION_ERROR",
                        "missing", faltantes
                ));
            }

            PersonaDTO respuesta = service.createPersona(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status","success",
                    "data", respuesta
            ));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al registrar",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/modificarPersona/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PersonaDTO persona,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try{
            PersonaDTO personaActualizado = service.updatePersona(id, persona);
            return ResponseEntity.ok(personaActualizado);
        }
        catch (ExceptionDatoNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExceptionDatosDuplicados e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", "Datos duplicados", "campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarPersona/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
        try{
            if (!service.deletePersona(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("X-Mensaje-Error", "Persona no encontrada")
                        .body(Map.of(
                                "error", "Not found",
                                "mensaje", "La persona no ha sido encontrada",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Persona eliminada exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar la Persona",
                    "detail", e.getMessage()
            ));
        }
    }

    // ---------- Helpers de normalización ----------
    private static boolean isBlank(String s){ return s == null || s.isBlank(); }

    private static String s(Map<String, Object> m, String... keys){
        for (String k : keys){
            Object v = m.get(k);
            if (v != null) return String.valueOf(v).trim();
        }
        return null;
    }

    private static Long l(Map<String, Object> m, String... keys){
        for (String k : keys){
            Object v = m.get(k);
            if (v == null) continue;
            try{
                if (v instanceof Number) return ((Number)v).longValue();
                return Long.parseLong(String.valueOf(v).trim());
            }catch (Exception ignored){}
        }
        return null;
    }

    private static LocalDate d(Map<String, Object> m, String... keys){
        for (String k : keys){
            Object v = m.get(k);
            if (v == null) continue;
            String s = String.valueOf(v).trim();
            try{
                return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE); // yyyy-MM-dd
            }catch (Exception ignored){}
        }
        return null;
    }

    /**
     * Construye un PersonaDTO aceptando alias del front:
     * - Pnombre | pnombre | firstName
     * - Snombre | snombre | secondName
     * - ApellidoP | apellidoP | lastNameP
     * - ApellidoM | apellidoM | lastNameM
     * - FechaN | fechaN | birthDate
     * - Direccion | direccion | address
     * - IdDoc | idDoc | idDocumento
     */
    private static PersonaDTO mapToPersonaDTO(Map<String, Object> body){
        PersonaDTO dto = new PersonaDTO();
        dto.setPnombre(  s(body, "Pnombre","pnombre","firstName") );
        dto.setSnombre(  s(body, "Snombre","snombre","secondName") );
        dto.setApellidoP(s(body, "ApellidoP","apellidoP","lastNameP") );
        dto.setApellidoM(s(body, "ApellidoM","apellidoM","lastNameM") );
        dto.setDireccion(s(body, "Direccion","direccion","address") );
        dto.setFechaN(   d(body, "FechaN","fechaN","birthDate") );
        dto.setIdDoc(    l(body, "IdDoc","idDoc","idDocumento") );
        // id (si lo mandan)
        Long id = l(body, "Id","id");
        if (id != null) dto.setId(id);
        return dto;
    }
}
