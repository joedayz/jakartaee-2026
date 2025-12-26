# Security Demo (Quarkus Security)

## Descripción

Este demo muestra cómo implementar **autenticación y autorización** en Quarkus usando **Quarkus Security**. Aunque Quarkus no implementa Jakarta Security estándar (que es parte del Platform Profile), Quarkus Security proporciona funcionalidades similares y es la forma recomendada de implementar seguridad en aplicaciones Quarkus.

## Objetivo

Aprender a:
- Implementar autenticación básica HTTP
- Usar `@Authenticated` para proteger endpoints
- Usar `@RolesAllowed` para autorización basada en roles
- Acceder al `SecurityContext` para obtener información del usuario
- Crear recursos públicos, protegidos y de administración
- Configurar usuarios y roles en `application.properties`

## Tema DC

Gestión de Heroes y Villanos de DC Comics con seguridad:
- **PublicResource**: Endpoints públicos sin autenticación
- **ProtectedResource**: Endpoints protegidos con diferentes roles (HERO, VILLAIN)
- **AdminResource**: Endpoints exclusivos para administradores (ADMIN)

## Soporte en Quarkus

⚠️ **Nota importante**: Quarkus NO implementa Jakarta Security estándar (que es parte del Platform Profile). En su lugar, Quarkus proporciona **Quarkus Security**, que es compatible con conceptos similares pero con una API diferente.

Este demo usa:
- **Quarkus 3.30.2** con la nueva API REST (`quarkus-rest`)
- **Quarkus Security** para autenticación y autorización
- **Autenticación básica HTTP** (Basic Authentication)
- **Autorización basada en roles** (`@RolesAllowed`)

## Dependencias

```xml
<!-- Jakarta RESTful Web Services -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- Quarkus Security -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-security</artifactId>
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

## Configuración de Seguridad

### Usuarios y Roles

Los usuarios se configuran en `application.properties`:

```properties
# Habilitar autenticación básica
quarkus.http.auth.proactive=true
quarkus.security.users.embedded.enabled=true
quarkus.security.users.embedded.plain-text=true

# Usuarios (formato: usuario=contraseña,roles)
quarkus.security.users.embedded.users.superman=superman,HERO,ADMIN
quarkus.security.users.embedded.users.batman=batman,HERO
quarkus.security.users.embedded.users.joker=joker,VILLAIN
quarkus.security.users.embedded.users.lex=lex,VILLAIN,ADMIN
```

### Usuarios Disponibles

| Usuario | Contraseña | Roles | Acceso |
|---------|------------|-------|--------|
| superman | superman | HERO, ADMIN | Todos los endpoints |
| batman | batman | HERO | Endpoints públicos y de HERO |
| joker | joker | VILLAIN | Endpoints públicos y de VILLAIN |
| lex | lex | VILLAIN, ADMIN | Todos los endpoints |

## Anotaciones de Seguridad

### @Authenticated

Requiere que el usuario esté autenticado, pero no verifica roles específicos:

```java
@Authenticated
@Path("/api/protected")
public class ProtectedResource {
    // Todos los endpoints requieren autenticación
}
```

### @RolesAllowed

Requiere que el usuario tenga uno de los roles especificados:

```java
@RolesAllowed("HERO")
@GET
@Path("/heroes")
public Response getHeroes() {
    // Solo usuarios con rol HERO pueden acceder
}
```

```java
@RolesAllowed({"HERO", "ADMIN"})
@GET
@Path("/heroes/{id}")
public Response getHeroById(@PathParam("id") Long id) {
    // Usuarios con rol HERO o ADMIN pueden acceder
}
```

### SecurityContext

Permite acceder a información del usuario autenticado:

```java
@Inject
SecurityContext securityContext;

