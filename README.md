# jakartaee-2026
Demos sobre JakartaEE para ver el uso de cada Spec

## 🦸 Tema: Heroes y Villanos de DC Comics

Todos los demos usan un tema común: **Heroes y Villanos de DC Comics**. Esto permite:
- Aprender diferentes specs con un contexto familiar
- Comparar cómo diferentes specs resuelven problemas similares
- Mantener consistencia entre demos

### Entidades Principales

- **Hero**: Superman, Batman, Wonder Woman, Flash, Green Lantern, etc.
- **Villain**: Joker, Lex Luthor, Darkseid, Sinestro, Brainiac, etc.

Ver `common/entities/` para las entidades compartidas.

## 🚀 Versiones

- **Quarkus**: `3.30.2` (para la mayoría de demos, algunos usan versiones específicas)
- **Java**: `21` (LTS - recomendado)
- **Jakarta EE**: `11` (compatible con Java 21)
- **REST API**: `quarkus-rest-jackson` (nueva API REST de Quarkus 3.x)

### Soporte de Especificaciones Especiales

- ✅ **Jakarta Data**: Soportado en Quarkus 3.30.2 (a través de Hibernate ORM)
- ❌ **Jakarta Query**: No soportado aún (especificación en desarrollo)

### Ventajas de Java 21

- ✅ **Virtual Threads**: Mejor escalabilidad (Jakarta EE 11 los aprovecha)
- ✅ **Pattern Matching**: Mejor sintaxis
- ✅ **Records**: Más conciso para DTOs
- ✅ **Sealed Classes**: Mejor modelado de dominio
- ✅ **LTS**: Soporte a largo plazo hasta 2031

## Perfiles de Jakarta EE

Jakarta EE tiene tres perfiles principales, de menor a mayor:

1. **Core Profile** - El más pequeño, para microservicios y runtimes ligeros
2. **Web Profile** - Core Profile + especificaciones web
3. **Platform** - Todas las especificaciones (Web Profile + Enterprise)

## Implementaciones por Runtime

### Quarkus
- ✅ **Core Profile completo** + algunas extras
- ✅ Implementa: CDI Lite, JAX-RS, JSON Processing/Binding, Bean Validation, JPA, Transactions, Batch
- ❌ NO implementa: JMS, JSF, EJB, JCA, Mail, WebSocket, etc.

### WildFly
- ✅ **Core Profile** (modo ligero)
- ✅ **Web Profile**
- ✅ **Platform completo** (todas las specs)

## Estructura del Proyecto

```
jakartaee-2026/
├── README.md
├── common/                          # Código compartido
│   ├── entities/                   # Entidades JPA compartidas
│   ├── dto/                        # DTOs compartidos
│   └── utils/                      # Utilidades comunes
│
├── quarkus-demos/                  # Demos con Quarkus (Core Profile + extras)
│   ├── cdi/
│   ├── jax-rs/
│   ├── json-processing/
│   ├── json-binding/
│   ├── bean-validation/
│   ├── jpa/
│   ├── transactions/
│   └── batch/
│
├── wildfly-core-profile/           # Demos con WildFly en modo Core Profile
│   ├── cdi/
│   ├── jax-rs/
│   ├── json-processing/
│   ├── json-binding/
│   ├── bean-validation/
│   ├── jpa/
│   ├── transactions/
│   └── batch/
│
└── wildfly-platform/               # Demos con WildFly Platform completo
    ├── core-profile/               # Specs del Core Profile (para comparar)
    │   └── ...
    ├── web-profile-only/           # Specs solo del Web Profile
    │   ├── servlet/
    │   ├── jsp/
    │   ├── jsf/
    │   ├── websocket/
    │   └── mvc/
    └── platform-only/              # Specs solo del Platform completo
        ├── ejb/
        ├── jms/
        ├── jca/
        ├── mail/
        └── security/
```

## Especificaciones por Perfil

### Jakarta EE Core Profile
- Jakarta Annotations
- Jakarta CDI (Lite)
- Jakarta RESTful Web Services (JAX-RS)
- Jakarta JSON Processing
- Jakarta JSON Binding
- Jakarta Bean Validation
- Jakarta Persistence (JPA)
- Jakarta Transactions
- Jakarta Batch

