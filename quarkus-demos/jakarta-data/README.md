# Jakarta Data Demo

## Descripción

Este demo muestra cómo usar **Jakarta Data** para crear repositorios que simplifican el acceso a datos usando el patrón Repository.

## Objetivo

Aprender a:
- Crear repositorios con Jakarta Data
- Usar métodos de consulta derivados del nombre
- Extender `CrudRepository` y `PageableRepository`
- Usar paginación y ordenamiento

## Tema DC

Repositorios para gestionar Heroes y Villanos de DC Comics:
- **HeroRepository**: CRUD y consultas personalizadas para Heroes
- **VillainRepository**: CRUD y consultas personalizadas para Villains

## Soporte en Quarkus

✅ **Jakarta Data está completamente soportado en Quarkus 3.30.2** a través de Hibernate ORM.

Este demo usa:
- **Quarkus 3.30.2** con RESTEasy Classic
- **Jakarta Data 1.0.1**
- **Hibernate Processor 7.1.11.Final**
- **Unidad de persistencia nombrada** (`DCHeroes`)

Los repositorios se generan automáticamente en tiempo de compilación usando el procesador de anotaciones de Hibernate.

## Dependencias

```xml
<!-- Jakarta Data API -->
<dependency>
    <groupId>jakarta.data</groupId>
    <artifactId>jakarta.data-api</artifactId>
</dependency>

<!-- Hibernate ORM (incluye soporte para Jakarta Data) -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-orm</artifactId>
</dependency>

<!-- H2 Database (más simple para demos) -->
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

- `GET /api/heroes` - Lista todos los héroes
- `GET /api/heroes/{id}` - Obtiene un héroe por ID
- `GET /api/heroes/search?name={pattern}` - Busca héroes por nombre (usa patrón)
- `GET /api/heroes/active` - Lista héroes activos
- `POST /api/heroes` - Crea un nuevo héroe
- `PUT /api/heroes/{id}` - Actualiza un héroe
- `DELETE /api/heroes/{id}` - Elimina un héroe

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

## Jakarta Query

⚠️ **Jakarta Query NO está soportado aún** - La especificación está en desarrollo y Quarkus aún no la implementa.

