# Bean Validation Demo

## Descripción

Este demo muestra cómo usar **Jakarta Bean Validation** (también conocido como Jakarta Validation) para validar datos en diferentes niveles de una aplicación Jakarta EE/Quarkus.

## Objetivo

Aprender a:
- Usar validaciones estándar de Bean Validation (`@NotNull`, `@NotBlank`, `@Size`, `@Email`, etc.)
- Crear validadores personalizados
- Validar parámetros de métodos REST (`@PathParam`, `@QueryParam`)
- Validar DTOs usando `@Valid`
- Validar entidades JPA
- Manejar errores de validación con `ExceptionMapper`
- Usar validación programática con `Validator`

## Tema DC

Validaciones para gestionar Heroes de DC Comics:
- **HeroCreateDTO**: DTO con validaciones para crear héroes
- **HeroUpdateDTO**: DTO con validaciones para actualizar héroes
- **Validadores personalizados**: `ValidPowerLevel`, `ValidHeroName`
- **ExceptionMapper**: Manejo centralizado de errores de validación

## Soporte en Quarkus

✅ **Bean Validation está completamente soportado en Quarkus 3.30.2** a través de Hibernate Validator.

Este demo usa:
- **Quarkus 3.30.2** con la nueva API REST (`quarkus-rest`)
- **Hibernate Validator** (implementación de Bean Validation)
- **Validación automática** en endpoints REST
- **Validación programática** usando `Validator`

## Dependencias

