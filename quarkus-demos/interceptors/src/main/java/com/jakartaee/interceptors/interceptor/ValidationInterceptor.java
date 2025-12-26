package com.jakartaee.interceptors.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.util.logging.Logger;

/**
 * Interceptor que valida parámetros antes de ejecutar métodos marcados con @Validated.
 * Demuestra el uso de @AroundInvoke para validación de parámetros.
 */
@Validated
@Interceptor
public class ValidationInterceptor {
    
    private static final Logger logger = Logger.getLogger(ValidationInterceptor.class.getName());
    
    @AroundInvoke
    public Object validateMethod(InvocationContext context) throws Exception {
        String className = context.getTarget().getClass().getSimpleName();
        String methodName = context.getMethod().getName();
        
        Object[] parameters = context.getParameters();
        
        // Validar que los parámetros no sean null (validación simple de ejemplo)
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] == null) {
                    String errorMsg = String.format(
                            "❌ [VALIDATION] Parámetro %d de %s.%s() es null", 
                            i, className, methodName);
                    logger.severe(errorMsg);
                    throw new IllegalArgumentException(errorMsg);
                }
                
                // Validación adicional: si es String, verificar que no esté vacío
                if (parameters[i] instanceof String str && str.trim().isEmpty()) {
                    String errorMsg = String.format(
                            "❌ [VALIDATION] Parámetro %d de %s.%s() es un String vacío", 
                            i, className, methodName);
                    logger.severe(errorMsg);
                    throw new IllegalArgumentException(errorMsg);
                }
            }
        }
        
        logger.fine(String.format("✅ [VALIDATION] Parámetros de %s.%s() validados correctamente", 
                className, methodName));
        
        return context.proceed();
    }
}

