# Jakarta Managed Beans Demo

## Descripción

Este demo muestra cómo usar **Jakarta Managed Beans** para crear beans gestionados por el contenedor con un ciclo de vida simple. Aunque CDI es más moderno y poderoso, Managed Beans sigue siendo parte de la especificación Jakarta EE Core Profile 11 y puede ser útil en ciertos casos.

## Objetivo

Aprender a:
- Crear Managed Beans con `@ManagedBean`
- Usar `@PostConstruct` y `@PreDestroy` para gestionar el ciclo de vida
- Inyectar dependencias en Managed Beans con `@Inject`
- Inyectar Managed Beans en otros componentes
- Comparar Managed Beans con CDI Beans

## Tema DC

Gestión de Heroes y Villanos de DC Comics usando Managed Beans:
- **HeroManagedBean**: Bean gestionado para operaciones CRUD de héroes
- **VillainManagedBean**: Bean gestionado para operaciones CRUD de villanos
- **BattleService**: Servicio Managed Bean que coordina batallas entre héroes y villanos

## Soporte en Quarkus

✅ **Jakarta Managed Beans está soportado en Quarkus 3.30.2** a través de CDI (ArC).

**Nota importante**: En Quarkus, los Managed Beans funcionan a través de CDI. Quarkus trata los beans anotados con `@ManagedBean` como beans CDI normales, pero respeta las características específicas de Managed Beans como el ciclo de vida.

Este demo usa:
- **Quarkus 3.30.2** con la nueva API REST (`quarkus-rest`)
- **CDI (ArC)** para gestión de beans
- **Hibernate ORM** para persistencia
- **H2 Database** para almacenamiento

## Dependencias

```xml
<!-- Jakarta RESTful Web Services -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- CDI (necesario para Managed Beans) -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-arc</artifactId>
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

## ¿Qué es un Managed Bean?

Un **Managed Bean** es una clase Java gestionada por el contenedor Jakarta EE que:

1. **Tiene un constructor sin parámetros** (o un constructor con `@Inject`)
2. **Está anotada con `@ManagedBean`**
3. **Puede usar `@PostConstruct` y `@PreDestroy`** para gestionar su ciclo de vida
4. **Puede usar `@Inject`** para inyección de dependencias (si CDI está disponible)
5. **Puede usar `@Resource`** para inyección de recursos JNDI

## Crear un Managed Bean

### Ejemplo Básico

```java
import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

@ManagedBean
@ApplicationScoped
public class HeroManagedBean {
    
    private String beanName;
    
    // Constructor sin parámetros requerido
    public HeroManagedBean() {
        // Inicialización básica
    }
    
    // Se ejecuta después de la construcción y la inyección
    @PostConstruct
    public void initialize() {
        beanName = "HeroManagedBean-" + System.currentTimeMillis();
        System.out.println("Bean inicializado: " + beanName);
    }
    
    // Se ejecuta antes de que el bean sea destruido
    @PreDestroy
    public void cleanup() {
        System.out.println("Limpiando bean: " + beanName);
    }
    
    // Métodos de negocio
    public List<Hero> getAllHeroes() {
        // Lógica de negocio
    }
}
```

## Características de Managed Beans

### 1. Ciclo de Vida

Los Managed Beans tienen un ciclo de vida simple:

1. **Construcción**: Se crea una instancia usando el constructor sin parámetros
2. **Inyección**: Se inyectan dependencias marcadas con `@Inject` o `@Resource`
3. **Inicialización**: Se ejecuta el método marcado con `@PostConstruct`
4. **Uso**: El bean está listo para ser usado
5. **Destrucción**: Se ejecuta el método marcado con `@PreDestroy` antes de destruir el bean

### 2. Inyección de Dependencias

Los Managed Beans pueden inyectar otros beans usando `@Inject`:

```java
@ManagedBean
@ApplicationScoped
public class BattleService {
    
