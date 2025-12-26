package com.jakartaee.interceptors.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interceptor que registra automáticamente las llamadas a métodos marcados con @Loggable.
 * Demuestra el uso de @AroundInvoke para logging.
 */
@Loggable
@Interceptor
public class LoggingInterceptor {
    
    private static final Logger logger = Logger.getLogger(LoggingInterceptor.class.getName());
    
    @AroundInvoke
    public Object logMethod(InvocationContext context) throws Exception {
        String className = context.getTarget().getClass().getSimpleName();
        String methodName = context.getMethod().getName();
        
        // Obtener el nivel de log del qualifier
        Loggable loggable = context.getMethod().getAnnotation(Loggable.class);
        if (loggable == null) {
            loggable = context.getTarget().getClass().getAnnotation(Loggable.class);
        }
        String level = loggable != null ? loggable.value() : "INFO";
        
        Level logLevel = Level.parse(level);
        
        // Log de entrada
        logger.log(logLevel, String.format(">>> [LOGGING] Entrando a %s.%s()", className, methodName));
        
        // Log de parámetros
        Object[] parameters = context.getParameters();
        if (parameters != null && parameters.length > 0) {
            StringBuilder params = new StringBuilder();
            for (int i = 0; i < parameters.length; i++) {
                if (i > 0) params.append(", ");
                params.append(parameters[i] != null ? parameters[i].toString() : "null");
            }
            logger.log(logLevel, String.format("    Parámetros: [%s]", params));
        }
        
        try {
            Object result = context.proceed();
            
            // Log de salida
            logger.log(logLevel, String.format("<<< [LOGGING] Saliendo de %s.%s()", className, methodName));
            if (result != null) {
                logger.log(logLevel, String.format("    Retorno: %s", result.toString()));
            }
            
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("!!! [LOGGING] Error en %s.%s(): %s", 
                    className, methodName, e.getMessage()), e);
            throw e;
        }
    }
}

