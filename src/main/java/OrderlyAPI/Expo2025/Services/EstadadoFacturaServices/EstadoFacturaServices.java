package OrderlyAPI.Expo2025.Services.EstadadoFacturaServices;

import OrderlyAPI.Expo2025.Entities.EstadoFactura.EstadoFacturaEntity;
import OrderlyAPI.Expo2025.Repositories.EstadoFactura.EstadoFacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EstadoFacturaServices {


    @Autowired
    private EstadoFacturaRepository estadoFacturaRepository;

    public Page<EstadoFacturaEntity> findAll(Pageable pageable){
        return estadoFacturaRepository.findAll(pageable);
    }

    public EstadoFacturaEntity findByIdOrThrow(Long id){
        return estadoFacturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EstadoFactura no encontrado"));
    }
}