    @Inject
    HeroManagedBean heroManagedBean;
    
    @Inject
    VillainManagedBean villainManagedBean;
    
    public void simulateBattle(Long heroId, Long villainId) {
        // Usar los beans inyectados
        Hero hero = heroManagedBean.getHeroById(heroId).orElse(null);
        Villain villain = villainManagedBean.getVillainById(villainId).orElse(null);
        // ...
    }
}
```

### 3. Scopes

Los Managed Beans pueden usar scopes de CDI:

- `@ApplicationScoped`: Una instancia por aplicación
- `@RequestScoped`: Una instancia por request HTTP
- `@SessionScoped`: Una instancia por sesión HTTP (no disponible en Quarkus)
- `@Dependent`: Una instancia por cada punto de inyección

## Comparación: Managed Beans vs CDI Beans

| Característica | Managed Beans | CDI Beans |
|----------------|---------------|-----------|
| Anotación | `@ManagedBean` | `@ApplicationScoped`, `@RequestScoped`, etc. |
| Constructor | Sin parámetros requerido | Puede tener parámetros con `@Inject` |
| Inyección | `@Inject` (si CDI disponible) | `@Inject` (nativo) |
| Lifecycle | `@PostConstruct`, `@PreDestroy` | `@PostConstruct`, `@PreDestroy` |
| Interceptores | Limitado | Completo |
| Eventos | No | Sí (`@Observes`) |
| Producers | No | Sí (`@Produces`) |
| Qualifiers | No | Sí |
| Uso recomendado | Legacy, compatibilidad | Nuevos proyectos |

## Estructura del Proyecto

```
managed-beans/
├── pom.xml
├── README.md
├── src/main/
│   ├── java/com/jakartaee/managedbeans/
│   │   ├── bean/
│   │   │   ├── HeroManagedBean.java      # Managed Bean para héroes
│   │   │   └── VillainManagedBean.java   # Managed Bean para villanos
│   │   ├── service/
│   │   │   └── BattleService.java        # Servicio Managed Bean
│   │   ├── resource/
│   │   │   └── ManagedBeanResource.java  # Recurso REST
│   │   └── config/
│   │       └── DataInitializer.java      # Inicializador de datos
│   └── resources/
│       └── application.properties
```

## Endpoints REST

### Héroes

#### Obtener todos los héroes
```bash
GET /api/managed-beans/heroes
```

#### Obtener héroe por ID
```bash
GET /api/managed-beans/heroes/{id}
```

#### Crear héroe
```bash
POST /api/managed-beans/heroes?name=Superman&power=Flight&powerLevel=95
```

#### Actualizar héroe
```bash
PUT /api/managed-beans/heroes/{id}?name=Superman&power=Flight&powerLevel=96
```

#### Eliminar héroe
```bash
DELETE /api/managed-beans/heroes/{id}
```

### Villanos

#### Obtener todos los villanos
```bash
GET /api/managed-beans/villains
```

#### Obtener villano por ID
```bash
GET /api/managed-beans/villains/{id}
```

#### Crear villano
```bash
POST /api/managed-beans/villains?name=Joker&power=Chaos&powerLevel=80
```

### Batallas

#### Simular batalla
```bash
POST /api/managed-beans/battles?heroId=1&villainId=1
```

**Respuesta ejemplo:**
```json
{
  "hero": "Superman",
  "heroPower": 95,
  "villain": "Joker",
  "villainPower": 80,
  "winner": "Superman",
  "status": "HERO_WINS",
  "message": "Superman derrota a Joker!",
  "battleNumber": 1
}
```

### Información y Estadísticas

#### Información sobre Managed Beans
```bash
GET /api/managed-beans/info
```

#### Estadísticas del servicio de batallas
```bash
GET /api/managed-beans/stats
```

#### Estadísticas del bean de héroes
```bash
GET /api/managed-beans/hero-stats
```

## Ejecutar el Demo

```bash
cd quarkus-demos/managed-beans
mvn quarkus:dev
```

La aplicación estará disponible en `http://localhost:8080`

