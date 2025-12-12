package com.jakartaee.cdi.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interceptor que registra automáticamente las llamadas a métodos marcados con @Loggable.
 * Demuestra el uso de interceptors en CDI.
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
        
        logger.log(logLevel, String.format(">>> Entrando a %s.%s()", className, methodName));
        
        long startTime = System.currentTimeMillis();
        try {
            Object result = context.proceed();
            long duration = System.currentTimeMillis() - startTime;
            logger.log(logLevel, String.format("<<< Saliendo de %s.%s() - Duración: %d ms", 
                    className, methodName, duration));
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.log(Level.SEVERE, String.format("!!! Error en %s.%s() después de %d ms: %s", 
                    className, methodName, duration, e.getMessage()), e);
            throw e;
        }
    }
}

