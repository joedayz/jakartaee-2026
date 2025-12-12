# CDI (Contexts and Dependency Injection) Demo

## Descripción

Este demo muestra cómo usar **Jakarta CDI (Contexts and Dependency Injection)** para gestionar la inyección de dependencias, scopes, qualifiers, interceptors, eventos y más en una aplicación Quarkus.

## Objetivo

Aprender a:
- Usar inyección de dependencias con `@Inject`
- Trabajar con diferentes scopes (`@ApplicationScoped`, `@RequestScoped`, `@Dependent`)
- Crear y usar qualifiers personalizados
- Usar producers (`@Produces`) para crear objetos configurables
- Crear interceptors para cross-cutting concerns
- Usar eventos CDI (`Event` y `@Observes`)
- Crear stereotypes para agrupar anotaciones

## Tema DC

Gestión de Heroes y Villains de DC Comics usando CDI:
- **HeroService**: Servicio para gestionar héroes con `@HeroQualifier`
- **VillainService**: Servicio para gestionar villanos con `@VillainQualifier`
- **PowerAnalysisService**: Servicio que demuestra inyección con qualifiers
- **EventObserver**: Observer que escucha eventos de creación
- **LoggingInterceptor**: Interceptor para logging automático

## Soporte en Quarkus

✅ **CDI está completamente soportado en Quarkus 3.30.2** a través de ArC (CDI implementation).

Este demo usa:
- **Quarkus 3.30.2** con la nueva API REST (`quarkus-rest`)
- **ArC** (CDI implementation de Quarkus)
- **Hibernate ORM** para persistencia
- **H2 Database** para almacenamiento

## Dependencias

```xml
<!-- Jakarta RESTful Web Services -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- CDI (incluido en Quarkus, pero explícito para claridad) -->
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

## Características Demostradas

### 1. Inyección de Dependencias

#### Inyección Básica

```java
@ApplicationScoped
public class HeroService {
    @Inject
    EntityManager entityManager;
    
    @Inject
    Event<HeroCreatedEvent> heroCreatedEvent;
}
```

#### Inyección con Qualifiers

```java
@RequestScoped
public class PowerAnalysisService {
    @Inject
    @HeroQualifier
    HeroService heroService;
    
    @Inject
    @VillainQualifier
    VillainService villainService;
}
```

### 2. Scopes

#### @ApplicationScoped
Una única instancia por aplicación:

```java
@ApplicationScoped
@Service
@HeroQualifier
public class HeroService {
    // Una única instancia compartida por toda la aplicación
}
```

#### @RequestScoped
Una instancia por request HTTP:

```java
@RequestScoped
public class PowerAnalysisService {
    // Nueva instancia para cada request HTTP
}
```

#### @Dependent (default)
Una nueva instancia cada vez que se inyecta:

```java
// Sin anotación de scope = @Dependent
public class SomeService {
    // Nueva instancia cada vez
}
```

### 3. Qualifiers Personalizados

#### Definir Qualifier

```java
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface HeroQualifier {
}
```

#### Usar Qualifier

```java
@Inject
@HeroQualifier
HeroService heroService;
```

### 4. Producers

Producers permiten crear objetos que no son beans CDI por sí mismos:

```java
@ApplicationScoped
public class ConfigurationProducer {
    
    @Produces
    @ApplicationScoped
    @Default
    public Logger produceApplicationLogger() {
        return Logger.getLogger("com.jakartaee.cdi");
    }
    
    @Produces
    @ApplicationScoped
    @Default
    public ApplicationConfig produceApplicationConfig() {
        ApplicationConfig config = new ApplicationConfig();
        config.setMaxHeroes(100);
        return config;
    }
}
```

### 5. Interceptors

#### Definir Interceptor Binding

```java
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, METHOD})
public @interface Loggable {
    String value() default "INFO";
}
```

#### Implementar Interceptor

```java
@Loggable
@Interceptor
public class LoggingInterceptor {
    
