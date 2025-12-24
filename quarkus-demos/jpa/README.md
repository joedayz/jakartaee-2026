# Jakarta Persistence (JPA) Demo

## Descripción

Este demo muestra cómo usar **Jakarta Persistence (JPA)** en Quarkus para acceder a bases de datos relacionales. Demuestra las características principales de JPA y cómo Quarkus implementa completamente la especificación JPA.

## Objetivo

Aprender a:
- Usar `EntityManager` para operaciones CRUD
- Crear y usar **Named Queries** (`@NamedQuery`)
- Escribir **JPQL queries** dinámicas
- Usar **Criteria API** para queries type-safe
- Manejar **relaciones** entre entidades (OneToMany, ManyToOne)
- Usar **lifecycle callbacks** (@PrePersist, @PreUpdate, @PostLoad)
- Implementar **optimistic locking** con @Version
- Manejar **lazy/eager loading**
- Usar métodos avanzados del EntityManager (flush, refresh, detach, clear)

## Tema DC

Gestión de Heroes y Missions de DC Comics:
- **HeroJPA**: Entidad Hero con relaciones OneToMany con Mission
- **Mission**: Entidad Mission con relación ManyToOne con HeroJPA
- **HeroDAO**: DAO usando EntityManager
- **MissionDAO**: DAO para Missions
- **HeroService**: Servicio con JPQL y Criteria API

## Soporte en Quarkus

✅ **JPA está completamente soportado en Quarkus 3.30.2** a través de Hibernate ORM.

Este demo usa:
- **Quarkus 3.30.2** con la nueva API REST (`quarkus-rest`)
- **Hibernate ORM** (implementación de JPA)
- **H2 Database** (base de datos en memoria)
- **EntityManager** con inyección CDI

## Dependencias

```xml
<!-- Jakarta RESTful Web Services -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- Hibernate ORM (implementación de JPA) -->
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

## Configuración

### application.properties

```properties
# H2 Database Configuration
quarkus.datasource.db-kind=h2
quarkus.datasource.username=sa
quarkus.datasource.password=
quarkus.datasource.jdbc.url=jdbc:h2:mem:jpademo

# Hibernate ORM Configuration
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.packages=com.jakartaee.jpa.entity
quarkus.hibernate-orm.log.sql=true
```

## Características de JPA Demostradas

### 1. EntityManager

El `EntityManager` es la interfaz principal para interactuar con la base de datos en JPA.

#### Inyección del EntityManager

```java
// Forma 1: @Inject (CDI - recomendado en Quarkus)
@Inject
EntityManager entityManager;

// Forma 2: @PersistenceContext (JPA estándar)
@PersistenceContext
EntityManager entityManager;
```

#### Operaciones Básicas

```java
// Persistir una entidad
entityManager.persist(hero);

// Buscar por ID
HeroJPA hero = entityManager.find(HeroJPA.class, id);

// Actualizar una entidad
HeroJPA updated = entityManager.merge(hero);

// Eliminar una entidad
entityManager.remove(hero);

// Obtener referencia lazy
HeroJPA hero = entityManager.getReference(HeroJPA.class, id);

// Refrescar desde la BD
entityManager.refresh(hero);

// Desvincular del contexto
entityManager.detach(hero);

// Verificar si está gestionada
boolean isManaged = entityManager.contains(hero);

// Forzar sincronización con BD
entityManager.flush();

// Limpiar el contexto
entityManager.clear();
```

### 2. Named Queries

Las Named Queries se definen en las entidades usando `@NamedQuery`:

```java
@Entity
@NamedQueries({
    @NamedQuery(
        name = "HeroJPA.findAll",
        query = "SELECT h FROM HeroJPA h ORDER BY h.name"
    ),
    @NamedQuery(
        name = "HeroJPA.findByPowerLevel",
        query = "SELECT h FROM HeroJPA h WHERE h.powerLevel >= :minLevel ORDER BY h.powerLevel DESC"
    )
})
public class HeroJPA {
    // ...
}
```

Uso de Named Queries:

```java
TypedQuery<HeroJPA> query = entityManager.createNamedQuery("HeroJPA.findAll", HeroJPA.class);
List<HeroJPA> heroes = query.getResultList();

