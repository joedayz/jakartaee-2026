# Renarde Framework Demo

## ⚠️ IMPORTANTE: Renarde NO es Jakarta MVC Estándar

**Renarde NO implementa Jakarta MVC estándar** (que es parte del Jakarta EE Web Profile).

**Renarde** es un framework web específico para Quarkus desarrollado por la comunidad Quarkiverse. Proporciona una experiencia MVC moderna y productiva, pero con una API diferente al estándar Jakarta MVC.

## Descripción

Este demo muestra cómo usar **Renarde** para crear una aplicación web MVC completa con CRUD de Heroes y Villanos. Renarde simplifica el desarrollo web en Quarkus proporcionando convenciones automáticas, utilidades integradas y una experiencia de desarrollo fluida.

## Objetivo

Aprender a:
- Usar Renarde extendiendo la clase `Controller`
- Aprovechar las convenciones automáticas de Renarde
- Usar `render()` para renderizar templates
- Usar `flash()` para mensajes temporales
- Usar `redirect()` para redirecciones type-safe
- Usar `validationFailed()` para manejo de validación
- Usar `@BeanParam` para binding automático de formularios

## Tema DC

CRUD completo de Heroes y Villanos de DC Comics usando Renarde:
- **Application**: Controlador de inicio
- **Heroes**: CRUD completo de héroes con Renarde
- **Villains**: CRUD completo de villanos con Renarde

## Soporte en Quarkus

❌ **Jakarta MVC NO está disponible en Quarkus** (es parte del Web Profile).

✅ **Renarde está disponible en Quarkus** a través de Quarkiverse.

Este demo usa:
- **Quarkus 3.30.2**
- **Renarde 3.1.3** (framework de Quarkiverse)
- **Qute** (usado internamente por Renarde)
- **Hibernate ORM** para persistencia
- **H2 Database** para almacenamiento

## Dependencias

```xml
<!-- Quarkus Renarde -->
<dependency>
    <groupId>io.quarkiverse.renarde</groupId>
    <artifactId>quarkus-renarde</artifactId>
    <version>3.1.3</version>
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

## ¿Qué es Renarde?

**Renarde** es un framework web del lado del servidor basado en Quarkus que:

1. **Extiende la clase `Controller`**: Proporciona una clase base con utilidades
2. **Convenciones automáticas**: Mapea métodos a rutas automáticamente
3. **Integración con Qute**: Usa Qute internamente para templates
4. **Utilidades integradas**: Flash messages, validación, redirecciones, etc.
5. **Type-safe**: Redirecciones y referencias type-safe

## Comparación: Renarde vs Qute Básico

### Tabla Comparativa

| Característica | Qute Básico (demo MVC) | Renarde |
|----------------|------------------------|---------|
| **Clase Base** | Sin clase base, uso directo de `@Inject Template` | Extiende `Controller` con utilidades integradas |
| **Convenciones** | Rutas manuales con `@Path`, `@GET`, `@POST` | Convenciones automáticas basadas en nombres de métodos |
| **Renderizado** | `TemplateInstance.data()` manual | `render()` simplificado |
| **Flash Messages** | Implementación manual con sesión | `flash()` integrado |
| **Redirecciones** | `Response.seeOther(URI.create())` manual | `redirect(Controller.class).method()` type-safe |
| **Validación** | Validación manual con `Validator` | `validationFailed()` integrado |
| **Binding de Formularios** | `@FormParam` manual para cada campo | `@BeanParam` binding automático a objetos |
| **Manejo de Errores** | Manejo manual de `NotFoundException` | `notFound()` integrado |
| **Código Requerido** | Más código boilerplate | Menos código, más productivo |
| **Curva de Aprendizaje** | Más baja (JAX-RS estándar) | Media (aprender convenciones) |
| **Flexibilidad** | Mayor control sobre rutas | Convenciones reducen flexibilidad |

### Ejemplo Comparativo

#### Con Qute Básico (demo MVC):

```java
@Path("/heroes")
public class HeroController {
    
    @Inject
    Template heroes;
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance listHeroes() {
        List<Hero> heroesList = heroService.getAllHeroes();
        return heroes.data("heroes", heroesList);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createHero(@FormParam("name") String name,
                               @FormParam("power") String power,
                               @FormParam("powerLevel") Integer powerLevel) {
        Hero hero = heroService.createHero(name, power, powerLevel);
        return Response.seeOther(URI.create("/heroes")).build();
    }
}
```

#### Con Renarde:

```java
public class Heroes extends Controller {
    
    public void list() {
        List<Hero> heroes = heroService.getAllHeroes();
        render("list", heroes);
    }
    
