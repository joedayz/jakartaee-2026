# Jakarta JSON Processing (JSON-P) Demo

## Descripción

Este demo muestra cómo usar **Jakarta JSON Processing (JSON-P)** en Quarkus para crear, parsear, transformar y consultar JSON de forma programática usando las APIs de Object Model y Streaming.

## Objetivo

Aprender a:
- Crear `JsonObject` y `JsonArray` programáticamente
- Parsear JSON strings usando `JsonReader`
- Escribir JSON con formato usando `JsonWriter`
- Usar `JsonPointer` para consultar JSON (RFC 6901)
- Usar `JsonPatch` para transformar JSON (RFC 6902)
- Usar Streaming API (`JsonParser`, `JsonGenerator`) para JSON grandes
- Filtrar y transformar estructuras JSON

## Tema DC

Procesamiento de datos de héroes de DC Comics:
- Creación de objetos JSON complejos con héroes y equipos
- Querying de datos JSON
- Transformaciones usando patches
- Streaming para procesar grandes volúmenes de datos

## Soporte en Quarkus

✅ **JSON-Processing está completamente soportado en Quarkus 3.30.2**.

Este demo usa:
- **Quarkus 3.30.2** con la nueva API REST (`quarkus-rest`)
- **Jakarta JSON Processing** a través de `quarkus-jsonp`
- **Object Model API** para manipulación de JSON
- **Streaming API** para procesamiento eficiente

## Dependencias

```xml
<!-- Jakarta RESTful Web Services -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- Jakarta JSON Processing -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jsonp</artifactId>
</dependency>
```

## Características de JSON-Processing Demostradas

### 1. Object Model API

#### Crear JsonObject

```java
JsonObjectBuilder builder = Json.createObjectBuilder();
builder.add("name", "Superman")
       .add("powerLevel", 95)
       .add("isActive", true);

// Objeto anidado
JsonObjectBuilder locationBuilder = Json.createObjectBuilder();
locationBuilder.add("city", "Metropolis");
builder.add("location", locationBuilder);

// Array
JsonArrayBuilder abilitiesBuilder = Json.createArrayBuilder();
abilitiesBuilder.add("Super strength").add("Flight");
builder.add("abilities", abilitiesBuilder);

JsonObject hero = builder.build();
```

#### Crear JsonArray

```java
JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

JsonObjectBuilder hero1 = Json.createObjectBuilder();
hero1.add("name", "Superman").add("powerLevel", 95);
arrayBuilder.add(hero1);

JsonObjectBuilder hero2 = Json.createObjectBuilder();
hero2.add("name", "Batman").add("powerLevel", 85);
arrayBuilder.add(hero2);

JsonArray heroes = arrayBuilder.build();
```

### 2. Parsing JSON

#### Parsear String a JsonObject

```java
String jsonString = "{\"name\":\"Superman\",\"powerLevel\":95}";
JsonReader reader = Json.createReader(new StringReader(jsonString));
JsonObject jsonObject = reader.readObject();
reader.close();
```

#### Parsear String a JsonArray

```java
String jsonArrayString = "[{\"name\":\"Superman\"},{\"name\":\"Batman\"}]";
JsonReader reader = Json.createReader(new StringReader(jsonArrayString));
JsonArray jsonArray = reader.readArray();
reader.close();
```

### 3. Writing JSON con Formato

```java
Map<String, Object> config = Map.of(JsonGenerator.PRETTY_PRINTING, true);
JsonWriterFactory factory = Json.createWriterFactory(config);
StringWriter writer = new StringWriter();
JsonWriter jsonWriter = factory.createWriter(writer);
jsonWriter.writeObject(jsonObject);
jsonWriter.close();
String formattedJson = writer.toString();
```

### 4. JsonPointer (RFC 6901) - Querying JSON

#### Consultar un valor

```java
JsonPointer pointer = Json.createPointer("/name");
JsonValue value = pointer.getValue(jsonObject);
// Retorna el valor en "/name"
```

#### Agregar un valor

```java
JsonPointer pointer = Json.createPointer("/newField");
JsonObject modified = pointer.add(jsonObject, Json.createValue("newValue"));
```

