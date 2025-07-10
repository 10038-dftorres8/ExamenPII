package ec.edu.espe.arquitectura.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> manejarResourceNotFound(ResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Recurso no encontrado");
        error.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler({ CreateEntityException.class, UpdateEntityException.class, DeleteEntityException.class })
    public ResponseEntity<Map<String, String>> manejarErroresDeNegocio(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error en la operación");
        error.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler({ TurnoYaAbiertoException.class, TurnoNoAbiertoException.class, DiferenciaEnCierreException.class })
    public ResponseEntity<Map<String, String>> manejarErroresDeCaja(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error en gestión de caja");
        error.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarErroresDeValidacion(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error de validación");
        error.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarErroresGenerales(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno");
        error.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

