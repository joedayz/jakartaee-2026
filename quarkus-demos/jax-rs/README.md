# Jakarta RESTful Web Services (JAX-RS) Demo

## Descripción

Este demo muestra cómo usar **Jakarta RESTful Web Services (JAX-RS)** para crear una API REST que gestiona Heroes y Villanos de DC Comics.

## Objetivo

Aprender a:
- Crear recursos REST con JAX-RS
- Usar anotaciones `@Path`, `@GET`, `@POST`, `@PUT`, `@DELETE`
- Manejar parámetros de path y query
- Devolver respuestas JSON
- Manejar códigos de estado HTTP

## Tema DC

API REST completa para gestionar:
- **Heroes**: Superman, Batman, Wonder Woman, Flash, etc.
- **Villains**: Joker, Lex Luthor, Darkseid, etc.

## Endpoints

- `GET /api/heroes` - Listar todos los héroes
- `GET /api/heroes/{id}` - Obtener un héroe por ID
- `POST /api/heroes` - Crear un nuevo héroe
- `PUT /api/heroes/{id}` - Actualizar un héroe
- `DELETE /api/heroes/{id}` - Eliminar un héroe
- `GET /api/villains` - Listar todos los villanos
- `GET /api/villains/{id}` - Obtener un villano por ID
- `POST /api/villains` - Crear un nuevo villano

## Dependencias

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>
```

**Nota**: Este demo usa `quarkus-rest` (nueva API REST de Quarkus 3.x) en lugar de `resteasy-reactive`. 
Las anotaciones JAX-RS estándar siguen funcionando igual.

## Cómo Ejecutar

```bash
cd quarkus-demos/jax-rs
mvn quarkus:dev
```

La aplicación estará disponible en: `http://localhost:8080`

## Ejemplos de Uso

### Listar todos los héroes
```bash
curl http://localhost:8080/api/heroes
```

### Obtener un héroe específico
```bash
curl http://localhost:8080/api/heroes/1
```

### Crear un nuevo héroe
```bash
curl -X POST http://localhost:8080/api/heroes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Superman",
    "power": "Super fuerza, vuelo, visión de rayos X",
    "powerLevel": 95
  }'
```

### Actualizar un héroe
```bash
curl -X PUT http://localhost:8080/api/heroes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Superman",
    "power": "Super fuerza, vuelo, visión de rayos X, super velocidad",
    "powerLevel": 98
  }'
```

### Eliminar un héroe
```bash
curl -X DELETE http://localhost:8080/api/heroes/1
```

## Validación

Para validar que JAX-RS está funcionando:

1. Verificar que los endpoints responden correctamente
2. Verificar que el contenido JSON se serializa/deserializa correctamente
3. Verificar códigos de estado HTTP (200, 201, 404, etc.)

```bash
# Verificar que la spec está disponible
curl http://localhost:8080/specs/check/jax-rs
```

## Estructura del Código

```
jax-rs/
├── src/main/java/com/jakartaee/jaxrs/
│   ├── HeroResource.java      # Recurso REST para Heroes
│   ├── VillainResource.java    # Recurso REST para Villains
│   └── service/
│       ├── HeroService.java    # Lógica de negocio para Heroes
│       └── VillainService.java # Lógica de negocio para Villains
└── pom.xml
```

