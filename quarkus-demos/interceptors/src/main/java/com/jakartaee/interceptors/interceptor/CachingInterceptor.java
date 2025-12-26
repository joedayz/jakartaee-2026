package com.jakartaee.interceptors.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Interceptor que cachea resultados de métodos marcados con @Cached.
 * Demuestra el uso de @AroundInvoke para implementar caché simple.
 */
@Cached
@Interceptor
public class CachingInterceptor {
    
    private static final Logger logger = Logger.getLogger(CachingInterceptor.class.getName());
    
    // Cache simple en memoria (en producción usar una solución más robusta)
    private static final Map<String, CacheEntry> cache = new HashMap<>();
    
    @AroundInvoke
    public Object cacheMethod(InvocationContext context) throws Exception {
        String className = context.getTarget().getClass().getSimpleName();
        String methodName = context.getMethod().getName();
        
        // Obtener TTL del qualifier
        Cached cached = context.getMethod().getAnnotation(Cached.class);
        if (cached == null) {
            cached = context.getTarget().getClass().getAnnotation(Cached.class);
        }
        int ttl = cached != null ? cached.ttl() : 60;
        
        // Crear clave de cache basada en método y parámetros
        String cacheKey = createCacheKey(className, methodName, context.getParameters());
        
        // Verificar si existe en cache y no ha expirado
        CacheEntry entry = cache.get(cacheKey);
        if (entry != null && !entry.isExpired(ttl)) {
            logger.info(String.format("💾 [CACHE] Hit para %s.%s() - Retornando desde cache", 
                    className, methodName));
            return entry.getValue();
        }
        
        // Ejecutar método y cachear resultado
        logger.info(String.format("💾 [CACHE] Miss para %s.%s() - Ejecutando y cacheando", 
                className, methodName));
        
        Object result = context.proceed();
        
        // Solo cachear si el resultado no es null
        if (result != null) {
            cache.put(cacheKey, new CacheEntry(result));
            logger.fine(String.format("💾 [CACHE] Resultado de %s.%s() cacheado (TTL: %d s)", 
                    className, methodName, ttl));
        }
        
        return result;
    }
    
    private String createCacheKey(String className, String methodName, Object[] parameters) {
        StringBuilder key = new StringBuilder(className).append(".").append(methodName);
        if (parameters != null && parameters.length > 0) {
            key.append("(");
            for (int i = 0; i < parameters.length; i++) {
                if (i > 0) key.append(",");
                key.append(parameters[i] != null ? parameters[i].hashCode() : "null");
            }
            key.append(")");
        }
        return key.toString();
    }
    
    /**
     * Entrada de cache con timestamp.
     */
    private static class CacheEntry {
        private final Object value;
        private final long timestamp;
        
        public CacheEntry(Object value) {
            this.value = value;
            this.timestamp = System.currentTimeMillis();
        }
        
        public Object getValue() {
            return value;
        }
        
        public boolean isExpired(int ttlSeconds) {
            return (System.currentTimeMillis() - timestamp) > (ttlSeconds * 1000L);
        }
    }
}

