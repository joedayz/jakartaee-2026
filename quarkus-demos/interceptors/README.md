# Jakarta Interceptors Demo

## Descripción

Este demo muestra cómo usar **Jakarta Interceptors** para implementar lógica transversal (cross-cutting concerns) en aplicaciones Quarkus. Los interceptores permiten interceptar invocaciones de métodos, construcción de objetos y eventos del ciclo de vida sin modificar el código de negocio.

## Objetivo

Aprender a:
- Crear interceptores con `@AroundInvoke` para métodos
- Crear interceptores con `@AroundConstruct` para constructores
- Crear interceptores con `@PostConstruct` y `@PreDestroy` para lifecycle
- Usar múltiples interceptores encadenados
- Registrar interceptores en `beans.xml`
- Implementar casos de uso prácticos: logging, timing, validación, caching

## Tema DC

Gestión de Heroes de DC Comics usando interceptores:
- **HeroService**: Servicio con interceptores de logging, timing, validación y caché
- **PowerAnalysisService**: Servicio que demuestra interceptores en constructores
- **DataInitializer**: Bean que demuestra interceptores de lifecycle

## Soporte en Quarkus

✅ **Jakarta Interceptors está completamente soportado en Quarkus 3.30.2** a través de CDI (ArC).

Este demo usa:
- **Quarkus 3.30.2** con la nueva API REST (`quarkus-rest`)
- **CDI (ArC)** para interceptores
- **Hibernate ORM** para persistencia
- **H2 Database** para almacenamiento

## Dependencias

```xml
<!-- Jakarta RESTful Web Services -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- CDI (necesario para interceptores) -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-arc</artifactId>
</dependency>

<!-- Hibernate ORM -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-orm</artifactId>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-h2</artifactId>
</dependency>
```

## Tipos de Interceptores

### 1. @AroundInvoke - Interceptores de Métodos

Interceptan la invocación de métodos de negocio. Se ejecutan antes y después del método.

#### Ejemplo: LoggingInterceptor

```java
@Loggable
@Interceptor
public class LoggingInterceptor {
    
    @AroundInvoke
    public Object logMethod(InvocationContext context) throws Exception {
        // Código antes de ejecutar el método
        logger.info("Entrando al método...");
        
        // Ejecutar el método original
        Object result = context.proceed();
        
        // Código después de ejecutar el método
        logger.info("Saliendo del método...");
        
        return result;
    }
}
```

**Uso:**
```java
@Loggable("INFO")
public List<Hero> getAllHeroes() {
    // Este método será interceptado automáticamente
    return heroService.findAll();
}
```

#### Ejemplo: TimingInterceptor

Mide el tiempo de ejecución de métodos:

```java
@Timed(unit = "ms")
@Interceptor
public class TimingInterceptor {
    @AroundInvoke
    public Object timeMethod(InvocationContext context) throws Exception {
        long startTime = System.nanoTime();
        Object result = context.proceed();
        long duration = System.nanoTime() - startTime;
        logger.info("Método ejecutado en " + duration + " ms");
        return result;
    }
}
```

#### Ejemplo: ValidationInterceptor

Valida parámetros antes de ejecutar métodos:

```java
@Validated
@Interceptor
public class ValidationInterceptor {
    @AroundInvoke
    public Object validateMethod(InvocationContext context) throws Exception {
        Object[] parameters = context.getParameters();
        // Validar parámetros
        for (Object param : parameters) {
            if (param == null) {
                throw new IllegalArgumentException("Parámetro null no permitido");
            }
        }
        return context.proceed();
    }
}
```

#### Ejemplo: CachingInterceptor

Cachea resultados de métodos:

```java
@Cached(ttl = 60)
@Interceptor
public class CachingInterceptor {
    @AroundInvoke
    public Object cacheMethod(InvocationContext context) throws Exception {
        String cacheKey = createCacheKey(context);
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey); // Retornar desde cache
        }
        Object result = context.proceed();
        cache.put(cacheKey, result); // Guardar en cache
        return result;
    }
}
```

### 2. @AroundConstruct - Interceptores de Constructores

Interceptan la creación de objetos. Se ejecutan durante la construcción.

#### Ejemplo: ConstructorInterceptor

```java
@Tracked
@Interceptor
public class ConstructorInterceptor {
    
    @AroundConstruct
    public void trackConstructor(InvocationContext context) throws Exception {
        String className = context.getConstructor().getDeclaringClass().getSimpleName();
        logger.info("Creando instancia de " + className);
        
        // Proceder con la construcción
        context.proceed();
        
        logger.info("Instancia creada exitosamente");
    }
}
```

**Uso:**
```java
@Tracked
public class PowerAnalysisService {
    // Este constructor será interceptado
    public PowerAnalysisService() {
        // ...
    }
}
```

### 3. @PostConstruct y @PreDestroy - Interceptores de Lifecycle

Interceptan eventos del ciclo de vida de beans.

#### Ejemplo: LifecycleInterceptor

