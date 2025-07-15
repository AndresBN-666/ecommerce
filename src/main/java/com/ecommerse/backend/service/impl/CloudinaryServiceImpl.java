package com.ecommerse.backend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ecommerse.backend.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;


    @Override
    public String subirImagen(MultipartFile archivo) {
        try{
            Map<String, Object> opciones = ObjectUtils.asMap(
                    "folder", "productos", // Carpeta personalizada
                    "use_filename", true,  // Usa el nombre del archivo original
                    "unique_filename", true // Agrega sufijo único para evitar duplicados
            );
            Map uploadResult = cloudinary.uploader().upload(archivo.getBytes(), opciones);
            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Error al subir imagen a Cloudinary", e);
        }
    }
}
