# 📋 TODO - Fases de Desarrollo del Proyecto Jakarta EE 2026

Última actualización: Abril 8, 2026

---

## 🚀 FASE 0: VALIDACIÓN INMEDIATA (Esta semana)

### Objetivo
Validar que TODO funciona correctamente AHORA antes de hacer cambios.

### Tareas

- [ ] **Ejecutar todos los 17 demos de Quarkus**
  ```bash
  # Crear script de validación
  for demo in annotations cdi interceptors managed-beans jax-rs json-processing \
              json-binding bean-validation jpa transactions batch jakarta-data \
              nosql panache security mvc renarde; do
    echo "Testing $demo..."
    cd quarkus-demos/$demo
    mvn clean package -q
    cd ../..
  done
  ```

- [ ] **Compilar proyecto common**
  ```bash
  cd common && mvn clean install -q && cd ..
  ```

- [ ] **Verificar versiones**
  - Quarkus: 3.30.2 ✅
  - Java: 21 ✅
  - Jakarta EE: 11 ✅

- [ ] **Revisar que cada demo tenga**
  - [ ] README.md con descripción clara
  - [ ] pom.xml con dependencias correctas
  - [ ] Código funcional (main class o endpoint)
  - [ ] Documentación de cómo ejecutar

---

## 📊 FASE 1: IMPLEMENTAR VALIDADORES (1 semana)

### Objetivo
Cumplir la promesa del README sobre `SpecValidator` y endpoints REST.

### Tareas

#### 1.1 Crear aplicación `validator` en common/
```
common/
├── validator/
│   ├── pom.xml
│   ├── src/main/java/com/jakartaee/utils/
│   │   ├── SpecValidator.java
│   │   ├── SpecReport.java
│   │   └── SpecStatus.java
│   └── README.md
```

**Funcionalidad:**
```java
// Clase principal
public class SpecValidator {
    public boolean isAvailable(String specName) { }
    public SpecReport getReport() { }
    public SpecStatus checkSpec(String specName) { }
}

// Entidad de reporte
public class SpecReport {
    String name;
    String version;
    List<SpecStatus> specs;
    int totalSpecs;
    int availableSpecs;
    double completeness; // porcentaje
}
```

#### 1.2 Crear REST endpoints en demo específico
- [ ] Crear `quarkus-demos/validator/` con endpoints:
  - `GET /specs/all` - Lista todas las specs
  - `GET /specs/report` - Reporte JSON completo
  - `GET /specs/check/{spec}` - Verificar una spec

#### 1.3 Documentar en README
- Explicar qué es cada spec
- Estado de implementación
- Cómo verificar disponibilidad

**Deadline:** 1 semana

---

## 🔧 FASE 2: MEJORAR CI/CD (1-2 semanas)

### Objetivo
Automatizar validación de todos los demos.

### Tareas

#### 2.1 Crear GitHub Actions
- [ ] Workflow para compilar todos los demos
- [ ] Workflow para ejecutar tests
- [ ] Workflow para documentación

**Archivo:** `.github/workflows/validate-demos.yml`

```yaml
name: Validate All Demos
on: [push, pull_request]
jobs:
  validate:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '21'
      - run: ./scripts/validate-all-specs.sh quarkus
```

#### 2.2 Mejorar scripts de validación
- [ ] Actualizar `validate-spec.sh`
- [ ] Actualizar `validate-all-specs.sh`
- [ ] Agregar salida JSON
- [ ] Agregar generador de reportes

#### 2.3 Agregar tests
- [ ] Tests unitarios para utils
- [ ] Tests de integración para endpoints

**Deadline:** 2 semanas

---

## 📚 FASE 3: AMPLIAR DOCUMENTACIÓN (2 semanas)

### Objetivo
Documentar mejor qué está implementado y qué no.

### Tareas

#### 3.1 Actualizar README.md principal
- [ ] Sección clara: "Qué está implementado"
- [ ] Sección: "Qué NO está implementado en Quarkus"
- [ ] Tabla de comparación Quarkus vs WildFly Core vs WildFly Web vs WildFly Platform
- [ ] Links a fuentes oficiales

#### 3.2 Crear matriz de cobertura
- [ ] Tabla HTML/Markdown mostrando:
  - Especificación
  - Core Profile 11
  - Web Profile 11
  - Platform 11
  - Estado en Quarkus
  - Estado en WildFly

#### 3.3 Crear guía "Quick Start"
- [ ] Cómo ejecutar un demo (5 min)
- [ ] Cómo ejecutar todos los demos (15 min)
- [ ] Cómo agregar un nuevo demo

#### 3.4 Mejorar comentarios en código
- [ ] Cada clase debe tener JavaDoc
- [ ] Explicar por qué se usa X patrón
- [ ] Links a documentación oficial

**Deadline:** 2 semanas

---

## 🏗️ FASE 4: MEJORAS A DEMOS EXISTENTES (2-3 semanas)

### Objetivo
Profundizar y mejorar los demos que ya existen.

