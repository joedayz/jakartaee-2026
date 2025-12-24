# Jakarta Data Demo

## Descripción

Este demo muestra cómo usar **Jakarta Data** para crear repositorios que simplifican el acceso a datos usando el patrón Repository. También incluye ejemplos de **Panache Next (Hibernate with Panache)**, una nueva extensión experimental que combina Panache con Jakarta Data.

## Objetivo

Aprender a:
- Crear repositorios con Jakarta Data usando `@Find`, `@Insert`, `@Delete`
- Usar métodos de consulta derivados del nombre
- Usar Panache Next con entidades que extienden `PanacheEntity`
- Crear repositorios anidados dentro de las entidades
- Usar Active Record Pattern con Panache
- Combinar Jakarta Data y Panache

## Tema DC

Repositorios para gestionar Heroes y Villanos de DC Comics:
- **HeroRepository**: CRUD y consultas personalizadas para Heroes
- **VillainRepository**: CRUD y consultas personalizadas para Villains

## Soporte en Quarkus

✅ **Jakarta Data está completamente soportado en Quarkus 3.30.2** a través de Hibernate ORM.

Este demo usa:
- **Quarkus 3.30.2** con la nueva API REST (`quarkus-rest`)
- **Jakarta Data 1.0.1**
- **Hibernate ORM with Panache** (para Panache Next)
- **Hibernate Processor 7.1.11.Final**
- **Unidad de persistencia nombrada** (`DCHeroes`)

Los repositorios se generan automáticamente en tiempo de compilación usando el procesador de anotaciones de Hibernate.

### ⚠️ Panache Next (Experimental)

Este demo incluye ejemplos de **Panache Next (Hibernate with Panache)**, una nueva extensión experimental de Quarkus que combina Panache con Jakarta Data. 

**Importante**: Panache Next es experimental y puede cambiar. La anotación `@HQL` mencionada en el artículo aún no está disponible en Jakarta Data 1.0.1, por lo que los ejemplos usan métodos de Panache tradicionales pero muestran la estructura que tendría con `@HQL`.

## Dependencias

```xml
<!-- Jakarta RESTful Web Services -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- Jakarta Data API -->
<dependency>
    <groupId>jakarta.data</groupId>
    <artifactId>jakarta.data-api</artifactId>
    <version>1.0.1</version>
</dependency>

<!-- Hibernate ORM with Panache (incluye soporte para Jakarta Data y Panache Next) -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-orm-panache</artifactId>
</dependency>

<!-- Hibernate Processor para generar implementaciones de repositorios Jakarta Data -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-processor</artifactId>
    <scope>provided</scope>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-h2</artifactId>
</dependency>
```

## Configuración Requerida

### 1. Procesador de Anotaciones de Hibernate

En `pom.xml`, configura el procesador de anotaciones:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>${compiler-plugin.version}</version>
    <configuration>
        <annotationProcessorPathsUseDepMgmt>true</annotationProcessorPathsUseDepMgmt>
        <annotationProcessorPaths>
            <path>
                <groupId>org.hibernate.orm</groupId>
                <artifactId>hibernate-processor</artifactId>
            </path>
        </annotationProcessorPaths>
        <compilerArgs>
            <arg>-parameters</arg>
        </compilerArgs>
    </configuration>
</plugin>
```

### 2. Configuración de Base de Datos y Unidad de Persistencia

En `application.properties`:

```properties
# H2 Database Configuration
quarkus.datasource.db-kind=h2
quarkus.datasource.username=sa
quarkus.datasource.password=
quarkus.datasource.jdbc.url=jdbc:h2:mem:dcheroes

