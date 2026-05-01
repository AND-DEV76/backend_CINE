package com.cinemaroyale.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Sube una imagen a Cloudinary y devuelve la URL pública segura (https).
     * Las imágenes se almacenan en la carpeta "cinemaroyale/posters" de tu cuenta.
     *
     * @param archivo El archivo de imagen recibido desde el formulario
     * @return URL pública de la imagen subida, o null si el archivo es nulo/vacío
     */
    public String uploadImage(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(archivo.getBytes(), ObjectUtils.asMap(
                    "folder", "cinemaroyale/posters",
                    "resource_type", "image"
            ));

            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            throw new RuntimeException("Error al subir imagen a Cloudinary", e);
        }
    }

    /**
     * Elimina una imagen de Cloudinary usando su public_id.
     *
     * @param imageUrl La URL completa de la imagen en Cloudinary
     */
    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar imagen de Cloudinary", e);
        }
    }

    /**
     * Extrae el public_id de una URL de Cloudinary.
     * El public_id es la parte después de "/upload/vXXXX/" sin la extensión.
     */
    private String extractPublicId(String url) {
        try {
            // Buscar "/upload/" en la URL y tomar todo lo que sigue
            int uploadIndex = url.indexOf("/upload/");
            if (uploadIndex == -1) return null;

            String afterUpload = url.substring(uploadIndex + 8); // saltar "/upload/"

            // Saltar la versión (v1234567890/)
            if (afterUpload.startsWith("v")) {
                int slashIndex = afterUpload.indexOf("/");
                if (slashIndex != -1) {
                    afterUpload = afterUpload.substring(slashIndex + 1);
                }
            }

            // Quitar la extensión del archivo
            int dotIndex = afterUpload.lastIndexOf(".");
            if (dotIndex != -1) {
                afterUpload = afterUpload.substring(0, dotIndex);
            }

            return afterUpload;
        } catch (Exception e) {
            return null;
        }
    }
}