TypedQuery<HeroJPA> query = entityManager.createNamedQuery("HeroJPA.findByPowerLevel", HeroJPA.class);
query.setParameter("minLevel", 80);
List<HeroJPA> heroes = query.getResultList();
```

### 3. JPQL Queries

JPQL (Java Persistence Query Language) permite escribir queries dinámicas:

```java
// Query simple
String jpql = "SELECT h FROM HeroJPA h WHERE h.powerLevel >= :minLevel";
TypedQuery<HeroJPA> query = entityManager.createQuery(jpql, HeroJPA.class);
query.setParameter("minLevel", 80);
List<HeroJPA> heroes = query.getResultList();

// Query con LIKE
String jpql = "SELECT h FROM HeroJPA h WHERE h.name LIKE :pattern";
TypedQuery<HeroJPA> query = entityManager.createQuery(jpql, HeroJPA.class);
query.setParameter("pattern", "%Super%");
List<HeroJPA> heroes = query.getResultList();

// Query con JOIN FETCH (evita N+1)
String jpql = "SELECT DISTINCT h FROM HeroJPA h LEFT JOIN FETCH h.missions";
TypedQuery<HeroJPA> query = entityManager.createQuery(jpql, HeroJPA.class);
List<HeroJPA> heroes = query.getResultList();

// Query con funciones de agregación
String jpql = "SELECT COUNT(h), AVG(h.powerLevel), MAX(h.powerLevel) FROM HeroJPA h";
TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
Object[] results = query.getSingleResult();

// Query UPDATE
String jpql = "UPDATE HeroJPA h SET h.powerLevel = :newLevel WHERE h.id = :id";
Query query = entityManager.createQuery(jpql);
query.setParameter("id", id);
query.setParameter("newLevel", 95);
int updated = query.executeUpdate();
```

### 4. Criteria API

La Criteria API permite construir queries type-safe programáticamente:

```java
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
CriteriaQuery<HeroJPA> query = cb.createQuery(HeroJPA.class);
Root<HeroJPA> hero = query.from(HeroJPA.class);

// Construir predicados
Predicate powerPredicate = cb.greaterThanOrEqualTo(hero.get("powerLevel"), 80);
Predicate activePredicate = cb.equal(hero.get("isActive"), true);

// Combinar predicados
query.where(cb.and(powerPredicate, activePredicate));

// Ordenar
query.orderBy(cb.desc(hero.get("powerLevel")));

