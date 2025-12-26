# Guía para Webinar: Jakarta EE en Quarkus - Heroes y Villanos

## 📋 Información General del Webinar

**Título:** "Jakarta EE en Quarkus: Una Guía Completa con Ejemplos Prácticos"

**Duración Total:** 90-120 minutos

**Audiencia:** Desarrolladores Java/Jakarta EE, arquitectos, tech leads

**Objetivo:** Demostrar cómo Quarkus implementa las especificaciones de Jakarta EE Core Profile 11 y más, con ejemplos prácticos usando el tema de Heroes y Villanos de DC Comics.

---

## 🎯 Estructura del Webinar

### Parte 1: Introducción (10 minutos)

#### 1.1 Bienvenida y Contexto (3 min)
- Presentación personal
- Objetivo del webinar
- Tema: Heroes y Villanos de DC Comics
- Estructura de la presentación

#### 1.2 Quarkus y Jakarta EE (4 min)
- ¿Qué es Quarkus?
- ¿Qué es Jakarta EE?
- **Punto clave:** Quarkus 3.30.2 implementa completamente el Jakarta EE Core Profile 11
- Diferencias entre Core Profile, Web Profile y Platform

**Demo rápido:**
```bash
# Mostrar estructura del proyecto
tree quarkus-demos/ -L 1
```

#### 1.3 Setup del Proyecto (3 min)
- Versiones: Quarkus 3.30.2, Java 21, Jakarta EE 11
- Estructura del proyecto
- Módulo `common` con entidades compartidas

**Mostrar:**
- `README.md` principal
- Estructura de carpetas
- `common/entities/` con Hero y Villain

---

### Parte 2: Especificaciones Core Profile (60 minutos)

#### 2.1 Jakarta Annotations (5 min)
**Demo:** `quarkus-demos/annotations/`

**Puntos clave:**
- `@PostConstruct` y `@PreDestroy` para lifecycle
- `@Resource` para inyección de recursos
- Anotaciones personalizadas

**Código a mostrar:**
```java
@PostConstruct
public void initialize() {
    // Código de inicialización
}
```

**Endpoints a probar:**
```bash
curl http://localhost:8080/api/annotations/lifecycle
```

**Tiempo:** 5 minutos

---

#### 2.2 Jakarta CDI (Contexts and Dependency Injection) (8 min)
**Demo:** `quarkus-demos/cdi/`

**Puntos clave:**
- Inyección de dependencias con `@Inject`
- Scopes: `@ApplicationScoped`, `@RequestScoped`, `@Dependent`
- Qualifiers personalizados (`@HeroQualifier`, `@VillainQualifier`)
- Producers (`@Produces`)
- Eventos CDI (`Event`, `@Observes`)
- Interceptores (mencionar que hay demo dedicado)

**Código a mostrar:**
```java
@Inject
@HeroQualifier
HeroService heroService;

@Inject
Event<HeroCreatedEvent> heroCreatedEvent;

public void createHero(Hero hero) {
    heroService.create(hero);
    heroCreatedEvent.fire(new HeroCreatedEvent(hero));
}
```

**Endpoints a probar:**
```bash
curl http://localhost:8080/api/cdi/heroes
curl http://localhost:8080/api/cdi/demo/power-analysis
curl http://localhost:8080/api/cdi/demo/events
```

**Tiempo:** 8 minutos

---

#### 2.3 Jakarta Interceptors (7 min)
**Demo:** `quarkus-demos/interceptors/`

**Puntos clave:**
- `@AroundInvoke` para interceptar métodos
- `@AroundConstruct` para interceptar constructores
- `@PostConstruct` y `@PreDestroy` para lifecycle
- Interceptores encadenados
- Casos de uso: logging, timing, validación, caching

**Código a mostrar:**
```java
@Loggable("INFO")
@Timed(unit = "ms")
@Validated
@Cached(ttl = 30)
public List<Hero> getAllHeroes() {
    // Múltiples interceptores se ejecutan en orden
}
```

