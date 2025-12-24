# Jakarta NoSQL (MongoDB) Demo con Dev Services

## Descripción

Este demo muestra cómo usar **MongoDB con Panache MongoDB** en Quarkus para trabajar con bases de datos NoSQL. Demuestra características específicas de MongoDB como documentos anidados, arrays y queries flexibles, todo usando **Dev Services** para configuración automática.

## Objetivo

Aprender a:
- Usar Panache MongoDB para acceso a datos NoSQL
- Trabajar con documentos anidados
- Manejar arrays de valores y documentos
- Usar Dev Services para MongoDB automático
- Realizar queries específicas de MongoDB
- Entender las diferencias entre SQL y NoSQL

## Tema DC

Gestión de Heroes y Villains usando MongoDB:
- **HeroMongo**: Documentos con ubicaciones anidadas, arrays de habilidades y misiones
- **VillainMongo**: Documentos con bases secretas anidadas y aliados
- Estructuras complejas que aprovechan la flexibilidad de MongoDB

## Soporte en Quarkus

✅ **MongoDB está completamente soportado en Quarkus 3.30.2** a través de Panache MongoDB.

Este demo usa:
- **Quarkus 3.30.2** con la nueva API REST (`quarkus-rest`)
- **Panache MongoDB** para acceso simplificado a MongoDB
- **Dev Services** para iniciar MongoDB automáticamente
- **Active Record Pattern** y **Repository Pattern**

## Dev Services: La Magia de Quarkus

**Dev Services** inicia automáticamente un contenedor MongoDB cuando ejecutas `mvn quarkus:dev`. No necesitas:
- Instalar MongoDB manualmente
- Configurar conexiones
- Gestionar contenedores

Solo necesitas tener Docker instalado y funcionando.

### ⚠️ Configuración Importante

**NO configures `quarkus.mongodb.connection-string` explícitamente** si quieres que Dev Services funcione automáticamente. Si lo configuras, Dev Services se deshabilitará.

## Dependencias

```xml
<!-- Jakarta RESTful Web Services -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- MongoDB with Panache -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-mongodb-panache</artifactId>
</dependency>
```

## Configuración

### application.properties

```properties
# MongoDB Configuration
# IMPORTANTE: NO configures quarkus.mongodb.connection-string explícitamente
# si quieres que Dev Services funcione automáticamente.
# Dev Services detectará automáticamente si MongoDB está corriendo o iniciará uno.

# MongoDB Database name
quarkus.mongodb.database=dcuniverse

# Dev Services Configuration
quarkus.mongodb.devservices.enabled=true
```

**Nota Importante**: 
- **Con Dev Services**: NO configures `quarkus.mongodb.connection-string`. Dev Services iniciará MongoDB automáticamente si Docker está disponible.
- **Sin Dev Services**: Si prefieres usar MongoDB manual, configura `quarkus.mongodb.connection-string=mongodb://localhost:27017` y deshabilita Dev Services.

## Características de MongoDB Demostradas

### 1. Documentos Anidados

MongoDB permite documentos dentro de documentos:

```java
@MongoEntity(collection = "heroes")
public class HeroMongo extends PanacheMongoEntity {
    public Location location; // Documento anidado
    
    public static class Location {
        public String city;
        public String planet;
        public Coordinates coordinates; // Documento anidado dentro de anidado
        
        public static class Coordinates {
            public Double latitude;
            public Double longitude;
        }
    }
}
```

### 2. Arrays de Valores

```java
public class HeroMongo extends PanacheMongoEntity {
    public List<String> abilities = new ArrayList<>(); // Array de strings
}
```

### 3. Arrays de Documentos Anidados

```java
public class HeroMongo extends PanacheMongoEntity {
    public List<Mission> missions = new ArrayList<>(); // Array de documentos
    
    public static class Mission {
        public String title;
        public String description;
        public String status;
    }
}
```

### 4. Queries en Documentos Anidados

```java
// Buscar por ciudad (documento anidado)
public List<HeroMongo> findByCity(String city) {
    return find("location.city", city).list();
}

// Buscar por coordenadas
public List<HeroMongo> findByCoordinates(double lat, double lon) {
    return find("location.coordinates.latitude", lat).list();
}
```

### 5. Queries en Arrays

```java
// Buscar héroes que tengan una habilidad específica
public List<HeroMongo> findByAbility(String ability) {
    return find("abilities", ability).list();
}

// Buscar héroes con misiones pendientes (array de documentos)
public List<HeroMongo> findWithPendingMissions() {
    return find("missions.status", "PENDING").list();
}
```

### 6. Active Record Pattern

