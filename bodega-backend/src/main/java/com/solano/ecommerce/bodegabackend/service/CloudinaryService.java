package com.solano.ecommerce.bodegabackend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String upload(MultipartFile file, String folder) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "bodega_ecommerce/".concat(folder)
            ));

            // Extraemos solo lo que nos importa: la URL segura (https)
            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            throw new RuntimeException("Error al subir la imagen a cloudinary", e);
        }
    }

    // --- NUEVO: MÉTODO PARA BORRAR ---
    public void delete(String publicId) {
        try {
            // El método destroy de Cloudinary requiere el public_id
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            System.err.println("Error al eliminar imagen anterior de Cloudinary: " + e.getMessage());
        }
    }

    // --- EXTRAER EL ID DE LA URL ---
    public String getPublicId(String imageUrl) {
        try {
            // Buscamos dónde empieza el nombre de tu carpeta principal
            int startIndex = imageUrl.indexOf("bodega_ecommerce");

            // Si no encuentra la carpeta, quizás es una imagen vieja o externa, retornamos null
            if (startIndex == -1) return null;

            // Extraemos desde la carpeta hasta el final
            String publicId = imageUrl.substring(startIndex);

            // Quitamos la extensión (.jpg, .png) porque el public_id no la lleva
            int lastDotIndex = publicId.lastIndexOf(".");
            if (lastDotIndex != -1) {
                publicId = publicId.substring(0, lastDotIndex);
            }

            return publicId;
        } catch (Exception e) {
            return null;
        }
    }
}