# Define a persistence unit named "DCHeroes" (requerido para Jakarta Data)
quarkus.hibernate-orm.DCHeroes.schema-management.strategy=drop-and-create
quarkus.hibernate-orm.DCHeroes.datasource=<default>
quarkus.hibernate-orm.DCHeroes.packages=com.jakartaee.common.entities
quarkus.hibernate-orm.DCHeroes.log.sql=true
```

**Importante**: La unidad de persistencia nombrada (`DCHeroes`) es requerida cuando usas `@Repository(dataStore = "DCHeroes")`.

## Ejemplos de Repositorios

### HeroRepository

Este demo usa **anotaciones específicas de Jakarta Data** como en la demo oficial de Quarkus:

```java
@Transactional
@Repository(dataStore = "DCHeroes")
public interface HeroRepository {

    @Find
    Optional<Hero> findById(Long id);

    @Find
    List<Hero> findByName(@Pattern String name);

    @Find
    List<Hero> findByIsActiveTrue();

    @Find
    List<Hero> findAll();

    @Insert
    void save(Hero hero);

    @Delete
    void deleteById(Long id);
}
```

**Características importantes**:
- `@Repository(dataStore = "DCHeroes")` - Especifica la unidad de persistencia nombrada
- `@Transactional` - Maneja transacciones automáticamente
- `@Find`, `@Insert`, `@Delete` - Anotaciones específicas de Jakarta Data
- `@Pattern` - Permite búsquedas con patrones (LIKE en SQL)

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
cd ../quarkus-demos/jakarta-data
mvn clean compile

# 3. Ejecuta el demo en modo desarrollo
mvn quarkus:dev
```

La aplicación estará disponible en: http://localhost:8080

### Endpoints Disponibles

#### Jakarta Data (Repositorios tradicionales)

- `GET /api/heroes` - Lista todos los héroes
- `GET /api/heroes/{id}` - Obtiene un héroe por ID
- `GET /api/heroes/search?name={pattern}` - Busca héroes por nombre (usa patrón)
- `GET /api/heroes/active` - Lista héroes activos
- `POST /api/heroes` - Crea un nuevo héroe
- `PUT /api/heroes/{id}` - Actualiza un héroe
- `DELETE /api/heroes/{id}` - Elimina un héroe

#### Panache Next (Experimental)

- `POST /api/panache-next/heroes/active-record` - Crear héroe usando Active Record Pattern
- `POST /api/panache-next/heroes/repository` - Crear héroe usando repositorio anidado
- `GET /api/panache-next/heroes/powerful?minLevel=80` - Buscar héroes poderosos
- `GET /api/panache-next/heroes/active` - Buscar héroes activos
- `GET /api/panache-next/heroes/power-range?minLevel=80&maxLevel=100` - Buscar por rango de poder
- `GET /api/panache-next/heroes/ordered-by-power` - Obtener ordenados por poder
- `GET /api/panache-next/heroes/{id}` - Obtener héroe por ID
- `PUT /api/panache-next/heroes/{id}` - Actualizar héroe (Active Record)
- `DELETE /api/panache-next/heroes/{id}` - Eliminar héroe (Active Record)
- `DELETE /api/panache-next/heroes/by-name/{name}` - Eliminar por nombre
- `GET /api/panache-next/heroes/count/active` - Contar héroes activos
- `GET /api/panache-next/villains/dangerous` - Buscar villanos peligrosos
- `GET /api/panache-next/villains/threat-level/{level}` - Buscar por nivel de amenaza

La aplicación estará disponible en: `http://localhost:8080`

### Verificar PostgreSQL

Cuando la aplicación inicie, verás en los logs algo como:
```
Dev Services for PostgreSQL started.
```

**Credenciales por defecto de DevServices:**
- **Host**: localhost
- **Port**: 5432 (o el que DevServices asigne)
- **Database**: dcheroes (configurado en `quarkus.datasource.devservices.db-name`)
- **Username**: quarkus (por defecto)
- **Password**: quarkus (por defecto)

**Nota**: DevServices configura automáticamente `quarkus.datasource.jdbc.url` con la URL correcta.
No necesitas configurarla manualmente en desarrollo.

## Ejemplos de Uso

### Listar héroes con paginación
```bash
curl http://localhost:8080/api/heroes?page=0&size=5
```