```java
@Monitored
@Interceptor
public class LifecycleInterceptor {
    
    @PostConstruct
    public void afterConstruction(InvocationContext context) throws Exception {
        logger.info("@PostConstruct ejecutado");
        // Inicializaciones adicionales
        context.proceed();
    }
    
    @PreDestroy
    public void beforeDestruction(InvocationContext context) throws Exception {
        logger.info("@PreDestroy ejecutado");
        // Limpieza de recursos
        context.proceed();
    }
}
```

**Uso:**
```java
@Monitored
@ApplicationScoped
public class HeroService {
    // @PostConstruct se ejecutará después de la construcción
    // @PreDestroy se ejecutará antes de la destrucción
}
```

## Registro de Interceptores

Los interceptores deben registrarse en `META-INF/beans.xml`:

```xml
<beans xmlns="https://jakarta.ee/xml/ns/jakartaee"
       version="4.0"
       bean-discovery-mode="all">
    <interceptors>
        <class>com.jakartaee.interceptors.interceptor.LoggingInterceptor</class>
        <class>com.jakartaee.interceptors.interceptor.TimingInterceptor</class>
        <class>com.jakartaee.interceptors.interceptor.ValidationInterceptor</class>
        <class>com.jakartaee.interceptors.interceptor.CachingInterceptor</class>
        <class>com.jakartaee.interceptors.interceptor.ConstructorInterceptor</class>
        <class>com.jakartaee.interceptors.interceptor.LifecycleInterceptor</class>
    </interceptors>
</beans>
```

**Orden de ejecución:** Los interceptores se ejecutan en el orden en que están registrados en `beans.xml`.

## Interceptores Encadenados

Puedes aplicar múltiples interceptores a un mismo método. Se ejecutan en el orden de registro:

```java
@Timed(unit = "ms")
@Validated
@Cached(ttl = 30)
@Loggable("INFO")
public List<Hero> getAllHeroes() {
    // Orden de ejecución:
    // 1. LoggingInterceptor (antes)
    // 2. TimingInterceptor (antes)
    // 3. ValidationInterceptor (antes)
    // 4. CachingInterceptor (antes - verifica cache)
    // 5. Método getAllHeroes()
    // 6. CachingInterceptor (después - guarda en cache)
    // 7. ValidationInterceptor (después)
    // 8. TimingInterceptor (después - registra tiempo)
    // 9. LoggingInterceptor (después)
    return heroService.findAll();
}
```

## Estructura del Proyecto

```
interceptors/
├── pom.xml
├── README.md
├── src/main/
│   ├── java/com/jakartaee/interceptors/
│   │   ├── config/
│   │   │   └── DataInitializer.java
│   │   ├── interceptor/
│   │   │   ├── Loggable.java              # Binding annotation
│   │   │   ├── LoggingInterceptor.java    # @AroundInvoke
│   │   │   ├── Timed.java                 # Binding annotation
│   │   │   ├── TimingInterceptor.java     # @AroundInvoke
│   │   │   ├── Validated.java             # Binding annotation
│   │   │   ├── ValidationInterceptor.java # @AroundInvoke
│   │   │   ├── Cached.java                # Binding annotation
│   │   │   ├── CachingInterceptor.java    # @AroundInvoke
│   │   │   ├── Tracked.java               # Binding annotation
│   │   │   ├── ConstructorInterceptor.java # @AroundConstruct
│   │   │   ├── Monitored.java             # Binding annotation
│   │   │   └── LifecycleInterceptor.java  # @PostConstruct/@PreDestroy
│   │   ├── resource/
│   │   │   └── InterceptorDemoResource.java
│   │   └── service/
│   │       ├── HeroService.java
│   │       └── PowerAnalysisService.java
│   └── resources/
│       ├── application.properties
│       └── META-INF/
│           └── beans.xml                  # Registro de interceptores
```

## Endpoints REST

### 1. Obtener todos los héroes (con caché)

```bash
GET /api/interceptors/heroes
```

**Interceptores aplicados:** Logging + Timing + Validación + Caché

**Primera llamada:** Ejecuta el método y cachea el resultado
**Siguientes llamadas:** Retorna desde caché (ver logs)

### 2. Obtener héroe por ID

```bash
GET /api/interceptors/heroes/{id}
```

**Interceptores aplicados:** Logging + Timing + Validación

### 3. Crear héroe

```bash
POST /api/interceptors/heroes?name=Superman&power=Flight&powerLevel=95
```

**Interceptores aplicados:** Logging + Timing + Validación

**Nota:** Si envías un parámetro `null`, el `ValidationInterceptor` lanzará una excepción.

### 4. Buscar héroes poderosos

```bash
GET /api/interceptors/heroes/powerful?minPowerLevel=80
```

**Interceptores aplicados:** Logging + Timing

### 5. Análisis de poderes (con caché)

```bash
GET /api/interceptors/analysis
```

**Interceptores aplicados:** Logging + Timing + Validación + Caché

**Nota:** Este servicio también demuestra `@Tracked` en el constructor.

### 6. Obtener tipos de poderes

```bash
GET /api/interceptors/powers
```

**Interceptores aplicados:** Logging + Timing

### 7. Información sobre interceptores

```bash
GET /api/interceptors/info
```

Retorna información sobre todos los interceptores disponibles.