public Response getProfile() {
    Principal principal = securityContext.getUserPrincipal();
    String username = principal.getName();
    // ...
}
```

## Estructura del Proyecto

```
security/
├── pom.xml
├── README.md
├── src/main/
│   ├── java/com/jakartaee/security/
│   │   ├── entity/
│   │   │   └── User.java              # Entidad de usuario (para futuras mejoras)
│   │   ├── resource/
│   │   │   ├── PublicResource.java    # Endpoints públicos
│   │   │   ├── ProtectedResource.java # Endpoints protegidos
│   │   │   └── AdminResource.java     # Endpoints de administración
│   │   └── config/
│   │       └── DataInitializer.java   # Inicializador de datos
│   └── resources/
│       └── application.properties     # Configuración de seguridad
```

## Endpoints REST

### Endpoints Públicos (Sin Autenticación)

#### Información pública
```bash
GET /api/public/info
```

#### Lista de héroes (pública)
```bash
GET /api/public/heroes
```

### Endpoints Protegidos (Requieren Autenticación)

#### Perfil del usuario autenticado
```bash
GET /api/protected/profile
Authorization: Basic <base64(username:password)>
```

**Ejemplo con curl:**
```bash
curl -u superman:superman http://localhost:8080/api/protected/profile
```

#### Lista de héroes (solo rol HERO)
```bash
GET /api/protected/heroes
Authorization: Basic <base64(username:password)>
```

**Ejemplo:**
```bash
curl -u batman:batman http://localhost:8080/api/protected/heroes
```

#### Lista de villanos (solo rol VILLAIN)
```bash
GET /api/protected/villains
Authorization: Basic <base64(username:password)>
```

**Ejemplo:**
```bash
curl -u joker:joker http://localhost:8080/api/protected/villains
```

#### Obtener héroe por ID (roles HERO o ADMIN)
```bash
GET /api/protected/heroes/{id}
Authorization: Basic <base64(username:password)>
```

#### Crear héroe (solo rol ADMIN)
```bash
POST /api/protected/heroes?name=Flash&power=Speed&powerLevel=88
Authorization: Basic <base64(username:password)>
```

#### Eliminar héroe (solo rol ADMIN)
```bash
DELETE /api/protected/heroes/{id}
Authorization: Basic <base64(username:password)>
```

### Endpoints de Administración (Solo rol ADMIN)

#### Dashboard de administración
```bash
GET /api/admin/dashboard
Authorization: Basic <base64(username:password)>
```

#### Lista de todos los héroes
```bash
GET /api/admin/heroes
Authorization: Basic <base64(username:password)>
```

#### Actualizar héroe
```bash
PUT /api/admin/heroes/{id}?name=Superman&powerLevel=96
Authorization: Basic <base64(username:password)>
```

#### Lista de usuarios
```bash
GET /api/admin/users
Authorization: Basic <base64(username:password)>
```

## Ejecutar el Demo

```bash
cd quarkus-demos/security
mvn quarkus:dev
```

La aplicación estará disponible en `http://localhost:8080`

## Probar los Endpoints

### 1. Endpoint Público (Sin Autenticación)

```bash
curl http://localhost:8080/api/public/info
```

**Respuesta esperada:**
```json
{
  "message": "Este es un endpoint público",
  "description": "No se requiere autenticación para acceder a este recurso"
}
```

### 2. Endpoint Protegido (Con Autenticación)

```bash
curl -u superman:superman http://localhost:8080/api/protected/profile
```

**Respuesta esperada:**
```json
{
  "username": "superman",
  "authenticated": true,
  "roles": ["HERO", "ADMIN"],
  "message": "Información de perfil del usuario autenticado"
}
```

### 3. Endpoint con Rol Específico

```bash
# Como HERO (funciona)
curl -u batman:batman http://localhost:8080/api/protected/heroes

# Como VILLAIN (falla - 403 Forbidden)
curl -u joker:joker http://localhost:8080/api/protected/heroes
```

### 4. Endpoint de Administración

```bash
curl -u superman:superman http://localhost:8080/api/admin/dashboard
```

