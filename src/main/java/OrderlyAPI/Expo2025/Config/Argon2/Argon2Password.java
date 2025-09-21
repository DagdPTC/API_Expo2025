package OrderlyAPI.Expo2025.Config.Argon2;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Service;

@Service
public class Argon2Password {
    private static final int INTERATIONS = 10;
    private static final int MEMORY = 32768;
    private static final int PARALLELISM = 2;

    //crear un instancia de argon2
    private Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    public String EncryptPassword(String password){
        return argon2.hash(INTERATIONS, MEMORY, PARALLELISM, password);
    }

    public boolean verifyPassword(String hasDB, String contrasena) {
        return argon2.verify(hasDB, contrasena);
    }

}