### Tareas

#### 4.1 Interceptors - Ampliar casos de uso
**Actual:** Solo básico
**Mejorar:**
- [ ] Caché interceptor funcional
- [ ] Logging interceptor con niveles
- [ ] Timing interceptor
- [ ] Validación interceptor
- [ ] Múltiples interceptores encadenados
- [ ] Interceptor para constructores `@AroundConstruct`
- [ ] Lifecycle interceptor
- Ejemplo: método se llamará múltiples veces para mostrar caché

#### 4.2 Managed Beans - Hacer más realista
**Actual:** Muy básico, poco útil
**Mejorar:**
- [ ] Comparativa clara: Managed Beans vs CDI Beans
- [ ] Casos donde Managed Beans es preferible
- [ ] Inyección en Managed Beans
- [ ] Lifecycle completo (`@PostConstruct`, `@PreDestroy`)

#### 4.3 Security - Aclarar que NO es Jakarta Security
**Actual:** Menciona que es Quarkus Security pero podría ser más claro
**Mejorar:**
- [ ] Sección grande aclarando: "Esto NO es Jakarta Security"
- [ ] Tabla comparativa: Quarkus Security vs Jakarta Security
- [ ] Notas sobre cómo sería con Jakarta Security en WildFly

#### 4.4 MVC - Organizar mejor
**Actual:** mvc/ y renarde/ separados, pero podría haber confusión
**Mejorar:**
- [ ] Crear demo comparativo: Qute vs Renarde vs Jakarta MVC (en WildFly)
- [ ] Deixar claro cuál es estándar y cuál no

#### 4.5 Agregar más ejemplos a cada demo
- [ ] CDI: Más casos de Qualifiers, Producers, Events
- [ ] JPA: Relaciones complejas (OneToMany, ManyToMany)
- [ ] Batch: Checkpointing y restart
- [ ] Transactions: Casos de rollback

**Deadline:** 3 semanas

---

## 🌍 FASE 5: CREAR DEMOSTRACIONES DE WILDFLY (4-6 semanas)

### Objetivo
Mostrar implementaciones en WildFly de specs que no están en Quarkus.

### Estructura
```
wildfly-core-profile/          # Que ya existe, completar
wildfly-web-profile/            # NUEVO
│   ├── servlet/
│   ├── jsp/
│   ├── jsf/
│   ├── websocket/
│   └── mvc/
wildfly-platform/              # NUEVO
    ├── ejb/
    ├── jms/
    ├── mail/
    ├── security/
    ├── jca/
    ├── concurrency/
    └── activation/
```

### Tareas

#### 5.1 WildFly Web Profile (Fases 5.1-5.5)
- [ ] **Jakarta Servlet** - Servlets básicos vs JAX-RS
- [ ] **Jakarta JSP** - Páginas JSP con taglibs
- [ ] **Jakarta Faces (JSF)** - Componentes UI, managed beans
- [ ] **Jakarta WebSocket** - Comunicación bidireccional
- [ ] **Jakarta MVC estándar** - Comparar con Qute/Renarde

#### 5.2 WildFly Platform (Fases 5.6-5.12)
- [ ] **Jakarta EJB** - Stateless, Stateful, Message-driven
- [ ] **Jakarta JMS** - Point-to-Point, Pub/Sub
- [ ] **Jakarta Mail** - Enviar emails
- [ ] **Jakarta Security** - Realm-based security
- [ ] **Jakarta Concurrency** - Async tasks
- [ ] **Jakarta Connectors** - Resource adapters
- [ ] **Jakarta Activation** - MIME types

#### 5.3 Para cada demo:
- [ ] README.md con descripción
- [ ] Código funcional
- [ ] Cómo ejecutar en WildFly
- [ ] Comparativa con equivalente en Quarkus (si existe)

**Deadline:** 4-6 semanas (largo plazo)

---

## 📝 FASE 6: CONTENIDO DE BLOG (3 semanas)

### Objetivo
Completar y mejorar posts de blog.

### Tareas

#### 6.1 Terminar posts existentes
- [ ] 01-jakarta-cdi-en-quarkus.md - Completar
- [ ] 02-jakarta-rest-en-quarkus.md - Completar
- [ ] ... (revisar los 12 posts)

#### 6.2 Crear nuevos posts
- [ ] **Comparativa: Quarkus vs WildFly** - Cuándo usar cada uno
- [ ] **Jakarta EE Perfiles explicados** - Core vs Web vs Platform
- [ ] **Quarkus Security vs Jakarta Security** - Diferencias
- [ ] **Qute vs Jakarta MVC** - Enfoques diferentes
- [ ] **Panache: Repository vs Active Record** - Patrones
- [ ] **Java 21 + Jakarta EE 11** - Best practices
- [ ] **Guía de migración** - De Jakarta EE 10 a 11
- [ ] **Dev Services en Quarkus** - Desarrollo sin Docker
- [ ] **Native Image con Quarkus** - Compilación nativa
- [ ] **Performance: Quarkus vs WildFly**

