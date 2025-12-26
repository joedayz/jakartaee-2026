# Guía Rápida de Referencia - Webinar Jakarta EE en Quarkus

## ⚡ Referencia Rápida por Demo

### 1. Annotations
**Demo:** `quarkus-demos/annotations/`
**Tiempo:** 5 min
**Endpoint clave:** `GET /api/annotations/lifecycle`
**Punto clave:** `@PostConstruct`, `@PreDestroy`, `@Resource`

---

### 2. CDI
**Demo:** `quarkus-demos/cdi/`
**Tiempo:** 8 min
**Endpoints:**
- `GET /api/cdi/heroes`
- `GET /api/cdi/demo/power-analysis`
- `GET /api/cdi/demo/events`
**Punto clave:** Scopes, Qualifiers, Events, Producers

---

### 3. Interceptors
**Demo:** `quarkus-demos/interceptors/`
**Tiempo:** 7 min
**Endpoints:**
- `GET /api/interceptors/heroes` (mostrar caché)
- `GET /api/interceptors/info`
**Punto clave:** `@AroundInvoke`, `@AroundConstruct`, lifecycle, encadenados
**Mostrar logs:** Timing, Logging, Caching

---

### 4. Managed Beans
**Demo:** `quarkus-demos/managed-beans/`
**Tiempo:** 5 min
**Endpoints:**
- `GET /api/managed-beans/heroes`
- `GET /api/managed-beans/stats`
**Punto clave:** `@ManagedBean`, lifecycle, vs CDI

---

### 5. JAX-RS
**Demo:** `quarkus-demos/jax-rs/`
**Tiempo:** 6 min
**Endpoints:**
- `GET /api/heroes`
- `GET /api/heroes/{id}`
- `POST /api/heroes`
**Punto clave:** `@Path`, `@GET`, `@POST`, serialización automática

---

### 6. JSON Processing
**Demo:** `quarkus-demos/json-processing/`
**Tiempo:** 5 min
**Endpoints:**
- `GET /api/json-processing/create-object`
- `GET /api/json-processing/json-pointer`
- `GET /api/json-processing/json-patch`
**Punto clave:** Object Model, Streaming, JsonPointer, JsonPatch

---

### 7. JSON Binding
**Demo:** `quarkus-demos/json-binding/`
**Tiempo:** 6 min
**Endpoints:**
- `GET /api/json-binding/serialize`
- `GET /api/json-binding/team`
**Punto clave:** Anotaciones, Custom Adapters, Field Ordering

---

### 8. Bean Validation
**Demo:** `quarkus-demos/bean-validation/`
**Tiempo:** 6 min
**Endpoints:**
- `POST /api/heroes` (con validación exitosa)
- `POST /api/heroes` (con validación fallida)
**Punto clave:** `@Valid`, `@NotNull`, `@Size`, validadores personalizados

---

### 9. JPA
**Demo:** `quarkus-demos/jpa/`
**Tiempo:** 8 min
**Endpoints:**
- `GET /api/jpa/heroes`
- `GET /api/jpa/heroes/named-query?minLevel=80`
- `GET /api/jpa/demo/entity-manager-operations`
**Punto clave:** EntityManager, Named Queries, JPQL, Criteria API, relaciones

---

### 10. Transactions
**Demo:** `quarkus-demos/transactions/`
**Tiempo:** 6 min
**Endpoints:**
- `GET /api/transactions/demo/required`
- `GET /api/transactions/demo/rollback`
**Punto clave:** `@Transactional`, tipos, rollback

---

### 11. Batch
**Demo:** `quarkus-demos/batch/`
**Tiempo:** 7 min
**Endpoints:**
- `POST /api/batch/jobs/hero-import`
- `GET /api/batch/jobs/hero-import/executions`
**Punto clave:** ItemReader, ItemProcessor, ItemWriter, Batchlet

---

### 12. Jakarta Data
**Demo:** `quarkus-demos/jakarta-data/`
**Tiempo:** 6 min
**Endpoints:**
- `GET /api/jakarta-data/heroes`
- `GET /api/jakarta-data/panache-next/heroes`
**Punto clave:** Repositorios, Panache Next, Active Record