    @AroundInvoke
    public Object logMethod(InvocationContext context) throws Exception {
        logger.info(">>> Entrando a " + context.getMethod().getName());
        Object result = context.proceed();
        logger.info("<<< Saliendo de " + context.getMethod().getName());
        return result;
    }
}
```

#### Registrar Interceptor en beans.xml

```xml
<beans xmlns="https://jakarta.ee/xml/ns/jakartaee"
       version="4.0"
       bean-discovery-mode="all">
    <interceptors>
        <class>com.jakartaee.cdi.interceptor.LoggingInterceptor</class>
    </interceptors>
</beans>
```

#### Usar Interceptor

```java
@Service  // Incluye @Loggable a través del stereotype
public class HeroService {
    
    @Loggable("INFO")
    public Hero createHero(Hero hero) {
        // Este método será interceptado
    }
}
```

### 6. Eventos CDI

#### Disparar Evento

```java
@Inject
Event<HeroCreatedEvent> heroCreatedEvent;

public Hero createHero(Hero hero) {
    entityManager.persist(hero);
    heroCreatedEvent.fire(new HeroCreatedEvent(hero));
    return hero;
}
```

#### Observar Evento

```java
@ApplicationScoped
public class EventObserver {
    
    public void onHeroCreated(@Observes HeroCreatedEvent event) {
        logger.info("Héroe creado: " + event.getHero().getName());
    }
}
```

### 7. Stereotypes

Stereotypes agrupan múltiples anotaciones:

```java
@ApplicationScoped
@Loggable
@Stereotype
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface Service {
}
```

Uso:

```java
@Service  // Equivale a @ApplicationScoped + @Loggable
@HeroQualifier
public class HeroService {
    // ...
}
```

## Cómo Ejecutar

### Requisitos Previos

- Java 21
- Maven 3.9+

### Pasos

```bash
# 1. Asegúrate de que el módulo common esté compilado
cd ../../common
mvn clean install

# 2. Compila el proyecto
cd ../quarkus-demos/cdi
mvn clean compile

# 3. Ejecuta el demo en modo desarrollo
mvn quarkus:dev
```

La aplicación estará disponible en: http://localhost:8080

## Endpoints Disponibles

### Heroes

- `GET /api/heroes` - Lista todos los héroes
- `GET /api/heroes/{id}` - Obtiene un héroe por ID
- `GET /api/heroes/search?name={name}` - Busca héroes por nombre
- `GET /api/heroes/powerful` - Lista héroes poderosos (powerLevel >= 80)
- `POST /api/heroes` - Crea un nuevo héroe
- `PUT /api/heroes/{id}` - Actualiza un héroe
- `DELETE /api/heroes/{id}` - Elimina un héroe

### Villains

- `GET /api/villains` - Lista todos los villanos
- `GET /api/villains/{id}` - Obtiene un villano por ID
- `GET /api/villains/search?name={name}` - Busca villanos por nombre
- `GET /api/villains/dangerous` - Lista villanos peligrosos (threatLevel HIGH/CRITICAL)
- `POST /api/villains` - Crea un nuevo villano
- `PUT /api/villains/{id}` - Actualiza un villano
- `DELETE /api/villains/{id}` - Elimina un villano

### CDI Demo Endpoints

- `GET /api/cdi-demo/power-analysis` - Análisis de niveles de poder
- `GET /api/cdi-demo/events` - Estadísticas de eventos CDI
- `GET /api/cdi-demo/config` - Configuración producida por producers
- `GET /api/cdi-demo/comparison` - Comparación de héroes y villanos poderosos

## Ejemplos de Uso

### Crear un héroe (dispara evento)

```bash
curl -X POST http://localhost:8080/api/heroes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Aquaman",
    "power": "Control del agua, comunicación con vida marina",
    "powerLevel": 85,
    "description": "Rey de Atlantis"
  }'