#### Eliminar un valor

```java
JsonPointer pointer = Json.createPointer("/fieldToRemove");
JsonObject modified = pointer.remove(jsonObject);
```

### 5. JsonPatch (RFC 6902) - Transformar JSON

#### Crear operaciones de patch

```java
JsonArrayBuilder patchBuilder = Json.createArrayBuilder();

// Operación: reemplazar
JsonObjectBuilder replaceOp = Json.createObjectBuilder();
replaceOp.add("op", "replace")
         .add("path", "/powerLevel")
         .add("value", 98);
patchBuilder.add(replaceOp);

// Operación: agregar
JsonObjectBuilder addOp = Json.createObjectBuilder();
addOp.add("op", "add")
     .add("path", "/newField")
     .add("value", "newValue");
patchBuilder.add(addOp);

JsonArray patchOperations = patchBuilder.build();
```

#### Aplicar patch

```java
JsonPatch patch = Json.createPatch(patchOperations);
JsonObject transformed = patch.apply(original);
```

### 6. Streaming API

#### JsonParser - Parsear JSON grande

```java
JsonParser parser = Json.createParser(new StringReader(jsonString));

while (parser.hasNext()) {
    JsonParser.Event event = parser.next();
    
    switch (event) {
        case KEY_NAME:
            String key = parser.getString();
            break;
        case VALUE_STRING:
            String value = parser.getString();
            break;
        case VALUE_NUMBER:
            if (parser.isIntegralNumber()) {
                long number = parser.getLong();
            } else {
                BigDecimal decimal = parser.getBigDecimal();
            }
            break;
        // ... más eventos
    }
}
parser.close();
```

#### JsonGenerator - Generar JSON grande

```java
Map<String, Object> config = Map.of(JsonGenerator.PRETTY_PRINTING, true);
JsonGenerator generator = Json.createGeneratorFactory(config)
    .createGenerator(writer);

generator.writeStartObject()
         .write("name", "Superman")
         .write("powerLevel", 95)
         .writeStartObject("location")
             .write("city", "Metropolis")
         .writeEnd()
         .writeStartArray("abilities")
             .write("Super strength")
             .write("Flight")
         .writeEnd()
         .writeEnd();

generator.close();
```

### 7. Filtrar y Transformar

#### Filtrar JsonArray

```java
JsonArrayBuilder filteredBuilder = Json.createArrayBuilder();

for (JsonValue heroValue : heroesArray) {
    if (heroValue.getValueType() == JsonValue.ValueType.OBJECT) {
        JsonObject hero = heroValue.asJsonObject();
        int powerLevel = hero.getInt("powerLevel", 0);
        
        if (powerLevel >= minPowerLevel) {
            filteredBuilder.add(hero);
        }
    }
}

JsonArray filtered = filteredBuilder.build();
```

#### Combinar JsonObjects

```java
JsonObjectBuilder mergedBuilder = Json.createObjectBuilder(obj1);

for (Map.Entry<String, JsonValue> entry : obj2.entrySet()) {
    mergedBuilder.add(entry.getKey(), entry.getValue());
}

JsonObject merged = mergedBuilder.build();
```

## Estructura del Proyecto

```
json-processing/
├── pom.xml
├── src/main/java/com/jakartaee/jsonprocessing/
│   ├── service/
│   │   └── JsonProcessingService.java    # Servicio con operaciones JSON-P
│   └── resource/
│       └── JsonProcessingResource.java   # REST endpoints
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
cd ../quarkus-demos/json-processing
mvn clean compile

# 3. Ejecuta el demo en modo desarrollo
mvn quarkus:dev
```

La aplicación estará disponible en: `http://localhost:8080`

## Endpoints Disponibles

### Object Model API

- `GET /api/json-processing/hero/object` - Crea un JsonObject programáticamente
- `GET /api/json-processing/heroes/array` - Crea un JsonArray de héroes
- `GET /api/json-processing/hero/object/formatted` - JsonObject formateado como string
- `POST /api/json-processing/parse` - Parsea JSON string a JsonObject
- `GET /api/json-processing/complex` - Crea JSON complejo con múltiples niveles
- `GET /api/json-processing/complex/formatted` - JSON complejo formateado

