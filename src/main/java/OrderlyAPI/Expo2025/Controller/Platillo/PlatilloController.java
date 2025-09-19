package OrderlyAPI.Expo2025.Controller.Platillo;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.PlatilloDTO;
import OrderlyAPI.Expo2025.Services.Platillo.PlatilloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/apiPlatillo")
@CrossOrigin
public class PlatilloController {

    @Autowired
    private PlatilloService service;

    @GetMapping("/getDataPlatillo")
    public ResponseEntity<Page<PlatilloDTO>> getData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PlatilloDTO> datos = service.getAllPlatillos(page, size);
        return ResponseEntity.ok(datos);
    }

    // NUEVO: resolver por ID
    @GetMapping("/getPlatilloById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            PlatilloDTO dto = service.getPlatilloById(id);
            return ResponseEntity.ok(dto);
        } catch (ExceptionDatoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error","Not found", "message","Platillo no encontrado", "timestamp", Instant.now().toString())
            );
        }
    }

    @PostMapping("/createPlatillo")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody PlatilloDTO platillo){
        PlatilloDTO respuesta = service.createPlatillo(platillo);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status","success","data",respuesta));
    }

    @PutMapping("/modificarPlatillo/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody PlatilloDTO platillo){
        PlatilloDTO actualizado = service.updatePlatillo(id, platillo);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/eliminarPlatillo/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        boolean ok = service.deletePlatillo(id);
        if (!ok) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Not found", "message", "El Platillo no ha sido encontrado", "timestamp", Instant.now().toString())
            );
        }
        return ResponseEntity.ok(Map.of("status","ok","message","Platillo eliminado"));
    }
}
