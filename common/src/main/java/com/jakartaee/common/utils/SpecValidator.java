package com.jakartaee.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Utilidad para validar que las especificaciones de Jakarta EE están disponibles.
 * 
 * Uso:
 * <pre>
 * SpecValidator validator = new SpecValidator();
 * if (validator.isAvailable("batch")) {
 *     System.out.println("Jakarta Batch está disponible");
 * }
 * 
 * Map<String, Boolean> allSpecs = validator.checkAll();
 * </pre>
 */
public class SpecValidator {
    
    private final Map<String, Supplier<Boolean>> specCheckers;
    
    public SpecValidator() {
        this.specCheckers = new HashMap<>();
        initializeCheckers();
    }
    
    private void initializeCheckers() {
        // Jakarta Annotations
        specCheckers.put("annotations", () -> {
            try {
                Class.forName("jakarta.annotation.PostConstruct");
                Class.forName("jakarta.annotation.PreDestroy");
                Class.forName("jakarta.annotation.Resource");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
        
        // Jakarta Activation
        specCheckers.put("activation", () -> {
            try {
                Class.forName("jakarta.activation.DataHandler");
                Class.forName("jakarta.activation.MimeType");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
        
        // Jakarta Authentication
        specCheckers.put("authentication", () -> {
            try {
                Class.forName("jakarta.security.auth.message.AuthException");
                Class.forName("jakarta.security.auth.message.config.AuthConfigFactory");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
        
        // Jakarta Authorization
        specCheckers.put("authorization", () -> {
            try {
                Class.forName("jakarta.security.jacc.PolicyContext");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
        
        // Jakarta Batch
        specCheckers.put("batch", () -> {
            try {
                Class.forName("jakarta.batch.runtime.BatchRuntime");
                Class.forName("jakarta.batch.operations.JobOperator");
                Class.forName("jakarta.batch.api.Batchlet");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
        
        // Jakarta CDI
        specCheckers.put("cdi", () -> {
            try {
                Class.forName("jakarta.enterprise.context.ApplicationScoped");
                Class.forName("jakarta.inject.Inject");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
        
        // Jakarta REST (JAX-RS)
        specCheckers.put("jax-rs", () -> {
            try {
                Class.forName("jakarta.ws.rs.Path");
                Class.forName("jakarta.ws.rs.GET");
                Class.forName("jakarta.ws.rs.core.Response");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
        
        // Jakarta JSON Processing
        specCheckers.put("json-processing", () -> {
            try {
                Class.forName("jakarta.json.Json");
                Class.forName("jakarta.json.JsonObject");
                Class.forName("jakarta.json.JsonReader");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
        
        // Jakarta JSON Binding
        specCheckers.put("json-binding", () -> {
            try {
                Class.forName("jakarta.json.bind.Jsonb");
                Class.forName("jakarta.json.bind.JsonbBuilder");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
        
        // Jakarta Bean Validation
        specCheckers.put("bean-validation", () -> {
            try {
                Class.forName("jakarta.validation.Validator");
                Class.forName("jakarta.validation.Constraint");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
        
        // Jakarta Persistence (JPA)
        specCheckers.put("jpa", () -> {
            try {
                Class.forName("jakarta.persistence.Entity");
                Class.forName("jakarta.persistence.EntityManager");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
        
        // Jakarta Transactions
        specCheckers.put("transactions", () -> {
            try {
                Class.forName("jakarta.transaction.Transactional");
                Class.forName("jakarta.transaction.UserTransaction");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
    }
    
    /**
     * Verifica si una especificación está disponible.
     * 
     * @param specName nombre de la spec (ej: "batch", "jax-rs")
     * @return true si está disponible, false en caso contrario
     */
    public boolean isAvailable(String specName) {
        Supplier<Boolean> checker = specCheckers.get(specName.toLowerCase());
        if (checker == null) {
            throw new IllegalArgumentException("Spec desconocida: " + specName);
        }
        return checker.get();
    }
    
    /**
     * Verifica todas las especificaciones y retorna un mapa con los resultados.
     * 
     * @return mapa con nombre de spec -> disponibilidad
     */
    public Map<String, Boolean> checkAll() {
        Map<String, Boolean> results = new HashMap<>();
        specCheckers.forEach((name, checker) -> {
            results.put(name, checker.get());
        });
        return results;
    }
    
    /**
     * Obtiene un reporte legible de las especificaciones disponibles.
     * 
     * @return string con el reporte
     */
    public String getReport() {
        StringBuilder report = new StringBuilder();
        report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        report.append("Reporte de Especificaciones Jakarta EE\n");
        report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
        
        Map<String, Boolean> results = checkAll();
        int available = 0;
        int unavailable = 0;
        
        for (Map.Entry<String, Boolean> entry : results.entrySet()) {
            String status = entry.getValue() ? "✅" : "❌";
            report.append(String.format("%s Jakarta %s\n", status, entry.getKey()));
            if (entry.getValue()) {
                available++;
            } else {
                unavailable++;
            }
        }
        
        report.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        report.append(String.format("Total disponible: %d\n", available));
        report.append(String.format("Total no disponible: %d\n", unavailable));
        report.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        
        return report.toString();
    }
}