### Jakarta EE Web Profile (Core Profile +)
- ✅ Todas las del Core Profile +
- Jakarta Servlet
- Jakarta Server Pages (JSP)
- Jakarta Server Faces (JSF)
- Jakarta WebSocket
- Jakarta MVC

### Jakarta EE Platform (Web Profile +)
- ✅ Todas las del Web Profile +
- Jakarta Enterprise Beans (EJB)
- Jakarta Messaging (JMS)
- Jakarta Connectors (JCA)
- Jakarta Concurrency
- Jakarta Security
- Jakarta Mail
- Jakarta Authentication
- Jakarta Authorization
- Jakarta Activation
- Jakarta Expression Language
- Jakarta XML Binding
- Jakarta XML Web Services

## Comparación de Implementaciones

| Spec | Quarkus | WildFly Core | WildFly Platform |
|------|---------|--------------|------------------|
| CDI Lite | ✅ | ✅ | ✅ |
| JAX-RS | ✅ | ✅ | ✅ |
| JSON Processing | ✅ | ✅ | ✅ |
| JSON Binding | ✅ | ✅ | ✅ |
| Bean Validation | ✅ | ✅ | ✅ |
| JPA | ✅ | ✅ | ✅ |
| Transactions | ✅ | ✅ | ✅ |
| Batch | ✅ | ✅ | ✅ |
| Servlet | ❌ | ✅ | ✅ |
| JSP | ❌ | ❌ | ✅ |
| JSF | ❌ | ❌ | ✅ |
| WebSocket | ❌ | ❌ | ✅ |
| MVC | ❌ | ❌ | ✅ |
| EJB | ❌ | ❌ | ✅ |
| JMS | ❌ | ❌ | ✅ |
| JCA | ❌ | ❌ | ✅ |
| Mail | ❌ | ❌ | ✅ |
| Security | ⚠️ Parcial | ❌ | ✅ |

## Recomendación para Probar Specs

1. **Core Profile**: Usa Quarkus o WildFly Core Profile
2. **Web Profile**: Usa WildFly Web Profile
3. **Platform completo**: Usa WildFly Platform

**Ventaja de tener ambos (Quarkus + WildFly):**
- Comparar implementaciones de las mismas specs
- Ver diferencias en enfoque (cloud-native vs tradicional)
- Aprender qué runtime usar según el caso de uso

## Cómo Validar el Uso de las Especificaciones

Para validar que una especificación está disponible y funcionando correctamente:

### 1. Scripts de Validación

```bash
# Validar una spec específica
./scripts/validate-spec.sh batch quarkus

# Validar todas las specs
./scripts/validate-all-specs.sh quarkus
```

### 2. Validación Programática (Java)

```java
import com.jakartaee.utils.SpecValidator;

SpecValidator validator = new SpecValidator();
if (validator.isAvailable("batch")) {
    // Jakarta Batch está disponible
}

// Obtener reporte completo
String report = validator.getReport();
System.out.println(report);
```

### 3. Validación vía REST (si JAX-RS está disponible)

```bash
# Verificar todas las specs
curl http://localhost:8080/specs/all

# Obtener reporte
curl http://localhost:8080/specs/report

# Verificar una spec específica
curl http://localhost:8080/specs/check/batch
```

### Especificaciones que Puedes Validar

- ✅ **Jakarta Annotations** - Anotaciones estándar (@PostConstruct, @PreDestroy, etc.)
- ✅ **Jakarta Activation** - Manejo de tipos MIME (solo WildFly Platform)
- ✅ **Jakarta Authentication** - Autenticación (solo WildFly Platform)
- ✅ **Jakarta Authorization** - Autorización JACC (solo WildFly Platform)
- ✅ **Jakarta Batch** - Procesamiento por lotes
- ✅ **Jakarta CDI** - Inyección de dependencias
- ✅ **Jakarta REST (JAX-RS)** - Servicios REST
- ✅ **Jakarta JSON Processing** - Procesamiento JSON
- ✅ **Jakarta JSON Binding** - Binding JSON
- ✅ **Jakarta Bean Validation** - Validación de beans
- ✅ **Jakarta Persistence (JPA)** - Persistencia
- ✅ **Jakarta Transactions** - Transacciones

Ver `scripts/README.md` para más detalles sobre los scripts de validación.
