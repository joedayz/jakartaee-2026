# MVC Demo (Qute Templates)

## ⚠️ IMPORTANTE: Quarkus NO Soporta Jakarta MVC Estándar

**Quarkus NO implementa Jakarta MVC estándar** (que es parte del Jakarta EE Web Profile).

Este demo usa **Qute**, el motor de plantillas nativo de Quarkus, como alternativa para implementar el patrón MVC. Qute proporciona funcionalidades similares pero con una API diferente al estándar Jakarta MVC.

## Descripción

Este demo muestra cómo implementar una aplicación web con patrón MVC usando **Qute** (motor de plantillas de Quarkus) junto con JAX-RS. Incluye un CRUD completo para gestionar Heroes y Villanos de DC Comics.

## Objetivo

Aprender a:
- Usar Qute para renderizar templates HTML
- Crear controladores REST que devuelven templates
- Implementar un CRUD completo con vistas HTML
- Manejar formularios y redirecciones
- Usar la sintaxis de Qute para templates dinámicos

## Tema DC

CRUD completo de Heroes y Villanos de DC Comics:
- **HeroController**: CRUD de héroes con vistas HTML
- **VillainController**: CRUD de villanos con vistas HTML
- **Templates Qute**: Vistas HTML con sintaxis Qute

## Soporte en Quarkus

❌ **Jakarta MVC NO está disponible en Quarkus** (es parte del Web Profile).

✅ **Qute está completamente soportado en Quarkus 3.30.2** como alternativa.

Este demo usa:
- **Quarkus 3.30.2** con la nueva API REST (`quarkus-rest`)
- **Qute** para templates HTML
- **Hibernate ORM** para persistencia
- **H2 Database** para almacenamiento

## Dependencias