```java
@MongoEntity(collection = "heroes")
public class HeroMongo extends PanacheMongoEntity {
    public String name;
    public Integer powerLevel;
    
    // Métodos de instancia
    public void activate() {
        this.isActive = true;
        this.update();
    }
    
    // Métodos estáticos
    public static List<HeroMongo> findPowerful(int minLevel) {
        return find("powerLevel >= ?1", minLevel).list();
    }
}
```

### 7. Repository Pattern

```java
@ApplicationScoped
public class HeroMongoRepository implements PanacheMongoRepository<HeroMongo> {
    
    public List<HeroMongo> findPowerful(int minPowerLevel) {
        return find("powerLevel >= ?1", minPowerLevel).list();
    }
    
    public List<HeroMongo> findByAbility(String ability) {
        return find("abilities", ability).list();
    }
}
```

## Estructura del Proyecto

```
nosql/
├── pom.xml
├── src/main/java/com/jakartaee/nosql/
│   ├── entity/
│   │   ├── HeroMongo.java          # Entidad con documentos anidados y arrays
│   │   └── VillainMongo.java       # Entidad con estructuras complejas
│   ├── repository/
│   │   ├── HeroMongoRepository.java    # Repositorio con queries MongoDB
│   │   └── VillainMongoRepository.java  # Repositorio para villanos
│   ├── service/
│   │   └── NoSQLService.java       # Servicio con operaciones NoSQL
│   ├── resource/
│   │   └── NoSQLResource.java      # REST endpoints
│   └── config/
│       └── DataInitializer.java    # Inicialización de datos
└── src/main/resources/
    └── application.properties       # Configuración con Dev Services
```

## Cómo Ejecutar

### Requisitos Previos

- Java 21
- Maven 3.9+
- **Docker** (para Dev Services) - Debe estar corriendo

### Pasos

```bash
# 1. Asegúrate de que Docker esté corriendo
docker ps

# 2. Compila el proyecto
cd quarkus-demos/nosql
mvn clean compile

# 3. Ejecuta el demo en modo desarrollo
mvn quarkus:dev
```

**¡Eso es todo!** Dev Services iniciará MongoDB automáticamente. Verás en los logs:

```
Dev Services for MongoDB started.
```

La aplicación estará disponible en: `http://localhost:8080`

### Si Docker no está disponible

Si no tienes Docker o prefieres usar MongoDB manualmente:

1. Instala MongoDB localmente o usa un servicio en la nube
2. Configura en `application.properties`:
   ```properties
   quarkus.mongodb.connection-string=mongodb://localhost:27017
   quarkus.mongodb.devservices.enabled=false
   ```

## Endpoints Disponibles

### Heroes

- `GET /api/nosql/heroes` - Obtiene todos los héroes
- `GET /api/nosql/heroes/{id}` - Obtiene un héroe por ID (ObjectId)
- `POST /api/nosql/heroes` - Crea un héroe
- `PUT /api/nosql/heroes/{id}` - Actualiza un héroe
- `DELETE /api/nosql/heroes/{id}` - Elimina un héroe
- `GET /api/nosql/heroes/powerful?minLevel=80` - Busca héroes poderosos
- `GET /api/nosql/heroes/active` - Busca héroes activos
- `GET /api/nosql/heroes/search?name=Super` - Búsqueda por nombre
- `GET /api/nosql/heroes/by-ability?ability=Flight` - Busca por habilidad (query en array)
- `GET /api/nosql/heroes/by-city?city=Metropolis` - Busca por ciudad (query en documento anidado)
- `GET /api/nosql/heroes/with-pending-missions` - Busca con misiones pendientes (query en array de documentos)
- `PUT /api/nosql/heroes/{id}/abilities` - Agrega habilidad a un héroe
- `POST /api/nosql/heroes/{id}/missions` - Agrega misión a un héroe
- `GET /api/nosql/heroes/statistics` - Estadísticas de héroes

### Villains

- `POST /api/nosql/villains` - Crea un villano
- `GET /api/nosql/villains/dangerous` - Busca villanos peligrosos
- `GET /api/nosql/villains/threat-level/{level}` - Busca por nivel de amenaza

### Información

- `GET /api/nosql/info` - Información sobre características demostradas

## Ejemplos de Uso

### Crear un Héroe con Estructura Compleja

```bash
curl -X POST http://localhost:8080/api/nosql/heroes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Aquaman",
    "power": "Control del agua",
    "powerLevel": 82,
    "description": "Rey de Atlantis",
    "abilities": ["Water control", "Underwater breathing", "Super strength"],
    "location": {
      "city": "Atlantis",
      "planet": "Earth",
      "coordinates": {
        "latitude": 0.0,
        "longitude": 0.0
      }
    },
    "missions": [
      {
        "title": "Proteger Atlantis",
        "description": "Defender el reino submarino",
        "status": "IN_PROGRESS"
      }
    ]
  }'
```