### Buscar héroes poderosos
```bash
curl http://localhost:8080/api/heroes/powerful?minLevel=90
```

### Buscar por nombre
```bash
curl http://localhost:8080/api/heroes/search?name=Super
```

### Crear un héroe
```bash
curl -X POST http://localhost:8080/api/heroes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Superman",
    "power": "Super fuerza, vuelo",
    "powerLevel": 95
  }'
```

### Obtener el héroe más poderoso
```bash
curl http://localhost:8080/api/heroes/most-powerful
```

## Ejemplos de Uso

### Inyectar y usar el repositorio

```java
@ApplicationScoped
public class HeroService {
    
    @Inject
    HeroRepository heroRepository;
    
    public Hero createHero(String name, String power, Integer powerLevel) {
        Hero hero = new Hero(name, power, powerLevel);
        return heroRepository.save(hero);
    }
    
    public List<Hero> findPowerfulHeroes(Integer minLevel) {
        return heroRepository.findByPowerLevelGreaterThan(minLevel);
    }
    
    public Page<Hero> getAllHeroes(int page, int size) {
        return heroRepository.findAll(Pageable.ofPage(page).size(size));
    }
}
```

## Endpoints REST

- `GET /api/heroes` - Listar todos los héroes (con paginación: `?page=0&size=10`)
- `GET /api/heroes/{id}` - Obtener un héroe por ID
- `POST /api/heroes` - Crear un héroe
- `GET /api/heroes/powerful?minLevel=90` - Buscar héroes poderosos
- `GET /api/heroes/search?name=Superman` - Buscar por nombre (búsqueda parcial)
- `GET /api/heroes/power-range?minLevel=80&maxLevel=100` - Buscar por rango de poder (con paginación)
- `GET /api/heroes/ordered-by-power` - Obtener todos ordenados por poder descendente
- `GET /api/heroes/most-powerful` - Obtener el héroe más poderoso
- `GET /api/heroes/count/active` - Contar héroes activos
- `PUT /api/heroes/{id}` - Actualizar un héroe
- `DELETE /api/heroes/{id}` - Eliminar un héroe

## Validación

Para validar que Jakarta Data está funcionando:

1. Verificar que el repositorio se inyecta correctamente
2. Ejecutar consultas y verificar resultados
3. Verificar que la paginación funciona

## Notas

- Jakarta Data usa Hibernate ORM bajo el capó
- Los métodos de consulta se generan automáticamente basándose en el nombre del método
- Soporta paginación, ordenamiento y consultas personalizadas
- Compatible con Jakarta Persistence (JPA)

## Panache Next (Hibernate with Panache) - Experimental

Este demo también incluye ejemplos de **Panache Next**, una nueva extensión experimental de Quarkus que combina Panache con Jakarta Data.

### ¿Qué es Panache Next?

Panache Next es una nueva versión de Panache diseñada para:
- Unificar Hibernate ORM y Hibernate Reactive
- Soportar entidades managed y stateless
- Integrar Jakarta Data con queries type-safe usando `@HQL`
- Unificar Active Record y Repository patterns
- Permitir mezclar diferentes modos de operación

### ⚠️ Estado Experimental

**Importante**: Panache Next es experimental y puede cambiar:
- Los nombres de extensiones pueden cambiar
- Los nombres de paquetes pueden cambiar
- Los nombres de clases pueden cambiar
- La API puede cambiar
- La anotación `@HQL` aún no está disponible en Jakarta Data 1.0.1

### Ejemplo de Entidad con Panache Next