**Endpoints a probar:**
```bash
# Primera llamada (ejecuta y cachea)
curl http://localhost:8080/api/interceptors/heroes

# Segunda llamada (desde cache)
curl http://localhost:8080/api/interceptors/heroes

# Ver logs para observar interceptores
```

**Mostrar logs:**
- LoggingInterceptor
- TimingInterceptor
- CachingInterceptor (hit/miss)
- ConstructorInterceptor
- LifecycleInterceptor

**Tiempo:** 7 minutos

---

#### 2.4 Jakarta Managed Beans (5 min)
**Demo:** `quarkus-demos/managed-beans/`

**Puntos clave:**
- `@ManagedBean` annotation
- Ciclo de vida con `@PostConstruct` y `@PreDestroy`
- Inyección de dependencias
- Comparación con CDI Beans

**Código a mostrar:**
```java
@ManagedBean
@ApplicationScoped
public class HeroManagedBean {
    @PostConstruct
    public void initialize() { }
    
    @PreDestroy
    public void cleanup() { }
}
```

**Endpoints a probar:**
```bash
curl http://localhost:8080/api/managed-beans/heroes
curl http://localhost:8080/api/managed-beans/stats
```

**Mostrar logs:**
- Constructor y lifecycle callbacks

**Tiempo:** 5 minutos

---

#### 2.5 Jakarta RESTful Web Services (JAX-RS) (6 min)
**Demo:** `quarkus-demos/jax-rs/`

**Puntos clave:**
- Anotaciones: `@Path`, `@GET`, `@POST`, `@PUT`, `@DELETE`
- Parámetros: `@PathParam`, `@QueryParam`
- Serialización automática JSON
- Códigos de estado HTTP

**Código a mostrar:**
```java
@Path("/api/heroes")
public class HeroResource {
    @GET
    public List<Hero> getAllHeroes() { }
    
    @GET
    @Path("/{id}")
    public Hero getHero(@PathParam("id") Long id) { }
}
```

**Endpoints a probar:**
```bash
curl http://localhost:8080/api/heroes
curl http://localhost:8080/api/heroes/1
curl -X POST http://localhost:8080/api/heroes -H "Content-Type: application/json" -d '{"name":"Superman","power":"Flight","powerLevel":95}'
```

**Tiempo:** 6 minutos

---

#### 2.6 Jakarta JSON Processing (JSON-P) (5 min)
**Demo:** `quarkus-demos/json-processing/`

**Puntos clave:**
- Object Model API: `JsonObject`, `JsonArray`
- Streaming API: `JsonParser`, `JsonGenerator`
- `JsonPointer` para consultas (RFC 6901)
- `JsonPatch` para transformaciones (RFC 6902)

**Código a mostrar:**
```java
JsonObject hero = Json.createObjectBuilder()
    .add("name", "Superman")
    .add("powerLevel", 95)
    .build();

JsonPointer pointer = Json.createPointer("/powerLevel");
int level = pointer.getValue(hero).asJsonNumber().intValue();
```

**Endpoints a probar:**
```bash
curl http://localhost:8080/api/json-processing/create-object
curl http://localhost:8080/api/json-processing/parse-string
curl http://localhost:8080/api/json-processing/json-pointer
curl http://localhost:8080/api/json-processing/json-patch
```

**Tiempo:** 5 minutos

---

#### 2.7 Jakarta JSON Binding (JSON-B) (6 min)
**Demo:** `quarkus-demos/json-binding/`

**Puntos clave:**
- Serialización/deserialización automática
- Anotaciones: `@JsonbProperty`, `@JsonbTransient`, `@JsonbDateFormat`
- Custom Adapters
- Field ordering

**Código a mostrar:**
```java
@JsonbPropertyOrder({"name", "powerLevel", "power"})
public class Hero {
    @JsonbProperty("heroName")
    private String name;
    
    @JsonbTransient
    private String internalId;
}
```

**Endpoints a probar:**
```bash
curl http://localhost:8080/api/json-binding/serialize
curl http://localhost:8080/api/json-binding/deserialize
curl http://localhost:8080/api/json-binding/team
```