## Observar el Ciclo de Vida

### 1. Al Iniciar la Aplicación

Verás logs como:

```
🏗️  Constructor de HeroManagedBean llamado
✅ @PostConstruct ejecutado para HeroManagedBean-1234567890
   Managed Bean inicializado y listo para usar

🏗️  Constructor de VillainManagedBean llamado
✅ @PostConstruct ejecutado para VillainManagedBean-1234567891
   Managed Bean de villanos inicializado

🏗️  Constructor de BattleService llamado
✅ @PostConstruct ejecutado para BattleService-1234567892
   BattleService inicializado con dependencias inyectadas
```

### 2. Durante el Uso

Cada operación incrementa un contador:

```
[HeroManagedBean-1234567890] getAllHeroes() - Operación #1
[HeroManagedBean-1234567890] getHeroById(1) - Operación #2
```

### 3. Al Detener la Aplicación

Verás logs como:

```
🛑 @PreDestroy ejecutado para BattleService-1234567892
   Total de batallas simuladas: 5

🛑 @PreDestroy ejecutado para VillainManagedBean-1234567891
   Total de operaciones: 3

🛑 @PreDestroy ejecutado para HeroManagedBean-1234567890
   Total de operaciones realizadas: 8
   Limpiando recursos del Managed Bean
```

## Casos de Uso

### 1. Compatibilidad con Código Legacy

Si tienes código legacy que usa `@ManagedBean`, puedes mantenerlo funcionando mientras migras gradualmente a CDI.

### 2. Beans Simples sin Necesidad de CDI Completo

Para beans simples que solo necesitan ciclo de vida básico y no requieren características avanzadas de CDI como eventos o qualifiers.

### 3. Separación de Responsabilidades

Puedes usar Managed Beans para encapsular lógica específica mientras usas CDI para la arquitectura general.

## Ventajas de Managed Beans

1. **Simplicidad**: Modelo de programación simple y directo
2. **Ciclo de Vida**: Gestión explícita del ciclo de vida con `@PostConstruct` y `@PreDestroy`
3. **Compatibilidad**: Parte del estándar Jakarta EE Core Profile
4. **Inyección**: Soporta inyección de dependencias con `@Inject`

## Limitaciones

1. **Menos Poderoso que CDI**: No tiene eventos, qualifiers, producers, etc.
2. **Menos Flexible**: Constructor sin parámetros requerido
3. **Menos Moderno**: CDI es el estándar moderno recomendado
4. **Limitado en Quarkus**: Quarkus lo trata como CDI bean, perdiendo algunas características específicas

## Mejores Prácticas

1. **Usar CDI para Nuevos Proyectos**: Prefiere CDI beans sobre Managed Beans
2. **Migrar Gradualmente**: Si tienes Managed Beans legacy, migra a CDI gradualmente
3. **Documentar el Uso**: Si usas Managed Beans, documenta por qué
4. **Mantener Simple**: Si usas Managed Beans, mantén la lógica simple

## ¿Cuándo Usar Managed Beans?

### ✅ Usar Managed Beans cuando:
- Tienes código legacy que ya los usa
- Necesitas compatibilidad con especificaciones antiguas
- Tienes beans muy simples sin necesidad de características avanzadas de CDI

### ❌ NO usar Managed Beans cuando:
- Estás creando un proyecto nuevo (usa CDI)
- Necesitas eventos, qualifiers, o producers
- Quieres aprovechar todas las características de CDI

## Referencias

- [Jakarta Managed Beans Specification](https://jakarta.ee/specifications/managed-beans/)
- [Quarkus CDI Guide](https://quarkus.io/guides/cdi)
- [Jakarta EE Core Profile 11](https://jakarta.ee/specifications/coreprofile/11/)