#### 6.3 Crear posts de casos de uso
- [ ] Microservicios con Quarkus
- [ ] Monolito tradicional con WildFly Platform
- [ ] API REST moderna con Quarkus + Panache
- [ ] Web app con JSF + EJB

**Deadline:** 3 semanas

---

## 🎓 FASE 7: MATERIAL DE ENTRENAMIENTO (2-3 semanas)

### Objetivo
Crear material para que otros aprendan fácilmente.

### Tareas

#### 7.1 Crear laboratorios paso a paso
- [ ] Lab 1: "Tu primer endpoint REST"
- [ ] Lab 2: "CDI y Dependency Injection"
- [ ] Lab 3: "Persistencia con JPA"
- [ ] Lab 4: "Validaciones y errores"
- [ ] Lab 5: "Transacciones"
- [ ] Lab 6: "Seguridad básica"

Cada lab debe tener:
- Objetivo claro
- Pasos numerados
- Código para copiar/pegar
- Solución completa
- "Desafío" extra

#### 7.2 Crear ejercicios
- [ ] "Completa el endpoint"
- [ ] "Arregla el bug"
- [ ] "Refactoriza este código"
- [ ] Quiz de múltiple elección

#### 7.3 Crear soluciones
- [ ] Repositorio separado con soluciones
- [ ] Ramas por dificultad (beginner, intermediate, advanced)

**Deadline:** 3 semanas

---

## 🐳 FASE 8: DOCKER Y DEPLOYMENT (2-3 semanas)

### Objetivo
Demostrar cómo desplegar en producción.

### Tareas

#### 8.1 Crear Dockerfiles
- [ ] `Dockerfile.jvm` - Image JVM estándar
- [ ] `Dockerfile.native` - Image nativa (GraalVM)
- [ ] `Dockerfile.multistage` - Construcción optimizada

#### 8.2 Crear Docker Compose
- [ ] `docker-compose.yml` - Levanta todos los servicios
- [ ] Con PostgreSQL, MongoDB, otros servicios necesarios

#### 8.3 Crear manifests de Kubernetes
- [ ] `k8s/deployment.yaml`
- [ ] `k8s/service.yaml`
- [ ] `k8s/configmap.yaml` y `secret.yaml`
- [ ] `k8s/ingress.yaml`

#### 8.4 Crear guía de deployment
- [ ] Cómo desplegar a Docker
- [ ] Cómo desplegar a Kubernetes
- [ ] Cómo desplegar a cloud (AWS, GCP, Azure)
- [ ] Consideraciones de producción

**Deadline:** 3 semanas

---

## 📊 FASE 9: BENCHMARKS Y PERFORMANCE (Opcional, 2-3 semanas)

### Objetivo
Mostrar performance y comparaciones.

### Tareas

- [ ] Crear aplicación de benchmark
- [ ] Comparar: Quarkus JVM vs Quarkus Native vs WildFly
- [ ] Medir: Startup time, memory, throughput
- [ ] Generar gráficos y reportes

---

## 🎯 PRIORIDADES RECOMENDADAS

### Semana 1-2
1. FASE 0 - Validar todo funciona
2. FASE 1 - Validadores
3. FASE 2 - CI/CD

### Semana 3-4
4. FASE 3 - Documentación
5. FASE 4 - Mejorar demos existentes

### Semana 5-6
6. FASE 6 - Posts de blog
7. FASE 7 - Material de entrenamiento

### Semana 7-8
8. FASE 5 - WildFly demos (larga, puede ser paralelo)

### Semana 9+
9. FASE 8 - Docker/Kubernetes
10. FASE 9 - Benchmarks

---

## 📈 CHECKLIST PARA EL WEBINAR ACTUAL

Si el webinar es PRONTO, enfócate en esto:

- [x] Todos los 17 demos de Quarkus compilados ✅
- [ ] Aclarar en cada demo si es Core Profile, Web Profile o extra
- [ ] Mejorar explicación de "Esto NO es Jakarta Security"
- [ ] Mejorar explicación de "Esto NO es Jakarta MVC estándar"
- [ ] Script de validación funcional
- [ ] Todos los endpoints documentados
- [ ] Casos de error documentados

---

## 🚀 RESUMEN EJECUTIVO

### Hoy (fase 0)
Validar que TODO funciona

### Esta semana (fase 1-2)
Implementar validadores y CI/CD

### Próximas 2-3 semanas (fase 3-4)
Mejorar documentación y demos

### Próximo mes (fase 5-7)
Agregar WildFly profiles, posts, labs

### Largo plazo (fase 8-9)
Docker, Kubernetes, benchmarks

---

## 💡 Notas Importantes

1. **El proyecto está en BUEN estado** - Tiene 17 demos funcionales
2. **Hay MUCHO potencial** - Puede crecer significativamente
3. **Priorizar** - No hacer todo de una vez
4. **Mantener consistencia** - Tema de Heroes/Villanos en TODO
5. **Documentar** - Cada cambio debe ser documentado


