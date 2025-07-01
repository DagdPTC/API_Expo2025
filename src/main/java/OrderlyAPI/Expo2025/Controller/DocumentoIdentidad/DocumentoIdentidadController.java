package OrderlyAPI.Expo2025.Controller.DocumentoIdentidad;

import OrderlyAPI.Expo2025.Models.DTO.DocumentoIdentidadDTO;
import OrderlyAPI.Expo2025.Services.DocumentoIdentidad.DocumentoIdentidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiDocumentoIdentidad")
public class DocumentoIdentidadController {

    @Autowired
    private DocumentoIdentidadService service;

    @GetMapping("/getDataDocumentoIdentidad")
    public List<DocumentoIdentidadDTO> getData(){
        return service.getAllDocumentosIdentidades();
    }
}
