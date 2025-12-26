package com.jakartaee.interceptors.interceptor;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.util.logging.Logger;

/**
 * Interceptor que monitorea el ciclo de vida de beans marcados con @Monitored.
 * Demuestra el uso de @PostConstruct y @PreDestroy para interceptar eventos del ciclo de vida.
 */
@Monitored
@Interceptor
public class LifecycleInterceptor {
    
    private static final Logger logger = Logger.getLogger(LifecycleInterceptor.class.getName());
    
    @PostConstruct
    public void afterConstruction(InvocationContext context) throws Exception {
        String className = context.getTarget().getClass().getSimpleName();
        logger.info(String.format("🚀 [LIFECYCLE] @PostConstruct ejecutado para %s", className));
        
        // Aquí podrías realizar inicializaciones adicionales
        // Por ejemplo, registrar métricas, conectar a servicios externos, etc.
        
        context.proceed();
        
        logger.info(String.format("✅ [LIFECYCLE] %s inicializado completamente", className));
    }
    
    @PreDestroy
    public void beforeDestruction(InvocationContext context) throws Exception {
        String className = context.getTarget().getClass().getSimpleName();
        logger.info(String.format("🛑 [LIFECYCLE] @PreDestroy ejecutado para %s", className));
        
        // Aquí podrías realizar limpieza
        // Por ejemplo, cerrar conexiones, liberar recursos, etc.
        
        context.proceed();
        
        logger.info(String.format("✅ [LIFECYCLE] %s destruido completamente", className));
    }
}