```

Verás en los logs:
```
>>> Observer: Héroe creado - Aquaman (Power Level: 85)
```

### Análisis de poder

```bash
curl http://localhost:8080/api/cdi-demo/power-analysis
```

Respuesta:
```json
{
  "heroCount": 5,
  "villainCount": 4,
  "heroAveragePower": 89.0,
  "villainAveragePower": 83.75,
  "powerDifference": 5.25
}
```

### Ver eventos CDI

```bash
curl http://localhost:8080/api/cdi-demo/events
```

Respuesta:
```json
{
  "heroEvents": 1,
  "villainEvents": 0,
  "totalEvents": 1
}
```

## Estructura del Proyecto

```
cdi/
├── src/main/java/com/jakartaee/cdi/
│   ├── qualifier/          # Qualifiers personalizados
│   │   ├── HeroQualifier.java
│   │   ├── VillainQualifier.java
│   │   └── Powerful.java
│   ├── interceptor/        # Interceptors
│   │   ├── Loggable.java
│   │   └── LoggingInterceptor.java
│   ├── event/              # Eventos CDI
│   │   ├── HeroCreatedEvent.java
│   │   └── VillainCreatedEvent.java
│   ├── observer/           # Observers
│   │   └── EventObserver.java
│   ├── producer/           # Producers
│   │   └── ConfigurationProducer.java
│   ├── stereotype/         # Stereotypes
│   │   └── Service.java
│   ├── service/            # Servicios con diferentes scopes
│   │   ├── HeroService.java
│   │   ├── VillainService.java
│   │   └── PowerAnalysisService.java
│   ├── resource/           # Recursos REST
│   │   ├── HeroResource.java
│   │   ├── VillainResource.java
│   │   └── CDIDemoResource.java
│   └── config/             # Configuración
│       └── DataInitializer.java
└── src/main/resources/
    ├── application.properties
    └── META-INF/
        └── beans.xml        # Configuración de CDI
```

## Conceptos Clave de CDI

### 1. Inyección de Dependencias

CDI permite inyectar dependencias automáticamente usando `@Inject`:

```java
@Inject
HeroService heroService;
```

### 2. Scopes

Los scopes determinan el ciclo de vida de los beans:

- **@ApplicationScoped**: Una instancia por aplicación
- **@RequestScoped**: Una instancia por request HTTP
- **@SessionScoped**: Una instancia por sesión (no disponible en Quarkus)
- **@Dependent**: Nueva instancia cada vez (default)

### 3. Qualifiers

Los qualifiers permiten distinguir entre múltiples implementaciones del mismo tipo:

```java
@Inject
@HeroQualifier
HeroService heroService;
```

### 4. Producers

Los producers crean objetos que no son beans CDI:

```java
@Produces
public Logger produceLogger() {
    return Logger.getLogger("app");
}
```

### 5. Interceptors

Los interceptors permiten agregar funcionalidad cross-cutting:

```java
@AroundInvoke
public Object intercept(InvocationContext context) throws Exception {
    // Antes
    Object result = context.proceed();
    // Después
    return result;
}
```

### 6. Eventos

Los eventos permiten desacoplar componentes:

```java
// Disparar
event.fire(new HeroCreatedEvent(hero));

// Observar
public void observe(@Observes HeroCreatedEvent event) {
    // Reaccionar al evento
}
```

### 7. Stereotypes

Los stereotypes agrupan múltiples anotaciones:

```java
@Service  // Equivale a múltiples anotaciones
public class MyService {
}
```

## Notas

- CDI está habilitado por defecto en Quarkus
- Los interceptors deben registrarse en `beans.xml`
- Los eventos son síncronos por defecto en Quarkus
- Los scopes de sesión no están disponibles en Quarkus (solo ApplicationScoped y RequestScoped)
- ArC es la implementación de CDI en Quarkus y es más ligera que Weld

## Referencias

- [Jakarta CDI Specification](https://jakarta.ee/specifications/cdi/)
- [Quarkus CDI Guide](https://quarkus.io/guides/cdi-reference)
- [ArC Documentation](https://quarkus.io/guides/cdi-reference)

