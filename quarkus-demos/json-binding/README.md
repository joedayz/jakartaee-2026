# Jakarta JSON Binding (JSON-B) Demo

## Descripción

Este demo muestra cómo usar **Jakarta JSON Binding (JSON-B)** en Quarkus para serializar y deserializar objetos Java a/desde JSON de forma automática y manual.

## Objetivo

Aprender a:
- Serializar/deserializar objetos Java a/desde JSON automáticamente
- Usar anotaciones JSON-B (`@JsonbProperty`, `@JsonbTransient`, `@JsonbDateFormat`, etc.)
- Crear Custom Adapters para conversiones personalizadas
- Configurar Jsonb con opciones personalizadas
- Manejar objetos anidados y colecciones
- Serialización/deserialización manual cuando sea necesario

## Tema DC

Gestión de Teams de héroes de DC Comics:
- **Team**: Equipo con miembros, ubicación y fechas
- **TeamMember**: Miembros del equipo con roles y habilidades
- **Location**: Ubicación con coordenadas
- **HeroWithCustomAdapter**: Héroe con adapter personalizado para nivel de poder

## Soporte en Quarkus

✅ **JSON-Binding está completamente soportado en Quarkus 3.30.2**.

Este demo usa:
- **Quarkus 3.30.2** con la nueva API REST (`quarkus-rest`)
- **Jakarta JSON Binding** a través de `quarkus-jsonb`
- **Serialización automática** en endpoints REST
- **Serialización manual** usando `Jsonb` directamente

## Dependencias

```xml
<!-- Jakarta RESTful Web Services -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- Jakarta JSON Binding -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jsonb</artifactId>
</dependency>
```

## Características de JSON-Binding Demostradas

### 1. Serialización Automática en REST

Quarkus serializa/deserializa automáticamente objetos Java en endpoints REST:

```java
@GET
@Path("/team/example")
public Response getExampleTeam() {
    Team team = jsonBindingService.createExampleTeam();
    return Response.ok(team).build(); // Se serializa automáticamente
}

@POST
@Path("/team")
public Response createTeam(Team team) {
    // El team ya está deserializado automáticamente desde JSON
    return Response.ok(team).build();
}
```

### 2. Anotaciones JSON-B

#### @JsonbProperty

Renombra campos en JSON:

```java
@JsonbProperty("team_id")
private Long id;

@JsonbProperty("team_name")
private String name;
```

#### @JsonbTransient

Excluye campos de la serialización:

```java
@JsonbTransient
private String internalNotes; // No aparece en JSON
```

#### @JsonbDateFormat

Formatea fechas:

```java
@JsonbProperty("formation_date")
@JsonbDateFormat("yyyy-MM-dd")
private LocalDate formationDate;
```

#### @JsonbPropertyOrder

Ordena campos en JSON:

```java
@JsonbPropertyOrder({"role", "hero_name", "power_level"})
public class TeamMember {
    // ...
}
```

### 3. Custom Adapters

Crea adapters personalizados para conversiones complejas:

```java
@JsonbTypeAdapter(PowerLevelAdapter.class)
private Integer powerLevel;
```

El adapter convierte el nivel de poder a un objeto complejo:

```java
public class PowerLevelAdapter implements JsonbAdapter<Integer, PowerLevelDTO> {
    @Override
    public PowerLevelDTO adaptToJson(Integer powerLevel) {
        PowerLevelDTO dto = new PowerLevelDTO();
        dto.value = powerLevel;
        dto.category = categorizePowerLevel(powerLevel);
        dto.description = getPowerDescription(powerLevel);
        return dto;
    }
    
    @Override
    public Integer adaptFromJson(PowerLevelDTO dto) {
        return dto.value;
    }
}
```

### 4. Configuración Personalizada

Configura Jsonb con opciones personalizadas:

```java
JsonbConfig config = new JsonbConfig()
    .withPropertyOrderStrategy(PropertyOrderStrategy.LEXICOGRAPHICAL)
    .withNullValues(true)
    .withFormatting(true);

Jsonb jsonb = JsonbBuilder.create(config);
String json = jsonb.toJson(team);
```

### 5. Serialización/Deserialización Manual

Cuando necesites control total:

```java
// Serializar
Jsonb jsonb = JsonbBuilder.create();
String json = jsonb.toJson(team);

// Deserializar
Team team = jsonb.fromJson(json, Team.class);
```