**Tiempo:** 6 minutos

---

#### 2.8 Jakarta Bean Validation (6 min)
**Demo:** `quarkus-demos/bean-validation/`

**Puntos clave:**
- Validaciones estándar: `@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max`, `@Email`
- Validadores personalizados
- Validación en DTOs con `@Valid`
- ExceptionMapper para manejo de errores

**Código a mostrar:**
```java
public class HeroCreateDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50)
    private String name;
    
    @Min(1) @Max(100)
    private Integer powerLevel;
}
```

**Endpoints a probar:**
```bash
# Validación exitosa
curl -X POST http://localhost:8080/api/heroes -H "Content-Type: application/json" -d '{"name":"Superman","power":"Flight","powerLevel":95}'

# Validación fallida (mostrar errores)
curl -X POST http://localhost:8080/api/heroes -H "Content-Type: application/json" -d '{"name":"","powerLevel":150}'
```

**Tiempo:** 6 minutos

---

#### 2.9 Jakarta Persistence (JPA) (8 min)
**Demo:** `quarkus-demos/jpa/`

**Puntos clave:**
- `EntityManager` y sus operaciones
- Named Queries (`@NamedQuery`)
- JPQL (Jakarta Persistence Query Language)
- Criteria API
- Relaciones: `@OneToMany`, `@ManyToOne`
- Lifecycle callbacks
- Optimistic locking (`@Version`)

**Código a mostrar:**
```java
// Named Query
@NamedQuery(name = "Hero.findPowerful", 
    query = "SELECT h FROM HeroJPA h WHERE h.powerLevel >= :minLevel")

// JPQL
List<Hero> heroes = em.createQuery(
    "SELECT h FROM HeroJPA h WHERE h.powerLevel >= :minLevel", 
    HeroJPA.class)
    .setParameter("minLevel", 80)
    .getResultList();

// Criteria API
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<HeroJPA> query = cb.createQuery(HeroJPA.class);
// ...
```

**Endpoints a probar:**
```bash
curl http://localhost:8080/api/jpa/heroes
curl http://localhost:8080/api/jpa/heroes/named-query?minLevel=80
curl http://localhost:8080/api/jpa/heroes/criteria?minLevel=80
curl http://localhost:8080/api/jpa/demo/entity-manager-operations
```

**Tiempo:** 8 minutos

---

#### 2.10 Jakarta Transactions (6 min)
**Demo:** `quarkus-demos/transactions/`

**Puntos clave:**
- `@Transactional` annotation
- Tipos de transacciones: REQUIRED, REQUIRES_NEW, SUPPORTS, etc.
- Rollback automático y manual
- Transacciones anidadas

**Código a mostrar:**
```java
@Transactional(rollbackOn = Exception.class)
public void transferPower(Hero from, Hero to, int amount) {
    from.setPowerLevel(from.getPowerLevel() - amount);
    to.setPowerLevel(to.getPowerLevel() + amount);
    // Si hay excepción, se hace rollback automático
}
```

**Endpoints a probar:**
```bash
curl http://localhost:8080/api/transactions/demo/required
curl http://localhost:8080/api/transactions/demo/requires-new
curl http://localhost:8080/api/transactions/demo/rollback
```

**Tiempo:** 6 minutos

---

#### 2.11 Jakarta Batch (7 min)
**Demo:** `quarkus-demos/batch/`

**Puntos clave:**
- `ItemReader`, `ItemProcessor`, `ItemWriter`
- `Batchlet` para trabajos simples
- Configuración XML (`batch-jobs.xml`)
- Job execution y monitoring

**Código a mostrar:**
```java
@Named
public class HeroImportReader implements ItemReader {
    @Override
    public Object readItem() throws Exception {
        // Leer siguiente item
    }
}

@Named
public class HeroImportProcessor implements ItemProcessor {
    @Override
    public Object processItem(Object item) throws Exception {
        // Procesar item
    }
}
```

**Endpoints a probar:**
```bash
curl -X POST http://localhost:8080/api/batch/jobs/hero-import
curl http://localhost:8080/api/batch/jobs/hero-import/executions
```