```xml
<!-- Jakarta RESTful Web Services -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- Qute - Motor de plantillas de Quarkus -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-qute</artifactId>
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

## Comparación: Jakarta MVC vs Qute

| Característica | Jakarta MVC | Qute (Quarkus) |
|----------------|-------------|----------------|
| Disponibilidad | ❌ No disponible en Quarkus | ✅ Disponible |
| Anotaciones | `@Controller`, `@View`, `@MvcBinding` | `@Inject Template` |
| Templates | JSP, Facelets, etc. | Templates Qute (.html) |
| Sintaxis | JSP/EL | Sintaxis Qute (`{#if}`, `{#for}`) |
| Integración | Jakarta EE Web Profile | Nativa en Quarkus |
| Rendimiento | Estándar | Optimizado para Quarkus |

## Estructura del Proyecto

```
mvc/
├── pom.xml
├── README.md
├── src/main/
│   ├── java/com/jakartaee/mvc/
│   │   ├── resource/
│   │   │   ├── HomeController.java      # Controlador de inicio
│   │   │   ├── HeroController.java      # CRUD de héroes
│   │   │   └── VillainController.java   # CRUD de villanos
│   │   ├── service/
│   │   │   ├── HeroService.java
│   │   │   └── VillainService.java
│   │   └── config/
│   │       └── DataInitializer.java
│   └── resources/
│       ├── application.properties
│       └── templates/                    # Templates Qute
│           ├── index.html                # Página de inicio
│           ├── heroes.html               # Lista de héroes
│           ├── hero-form.html            # Formulario crear/editar héroe
│           ├── hero-detail.html          # Detalle de héroe
│           ├── villains.html             # Lista de villanos
│           ├── villain-form.html         # Formulario crear/editar villano
│           └── villain-detail.html       # Detalle de villano
```

## Uso de Qute

### Inyectar Templates

```java
@Inject
Template heroes; // Busca heroes.html en templates/

@Inject
Template heroForm; // Busca hero-form.html en templates/
```

### Renderizar Templates

```java
@GET
@Produces(MediaType.TEXT_HTML)
public TemplateInstance listHeroes() {
    List<Hero> heroesList = heroService.getAllHeroes();
    return heroes.data("heroes", heroesList);
}
```

### Sintaxis Qute en Templates

```html
<!-- Condicionales -->
{#if heroes.size > 0}
    <p>Hay héroes</p>
{#else}
    <p>No hay héroes</p>
{/if}

<!-- Iteraciones -->
{#for hero in heroes}
    <div>{hero.name} - {hero.powerLevel}</div>
{/for}

<!-- Valores -->
<h1>{hero.name}</h1>
<p>{hero.description ?: 'Sin descripción'}</p>
```

## Endpoints Web

### Página de Inicio
```
GET /
```
Muestra la página principal con enlaces a héroes y villanos.

### CRUD de Héroes

#### Listar héroes
```
GET /heroes
```
Muestra una tabla con todos los héroes.

#### Crear nuevo héroe
```
GET /heroes/new          # Mostrar formulario
POST /heroes             # Crear héroe
```

#### Ver detalle de héroe
```
GET /heroes/{id}
```
Muestra los detalles de un héroe específico.

#### Editar héroe
```
GET /heroes/{id}/edit    # Mostrar formulario
POST /heroes/{id}        # Actualizar héroe
```

#### Eliminar héroe
```
POST /heroes/{id}/delete
```
Elimina un héroe y redirige a la lista.

### CRUD de Villanos

Los mismos endpoints pero con `/villains`:
- `GET /villains` - Listar
- `GET /villains/new` - Formulario crear
- `POST /villains` - Crear
- `GET /villains/{id}` - Ver detalle
- `GET /villains/{id}/edit` - Formulario editar
- `POST /villains/{id}` - Actualizar
- `POST /villains/{id}/delete` - Eliminar

## Ejecutar el Demo

```bash
cd quarkus-demos/mvc
mvn quarkus:dev
```

La aplicación estará disponible en `http://localhost:8080`

## Navegación

1. **Inicio**: `http://localhost:8080/`
   - Página principal con advertencia sobre Jakarta MVC
   - Enlaces a héroes y villanos

2. **Héroes**: `http://localhost:8080/heroes`
   - Lista de héroes
   - Botones para crear, ver, editar y eliminar

3. **Villanos**: `http://localhost:8080/villains`
   - Lista de villanos
   - Botones para crear, ver, editar y eliminar

## Características del CRUD

### Crear
- Formulario con validación HTML5
- Campos: nombre, poder, nivel de poder, descripción
- Redirección a la lista después de crear

### Leer
- Lista en tabla con todos los registros
- Vista de detalle individual

### Actualizar
- Formulario prellenado con datos existentes
- Mismo formulario que crear pero con datos actuales
- Redirección al detalle después de actualizar

### Eliminar
- Confirmación JavaScript antes de eliminar
- Redirección a la lista después de eliminar

## Ventajas de Qute

1. **Integración Nativa**: Funciona perfectamente con Quarkus
2. **Rendimiento**: Optimizado para aplicaciones cloud-native
3. **Type-Safe**: Verificación de tipos en tiempo de compilación
4. **Sintaxis Limpia**: Más legible que JSP
5. **Templates Reutilizables**: Fácil de mantener y reutilizar

## Limitaciones

1. **No es Jakarta MVC Estándar**: API diferente al estándar Jakarta
2. **Solo en Quarkus**: No es portable a otros runtimes Jakarta EE
3. **Sintaxis Propia**: Requiere aprender la sintaxis de Qute

## Mejores Prácticas

1. **Separar Lógica**: Mantener la lógica de negocio en servicios
2. **Templates Simples**: Evitar lógica compleja en templates
3. **Reutilizar Templates**: Crear componentes reutilizables
4. **Validación**: Validar datos tanto en cliente como servidor

## Referencias

- [Quarkus Qute Guide](https://quarkus.io/guides/qute)
- [Qute Reference](https://quarkus.io/guides/qute-reference)
- [Jakarta MVC Specification](https://jakarta.ee/specifications/mvc/) (no disponible en Quarkus)

## Alternativa Avanzada: Renarde

Si buscas una experiencia MVC aún más simplificada, considera usar **Renarde**, un framework MVC para Quarkus que añade convenciones automáticas y utilidades adicionales sobre Qute.

**Ventajas de Renarde sobre Qute básico:**
- ✅ Convenciones automáticas de rutas
- ✅ Clase base `Controller` con utilidades integradas
- ✅ Flash messages integrado
- ✅ Redirecciones type-safe
- ✅ Binding automático de formularios con `@BeanParam`
- ✅ Validación integrada con `validationFailed()`

**Ver demo completo:** `quarkus-demos/renarde/`

## Nota Final

Este demo demuestra cómo implementar el patrón MVC en Quarkus usando **Qute** como alternativa a Jakarta MVC. Aunque no es Jakarta MVC estándar, Qute proporciona una solución moderna y eficiente para crear aplicaciones web con templates en Quarkus.

**Opciones disponibles:**
1. **Qute Básico** (este demo): Control total con JAX-RS manual
2. **Renarde** (`quarkus-demos/renarde/`): Convenciones automáticas y más productivo
3. **Jakarta MVC Estándar**: Solo disponible en WildFly Platform (Web Profile)

Para aplicaciones que requieren Jakarta MVC estándar, considera usar WildFly Platform que implementa el Web Profile completo.

