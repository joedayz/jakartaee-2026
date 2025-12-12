# Quarkus Panache Demo

## Descripción

Este demo muestra cómo usar **Quarkus Panache** para simplificar el acceso a datos con Hibernate ORM. Panache ofrece dos patrones de programación:

1. **Repository Pattern** - Similar a Spring Data, usando interfaces de repositorio
2. **Active Record Pattern** - Donde las entidades tienen métodos directamente

## Objetivo

Aprender a:
- Usar Panache Repository Pattern para acceso a datos
- Usar Panache Active Record Pattern para acceso a datos
- Comparar ambos patrones y cuándo usar cada uno
- Realizar consultas con Panache Query API
- Usar métodos mágicos de Panache

## Tema DC

Gestión de Heroes y Villanos usando ambos patrones:
- **Repository Pattern**: Para Heroes (más estructurado, mejor para lógica compleja)
- **Active Record Pattern**: Para Villains (más simple, mejor para operaciones CRUD básicas)

## Patrones de Panache

### 1. Repository Pattern

```java
@ApplicationScoped
public class HeroRepository implements PanacheRepository<Hero> {
    // Métodos personalizados
    public List<Hero> findPowerfulHeroes() {
        return find("powerLevel >= ?1", 80).list();
    }
}
```

**Ventajas:**
- Separación clara entre entidad y lógica de acceso a datos
- Fácil de testear (puedes mockear el repositorio)
- Mejor para lógica compleja y consultas avanzadas
- Similar a Spring Data (familiar para muchos desarrolladores)

### 2. Active Record Pattern

```java
@Entity
public class Villain extends PanacheEntity {
    public String name;
    public Integer powerLevel;
    
    // Métodos estáticos y de instancia disponibles directamente
    public static List<Villain> findPowerful() {
        return find("powerLevel >= ?1", 80).list();
    }
}
```

**Ventajas:**
- Más simple y directo
- Menos código (no necesitas repositorio separado)
- Ideal para operaciones CRUD simples
- Métodos disponibles directamente en la entidad

## Dependencias

```xml
<!-- Hibernate ORM with Panache -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-orm-panache</artifactId>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-h2</artifactId>
</dependency>
```

**Versión**: Quarkus 3.30.2

## Cómo Ejecutar

```bash
cd quarkus-demos/panache
mvn quarkus:dev
```

La aplicación estará disponible en: `http://localhost:8080`

## Endpoints Disponibles

### Repository Pattern (Heroes)
- `GET /api/heroes/repository` - Listar héroes usando Repository Pattern
- `GET /api/heroes/repository/{id}` - Obtener héroe por ID
- `GET /api/heroes/repository/powerful` - Buscar héroes poderosos (powerLevel >= 80)
- `POST /api/heroes/repository` - Crear héroe usando Repository Pattern
- `PUT /api/heroes/repository/{id}` - Actualizar héroe
- `DELETE /api/heroes/repository/{id}` - Eliminar héroe

### Active Record Pattern (Villains)
- `GET /api/villains/activerecord` - Listar villanos usando Active Record Pattern
- `GET /api/villains/activerecord/{id}` - Obtener villano por ID
- `GET /api/villains/activerecord/powerful` - Buscar villanos poderosos
- `POST /api/villains/activerecord` - Crear villano usando Active Record Pattern
- `PUT /api/villains/activerecord/{id}` - Actualizar villano
- `DELETE /api/villains/activerecord/{id}` - Eliminar villano

### Comparación
- `GET /api/panache/comparison` - Comparar ambos patrones con ejemplos

## Ejemplos de Uso

### Repository Pattern - Crear héroe
```bash
curl -X POST http://localhost:8080/api/heroes/repository \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Superman",
    "power": "Super fuerza, vuelo",
    "powerLevel": 95
  }'
```

### Repository Pattern - Buscar héroes poderosos
```bash
curl http://localhost:8080/api/heroes/repository/powerful
```

### Active Record Pattern - Crear villano
```bash
curl -X POST http://localhost:8080/api/villains/activerecord \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Joker",
    "power": "Caos, inteligencia criminal",
    "powerLevel": 80
  }'
```

### Active Record Pattern - Buscar villanos poderosos
```bash
curl http://localhost:8080/api/villains/activerecord/powerful
```

## Estructura del Código

```
panache/
├── src/main/java/com/jakartaee/panache/
│   ├── entity/
│   │   ├── HeroEntity.java          # Entidad para Repository Pattern
│   │   └── VillainEntity.java       # Entidad para Active Record Pattern (extiende PanacheEntity)
│   ├── repository/
│   │   └── HeroRepository.java     # Repositorio usando PanacheRepository
│   └── resource/
│       ├── HeroRepositoryResource.java    # Endpoints usando Repository Pattern
│       ├── VillainActiveRecordResource.java # Endpoints usando Active Record Pattern
│       └── PanacheComparisonResource.java  # Comparación de ambos patrones
└── pom.xml
```

## Características de Panache

### Métodos Mágicos Disponibles

**En PanacheRepository y PanacheEntity:**
- `findAll()` - Encontrar todos
- `findById(id)` - Encontrar por ID
- `persist(entity)` - Guardar entidad
- `delete(entity)` - Eliminar entidad
- `count()` - Contar registros
- `find(query, params)` - Consulta con parámetros
- `find(query)` - Consulta simple
- `update(query)` - Actualizar con query

### Consultas Panache Query

```java
// Consulta simple
find("name", "Superman")

// Consulta con parámetros posicionales
find("powerLevel >= ?1", 80)

// Consulta con parámetros nombrados
find("powerLevel >= :level", Parameters.with("level", 80))

// Consulta con ordenamiento
find("powerLevel >= ?1 ORDER BY powerLevel DESC", 80)

// Consulta con paginación
find("powerLevel >= ?1", 80).page(0, 10)
```

## Comparación de Patrones

| Característica | Repository Pattern | Active Record Pattern |
|----------------|-------------------|---------------------|
| **Complejidad** | Más estructurado | Más simple |
| **Separación** | Entidad separada del acceso a datos | Todo en la entidad |
| **Testabilidad** | Fácil de mockear | Requiere más setup |
| **Uso recomendado** | Lógica compleja, múltiples fuentes | CRUD simple, prototipado rápido |
| **Familiaridad** | Similar a Spring Data | Similar a Rails Active Record |

## Validación

Para validar que Panache está funcionando:

1. Verificar que las entidades se crean correctamente
2. Verificar que los métodos mágicos funcionan
3. Verificar que las consultas Panache Query funcionan
4. Verificar que ambos patrones funcionan correctamente

```bash
# Verificar comparación de patrones
curl http://localhost:8080/api/panache/comparison
```