```java
@Entity
@Table(name = "heroes_panache")
public class HeroPanacheEntity extends PanacheEntity {
    
    public String name;
    public String power;
    public Integer powerLevel;
    public Boolean isActive = true;
    
    /**
     * Repositorio anidado dentro de la entidad.
     * En Panache Next experimental se usaría @HQL para queries type-safe.
     */
    public interface Repo extends PanacheRepository<HeroPanacheEntity> {
        
        // En Panache Next experimental sería:
        // @HQL("where isActive = true order by name")
        default List<HeroPanacheEntity> findActive() {
            return find("isActive = true order by name").list();
        }
        
        // En Panache Next experimental sería:
        // @HQL("where powerLevel >= :minLevel order by powerLevel desc")
        default List<HeroPanacheEntity> findPowerful(int minLevel) {
            return find("powerLevel >= ?1 order by powerLevel desc", minLevel).list();
        }
    }
}
```

### Active Record Pattern

Con Panache Next, puedes usar Active Record Pattern directamente en las instancias:

```java
@Transactional
public void createHero() {
    HeroPanacheEntity hero = new HeroPanacheEntity();
    hero.name = "Superman";
    hero.power = "Super fuerza";
    hero.powerLevel = 95;
    
    // Active Record: persistir directamente
    hero.persist();
    
    // Modificar y los cambios se persisten automáticamente (en managed entities)
    hero.name = "Superman Updated";
    
    // Eliminar directamente
    hero.delete();
}
```

### Usar el Repositorio Anidado

También puedes usar el repositorio anidado:

```java
@Inject
HeroPanacheEntity.Repo heroRepo;

public void useRepository() {
    // Usar métodos del repositorio anidado
    List<HeroPanacheEntity> powerful = heroRepo.findPowerful(80);
    List<HeroPanacheEntity> active = heroRepo.findActive();
    
    // O usar métodos estándar de Panache
    HeroPanacheEntity hero = heroRepo.findById(1L);
    heroRepo.persist(newHero);
}
```

### Endpoints de Panache Next

Este demo incluye endpoints adicionales que demuestran Panache Next:

- `POST /api/panache-next/heroes/active-record` - Crear héroe usando Active Record
- `POST /api/panache-next/heroes/repository` - Crear héroe usando repositorio
- `GET /api/panache-next/heroes/powerful?minLevel=80` - Buscar héroes poderosos
- `GET /api/panache-next/heroes/active` - Buscar héroes activos
- `GET /api/panache-next/heroes/power-range?minLevel=80&maxLevel=100` - Buscar por rango
- `GET /api/panache-next/heroes/ordered-by-power` - Obtener ordenados por poder
- `DELETE /api/panache-next/heroes/by-name/{name}` - Eliminar por nombre
- `GET /api/panache-next/heroes/count/active` - Contar activos
- `GET /api/panache-next/villains/dangerous` - Buscar villanos peligrosos

### Comparación: Jakarta Data vs Panache Next

| Característica | Jakarta Data Puro | Panache Next |
|----------------|-------------------|--------------|
| Entidades | JPA estándar | `PanacheEntity` |
| Repositorios | Interfaces separadas | Anidados en entidades |
| Active Record | No | Sí |
| Queries type-safe | `@Find` con métodos derivados | `@HQL` (experimental) |
| Queries tradicionales | No | Sí (métodos de Panache) |
| Reactive | No | Sí (experimental) |
| Stateless | No | Sí (experimental) |

### Ventajas de Panache Next

1. **Repositorios anidados**: Mantiene las operaciones cerca de la entidad
2. **Active Record**: Permite operaciones directas en instancias
3. **Flexibilidad**: Puedes usar Active Record o Repository según necesites
4. **Type-safe queries**: Con `@HQL` (cuando esté disponible)
5. **Unificación**: Un solo API para blocking y reactive

### Notas sobre Panache Next

- Panache Next es experimental - todo puede cambiar
- `@HQL` aún no está disponible - usamos métodos de Panache tradicionales
- Los ejemplos muestran la estructura que tendría con `@HQL`
- Cuando `@HQL` esté disponible, simplemente reemplaza los métodos `default` con anotaciones `@HQL`

## Jakarta Query

⚠️ **Jakarta Query NO está soportado aún** - La especificación está en desarrollo y Quarkus aún no la implementa.