    public void create(@BeanParam @Valid Hero hero) {
        if (validationFailed()) {
            render("form", hero, "create");
            return;
        }
        heroService.createHero(hero);
        flash("success", "Héroe creado");
        redirect(Heroes.class).list();
    }
}
```

**Diferencias clave:**
- ✅ Menos código en Renarde
- ✅ Binding automático con `@BeanParam`
- ✅ Validación integrada
- ✅ Flash messages integrado
- ✅ Redirecciones type-safe

## Ventajas de Renarde

### 1. Menos Código Boilerplate

Renarde elimina mucho código repetitivo:

```java
// Sin Renarde: ~15 líneas
@Path("/heroes")
@GET
@Produces(MediaType.TEXT_HTML)
public TemplateInstance list() {
    List<Hero> heroes = service.getAllHeroes();
    return template.data("heroes", heroes);
}

// Con Renarde: ~3 líneas
public void list() {
    render("list", service.getAllHeroes());
}
```

### 2. Convenciones Automáticas

Renarde mapea automáticamente métodos a rutas:

- `public void list()` → `GET /heroes/list`
- `public void view(Long id)` → `GET /heroes/view/{id}`
- `public void create(@BeanParam Hero hero)` → `POST /heroes/create`

### 3. Flash Messages Integrado

```java
flash("success", "Operación exitosa");
flash("error", "Algo salió mal");

// En el template:
{#if flash("success")}
    <div class="success">{flash("success")}</div>
{/if}
```

### 4. Redirecciones Type-Safe

```java
// Type-safe, detecta errores en tiempo de compilación
redirect(Heroes.class).list();
redirect(Heroes.class).view(id);
```

### 5. Validación Integrada

```java
public void create(@BeanParam @Valid Hero hero) {
    if (validationFailed()) {
        // Automáticamente muestra errores en el template
        render("form", hero, "create");
        return;
    }
    // Continuar con la creación
}
```

### 6. Binding Automático de Formularios

```java
// El formulario se bindea automáticamente al objeto Hero
public void create(@BeanParam Hero hero) {
    // hero ya tiene todos los campos del formulario
}
```

## Convenciones de Renarde

### Rutas Automáticas

| Método | Ruta Generada |
|--------|---------------|
| `public void list()` | `GET /heroes/list` |
| `public void view(Long id)` | `GET /heroes/view/{id}` |
| `public void create(@BeanParam Hero hero)` | `POST /heroes/create` |
| `public void update(Long id, @BeanParam Hero hero)` | `POST /heroes/update/{id}` |

### Templates Automáticos

Renarde busca templates en `templates/{ControllerName}/{methodName}.html`:

- `Heroes.list()` → `templates/Heroes/list.html`
- `Heroes.view(Long id)` → `templates/Heroes/view.html`
- `Heroes.form()` → `templates/Heroes/form.html`

### Nombres de Controladores

- `Heroes` → Rutas bajo `/heroes`
- `Villains` → Rutas bajo `/villains`
- `Application` → Rutas bajo `/` (raíz)

## Estructura del Proyecto

```
renarde/
├── pom.xml
├── README.md
├── src/main/
│   ├── java/com/jakartaee/renarde/
│   │   ├── controller/
│   │   │   ├── Application.java      # Controlador de inicio
│   │   │   ├── Heroes.java           # CRUD de héroes con Renarde
│   │   │   └── Villains.java         # CRUD de villanos con Renarde
│   │   └── config/
│   │       └── DataInitializer.java  # Inicializador de datos
│   └── resources/
│       ├── application.properties
│       └── templates/                # Templates Qute
│           ├── Application/
│           │   └── index.html         # Página de inicio
│           ├── Heroes/
│           │   ├── list.html          # Lista de héroes
│           │   ├── view.html          # Detalle de héroe
│           │   └── form.html          # Formulario crear/editar
│           └── Villains/
│               ├── list.html          # Lista de villanos
│               ├── view.html          # Detalle de villano
│               └── form.html          # Formulario crear/editar
```

## Métodos Útiles de Controller

### render()

Renderiza un template con datos:

```java
render("list", heroes);                    // Un parámetro
render("form", hero, "create");            // Múltiples parámetros
render("view", hero, "extra", "data");    // Varios parámetros
```

### redirect()

Redirección type-safe a otro método del controlador:

```java
redirect(Heroes.class).list();            // Redirige a Heroes.list()
redirect(Heroes.class).view(id);          // Redirige a Heroes.view(id)
redirect(Villains.class).list();          // Redirige a Villains.list()
```

### flash()

Mensajes temporales que persisten entre requests:

```java
flash("success", "Operación exitosa");
flash("error", "Error en la operación");
flash("info", "Información adicional");
```

### validationFailed()

Verifica si la validación falló:

```java
if (validationFailed()) {
    render("form", hero, "create");
    return;
}
```

### notFound()

Retorna respuesta 404:

```java
if (hero == null) {
    notFound();
    return;
}
```

## Endpoints Web

### Página de Inicio
```
GET /
```
Muestra la página principal con comparación Renarde vs Qute básico.

### CRUD de Héroes

#### Listar héroes
```
GET /heroes/list
```
Muestra una tabla con todos los héroes.

#### Ver detalle de héroe
```
GET /heroes/view/{id}
```
Muestra los detalles de un héroe específico.

#### Crear nuevo héroe
```
GET /heroes/newHero          # Mostrar formulario
POST /heroes/create          # Crear héroe
```

#### Editar héroe
```
GET /heroes/edit/{id}        # Mostrar formulario
POST /heroes/update/{id}     # Actualizar héroe
```

#### Eliminar héroe
```
POST /heroes/delete/{id}
```
Elimina un héroe y redirige a la lista.

### CRUD de Villanos

Los mismos endpoints pero con `/villains`:
- `GET /villains/list` - Listar
- `GET /villains/view/{id}` - Ver detalle
- `GET /villains/newVillain` - Formulario crear
- `POST /villains/create` - Crear
- `GET /villains/edit/{id}` - Formulario editar
- `POST /villains/update/{id}` - Actualizar
- `POST /villains/delete/{id}` - Eliminar

## Ejecutar el Demo

```bash
cd quarkus-demos/renarde
mvn quarkus:dev
```

La aplicación estará disponible en `http://localhost:8080`

## Navegación

1. **Inicio**: `http://localhost:8080/`
   - Página principal con comparación Renarde vs Qute básico
   - Enlaces a héroes y villanos

2. **Héroes**: `http://localhost:8080/heroes/list`
   - Lista de héroes con flash messages
   - Botones para crear, ver, editar y eliminar

3. **Villanos**: `http://localhost:8080/villains/list`
   - Lista de villanos con flash messages
   - Botones para crear, ver, editar y eliminar

## Características del CRUD

### Crear
- Formulario con validación Bean Validation
- Binding automático con `@BeanParam`
- Flash message de éxito después de crear
- Redirección automática a la lista

### Leer
- Lista en tabla con todos los registros
- Vista de detalle individual
- Flash messages visibles

### Actualizar
- Formulario prellenado con datos existentes
- Validación integrada
- Flash message de éxito
- Redirección al detalle después de actualizar

### Eliminar
- Confirmación JavaScript antes de eliminar
- Flash message de éxito
- Redirección a la lista después de eliminar

## Cuándo Usar Renarde vs Qute Básico

### ✅ Usar Renarde cuando:
- Quieres desarrollo rápido con menos código
- Te gustan las convenciones automáticas
- Necesitas flash messages frecuentemente
- Prefieres redirecciones type-safe
- Quieres binding automático de formularios
- Estás construyendo una aplicación web tradicional (no solo API REST)

### ✅ Usar Qute Básico cuando:
- Necesitas control total sobre las rutas
- Estás construyendo principalmente APIs REST
- Prefieres seguir estándares JAX-RS
- Quieres más flexibilidad en la estructura
- Necesitas compatibilidad con código existente

## Limitaciones de Renarde

1. **No es Jakarta MVC Estándar**: API diferente al estándar Jakarta
2. **Solo en Quarkus**: No es portable a otros runtimes
3. **Convenciones**: Menos flexibilidad que rutas manuales
4. **Dependencia Externa**: Requiere dependencia de Quarkiverse
5. **Curva de Aprendizaje**: Requiere aprender las convenciones

## Mejores Prácticas con Renarde

1. **Seguir Convenciones**: Aprovecha las convenciones automáticas
2. **Usar Flash Messages**: Para feedback al usuario
3. **Validación**: Usa `@Valid` y `validationFailed()`
4. **Type-Safe Redirects**: Usa `redirect(Controller.class).method()`
5. **Binding Automático**: Usa `@BeanParam` para formularios

## Referencias

- [Renarde Documentation](https://docs.quarkiverse.io/quarkus-renarde/dev/)
- [Renarde GitHub](https://github.com/quarkiverse/quarkus-renarde)
- [Quarkus Qute Guide](https://quarkus.io/guides/qute)
- [Jakarta MVC Specification](https://jakarta.ee/specifications/mvc/) (no disponible en Quarkus)

## Nota Final

Este demo demuestra cómo **Renarde** simplifica el desarrollo web en Quarkus proporcionando convenciones automáticas y utilidades integradas. Aunque no es Jakarta MVC estándar, Renarde ofrece una experiencia moderna y productiva para crear aplicaciones web en Quarkus.

**Comparación con el demo MVC básico:**
- El demo `mvc/` muestra Qute básico con JAX-RS manual
- Este demo muestra Renarde con convenciones automáticas
- Ambos usan Qute internamente, pero Renarde añade una capa de abstracción

Para aplicaciones que requieren Jakarta MVC estándar, considera usar WildFly Platform que implementa el Web Profile completo.

