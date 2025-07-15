package com.ecommerse.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String subirImagen(MultipartFile archivo);
}
