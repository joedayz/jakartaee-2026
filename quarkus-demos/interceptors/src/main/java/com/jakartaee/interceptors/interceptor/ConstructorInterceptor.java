package com.jakartaee.interceptors.interceptor;

import jakarta.interceptor.AroundConstruct;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.util.logging.Logger;

/**
 * Interceptor que rastrea la creación de objetos marcados con @Tracked.
 * Demuestra el uso de @AroundConstruct para interceptar constructores.
 */
@Tracked
@Interceptor
public class ConstructorInterceptor {
    
    private static final Logger logger = Logger.getLogger(ConstructorInterceptor.class.getName());
    
    @AroundConstruct
    public void trackConstructor(InvocationContext context) throws Exception {
        String className = context.getConstructor().getDeclaringClass().getSimpleName();
        
        logger.info(String.format("🏗️  [CONSTRUCTOR] Creando instancia de %s", className));
        
        // Obtener parámetros del constructor
        Object[] parameters = context.getParameters();
        if (parameters != null && parameters.length > 0) {
            StringBuilder params = new StringBuilder();
            for (int i = 0; i < parameters.length; i++) {
                if (i > 0) params.append(", ");
                params.append(parameters[i] != null ? parameters[i].toString() : "null");
            }
            logger.info(String.format("    Parámetros del constructor: [%s]", params));
        }
        
        // Proceder con la construcción
        context.proceed();
        
        logger.info(String.format("✅ [CONSTRUCTOR] Instancia de %s creada exitosamente", className));
    }
}