## Ejecutar el Demo

```bash
cd quarkus-demos/interceptors
mvn quarkus:dev
```

La aplicación estará disponible en `http://localhost:8080`

## Observar los Interceptores

### 1. Logs de LoggingInterceptor

Al llamar a cualquier endpoint, verás logs como:

```
>>> [LOGGING] Entrando a HeroService.getAllHeroes()
    Parámetros: []
<<< [LOGGING] Saliendo de HeroService.getAllHeroes()
    Retorno: [Hero{id=1, name='Superman'}, ...]
```

### 2. Logs de TimingInterceptor

Verás logs como:

```
⏱️  [TIMING] HeroService.getAllHeroes() ejecutado en 15.234 ms
```

### 3. Logs de ValidationInterceptor

Si envías parámetros inválidos:

```
❌ [VALIDATION] Parámetro 0 de HeroService.createHero() es null
```

### 4. Logs de CachingInterceptor

Primera llamada:
```
💾 [CACHE] Miss para HeroService.getAllHeroes() - Ejecutando y cacheando
```

Siguientes llamadas:
```
💾 [CACHE] Hit para HeroService.getAllHeroes() - Retornando desde cache
```

### 5. Logs de ConstructorInterceptor

Al crear instancias de servicios con `@Tracked`:

```
🏗️  [CONSTRUCTOR] Creando instancia de PowerAnalysisService
    Parámetros del constructor: []
✅ [CONSTRUCTOR] Instancia de PowerAnalysisService creada exitosamente
```

### 6. Logs de LifecycleInterceptor

Al inicializar beans con `@Monitored`:

```
🚀 [LIFECYCLE] @PostConstruct ejecutado para HeroService
✅ [LIFECYCLE] HeroService inicializado completamente
```

Al destruir la aplicación:

```
🛑 [LIFECYCLE] @PreDestroy ejecutado para HeroService
✅ [LIFECYCLE] HeroService destruido completamente
```

## Casos de Uso Prácticos

### 1. Logging Automático

Los interceptores permiten agregar logging sin modificar el código de negocio:

```java
@Loggable("DEBUG")
public void processPayment(Payment payment) {
    // El logging se agrega automáticamente
    paymentService.process(payment);
}
```

### 2. Medición de Rendimiento

Monitorear el tiempo de ejecución de métodos críticos:

```java
@Timed(unit = "ms")
public List<Report> generateReport(Date start, Date end) {
    // El tiempo se mide automáticamente
    return reportService.generate(start, end);
}
```

### 3. Validación de Parámetros

Validar parámetros antes de ejecutar métodos:

```java
@Validated
public void transferMoney(Account from, Account to, BigDecimal amount) {
    // Los parámetros se validan automáticamente
    accountService.transfer(from, to, amount);
}
```

### 4. Caché de Resultados

Cachear resultados de métodos costosos:

```java
@Cached(ttl = 300) // 5 minutos
public List<Product> getPopularProducts() {
    // El resultado se cachea automáticamente
    return productService.findPopular();
}
```

### 5. Rastreo de Objetos

Rastrear la creación de objetos importantes:

```java
@Tracked
public class PaymentProcessor {
    // La creación se rastrea automáticamente
}
```

### 6. Gestión de Recursos

Inicializar y limpiar recursos automáticamente:

```java
@Monitored
@ApplicationScoped
public class DatabaseConnectionPool {
    // @PostConstruct: Inicializar pool
    // @PreDestroy: Cerrar conexiones
}
```

## Ventajas de los Interceptores

1. **Separación de Concerns**: La lógica transversal (logging, timing, etc.) está separada del código de negocio
2. **Reutilización**: Un interceptor puede aplicarse a múltiples métodos/clases
3. **Mantenibilidad**: Cambios en la lógica transversal solo requieren modificar el interceptor
4. **No Invasivo**: No necesitas modificar el código existente para agregar funcionalidad
5. **Composición**: Puedes combinar múltiples interceptores según necesites

## Limitaciones

1. **Orden de Ejecución**: El orden importa y está definido por `beans.xml`
2. **Rendimiento**: Los interceptores agregan overhead (aunque mínimo)
3. **Debugging**: Puede ser más difícil depurar código con muchos interceptores
4. **Complejidad**: Demasiados interceptores pueden hacer el código difícil de seguir

## Mejores Prácticas

1. **Usar interceptores para lógica transversal**: Logging, timing, validación, caching
2. **Evitar lógica de negocio en interceptores**: Los interceptores deben ser genéricos
3. **Documentar interceptores**: Explicar qué hace cada interceptor
4. **Mantener interceptores simples**: Un interceptor debe hacer una cosa bien
5. **Usar anotaciones descriptivas**: Nombres claros como `@Loggable`, `@Timed`, etc.

## Referencias

- [Jakarta Interceptors Specification](https://jakarta.ee/specifications/interceptors/)
- [Quarkus CDI Guide](https://quarkus.io/guides/cdi)
- [CDI Interceptors Documentation](https://jakarta.ee/specifications/cdi/4.0/jakarta-cdi-spec-4.0.html#interceptors)