// Ejecutar
TypedQuery<HeroJPA> typedQuery = entityManager.createQuery(query);
List<HeroJPA> heroes = typedQuery.getResultList();
```

### 5. Relaciones entre Entidades

#### OneToMany (HeroJPA -> Mission)

```java
@Entity
public class HeroJPA {
    @OneToMany(mappedBy = "hero", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Mission> missions = new ArrayList<>();
    
    // Helper methods para manejar la relación bidireccional
    public void addMission(Mission mission) {
        missions.add(mission);
        mission.setHero(this);
    }
}
```

#### ManyToOne (Mission -> HeroJPA)

```java
@Entity
public class Mission {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hero_id", nullable = false)
    private HeroJPA hero;
}
```

### 6. Lifecycle Callbacks

Los callbacks permiten ejecutar código en diferentes momentos del ciclo de vida de la entidad:

```java
@Entity
public class HeroJPA {
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @PostLoad
    protected void onLoad() {
        // Inicializar colecciones lazy si es necesario
        if (missions == null) {
            missions = new ArrayList<>();
        }
    }
}
```

### 7. Optimistic Locking

El `@Version` permite implementar optimistic locking:

```java
@Entity
public class HeroJPA {
    @Version
    @Column(name = "version")
    private Long version;
}
```

### 8. Transacciones

Las transacciones se manejan con `@Transactional`:

```java
@Transactional
public HeroJPA createHero(HeroJPA hero) {
    entityManager.persist(hero);
    entityManager.flush(); // Forzar sincronización
    return hero;
}
```

## Estructura del Proyecto

```
jpa/
├── pom.xml
├── src/main/java/com/jakartaee/jpa/
│   ├── entity/
│   │   ├── HeroJPA.java          # Entidad Hero con Named Queries
│   │   └── Mission.java          # Entidad Mission con relaciones
│   ├── dao/
│   │   ├── HeroDAO.java          # DAO usando EntityManager
│   │   └── MissionDAO.java       # DAO para Missions
│   ├── service/
│   │   └── HeroService.java      # Servicio con JPQL y Criteria API
│   ├── resource/
│   │   ├── HeroResource.java     # REST endpoints para Heroes
│   │   ├── MissionResource.java  # REST endpoints para Missions
│   │   └── JPADemoResource.java  # Demo de características JPA
│   └── config/
│       └── DataInitializer.java   # Inicialización de datos
└── src/main/resources/
    └── application.properties     # Configuración
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
cd ../quarkus-demos/jpa
mvn clean compile

# 3. Ejecuta el demo en modo desarrollo
mvn quarkus:dev
```

La aplicación estará disponible en: `http://localhost:8080`

## Endpoints Disponibles

### Heroes

- `GET /api/heroes` - Obtiene todos los héroes (Named Query)
- `GET /api/heroes/{id}` - Obtiene un héroe por ID (EntityManager.find)
- `GET /api/heroes/search?name=Superman` - Busca por nombre (Named Query)
- `GET /api/heroes/powerful?minLevel=80` - Busca héroes poderosos (Named Query)
- `GET /api/heroes/power-range?minLevel=80&maxLevel=100` - Busca por rango (Named Query)
- `GET /api/heroes/criteria?minPowerLevel=80&activeOnly=true` - Busca usando Criteria API
- `GET /api/heroes/advanced-search?namePattern=Super&minPowerLevel=80&maxPowerLevel=100` - Búsqueda avanzada con Criteria API
- `GET /api/heroes/with-missions` - Obtiene héroes con misiones (JOIN FETCH)
- `GET /api/heroes/statistics` - Estadísticas de héroes (JPQL con agregaciones)
- `POST /api/heroes` - Crea un nuevo héroe (EntityManager.persist)
- `PUT /api/heroes/{id}` - Actualiza un héroe (EntityManager.merge)
- `PATCH /api/heroes/{id}/power-level?newLevel=95` - Actualiza nivel de poder (Named Query UPDATE)
- `DELETE /api/heroes/{id}` - Elimina un héroe (EntityManager.remove)

### Missions

- `GET /api/missions/hero/{heroId}` - Obtiene misiones de un héroe (Named Query con relación)
- `POST /api/missions` - Crea una nueva misión (relación ManyToOne)
- `PUT /api/missions/{id}/complete` - Completa una misión
- `GET /api/missions/statistics` - Estadísticas de misiones por héroe (JPQL con GROUP BY)

### JPA Demo

- `GET /api/jpa-demo/entity-manager-methods/{id}` - Demuestra métodos del EntityManager
- `GET /api/jpa-demo/flush-demo` - Información sobre flush()
- `GET /api/jpa-demo/clear-demo` - Información sobre clear()
- `GET /api/jpa-demo/info` - Información sobre características JPA demostradas

## Ejemplos de Uso

### Crear un Héroe

```bash
curl -X POST http://localhost:8080/api/heroes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Aquaman",
    "power": "Control del agua",
    "powerLevel": 82,
    "description": "Rey de Atlantis"
  }'
```

### Buscar Héroes con Criteria API

```bash
curl "http://localhost:8080/api/heroes/criteria?minPowerLevel=85&activeOnly=true"
```

### Crear una Misión

```bash
curl -X POST http://localhost:8080/api/missions \
  -H "Content-Type: application/json" \
  -d '{
    "heroId": 1,
    "title": "Salvar el mundo",
    "description": "Prevenir una catástrofe global"
  }'
```

### Obtener Estadísticas

```bash
curl http://localhost:8080/api/heroes/statistics
```

## Características Clave de JPA en Quarkus

### ✅ Implementación Completa

Quarkus implementa completamente la especificación JPA:
- EntityManager con todas sus operaciones
- Named Queries
- JPQL queries
- Criteria API
- Relaciones (OneToMany, ManyToOne, OneToOne, ManyToMany)
- Lifecycle callbacks
- Optimistic locking
- Transacciones

### ✅ Integración con CDI

- Inyección del EntityManager usando `@Inject`
- Soporte para `@PersistenceContext`
- Transacciones con `@Transactional`

### ✅ Performance

- Lazy loading por defecto
- JOIN FETCH para evitar N+1
- Optimistic locking para concurrencia

## Notas

- JPA usa Hibernate ORM bajo el capó en Quarkus
- Las transacciones se manejan automáticamente con `@Transactional`
- El EntityManager se inyecta automáticamente por CDI
- Las Named Queries se validan en tiempo de compilación
- La Criteria API proporciona type-safety en tiempo de compilación

## Referencias

- [Jakarta Persistence Specification](https://jakarta.ee/specifications/persistence/)
- [Quarkus Hibernate ORM Guide](https://quarkus.io/guides/hibernate-orm)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)