**Respuesta esperada:**
```json
{
  "user": "superman",
  "role": "ADMIN",
  "heroCount": 5,
  "message": "Panel de administración"
}
```

## Casos de Uso

### 1. Proteger Endpoints Específicos

```java
@Path("/api/protected")
@Authenticated
public class ProtectedResource {
    // Todos los endpoints requieren autenticación
}
```

### 2. Autorización Basada en Roles

```java
@RolesAllowed("ADMIN")
@POST
@Path("/heroes")
public Response createHero(...) {
    // Solo administradores pueden crear héroes
}
```

### 3. Múltiples Roles Permitidos

```java
@RolesAllowed({"HERO", "ADMIN"})
@GET
@Path("/heroes/{id}")
public Response getHeroById(@PathParam("id") Long id) {
    // Héroes y administradores pueden ver detalles
}
```

### 4. Acceder a Información del Usuario

```java
@Inject
SecurityContext securityContext;

public Response getProfile() {
    Principal principal = securityContext.getUserPrincipal();
    String username = principal.getName();
    // Usar información del usuario
}
```

## Comparación: Quarkus Security vs Jakarta Security

| Característica | Quarkus Security | Jakarta Security |
|----------------|------------------|------------------|
| Disponibilidad | ✅ Disponible en Quarkus | ❌ No disponible (Platform Profile) |
| Autenticación | ✅ Básica, OAuth2, JWT | ✅ Estándar Jakarta |
| Autorización | ✅ `@RolesAllowed` | ✅ `@RolesAllowed` |
| Anotaciones | `@Authenticated`, `@RolesAllowed` | `@RolesAllowed`, `@PermitAll`, `@DenyAll` |
| SecurityContext | ✅ Disponible | ✅ Disponible |
| Integración | Nativa en Quarkus | Requiere Platform Profile |

## Ventajas de Quarkus Security

1. **Integración Nativa**: Funciona perfectamente con Quarkus
2. **Rendimiento**: Optimizado para aplicaciones cloud-native
3. **Flexibilidad**: Soporta múltiples métodos de autenticación
4. **Simplicidad**: Configuración sencilla en `application.properties`

## Limitaciones

1. **No es Jakarta Security Estándar**: API diferente al estándar Jakarta
2. **Solo en Quarkus**: No es portable a otros runtimes Jakarta EE
3. **Configuración Específica**: Requiere configuración específica de Quarkus

## Mejores Prácticas

1. **Usar Roles Específicos**: Define roles claros y específicos para tu aplicación
2. **Proteger por Defecto**: Usa `@Authenticated` a nivel de clase cuando sea posible
3. **Principio de Menor Privilegio**: Asigna solo los roles necesarios a cada usuario
4. **No Exponer Credenciales**: En producción, usa almacenamiento seguro de contraseñas
5. **Usar HTTPS**: Siempre usa HTTPS en producción

## Alternativas Avanzadas

### JWT (JSON Web Tokens)

Para aplicaciones más complejas, puedes usar JWT:

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-jwt</artifactId>
</dependency>
```

### OAuth2 / OpenID Connect

Para integración con proveedores de identidad:

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-oidc</artifactId>
</dependency>
```

### Keycloak Integration

Para gestión avanzada de identidades:

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-keycloak-authorization</artifactId>
</dependency>
```

## Referencias

- [Quarkus Security Guide](https://quarkus.io/guides/security)
- [Quarkus Security Basic Authentication](https://quarkus.io/guides/security-basic-authentication-howto)
- [Quarkus Security JWT](https://quarkus.io/guides/security-jwt)
- [Jakarta Security Specification](https://jakarta.ee/specifications/security/) (no disponible en Quarkus)

## Nota Final

Este demo usa **Quarkus Security**, que aunque no es Jakarta Security estándar, proporciona funcionalidades similares y es la forma recomendada de implementar seguridad en aplicaciones Quarkus. Para aplicaciones que requieren Jakarta Security estándar, considera usar WildFly Platform.

