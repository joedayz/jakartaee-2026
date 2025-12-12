package com.jakartaee.annotations.resource;

import com.jakartaee.annotations.annotation.HeroPower;
import com.jakartaee.annotations.annotation.Loggable;
import com.jakartaee.annotations.service.HeroService;
import com.jakartaee.annotations.service.PowerService;
import com.jakartaee.common.entities.Hero;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Recurso REST que demuestra el uso de anotaciones de Jakarta EE.
 * Muestra información sobre anotaciones estándar y personalizadas.
 */
@Path("/api/annotations")
@Produces(MediaType.APPLICATION_JSON)
@Loggable(level = "INFO")
public class AnnotationsResource {
    
    private static final Logger logger = Logger.getLogger(AnnotationsResource.class.getName());
    
    @Inject
    HeroService heroService;
    
    @Inject
    PowerService powerService;
    
    private boolean initialized = false;
    
    /**
     * @PostConstruct: Se ejecuta después de la construcción del bean.
     */
    @PostConstruct
    public void init() {
        logger.info("=== Inicializando AnnotationsResource (@PostConstruct) ===");
        initialized = true;
        logger.info("AnnotationsResource listo para recibir peticiones");
    }
    
    /**
     * @PreDestroy: Se ejecuta antes de destruir el bean.
     */
    @PreDestroy
    public void cleanup() {
        logger.info("=== Limpiando AnnotationsResource (@PreDestroy) ===");
        initialized = false;
    }
    
    /**
     * Endpoint que muestra información sobre las anotaciones disponibles.
     */
    @GET
    @Path("/info")
    @Loggable(level = "INFO", includeParameters = false)
    public Response getAnnotationsInfo() {
        Map<String, Object> info = new HashMap<>();
        
        // Información sobre anotaciones estándar de Jakarta
        Map<String, String> standardAnnotations = new HashMap<>();
        standardAnnotations.put("@PostConstruct", "Se ejecuta después de la construcción del bean");
        standardAnnotations.put("@PreDestroy", "Se ejecuta antes de destruir el bean");
        standardAnnotations.put("@Resource", "Inyección de recursos del contenedor");
        standardAnnotations.put("@Generated", "Marca código generado automáticamente");
        
        // Información sobre anotaciones personalizadas
        Map<String, String> customAnnotations = new HashMap<>();
        customAnnotations.put("@HeroPower", "Marca métodos/clases relacionados con poder de héroes");
        customAnnotations.put("@Loggable", "Habilita logging automático de métodos");
        customAnnotations.put("@PowerLevel", "Valida niveles de poder de héroes");
        
        info.put("standardAnnotations", standardAnnotations);
        info.put("customAnnotations", customAnnotations);
        info.put("resourceInitialized", initialized);
        info.put("heroServiceInitialized", heroService.isInitialized());
        
        return Response.ok(info).build();
    }
    
    /**
     * Endpoint que demuestra el ciclo de vida con @PostConstruct y @PreDestroy.
     */
    @GET
    @Path("/lifecycle")
    @HeroPower(category = "INFO", description = "Información sobre ciclo de vida")
    public Response getLifecycleInfo() {
        Map<String, Object> lifecycle = new HashMap<>();
        
        lifecycle.put("postConstructExecuted", initialized);
        lifecycle.put("heroServiceInitialized", heroService.isInitialized());
        lifecycle.put("heroCount", heroService.getHeroCount());
        lifecycle.put("note", "Ver logs de la aplicación para ver @PostConstruct y @PreDestroy en acción");
        
        return Response.ok(lifecycle).build();
    }
    
    /**
     * Endpoint que muestra ejemplos de anotaciones personalizadas.
     */
    @GET
    @Path("/custom")
    @Loggable(level = "DEBUG")
    public Response getCustomAnnotationsExamples() {
        Map<String, Object> examples = new HashMap<>();
        
        // Obtener métodos con anotaciones personalizadas usando reflexión
        List<Map<String, Object>> heroPowerMethods = new ArrayList<>();
        List<Map<String, Object>> loggableMethods = new ArrayList<>();
        
        // Analizar HeroService
        for (Method method : HeroService.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(HeroPower.class)) {
                HeroPower annotation = method.getAnnotation(HeroPower.class);
                Map<String, Object> methodInfo = new HashMap<>();
                methodInfo.put("method", method.getName());
                methodInfo.put("minLevel", annotation.minLevel());
                methodInfo.put("category", annotation.category());
                methodInfo.put("description", annotation.description());
                heroPowerMethods.add(methodInfo);
            }
            
            if (method.isAnnotationPresent(Loggable.class)) {
                Loggable annotation = method.getAnnotation(Loggable.class);
                Map<String, Object> methodInfo = new HashMap<>();
                methodInfo.put("method", method.getName());
                methodInfo.put("level", annotation.level());
                methodInfo.put("includeParameters", annotation.includeParameters());
                methodInfo.put("includeResult", annotation.includeResult());
                loggableMethods.add(methodInfo);
            }
        }
        
        examples.put("heroPowerMethods", heroPowerMethods);
        examples.put("loggableMethods", loggableMethods);
        examples.put("powerCategories", powerService.getPowerCategories());
        
        return Response.ok(examples).build();
    }
}