**Mostrar:**
- Archivo `batch-jobs.xml`
- Logs de ejecución del job

**Tiempo:** 7 minutos

---

### Parte 3: Especificaciones Adicionales (25 minutos)

#### 3.1 Jakarta Data (6 min)
**Demo:** `quarkus-demos/jakarta-data/`

**Puntos clave:**
- Repositorios con `@Find`, `@Insert`, `@Delete`
- Métodos de consulta derivados del nombre
- Panache Next (experimental)
- Active Record Pattern

**Código a mostrar:**
```java
@Repository
public interface HeroRepository extends DataRepository<Hero, Long> {
    @Find
    List<Hero> findByPowerLevelGreaterThan(int minLevel);
    
    @Insert
    Hero save(Hero hero);
}
```

**Endpoints a probar:**
```bash
curl http://localhost:8080/api/jakarta-data/heroes
curl http://localhost:8080/api/jakarta-data/heroes/powerful?minLevel=80
curl http://localhost:8080/api/jakarta-data/panache-next/heroes
```

**Tiempo:** 6 minutos

---

#### 3.2 Jakarta NoSQL (MongoDB) (6 min)
**Demo:** `quarkus-demos/nosql/`

**Puntos clave:**
- Panache MongoDB
- Documentos anidados
- Arrays en documentos
- Queries flexibles
- **Dev Services** para MongoDB automático

**Código a mostrar:**
```java
public class HeroMongo extends PanacheMongoEntity {
    public String name;
    public Location location; // Documento anidado
    public List<String> abilities; // Array
    public List<Mission> missions; // Array de documentos
}

// Query en documento anidado
find("location.city", "Metropolis").list();
```

**Endpoints a probar:**
```bash
curl http://localhost:8080/api/nosql/heroes
curl http://localhost:8080/api/nosql/heroes/powerful?minLevel=80
curl http://localhost:8080/api/nosql/heroes/by-city?city=Metropolis
curl http://localhost:8080/api/nosql/heroes/by-ability?ability=Flight
```

**Destacar:**
- Dev Services iniciando MongoDB automáticamente
- Logs de conexión

**Tiempo:** 6 minutos

---

#### 3.3 Quarkus Panache (5 min)
**Demo:** `quarkus-demos/panache/`

**Puntos clave:**
- Repository Pattern
- Active Record Pattern
- Queries simplificadas
- Menos código boilerplate

**Código a mostrar:**
```java
// Repository Pattern
@ApplicationScoped
public class HeroRepository implements PanacheRepository<Hero> {
    public List<Hero> findPowerful(int minLevel) {
        return find("powerLevel >= ?1", minLevel).list();
    }
}

// Active Record Pattern
Hero hero = new Hero();
hero.name = "Superman";
hero.persist(); // Método directamente en la entidad
```

**Endpoints a probar:**
```bash
curl http://localhost:8080/api/panache/heroes
curl http://localhost:8080/api/panache/heroes/repository/powerful?minLevel=80
curl http://localhost:8080/api/panache/heroes/active-record/powerful?minLevel=80
```

**Tiempo:** 5 minutos

---

#### 3.4 Security (Quarkus Security) (5 min)
**Demo:** `quarkus-demos/security/`

**Puntos clave:**
- ⚠️ NO es Jakarta Security estándar
- Autenticación básica HTTP
- `@Authenticated` y `@RolesAllowed`
- `SecurityContext` para información del usuario

**Código a mostrar:**
```java
@Authenticated
@RolesAllowed("HERO")
@GET
@Path("/heroes")
public Response getHeroes() {
    // Solo usuarios con rol HERO
}
```

**Endpoints a probar:**
```bash
# Sin autenticación (falla)
curl http://localhost:8080/api/protected/profile

# Con autenticación (funciona)
curl -u superman:superman http://localhost:8080/api/protected/profile

# Con rol incorrecto (403)
curl -u joker:joker http://localhost:8080/api/protected/heroes

# Con rol correcto (200)
curl -u batman:batman http://localhost:8080/api/protected/heroes
```

