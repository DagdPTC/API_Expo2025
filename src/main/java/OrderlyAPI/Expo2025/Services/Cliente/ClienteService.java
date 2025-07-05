package OrderlyAPI.Expo2025.Services.Cliente;

import OrderlyAPI.Expo2025.Entities.Cliente.ClienteEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.ClienteDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.Cliente.ClienteRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClienteService {
    private ClienteRepository repo;


    public List<ClienteDTO> getAllClientes(){
        List<ClienteEntity> clientes = repo.findAll();
        return clientes.stream()
                .map(this::convertirAClientesDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO createRol(ClienteDTO clienteDTO){
        if (clienteDTO == null || clienteDTO.getIdPersona() == null || clienteDTO.getIdPersona().describeConstable().isEmpty()){
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        try{
            ClienteEntity clienteEntity = convertirAClientesEntity(clienteDTO);
            ClienteEntity clienteGuardado = repo.save(clienteEntity);
            return convertirAClientesDTO(clienteGuardado);
        }catch (Exception e){
            log.error("Error al registrar cliente: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el cliente" + e.getMessage());
        }
    }

    public ClienteDTO updateCliente(Long id, ClienteDTO cliente){
        ClienteEntity clienteExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Cliente no encontrado"));

        clienteExistente.setIdPersona(cliente.getIdPersona());
        clienteExistente.setFRegistro(cliente.getFRegistro());

        ClienteEntity clienteActualizado = repo.save(clienteExistente);
        return convertirAClientesDTO(clienteActualizado);
    }

    public boolean deleteCliente(Long id){
        try{
            ClienteEntity objCliente = repo.findById(id).orElse(null);
            if (objCliente != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Cliente no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro cliente con ID:" + id + " para eliminar.", 1);
        }
    }


    public ClienteEntity convertirAClientesEntity(ClienteDTO cliente){
        ClienteEntity dto = new ClienteEntity();
        dto.setId(cliente.getId());
        dto.setIdPersona(cliente.getIdPersona());
        dto.setFRegistro(cliente.getFRegistro());
        return dto;
    }

    public ClienteDTO convertirAClientesDTO(ClienteEntity cliente){
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setIdPersona(cliente.getIdPersona());
        dto.setFRegistro(cliente.getFRegistro());
        return dto;
    }
}
