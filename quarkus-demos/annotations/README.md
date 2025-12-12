# Jakarta Annotations Demo

## Descripción

Este demo muestra cómo usar **anotaciones de Jakarta EE** para:
- Gestionar el ciclo de vida de beans con `@PostConstruct` y `@PreDestroy`
- Crear anotaciones personalizadas para el dominio de héroes
- Usar anotaciones para logging, validación y metadatos
- Demostrar el uso de `@Resource` y `@Generated`

## Objetivo

Aprender a:
- Usar anotaciones estándar de Jakarta (`@PostConstruct`, `@PreDestroy`, `@Resource`, `@Generated`)
- Crear anotaciones personalizadas
- Aplicar anotaciones en diferentes contextos
- Entender el ciclo de vida de beans con anotaciones

## Tema DC

Anotaciones aplicadas al dominio de héroes:
- **@PostConstruct**: Inicializar servicios de héroes al arrancar
- **@PreDestroy**: Limpiar recursos al cerrar la aplicación
- **@HeroPower**: Anotación personalizada para marcar niveles de poder
- **@Loggable**: Anotación personalizada para logging automático
- **@Generated**: Marcar código generado

## Anotaciones Demostradas

### Anotaciones Estándar de Jakarta

1. **@PostConstruct**
   - Se ejecuta después de la construcción del bean
   - Útil para inicialización

2. **@PreDestroy**
   - Se ejecuta antes de destruir el bean
   - Útil para limpieza de recursos

3. **@Resource**
   - Inyección de recursos del contenedor
   - Ejemplo: Logger, DataSource, etc.

4. **@Generated**
   - Marca código generado automáticamente
   - Útil para documentar código generado

### Anotaciones Personalizadas

1. **@HeroPower**
   - Marca métodos relacionados con poder de héroes
   - Incluye nivel mínimo requerido

2. **@Loggable**
   - Habilita logging automático de métodos
   - Nivel de log configurable

3. **@PowerLevel**
   - Valida niveles de poder de héroes
   - Rango permitido: 1-100

## Dependencias

```xml
<!-- Jakarta RESTful Web Services -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>
```

**Versión**: Quarkus 3.30.2

## Cómo Ejecutar

```bash
cd quarkus-demos/annotations
mvn quarkus:dev
```

La aplicación estará disponible en: `http://localhost:8080`

## Endpoints Disponibles

### Información sobre Anotaciones
- `GET /api/annotations/info` - Información sobre anotaciones disponibles
- `GET /api/annotations/lifecycle` - Demostrar ciclo de vida (@PostConstruct/@PreDestroy)
- `GET /api/annotations/custom` - Ejemplos de anotaciones personalizadas

### Demostración de Héroes
- `GET /api/heroes/annotated` - Listar héroes usando anotaciones personalizadas
- `POST /api/heroes/validate` - Validar héroe usando anotaciones

## Ejemplos de Uso

### Ver información de anotaciones
```bash
curl http://localhost:8080/api/annotations/info
```

### Demostrar ciclo de vida
```bash
curl http://localhost:8080/api/annotations/lifecycle
```

### Ver héroes con anotaciones personalizadas
```bash
curl http://localhost:8080/api/heroes/annotated
```

### Validar un héroe
```bash
curl -X POST http://localhost:8080/api/heroes/validate \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Superman",
    "power": "Super fuerza",
    "powerLevel": 95
  }'
```

## Estructura del Código

```
annotations/
├── src/main/java/com/jakartaee/annotations/
│   ├── annotation/
│   │   ├── HeroPower.java          # Anotación personalizada para poder
│   │   ├── Loggable.java           # Anotación personalizada para logging
│   │   └── PowerLevel.java         # Anotación de validación personalizada
│   ├── service/
│   │   ├── HeroService.java        # Servicio con @PostConstruct/@PreDestroy
│   │   └── PowerService.java      # Servicio usando anotaciones personalizadas
│   └── resource/
│       └── AnnotationsResource.java # Endpoints REST para demostrar anotaciones
└── pom.xml
```

## Validación

Para validar que Jakarta Annotations está funcionando:

1. Verificar que `@PostConstruct` se ejecuta al iniciar
2. Verificar que `@PreDestroy` se ejecuta al cerrar
3. Verificar que las anotaciones personalizadas funcionan
4. Verificar que los endpoints REST responden correctamente

```bash
# Verificar que la spec está disponible
curl http://localhost:8080/api/annotations/info
```

## Conceptos Clave

### Ciclo de Vida de Beans

```java
@ApplicationScoped
public class HeroService {
    
    @PostConstruct
    public void init() {
        // Se ejecuta después de la construcción
        // Útil para inicialización
    }
    
    @PreDestroy
    public void cleanup() {
        // Se ejecuta antes de la destrucción
        // Útil para liberar recursos
    }
}
```

### Anotaciones Personalizadas

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HeroPower {
    int minLevel() default 1;
    String description() default "";
}
```

### Uso de Anotaciones

```java
@HeroPower(minLevel = 80, description = "Héroe poderoso")
@Loggable(level = "INFO")
public void activateHero(String name) {
    // Método marcado con anotaciones personalizadas
}
```

