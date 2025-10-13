package OrderlyAPI.Expo2025.Controller.EstadoFacturaController;


import OrderlyAPI.Expo2025.Entities.EstadoFactura.EstadoFacturaEntity;
import OrderlyAPI.Expo2025.Services.EstadadoFacturaServices.EstadoFacturaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiEstadoFactura")
@CrossOrigin // si lo usas
public class EstadoFacturaController {

    @Autowired
    private EstadoFacturaServices estadoFacturaService;

    @GetMapping("/getDataEstadoFactura")
    public Page<EstadoFacturaEntity> getDataEstadoFactura(Pageable pageable){
        return estadoFacturaService.findAll(pageable);
    }

    @GetMapping("/getEstadoFacturaById/{id}")
    public ResponseEntity<EstadoFacturaEntity> getEstadoFacturaById(@PathVariable Long id){
        return ResponseEntity.ok(estadoFacturaService.findByIdOrThrow(id));
    }
}