### Buscar Héroes por Habilidad

```bash
curl "http://localhost:8080/api/nosql/heroes/by-ability?ability=Flight"
```

### Buscar Héroes por Ciudad

```bash
curl "http://localhost:8080/api/nosql/heroes/by-city?city=Metropolis"
```

### Agregar Habilidad a un Héroe

```bash
curl -X PUT http://localhost:8080/api/nosql/heroes/{id}/abilities \
  -H "Content-Type: application/json" \
  -d '{"ability": "Super hearing"}'
```

### Agregar Misión a un Héroe

```bash
curl -X POST http://localhost:8080/api/nosql/heroes/{id}/missions \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Nueva misión",
    "description": "Descripción de la misión",
    "status": "PENDING"
  }'
```

## Características Clave de MongoDB en Quarkus

### ✅ Dev Services

- MongoDB se inicia automáticamente
- No necesitas configuración manual
- Perfecto para desarrollo y testing
- **IMPORTANTE**: No configures `connection-string` si quieres Dev Services

### ✅ Panache MongoDB

- Active Record Pattern
- Repository Pattern
- Queries simplificadas
- Menos código boilerplate

### ✅ Flexibilidad de NoSQL

- Documentos anidados
- Arrays de valores y documentos
- Esquema flexible
- Queries en estructuras complejas

### ✅ ObjectId

- Identificador único de MongoDB
- Generado automáticamente
- Type-safe con Panache

## Comparación: SQL vs NoSQL

| Característica | SQL (JPA) | NoSQL (MongoDB) |
|----------------|-----------|-----------------|
| Estructura | Tablas fijas | Documentos flexibles |
| Relaciones | JOINs explícitos | Documentos anidados |
| Arrays | Tablas separadas | Arrays nativos |
| Esquema | Fijo | Flexible |
| Queries | SQL/JPQL | Queries de documentos |
| Escalabilidad | Vertical | Horizontal |

## Ventajas de MongoDB

1. **Flexibilidad**: Esquema flexible, documentos pueden variar
2. **Performance**: Mejor para lectura de documentos completos
3. **Escalabilidad**: Escala horizontalmente fácilmente
4. **Estructuras Complejas**: Documentos anidados y arrays nativos

## Cuándo Usar MongoDB

- Datos con estructura variable
- Documentos complejos con anidación
- Escalabilidad horizontal importante
- Lecturas frecuentes de documentos completos
- Datos que no requieren transacciones complejas

## Dev Services en Detalle

Dev Services detecta automáticamente:
- Si MongoDB está corriendo localmente
- Si no está corriendo, inicia un contenedor Docker
- Configura la conexión automáticamente
- Limpia el contenedor al detener la aplicación

**Credenciales por defecto de Dev Services:**
- **Host**: localhost
- **Port**: 27017 (o el que Dev Services asigne)
- **Database**: dcuniverse (configurado en application.properties)

**⚠️ Importante**: Si configuras `quarkus.mongodb.connection-string` explícitamente, Dev Services se deshabilitará. Deja que Dev Services configure la conexión automáticamente.

## Troubleshooting

### MongoDB no se inicia automáticamente

1. **Verifica que Docker esté corriendo**:
   ```bash
   docker ps
   ```

2. **Verifica que Dev Services esté habilitado**:
   ```properties
   quarkus.mongodb.devservices.enabled=true
   ```

3. **Asegúrate de NO tener `quarkus.mongodb.connection-string` configurado**:
   Si lo tienes, elimínalo o coméntalo.

4. **Si Docker no está disponible**, instala MongoDB manualmente y configura:
   ```properties
   quarkus.mongodb.connection-string=mongodb://localhost:27017
   quarkus.mongodb.devservices.enabled=false
   ```

## Notas

- MongoDB usa ObjectId como identificador (no Long como en JPA)
- Los documentos pueden tener estructuras diferentes
- Las queries son case-sensitive por defecto
- Dev Services solo funciona en modo desarrollo
- Para producción, configura MongoDB manualmente

## Conclusión

MongoDB con Panache MongoDB en Quarkus proporciona acceso simple a bases de datos NoSQL. Dev Services elimina la configuración manual, mientras que Panache MongoDB simplifica el código. La flexibilidad de MongoDB es perfecta para datos con estructuras complejas y variables.

## Recursos

- [Demo completo](../nosql/)
- [Quarkus MongoDB Panache Guide](https://quarkus.io/guides/mongodb-panache)
- [Quarkus Dev Services](https://quarkus.io/guides/dev-services)
- [MongoDB Documentation](https://www.mongodb.com/docs/)
