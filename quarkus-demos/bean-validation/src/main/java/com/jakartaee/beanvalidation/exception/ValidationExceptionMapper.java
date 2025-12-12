package com.jakartaee.beanvalidation.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ExceptionMapper para manejar errores de validación de Bean Validation.
 * Convierte ConstraintViolationException en respuestas HTTP 400 con detalles de las violaciones.
 * 
 * Demuestra cómo personalizar el manejo de errores de validación en REST endpoints.
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        // Extraer todas las violaciones
        List<Map<String, Object>> violations = exception.getConstraintViolations().stream()
                .map(this::violationToMap)
                .collect(Collectors.toList());
        
        // Crear respuesta estructurada
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Errores de validación");
        errorResponse.put("violations", violations);
        errorResponse.put("violationCount", violations.size());
        
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
    /**
     * Convierte una ConstraintViolation en un Map para la respuesta JSON.
     */
    private Map<String, Object> violationToMap(ConstraintViolation<?> violation) {
        Map<String, Object> map = new HashMap<>();
        map.put("field", getFieldName(violation));
        map.put("message", violation.getMessage());
        map.put("invalidValue", violation.getInvalidValue());
        map.put("constraint", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
        return map;
    }
    
    /**
     * Extrae el nombre del campo de la violación.
     */
    private String getFieldName(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        // Extraer solo el nombre del campo (última parte del path)
        int lastDot = propertyPath.lastIndexOf('.');
        return lastDot >= 0 ? propertyPath.substring(lastDot + 1) : propertyPath;
    }
}