---

### 13. NoSQL (MongoDB)
**Demo:** `quarkus-demos/nosql/`
**Tiempo:** 6 min
**Endpoints:**
- `GET /api/nosql/heroes`
- `GET /api/nosql/heroes/by-city?city=Metropolis`
**Punto clave:** Panache MongoDB, documentos anidados, Dev Services

---

### 14. Panache
**Demo:** `quarkus-demos/panache/`
**Tiempo:** 5 min
**Endpoints:**
- `GET /api/panache/heroes/repository/powerful?minLevel=80`
- `GET /api/panache/heroes/active-record/powerful?minLevel=80`
**Punto clave:** Repository Pattern vs Active Record

---

### 15. Security
**Demo:** `quarkus-demos/security/`
**Tiempo:** 5 min
**Endpoints:**
- `curl -u superman:superman http://localhost:8080/api/protected/profile`
- `curl -u batman:batman http://localhost:8080/api/protected/heroes`
**Punto clave:** ⚠️ Quarkus Security (no Jakarta Security), `@Authenticated`, `@RolesAllowed`

---

### 16. MVC (Qute)
**Demo:** `quarkus-demos/mvc/`
**Tiempo:** 3 min
**Navegador:** `http://localhost:8080/heroes`
**Punto clave:** ⚠️ Qute básico (no Jakarta MVC), `@Inject Template`

---

### 17. Renarde
**Demo:** `quarkus-demos/renarde/`
**Tiempo:** 5 min
**Navegador:** `http://localhost:8080/heroes/list`
**Punto clave:** Convenciones automáticas, `Controller`, flash messages, type-safe redirects

---

## 🎯 Orden de Presentación Recomendado

### Bloque 1: Fundamentos (20 min)
1. Annotations (5 min)
2. CDI (8 min)
3. Interceptors (7 min)

### Bloque 2: REST y JSON (17 min)
4. JAX-RS (6 min)
5. JSON Processing (5 min)
6. JSON Binding (6 min)

### Bloque 3: Persistencia (21 min)
7. Bean Validation (6 min)
8. JPA (8 min)
9. Transactions (6 min)
10. Batch (7 min) - Opcional mover al final

### Bloque 4: Avanzado (17 min)
11. Jakarta Data (6 min)
12. NoSQL (6 min)
13. Panache (5 min)

### Bloque 5: Web y Seguridad (18 min)
14. Security (5 min)
15. MVC Qute (3 min)
16. Renarde (5 min)
17. Managed Beans (5 min) - Puede ir antes

---

## 💡 Tips de Presentación

### Para Cada Demo:

1. **Explicar el concepto** (30 seg)
2. **Mostrar código clave** (1 min)
3. **Ejecutar endpoint** (30 seg)
4. **Mostrar resultado** (30 seg)
5. **Explicar ventajas/uso** (30 seg)

### Transiciones:

- "Ahora veamos cómo Quarkus maneja..."
- "Pasemos a la siguiente especificación..."
- "Comparemos esto con..."

### Puntos a Repetir:

- "Quarkus implementa completamente el Core Profile 11"
- "Esto NO es parte del estándar Jakarta, pero..."
- "Veamos cómo esto se compara con..."

---

## 🚨 Errores Comunes a Evitar

1. ❌ Decir "Jakarta Security" cuando es "Quarkus Security"
2. ❌ Decir "Jakarta MVC" cuando es "Qute" o "Renarde"
3. ❌ Olvidar mencionar que algunas specs son "extras"
4. ❌ No explicar diferencias entre Core/Web/Platform Profiles

---

## ✅ Checklist Pre-Webinar

- [ ] Todos los demos compilados
- [ ] Terminales preparadas
- [ ] Navegador abierto
- [ ] Logs visibles
- [ ] MongoDB Dev Services funcionando
- [ ] Código fuente abierto
- [ ] READMEs revisados

---

## 📊 Tiempo Total Estimado

- Introducción: 10 min
- Core Profile: 60 min
- Adicionales: 25 min
- Resumen: 10 min
- **Total: 105 min** (1h 45min)

**Con Q&A extendido: 120 min** (2 horas)