**Tiempo:** 5 minutos

---

#### 3.5 MVC - Qute Básico vs Renarde (8 min)

**Demo 1: Qute Básico** (`quarkus-demos/mvc/`) - 3 min
- ⚠️ NO es Jakarta MVC estándar
- Uso directo de `@Inject Template`
- Control total sobre rutas

**Código a mostrar:**
```java
@Inject
Template heroes;

@GET
@Path("/heroes")
public TemplateInstance listHeroes() {
    return heroes.data("heroes", heroService.getAllHeroes());
}
```

**Demo 2: Renarde** (`quarkus-demos/renarde/`) - 5 min
- Convenciones automáticas
- Clase base `Controller`
- Flash messages, validación, redirecciones type-safe

**Código a mostrar:**
```java
public class Heroes extends Controller {
    public void list() {
        render("list", heroService.getAllHeroes());
    }
    
    public void create(@BeanParam @Valid Hero hero) {
        if (validationFailed()) {
            render("form", hero, "create");
            return;
        }
        heroService.create(hero);
        flash("success", "Héroe creado");
        redirect(Heroes.class).list();
    }
}
```

**Comparación:**
- Mostrar tabla comparativa del README
- Ventajas de cada enfoque

**Endpoints a probar:**
```bash
# Qute básico
curl http://localhost:8080/heroes

# Renarde
curl http://localhost:8080/heroes/list
```

**Tiempo:** 8 minutos

---

### Parte 4: Resumen y Q&A (10 minutos)

#### 4.1 Resumen de Especificaciones (5 min)

**Tabla resumen:**

| Especificación | Demo | Estado |
|----------------|------|--------|
| Jakarta Annotations | ✅ | Core Profile |
| Jakarta CDI | ✅ | Core Profile |
| Jakarta Interceptors | ✅ | Core Profile |
| Jakarta Managed Beans | ✅ | Core Profile |
| Jakarta REST (JAX-RS) | ✅ | Core Profile |
| Jakarta JSON Processing | ✅ | Core Profile |
| Jakarta JSON Binding | ✅ | Core Profile |
| Jakarta Bean Validation | ✅ | Core Profile |
| Jakarta Persistence (JPA) | ✅ | Core Profile |
| Jakarta Transactions | ✅ | Core Profile |
| Jakarta Batch | ✅ | Core Profile |
| Jakarta Data | ✅ | Extra |
| Jakarta NoSQL | ✅ | Extra |
| Quarkus Panache | ✅ | Extra |
| Security | ⚠️ | Quarkus Security |
| MVC | ⚠️ | Qute/Renarde |

**Puntos clave:**
- Quarkus implementa completamente el Core Profile 11
- Algunas especificaciones adicionales disponibles
- Alternativas para Security y MVC (no estándar)

#### 4.2 Recursos y Próximos Pasos (2 min)
- Repositorio GitHub
- Documentación oficial
- Blog posts (12 artículos)
- Próximos temas a cubrir

#### 4.3 Q&A (3 min)
- Preguntas del público
- Demos adicionales si se requiere

---

## 🎬 Checklist Pre-Webinar

### Preparación Técnica

- [ ] Todos los demos compilados y funcionando
- [ ] Servidores locales iniciados (o usar Dev Services)
- [ ] Terminales preparadas con comandos curl
- [ ] Navegador abierto para demos web (MVC, Renarde)
- [ ] Logs visibles para mostrar interceptores
- [ ] MongoDB Dev Services funcionando (para demo NoSQL)

### Preparación de Contenido

- [ ] Slides preparadas (opcional)
- [ ] Código fuente abierto en IDE
- [ ] READMEs de cada demo revisados
- [ ] Tabla comparativa lista
- [ ] Ejemplos de código preparados

### Preparación de Demos

- [ ] Probar cada endpoint antes del webinar
- [ ] Verificar que los datos iniciales se cargan
- [ ] Preparar casos de éxito y error
- [ ] Tener ejemplos de validación fallida listos

---

## 📝 Notas para el Presentador

### Orden Sugerido de Presentación

