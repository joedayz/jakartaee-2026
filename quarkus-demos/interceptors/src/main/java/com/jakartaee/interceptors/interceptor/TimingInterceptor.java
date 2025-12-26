package com.jakartaee.interceptors.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.util.logging.Logger;

/**
 * Interceptor que mide el tiempo de ejecución de métodos marcados con @Timed.
 * Demuestra el uso de @AroundInvoke para medición de rendimiento.
 */
@Timed
@Interceptor
public class TimingInterceptor {
    
    private static final Logger logger = Logger.getLogger(TimingInterceptor.class.getName());
    
    @AroundInvoke
    public Object timeMethod(InvocationContext context) throws Exception {
        String className = context.getTarget().getClass().getSimpleName();
        String methodName = context.getMethod().getName();
        
        // Obtener la unidad de tiempo del qualifier
        Timed timed = context.getMethod().getAnnotation(Timed.class);
        if (timed == null) {
            timed = context.getTarget().getClass().getAnnotation(Timed.class);
        }
        String unit = timed != null ? timed.unit() : "ms";
        
        long startTime = System.nanoTime();
        
        try {
            Object result = context.proceed();
            long durationNanos = System.nanoTime() - startTime;
            
            // Convertir a la unidad solicitada
            String durationStr = formatDuration(durationNanos, unit);
            
            logger.info(String.format("⏱️  [TIMING] %s.%s() ejecutado en %s", 
                    className, methodName, durationStr));
            
            return result;
        } catch (Exception e) {
            long durationNanos = System.nanoTime() - startTime;
            String durationStr = formatDuration(durationNanos, unit);
            logger.warning(String.format("⏱️  [TIMING] %s.%s() falló después de %s", 
                    className, methodName, durationStr));
            throw e;
        }
    }
    
    private String formatDuration(long nanos, String unit) {
        return switch (unit.toLowerCase()) {
            case "ns" -> nanos + " ns";
            case "s" -> String.format("%.3f s", nanos / 1_000_000_000.0);
            default -> String.format("%.3f ms", nanos / 1_000_000.0);
        };
    }
}

