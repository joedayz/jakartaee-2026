# Quarkus Demos

Demos de especificaciones de Jakarta EE usando **Quarkus**.

## Versión del Core Profile

**Quarkus 3.30.2 implementa completamente el Jakarta EE Core Profile 11** (versión estable actual).

> **Nota**: Jakarta EE Core Profile 12 está en desarrollo. Quarkus 3.30.2 se basa en la versión estable Core Profile 11.

## Especificaciones Disponibles

Quarkus implementa el **Jakarta EE Core Profile 11** completo más algunas especificaciones adicionales:

- ✅ Jakarta Annotations
- ✅ Jakarta CDI (Lite)
- ✅ Jakarta Interceptors
- ✅ Jakarta Managed Beans
- ✅ Jakarta RESTful Web Services (JAX-RS)
- ✅ Jakarta JSON Processing
- ✅ Jakarta JSON Binding
- ✅ Jakarta Bean Validation
- ✅ Jakarta Persistence (JPA)
- ✅ Jakarta Transactions
- ✅ Jakarta Batch

### Especificaciones Adicionales (no parte del Core Profile)

- ✅ Jakarta Data (a través de Hibernate ORM)
- ✅ Jakarta NoSQL (MongoDB con Panache MongoDB)
- ✅ Quarkus Panache (Repository y Active Record patterns)
- ⚠️ Security (usando Quarkus Security, no Jakarta Security estándar)
- ⚠️ MVC (usando Qute templates, no Jakarta MVC estándar)
- ⚠️ Renarde (framework MVC para Quarkus, alternativa avanzada a Qute básico)

## Estructura

Cada demo está en su propio directorio con su `pom.xml` o configuración Maven/Gradle.

## Ejecutar un Demo

```bash
cd quarkus-demos/[spec-name]
mvn quarkus:dev
```

