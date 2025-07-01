package OrderlyAPI.Expo2025.Controller.Categoria;

import OrderlyAPI.Expo2025.Models.DTO.CategoriaDTO;
import OrderlyAPI.Expo2025.Services.Categoria.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiCategoria")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping("/getDataCategoria")
    public List<CategoriaDTO> getData(){
        return service.getAllCategorias();
    }
}
