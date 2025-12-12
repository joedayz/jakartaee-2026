package com.jakartaee.panache.resource;

import com.jakartaee.panache.entity.HeroEntity;
import com.jakartaee.panache.entity.VillainEntity;
import com.jakartaee.panache.repository.HeroRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Recurso que compara ambos patrones de Panache mostrando ejemplos lado a lado.
 */
@Path("/api/panache")
@Produces(MediaType.APPLICATION_JSON)
public class PanacheComparisonResource {
    
    @Inject
    HeroRepository heroRepository;
    
    /**
     * Comparación completa de ambos patrones con ejemplos
     */
    @GET
    @Path("/comparison")
    public Response comparePatterns() {
        Map<String, Object> comparison = new HashMap<>();
        
        // Ejemplos de código para Repository Pattern
        Map<String, String> repositoryExamples = new HashMap<>();
        repositoryExamples.put("findAll", "heroRepository.listAll()");
        repositoryExamples.put("findById", "heroRepository.findById(id)");
        repositoryExamples.put("findPowerful", "heroRepository.findPowerfulHeroes()");
        repositoryExamples.put("persist", "heroRepository.persist(hero)");
        repositoryExamples.put("delete", "heroRepository.delete(hero)");
        repositoryExamples.put("count", "heroRepository.count()");
        repositoryExamples.put("customMethod", "heroRepository.findByNameIgnoreCase(name)");
        
        // Ejemplos de código para Active Record Pattern
        Map<String, String> activeRecordExamples = new HashMap<>();
        activeRecordExamples.put("findAll", "VillainEntity.listAll()");
        activeRecordExamples.put("findById", "VillainEntity.findById(id)");
        activeRecordExamples.put("findPowerful", "VillainEntity.findPowerful()");
        activeRecordExamples.put("persist", "villain.persist()");
        activeRecordExamples.put("delete", "villain.delete()");
        activeRecordExamples.put("count", "VillainEntity.count()");
        activeRecordExamples.put("customMethod", "VillainEntity.findByThreatLevel(level)");
        
        // Comparación de características
        List<Map<String, Object>> features = List.of(
            Map.of(
                "feature", "Separación de responsabilidades",
                "repository", "✅ Entidad separada del acceso a datos",
                "activeRecord", "⚠️ Todo en la entidad"
            ),
            Map.of(
                "feature", "Testabilidad",
                "repository", "✅ Fácil de mockear el repositorio",
                "activeRecord", "⚠️ Requiere más setup para tests"
            ),
            Map.of(
                "feature", "Complejidad",
                "repository", "Más estructurado",
                "activeRecord", "Más simple y directo"
            ),
            Map.of(
                "feature", "Uso recomendado",
                "repository", "Lógica compleja, múltiples fuentes de datos",
                "activeRecord", "CRUD simple, prototipado rápido"
            ),
            Map.of(
                "feature", "Métodos personalizados",
                "repository", "En el repositorio",
                "activeRecord", "Métodos estáticos en la entidad"
            )
        );
        
        // Estadísticas reales
        long heroCount = heroRepository.count();
        long villainCount = VillainEntity.count();
        long powerfulHeroes = heroRepository.countPowerfulHeroes();
        long powerfulVillains = VillainEntity.countPowerful();
        
        comparison.put("repositoryPattern", Map.of(
            "entity", "HeroEntity (NO extiende PanacheEntity)",
            "repository", "HeroRepository implements PanacheRepository<HeroEntity>",
            "examples", repositoryExamples,
            "stats", Map.of(
                "totalHeroes", heroCount,
                "powerfulHeroes", powerfulHeroes
            )
        ));
        
        comparison.put("activeRecordPattern", Map.of(
            "entity", "VillainEntity extends PanacheEntity",
            "repository", "No necesario - métodos en la entidad",
            "examples", activeRecordExamples,
            "stats", Map.of(
                "totalVillains", villainCount,
                "powerfulVillains", powerfulVillains
            )
        ));
        
        comparison.put("features", features);
        comparison.put("recommendation", Map.of(
            "useRepository", "Cuando necesites separación clara, testabilidad, o lógica compleja",
            "useActiveRecord", "Cuando necesites simplicidad, prototipado rápido, o CRUD básico"
        ));
        
        return Response.ok(comparison).build();
    }
    
    /**
     * Ejemplos de consultas Panache Query para ambos patrones
     */
    @GET
    @Path("/queries")
    public Response getQueryExamples() {
        Map<String, Object> queries = new HashMap<>();
        
        // Ejemplos de consultas para Repository Pattern
        Map<String, String> repositoryQueries = new HashMap<>();
        repositoryQueries.put("findAll", "heroRepository.listAll()");
        repositoryQueries.put("findByField", "heroRepository.find(\"name\", \"Superman\")");
        repositoryQueries.put("findWithCondition", "heroRepository.find(\"powerLevel >= ?1\", 80)");
        repositoryQueries.put("findWithNamedParams", "heroRepository.find(\"powerLevel >= :level\", Parameters.with(\"level\", 80))");
        repositoryQueries.put("findWithOrder", "heroRepository.find(\"powerLevel >= ?1 ORDER BY powerLevel DESC\", 80)");
        repositoryQueries.put("findPaginated", "heroRepository.find(\"powerLevel >= ?1\", 80).page(0, 10)");
        repositoryQueries.put("count", "heroRepository.count(\"powerLevel >= ?1\", 80)");
        repositoryQueries.put("update", "heroRepository.update(\"powerLevel = ?1 WHERE id = ?2\", 90, id)");
        
        // Ejemplos de consultas para Active Record Pattern
        Map<String, String> activeRecordQueries = new HashMap<>();
        activeRecordQueries.put("findAll", "VillainEntity.listAll()");
        activeRecordQueries.put("findByField", "VillainEntity.find(\"name\", \"Joker\")");
        activeRecordQueries.put("findWithCondition", "VillainEntity.find(\"powerLevel >= ?1\", 80)");
        activeRecordQueries.put("findWithNamedParams", "VillainEntity.find(\"powerLevel >= :level\", Parameters.with(\"level\", 80))");
        activeRecordQueries.put("findWithOrder", "VillainEntity.find(\"powerLevel >= ?1 ORDER BY powerLevel DESC\", 80)");
        activeRecordQueries.put("findPaginated", "VillainEntity.find(\"powerLevel >= ?1\", 80).page(0, 10)");
        activeRecordQueries.put("count", "VillainEntity.count(\"powerLevel >= ?1\", 80)");
        activeRecordQueries.put("update", "VillainEntity.update(\"powerLevel = ?1 WHERE id = ?2\", 90, id)");
        
        queries.put("repositoryPattern", repositoryQueries);
        queries.put("activeRecordPattern", activeRecordQueries);
        queries.put("note", "Ambos patrones usan la misma API de consultas Panache Query");
        
        return Response.ok(queries).build();
    }
}

