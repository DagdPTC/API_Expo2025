package OrderlyAPI.Expo2025.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".png", ".jpeg"};

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }

    // Subida por defecto -> siempre a la carpeta "menu"
    public String uploadImage(MultipartFile file) throws IOException{
        validateImage(file);
        Map<?, ?> uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.asMap(
                        "resource_type", "auto",
                        "quality", "auto:good",
                        "folder", "menu" // <-- guardamos en la carpeta menu
                ));
        return (String) uploadResult.get("secure_url");
    }

    // Subida a la carpeta indicada (se mantiene igual)
    public String uploadImage(MultipartFile file, String folder) throws IOException{
        validateImage(file);
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        String uniqueFilename = "img_" + UUID.randomUUID() + fileExtension;

        Map<String, Object> options = ObjectUtils.asMap(
                "folder", folder,                 // Carpeta de destino
                "public_id", uniqueFilename,      // Nombre único para el archivo
                "use_filename", false,            // No usar el nombre original
                "unique_filename", false,         // Ya generamos nombre único
                "overwrite", false,               // No sobreescribir archivos existentes
                "resource_type", "auto",
                "quality", "auto:good"
        );
        Map<?,?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return (String) uploadResult.get("secure_url");
    }

    private void validateImage(MultipartFile file) {
        if (file.isEmpty()) throw new IllegalArgumentException("El archivo no puede estar vacio");
        if (file.getSize() > MAX_FILE_SIZE) throw new IllegalArgumentException("El tamaño del archivo no puede exceder los 5MB");
        String originalFilename = file.getOriginalFilename();
        if(originalFilename == null) throw new IllegalArgumentException("Nombre del archivo no valido");
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if(!Arrays.asList(ALLOWED_EXTENSIONS).contains(extension)) throw new IllegalArgumentException("Solo se permiten archivos jpg, jpeg o png");
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) throw new IllegalArgumentException("El archivo debe ser una imagen valida");
    }
}
