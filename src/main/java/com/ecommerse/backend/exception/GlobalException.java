package com.ecommerse.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> manejarRuntimeException(RuntimeException ex){
        Map<String, String> errores = new HashMap<>();
        errores.put("mensaje", ex.getMessage());
        return new ResponseEntity<>(errores,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>errosDeValidaciones(MethodArgumentNotValidException ex){
        Map<String, String> mapa = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                mapa.put(error.getField(),error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(mapa);
    }
}
