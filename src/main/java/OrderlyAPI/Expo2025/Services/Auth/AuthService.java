package OrderlyAPI.Expo2025.Services.Auth;

import OrderlyAPI.Expo2025.Config.Argon2.Argon2Password;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Repositories.Usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository repo;

    public boolean login(String correo, String contrasena){
        Argon2Password obHash = new Argon2Password();
        Optional<Object> list = repo.findByCorreo(correo).stream().findFirst();
        if (list.isPresent()){
            UsuarioEntity usuario = (UsuarioEntity) list.get();
            String nombreTipoUsuario = usuario.getRol().getUrol();
            System.out.println("Usuario ID encontrado: " + usuario.getId()+
                    ", email: " + usuario.getCorreo() +
                    ", rol: " + nombreTipoUsuario);
            //obtenrer la clasve del usuario que esta en la base de datos
            return obHash.verifyPassword(usuario.getContrasenia(), contrasena);
            // String HasDB = usuario.getContrasena();
            // boolean verificado = obHash.verifyPassword(HasDB, contrasena);
            //return verificado;
        }
        return false;
    }

    public Optional<UsuarioEntity> obtenerUsuario(String correo){
        Optional<UsuarioEntity> userOpt = repo.findByCorreo(correo);
        return (userOpt != null) ? userOpt : null;
    }

}