### JsonPointer (Querying)

- `GET /api/json-processing/query?path=/name` - Consulta JSON usando JsonPointer
- `PATCH /api/json-processing/modify?path=/powerLevel&value=98` - Modifica JSON usando JsonPointer

### JsonPatch (Transformations)

- `POST /api/json-processing/transform` - Transforma JSON usando JsonPatch
- `GET /api/json-processing/patch/example` - Obtiene operaciones de patch de ejemplo

### Streaming API

- `POST /api/json-processing/stream/parse` - Parsea JSON usando streaming (JsonParser)
- `GET /api/json-processing/stream/generate` - Genera JSON usando streaming (JsonGenerator)

### Filtrado

- `GET /api/json-processing/heroes/filter?minPowerLevel=90` - Filtra héroes por nivel de poder

### Información

- `GET /api/json-processing/info` - Información sobre características demostradas

## Ejemplos de Uso

### Crear JsonObject

```bash
curl http://localhost:8080/api/json-processing/hero/object
```

### Parsear JSON String

```bash
curl -X POST http://localhost:8080/api/json-processing/parse \
  -H "Content-Type: text/plain" \
  -d '{"name":"Superman","powerLevel":95}'
```

### Query JSON

```bash
curl "http://localhost:8080/api/json-processing/query?path=/name"
```

### Modificar JSON

```bash
curl -X PATCH "http://localhost:8080/api/json-processing/modify?path=/powerLevel&value=98"
```

### Transformar con JsonPatch

```bash
curl -X POST http://localhost:8080/api/json-processing/transform \
  -H "Content-Type: application/json" \
  -d '[
    {
      "op": "replace",
      "path": "/powerLevel",
      "value": 98
    },
    {
      "op": "add",
      "path": "/newField",
      "value": "newValue"
    }
  ]'
```

### Generar JSON con Streaming

```bash
curl http://localhost:8080/api/json-processing/stream/generate
```

### Filtrar Héroes

```bash
curl "http://localhost:8080/api/json-processing/heroes/filter?minPowerLevel=90"
```

## Características Clave de JSON-Processing en Quarkus

### ✅ Object Model API

- `JsonObject` y `JsonArray` para manipulación de JSON
- Builders para crear estructuras complejas
- Lectura y escritura con formato

### ✅ Streaming API

- `JsonParser` para parsear JSON grandes eficientemente
- `JsonGenerator` para generar JSON grandes eficientemente
- Útil para archivos grandes o streams

### ✅ Querying y Transformations

- `JsonPointer` para consultar JSON (RFC 6901)
- `JsonPatch` para transformar JSON (RFC 6902)
- Operaciones estándar de la industria

### ✅ Flexibilidad

- Control total sobre la estructura JSON
- Útil cuando JSON-Binding no es suficiente
- Ideal para transformaciones complejas

## Comparación: JSON-Processing vs JSON-Binding

| Característica | JSON-Processing | JSON-Binding |
|----------------|-----------------|--------------|
| Propósito | Manipulación programática | Serialización automática |
| API | JsonObject, JsonArray | Anotaciones en clases Java |
| Uso | Cuando necesitas control total | Cuando trabajas con objetos Java |
| Streaming | Sí (JsonParser/Generator) | No |
| Querying | Sí (JsonPointer) | No |
| Transformations | Sí (JsonPatch) | No |

## Notas

- JSON-Processing es útil cuando necesitas manipular JSON directamente
- JSON-Binding es mejor cuando trabajas con objetos Java
- Puedes usar ambos en el mismo proyecto según necesites
- Streaming API es eficiente para JSON grandes

## Referencias

- [Jakarta JSON Processing Specification](https://jakarta.ee/specifications/jsonp/)
- [RFC 6901 - JavaScript Object Notation (JSON) Pointer](https://tools.ietf.org/html/rfc6901)
- [RFC 6902 - JavaScript Object Notation (JSON) Patch](https://tools.ietf.org/html/rfc6902)

