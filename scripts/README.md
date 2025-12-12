# Scripts de Validación

Scripts útiles para validar especificaciones de Jakarta EE.

## validate-spec.sh

Valida que una especificación específica está disponible en el proyecto.

**Uso:**
```bash
./scripts/validate-spec.sh [spec-name] [runtime]

# Ejemplos:
./scripts/validate-spec.sh batch quarkus
./scripts/validate-spec.sh batch wildfly
./scripts/validate-spec.sh annotations quarkus
./scripts/validate-spec.sh activation wildfly
```

**Especificaciones soportadas:**
- `annotations` - Jakarta Annotations
- `activation` - Jakarta Activation (solo WildFly Platform)
- `authentication` - Jakarta Authentication (solo WildFly Platform)
- `authorization` - Jakarta Authorization (solo WildFly Platform)
- `batch` - Jakarta Batch
- `cdi` - Jakarta CDI
- `jax-rs` - Jakarta RESTful Web Services
- `json-processing` - Jakarta JSON Processing
- `json-binding` - Jakarta JSON Binding
- `bean-validation` - Jakarta Bean Validation
- `jpa` - Jakarta Persistence
- `transactions` - Jakarta Transactions

## validate-all-specs.sh

Valida todas las especificaciones disponibles según el runtime.

**Uso:**
```bash
./scripts/validate-all-specs.sh [runtime]

# Ejemplos:
./scripts/validate-all-specs.sh quarkus
./scripts/validate-all-specs.sh wildfly
```

## Validación Programática

También puedes usar las clases Java en `common/utils/`:

### SpecValidator

```java
import com.jakartaee.utils.SpecValidator;

SpecValidator validator = new SpecValidator();

// Verificar una spec específica
if (validator.isAvailable("batch")) {
    System.out.println("Jakarta Batch está disponible");
}

// Verificar todas
Map<String, Boolean> allSpecs = validator.checkAll();

// Obtener reporte
String report = validator.getReport();
System.out.println(report);
```

### SpecValidationResource

Si tienes JAX-RS disponible, puedes exponer endpoints REST:

- `GET /specs/all` - JSON con todas las specs
- `GET /specs/report` - Reporte en texto plano
- `GET /specs/check/{spec}` - Verificar una spec específica

## Ejemplo de Uso Completo

```bash
# 1. Validar una spec específica
cd quarkus-demos/batch
../../scripts/validate-spec.sh batch quarkus

# 2. Validar todas las specs del proyecto
../../scripts/validate-all-specs.sh quarkus

# 3. En tiempo de ejecución, usar el endpoint REST
curl http://localhost:8080/specs/all
curl http://localhost:8080/specs/report
curl http://localhost:8080/specs/check/batch
```