1. **Empezar con lo básico:** Annotations, CDI
2. **Continuar con REST:** JAX-RS, JSON Processing/Binding
3. **Persistencia:** JPA, Transactions, Batch
4. **Avanzado:** Data, NoSQL, Panache
5. **Web:** Security, MVC, Renarde

### Tips de Presentación

1. **Mostrar código primero**, luego ejecutar
2. **Explicar el "por qué"** antes del "cómo"
3. **Comparar con alternativas** cuando sea relevante
4. **Mostrar logs** para interceptores y lifecycle
5. **Usar casos reales** del tema de Heroes y Villanos

### Puntos a Enfatizar

- ✅ Quarkus implementa **completamente** el Core Profile 11
- ⚠️ Security y MVC usan alternativas (no estándar)
- 🚀 Dev Services para desarrollo sin configuración
- 💡 Convenciones de Renarde vs control manual de Qute
- 📊 Comparaciones cuando hay múltiples enfoques

### Errores Comunes a Evitar

- No confundir Quarkus Security con Jakarta Security
- No decir que Renarde es Jakarta MVC
- Aclarar que algunas specs son extras (no Core Profile)
- Explicar diferencias entre Core/Web/Platform Profiles

---

## 🎯 Duración por Sección

| Sección | Duración |
|---------|----------|
| Introducción | 10 min |
| Core Profile (11 specs) | 60 min |
| Especificaciones Adicionales | 25 min |
| Resumen y Q&A | 10 min |
| **TOTAL** | **105 min** |

---

## 📚 Recursos Adicionales

### Enlaces a Compartir

- Repositorio: [GitHub del proyecto]
- Blog: https://blog.joedayz.pe/
- Documentación Quarkus: https://quarkus.io/guides/
- Jakarta EE Specs: https://jakarta.ee/specifications/

### Comandos Útiles

```bash
# Iniciar cualquier demo
cd quarkus-demos/[demo-name]
mvn quarkus:dev

# Verificar salud de la aplicación
curl http://localhost:8080/q/health

# Ver información de la aplicación
curl http://localhost:8080/q/info
```

---

## 🎥 Sugerencias para Grabación

1. **Pantalla dividida:** Código a la izquierda, terminal/navegador a la derecha
2. **Zoom en código:** Resaltar secciones importantes
3. **Mostrar logs:** Terminal con logs visibles
4. **Navegador:** Para demos MVC y Renarde
5. **Transiciones:** Pausas entre secciones para preguntas

---

## ✅ Post-Webinar

- Compartir grabación (si se graba)
- Compartir slides y código
- Enlaces a blog posts
- Repositorio GitHub
- Redes sociales (LinkedIn, Twitter/X, Facebook)

---

## 📋 Script de Apertura (Sugerido)

"Bienvenidos al webinar sobre Jakarta EE en Quarkus. Hoy vamos a explorar cómo Quarkus 3.30.2 implementa completamente el Jakarta EE Core Profile 11, y algunas especificaciones adicionales.

Usaremos un tema común en todos los demos: Heroes y Villanos de DC Comics, lo que nos permitirá ver diferentes especificaciones con un contexto familiar.

Comenzaremos con las especificaciones del Core Profile, luego veremos algunas extras, y finalmente compararemos diferentes enfoques cuando hay múltiples opciones disponibles.

¿Hay alguna pregunta antes de comenzar?"

---

## 📋 Script de Cierre (Sugerido)

"Hemos cubierto todas las especificaciones principales del Jakarta EE Core Profile 11 que Quarkus implementa, además de algunas especificaciones adicionales como Jakarta Data, Jakarta NoSQL, y extensiones de Quarkus como Panache.

Recuerden que:
- Quarkus implementa completamente el Core Profile 11
- Para Security y MVC, Quarkus proporciona alternativas (Quarkus Security y Qute/Renarde)
- Todos los demos están disponibles en el repositorio GitHub
- Hay 12 artículos de blog disponibles con más detalles

¿Hay alguna pregunta final?"

---

¡Éxito con tu webinar! 🚀