### 6. Objetos Anidados y Colecciones

JSON-B maneja automáticamente objetos anidados y colecciones:

```java
public class Team {
    @JsonbProperty("members")
    private List<TeamMember> members;
    
    @JsonbProperty("headquarters")
    private Location headquarters;
}
```

## Estructura del Proyecto

```
json-binding/
├── pom.xml
├── src/main/java/com/jakartaee/jsonbinding/
│   ├── model/
│   │   ├── Team.java                    # Modelo principal con anotaciones
│   │   ├── TeamMember.java              # Miembro con @JsonbPropertyOrder
│   │   ├── Location.java                # Objeto anidado
│   │   └── HeroWithCustomAdapter.java   # Héroe con adapter personalizado
│   ├── adapter/
│   │   └── PowerLevelAdapter.java       # Custom adapter
│   ├── service/
│   │   └── JsonBindingService.java      # Servicio con operaciones JSON-B
│   └── resource/
│       └── JsonBindingResource.java     # REST endpoints
└── README.md
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
cd ../quarkus-demos/json-binding
mvn clean compile

# 3. Ejecuta el demo en modo desarrollo
mvn quarkus:dev
```

La aplicación estará disponible en: `http://localhost:8080`

## Endpoints Disponibles

### Teams

- `GET /api/json-binding/team/example` - Obtiene un team de ejemplo (serialización automática)
- `GET /api/json-binding/teams/example` - Obtiene múltiples teams
- `POST /api/json-binding/team` - Crea un team desde JSON (deserialización automática)
- `GET /api/json-binding/team/{id}/serialize` - Serializa manualmente a JSON string
- `POST /api/json-binding/team/deserialize` - Deserializa JSON string manualmente
- `GET /api/json-binding/team/{id}/custom-config` - Serializa con configuración personalizada

### Heroes con Custom Adapter

- `GET /api/json-binding/hero/adapter-example` - Obtiene héroe con adapter (serialización automática)
- `GET /api/json-binding/hero/adapter-example/serialize` - Serializa manualmente con adapter

### Información

- `GET /api/json-binding/info` - Información sobre características demostradas

## Ejemplos de Uso

### Crear un Team

```bash
curl -X POST http://localhost:8080/api/json-binding/team \
  -H "Content-Type: application/json" \
  -d '{
    "team_name": "Justice League",
    "description": "El equipo más poderoso",
    "formation_date": "1960-02-01",
    "active": true,
    "members": [
      {
        "hero_name": "Superman",
        "power_level": 95,
        "role": "Leader"
      }
    ],
    "headquarters": {
      "city": "Washington D.C.",
      "country": "USA"
    }
  }'
```

### Obtener Team de Ejemplo

```bash
curl http://localhost:8080/api/json-binding/team/example
```

### Serializar Manualmente

```bash
curl http://localhost:8080/api/json-binding/team/1/serialize
```

### Deserializar Manualmente

```bash
curl -X POST http://localhost:8080/api/json-binding/team/deserialize \
  -H "Content-Type: text/plain" \
  -d '{"team_name":"Justice League","description":"El equipo más poderoso"}'
```

## Características Clave de JSON-Binding en Quarkus

### ✅ Integración Automática

- Serialización/deserialización automática en endpoints REST
- No necesitas código adicional para convertir objetos Java ↔ JSON
- Soporte completo para anotaciones JSON-B

### ✅ Flexibilidad

- Serialización manual cuando necesites control total
- Custom adapters para conversiones complejas
- Configuración personalizada de Jsonb

### ✅ Anotaciones Poderosas

- `@JsonbProperty` - Renombrar campos
- `@JsonbTransient` - Excluir campos
- `@JsonbDateFormat` - Formatear fechas
- `@JsonbPropertyOrder` - Ordenar campos
- `@JsonbTypeAdapter` - Adapters personalizados

## Notas

- JSON-Binding usa Yasson (implementación de referencia) en Quarkus
- La serialización automática funciona con cualquier objeto Java
- Los adapters permiten convertir tipos complejos
- La configuración personalizada es útil para casos específicos

## Referencias

- [Jakarta JSON Binding Specification](https://jakarta.ee/specifications/jsonb/)
- [Quarkus JSON-B Guide](https://quarkus.io/guides/rest-json#json-b)