```xml
<!-- Jakarta RESTful Web Services - Nueva API REST de Quarkus -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- Bean Validation (Hibernate Validator) -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-validator</artifactId>
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

### 1. Validaciones Estándar

#### En DTOs

```java
public record HeroCreateDTO(
    @NotBlank(message = "El nombre del héroe es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @ValidHeroName(message = "El nombre del héroe no es válido")
    String name,
    
    @NotBlank(message = "El poder es requerido")
    @Size(min = 3, max = 200, message = "El poder debe tener entre 3 y 200 caracteres")
    String power,
    
    @NotNull(message = "El nivel de poder es requerido")
    @ValidPowerLevel(message = "El nivel de poder debe estar entre 1 y 100")
    Integer powerLevel,
    
    @Email(message = "El email debe tener un formato válido")
    String email
) {}
```

#### En Parámetros de Métodos REST

```java
@GET
@Path("/{id}")
public Response getHero(
    @PathParam("id") 
    @NotNull(message = "El ID es requerido")
    @Min(value = 1, message = "El ID debe ser mayor que 0")
    Long id) {
    // ...
}

@GET
@Path("/search")
public Response searchHeroesByName(
    @QueryParam("name") 
    @NotBlank(message = "El parámetro 'name' es requerido")
    String name) {
    // ...
}
```

### 2. Validadores Personalizados

#### Anotación Personalizada

```java
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PowerLevelValidator.class)
@Documented
public @interface ValidPowerLevel {
    String message() default "El nivel de poder debe estar entre 1 y 100";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

#### Implementación del Validador

```java
public class PowerLevelValidator implements ConstraintValidator<ValidPowerLevel, Integer> {
    
    @Override
    public boolean isValid(Integer powerLevel, ConstraintValidatorContext context) {
        if (powerLevel == null) {
            return true; // @NotNull ya maneja valores nulos
        }
        
        boolean isValid = powerLevel >= 1 && powerLevel <= 100;
        
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                String.format("El nivel de poder debe estar entre 1 y 100, pero se recibió: %d", powerLevel)
            ).addConstraintViolation();
        }
        
        return isValid;
    }
}
```

### 3. Validación Automática en REST Endpoints

Quarkus valida automáticamente los parámetros y DTOs marcados con `@Valid`:

```java
@POST
public Response createHero(@Valid HeroCreateDTO dto) {
    // Si el DTO no es válido, se lanza ConstraintViolationException
    // que es capturada por ValidationExceptionMapper
    Hero hero = heroService.createHero(dto);
    return Response.status(Response.Status.CREATED).entity(hero).build();
}
```

### 4. Validación Programática

Usando `Validator` inyectado para validar objetos manualmente:

```java
@ApplicationScoped
public class HeroService {
    
    @Inject
    Validator validator;
    
    @Transactional
    public Hero createHero(HeroCreateDTO dto) {
        // Validación programática
        Set<ConstraintViolation<HeroCreateDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        
        // Convertir DTO a entidad y validar
        Hero hero = new Hero();
        hero.setName(dto.name());
        // ...
        
        Set<ConstraintViolation<Hero>> entityViolations = validator.validate(hero);
        if (!entityViolations.isEmpty()) {
            throw new ConstraintViolationException(entityViolations);
        }
        
        entityManager.persist(hero);
        return hero;
    }
}
```

### 5. Manejo de Errores de Validación

`ExceptionMapper` personalizado para convertir errores de validación en respuestas HTTP estructuradas:

```java
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<Map<String, Object>> violations = exception.getConstraintViolations().stream()
                .map(this::violationToMap)
                .collect(Collectors.toList());
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Errores de validación");
        errorResponse.put("violations", violations);
        errorResponse.put("violationCount", violations.size());
        
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
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
cd ../quarkus-demos/bean-validation
mvn clean compile

# 3. Ejecuta el demo en modo desarrollo
mvn quarkus:dev
```

La aplicación estará disponible en: http://localhost:8080

## Endpoints Disponibles

### GET /api/heroes
Lista todos los héroes.

**Ejemplo:**
```bash
curl http://localhost:8080/api/heroes
```

### GET /api/heroes/{id}
Obtiene un héroe por ID (con validación en `@PathParam`).

**Ejemplo válido:**
```bash
curl http://localhost:8080/api/heroes/1
```

**Ejemplo inválido (debe retornar error 400):**
```bash
curl http://localhost:8080/api/heroes/0
```

### GET /api/heroes/search?name={name}
Busca héroes por nombre (con validación en `@QueryParam`).

**Ejemplo válido:**
```bash
curl "http://localhost:8080/api/heroes/search?name=Super"
```

**Ejemplo inválido (debe retornar error 400):**
```bash
curl "http://localhost:8080/api/heroes/search?name="
```

### GET /api/heroes/active
Lista todos los héroes activos.

**Ejemplo:**
```bash
curl http://localhost:8080/api/heroes/active
```

### POST /api/heroes
Crea un nuevo héroe (con validación en DTO).

**Ejemplo válido:**
```bash
curl -X POST http://localhost:8080/api/heroes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Aquaman",
    "power": "Control del agua, comunicación con vida marina",
    "powerLevel": 85,
    "description": "Rey de Atlantis",
    "email": "aquaman@dc.com"
  }'
```

**Ejemplos inválidos (deben retornar error 400 con detalles):**

Nombre vacío:
```bash
curl -X POST http://localhost:8080/api/heroes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "power": "Control del agua",
    "powerLevel": 85
  }'
```

Nivel de poder inválido:
```bash
curl -X POST http://localhost:8080/api/heroes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Aquaman",
    "power": "Control del agua",
    "powerLevel": 150
  }'
```

Email inválido:
```bash
curl -X POST http://localhost:8080/api/heroes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Aquaman",
    "power": "Control del agua",
    "powerLevel": 85,
    "email": "email-invalido"
  }'
```

Nombre con solo números (validador personalizado):
```bash
curl -X POST http://localhost:8080/api/heroes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "12345",
    "power": "Control del agua",
    "powerLevel": 85
  }'
```

### PUT /api/heroes/{id}
Actualiza un héroe existente (con validación en DTO y `@PathParam`).

**Ejemplo válido:**
```bash
curl -X PUT http://localhost:8080/api/heroes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "Superman Updated",
    "power": "Super fuerza, vuelo, visión de rayos X, super velocidad",
    "powerLevel": 98,
    "description": "El último hijo de Krypton - Actualizado"
  }'
```

### DELETE /api/heroes/{id}
Elimina un héroe (con validación en `@PathParam`).

**Ejemplo:**
```bash
curl -X DELETE http://localhost:8080/api/heroes/1
```

## Respuesta de Error de Validación

Cuando hay errores de validación, la respuesta tiene el siguiente formato:

```json
{
  "message": "Errores de validación",
  "violations": [
    {
      "field": "name",
      "message": "El nombre del héroe es requerido",
      "invalidValue": "",
      "constraint": "NotBlank"
    },
    {
      "field": "powerLevel",
      "message": "El nivel de poder debe estar entre 1 y 100, pero se recibió: 150",
      "invalidValue": 150,
      "constraint": "ValidPowerLevel"
    }
  ],
  "violationCount": 2
}
```

## Validaciones Implementadas

### Validaciones Estándar

- `@NotNull`: Campo no puede ser null
- `@NotBlank`: String no puede ser null, vacío o solo espacios
- `@Size`: Longitud mínima/máxima de String o colección
- `@Min`: Valor mínimo numérico
- `@Email`: Formato de email válido
- `@Pattern`: Expresión regular

### Validadores Personalizados

- `@ValidPowerLevel`: Valida que el nivel de poder esté entre 1 y 100
- `@ValidHeroName`: Valida que el nombre:
  - No contenga solo números
  - Tenga al menos una letra
  - No contenga caracteres especiales prohibidos

## Validación en Entidades JPA

La entidad `Hero` en el módulo `common` también tiene validaciones:

```java
@Entity
public class Hero {
    @NotBlank(message = "El nombre del héroe es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;
    
    @NotBlank(message = "El poder es requerido")
    private String power;
    
    @NotNull(message = "El nivel de poder es requerido")
    private Integer powerLevel;
}
```

Estas validaciones se aplican automáticamente cuando se persiste la entidad si se usa `@Valid` o validación programática.

## Grupos de Validación

Aunque este demo no usa grupos de validación activamente, están disponibles en el código:

- `CreateGroup`: Para operaciones de creación
- `UpdateGroup`: Para operaciones de actualización

Para usar grupos, puedes especificarlos en `@Valid`:

```java
@POST
public Response createHero(@Valid(groups = CreateGroup.class) HeroCreateDTO dto) {
    // ...
}
```

Y en el servicio:

```java
Set<ConstraintViolation<HeroCreateDTO>> violations = 
    validator.validate(dto, CreateGroup.class);
```

## Notas

- Bean Validation funciona automáticamente en Quarkus sin configuración adicional
- Las validaciones en `@PathParam` y `@QueryParam` se ejecutan automáticamente
- `@Valid` en el cuerpo de la petición valida automáticamente el DTO
- Los errores de validación se pueden manejar con `ExceptionMapper`
- La validación programática es útil cuando necesitas más control sobre cuándo y cómo validar

## Referencias

- [Jakarta Bean Validation Specification](https://jakarta.ee/specifications/bean-validation/)
- [Hibernate Validator Documentation](https://docs.jboss.org/hibernate/validator/8.0/reference/en-US/html_single/)
- [Quarkus Validation Guide](https://quarkus.io/guides/validation)

