# Jakarta Transactions Demo

## Descripción

Este demo muestra cómo usar **Jakarta Transactions** en Quarkus para manejar transacciones de base de datos de forma declarativa y programática. Demuestra diferentes tipos de transacciones, rollback automático y manual, y operaciones transaccionales complejas.

## Objetivo

Aprender a:
- Usar `@Transactional` para transacciones declarativas
- Entender diferentes tipos de transacciones (REQUIRED, REQUIRES_NEW, MANDATORY, etc.)
- Manejar rollback automático y manual
- Implementar transacciones con múltiples operaciones atómicas
- Usar `TransactionManager` para control programático
- Configurar timeout y condiciones de rollback personalizadas
- Manejar transacciones anidadas

## Tema DC

Operaciones transaccionales con héroes y villanos de DC Comics:
- **PowerTransfer**: Transferencias de poder entre héroes (operaciones atómicas)
- **Battle**: Batallas entre héroes y villanos (transacciones complejas)
- Diferentes tipos de transacciones demostradas con operaciones reales

## Soporte en Quarkus

✅ **Transactions está completamente soportado en Quarkus 3.30.2**.

Este demo usa:
- **Quarkus 3.30.2** con la nueva API REST (`quarkus-rest`)
- **Jakarta Transactions** a través de `quarkus-narayana-jta`
- **Hibernate ORM** para persistencia
- **Transacciones declarativas** con `@Transactional`
- **TransactionManager** para control programático

## Dependencias

```xml
<!-- Jakarta RESTful Web Services -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- Hibernate ORM (implementación de JPA) -->
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

**Nota**: Quarkus incluye soporte para transacciones automáticamente cuando usas Hibernate ORM. No necesitas agregar una dependencia explícita para `quarkus-narayana-jta` en la mayoría de los casos.

## Características de Transactions Demostradas

### 1. Transacción Básica

```java
@Transactional
public Hero createHero(String name, String power, Integer powerLevel) {
    Hero hero = new Hero(name, power, powerLevel);
    entityManager.persist(hero);
    entityManager.flush();
    return hero;
}
```

### 2. Tipos de Transacciones

#### REQUIRED (por defecto)

```java
@Transactional(Transactional.TxType.REQUIRED)
public Hero createHeroRequired(String name, String power, Integer powerLevel) {
    // Usa transacción existente si existe, sino crea una nueva
    Hero hero = new Hero(name, power, powerLevel);
    entityManager.persist(hero);
    return hero;
}
```

#### REQUIRES_NEW

```java
@Transactional(Transactional.TxType.REQUIRES_NEW)
public void logOperation(String message) {
    // Siempre crea una nueva transacción
    // Útil para operaciones que deben ejecutarse independientemente
    logger.info("Logging: " + message);
}
```

#### MANDATORY

```java
@Transactional(Transactional.TxType.MANDATORY)
public void updateHeroPower(Long heroId, Integer newPowerLevel) {
    // Requiere que exista una transacción activa
    // Lanza excepción si no hay transacción
    Hero hero = entityManager.find(Hero.class, heroId);
    hero.setPowerLevel(newPowerLevel);
}
```

#### SUPPORTS

```java
@Transactional(Transactional.TxType.SUPPORTS)
public Hero findHero(Long id) {
    // Usa transacción si existe, sino ejecuta sin transacción
    return entityManager.find(Hero.class, id);
}
```

#### NOT_SUPPORTED

```java
@Transactional(Transactional.TxType.NOT_SUPPORTED)
public String readOnlyOperation(Long heroId) {
    // Suspende cualquier transacción existente
    // Ejecuta sin transacción
    Hero hero = entityManager.find(Hero.class, heroId);
    return hero.getName();
}
```

#### NEVER

```java
@Transactional(Transactional.TxType.NEVER)
public String nonTransactionalOperation(Long heroId) {
    // Lanza excepción si hay una transacción activa
    // Debe ejecutarse sin transacción
    Hero hero = entityManager.find(Hero.class, heroId);
    return hero.getName();
}
```

### 3. Rollback Automático

Las transacciones hacen rollback automáticamente cuando se lanza una excepción no marcada:

```java
@Transactional
public PowerTransfer transferPowerWithFailure(Long fromHeroId, Long toHeroId, Integer amount) {
    // ... operaciones ...
    
    // Si se lanza una excepción, todas las operaciones se revierten
    throw new RuntimeException("Error - transaction will rollback");
}
```

### 4. Rollback Manual

Puedes marcar una transacción para rollback manualmente:

```java
@Inject
TransactionManager transactionManager;

@Transactional
public void transferPowerWithManualRollback(Long fromHeroId, Long toHeroId, Integer amount) {
    try {
        // ... validaciones ...
        
        if (amount > 50) {
            // Marcar para rollback manual
            transactionManager.setRollbackOnly();
            throw new IllegalArgumentException("Amount too large");
        }
        
        // ... operaciones ...
    } catch (Exception e) {
        transactionManager.setRollbackOnly();
        throw e;
    }
}
```

### 5. Transacciones con Múltiples Operaciones

Las transacciones garantizan que múltiples operaciones sean atómicas:

```java
@Transactional
public PowerTransfer transferPower(Long fromHeroId, Long toHeroId, Integer amount) {
    // 1. Validar héroes
    Hero fromHero = entityManager.find(Hero.class, fromHeroId);
    Hero toHero = entityManager.find(Hero.class, toHeroId);
    
    // 2. Crear registro de transferencia
    PowerTransfer transfer = new PowerTransfer(fromHeroId, toHeroId, amount);
    entityManager.persist(transfer);
    
    // 3. Actualizar niveles de poder (operaciones atómicas)
    fromHero.setPowerLevel(fromHero.getPowerLevel() - amount);
    toHero.setPowerLevel(toHero.getPowerLevel() + amount);
    
    entityManager.merge(fromHero);
    entityManager.merge(toHero);
    
    // 4. Completar transferencia
    transfer.setStatus(PowerTransfer.TransferStatus.COMPLETED);
    
    // Si cualquier operación falla, todas se revierten
    return transfer;
}
```

### 6. Timeout de Transacciones

Puedes especificar un timeout para transacciones:

```java
@Transactional(timeout = 5) // 5 segundos
public void longRunningOperation() throws InterruptedException {
    // Si la operación excede 5 segundos, se cancela
    Thread.sleep(6000); // Excederá el timeout
}
```

### 7. Condiciones de Rollback Personalizadas

Puedes especificar qué excepciones causan rollback:

```java
@Transactional(
    rollbackOn = {IllegalArgumentException.class, RuntimeException.class},
    dontRollbackOn = {IllegalStateException.class}
)
public void transferWithCustomRollback(Long fromHeroId, Long toHeroId, Integer amount) {
    if (fromHero == null) {
        // IllegalArgumentException causa rollback
        throw new IllegalArgumentException("Hero not found");
    }
    
    if (amount < 0) {
        // IllegalStateException NO causa rollback
        throw new IllegalStateException("Negative amount - no rollback");
    }
}
```

### 8. Transacciones Anidadas

Las transacciones pueden anidarse usando REQUIRES_NEW:

```java
@Transactional
public void nestedTransactionExample(Long heroId) {
    Hero hero = entityManager.find(Hero.class, heroId);
    
    // Esta llamada crea una nueva transacción independiente
    logOperation("Processing hero: " + hero.getName());
    
    // Si esta transacción hace rollback, el log anterior NO se revierte
    // porque se ejecutó en REQUIRES_NEW
}
```

## Estructura del Proyecto

```
transactions/
├── pom.xml
├── src/main/java/com/jakartaee/transactions/
│   ├── entity/
│   │   ├── PowerTransfer.java      # Entidad para transferencias
│   │   └── Battle.java             # Entidad para batallas
│   ├── service/
│   │   └── TransactionDemoService.java  # Servicio con ejemplos de transacciones
│   ├── resource/
│   │   └── TransactionResource.java     # REST endpoints
│   └── config/
│       └── DataInitializer.java         # Inicialización de datos
└── src/main/resources/
    └── application.properties            # Configuración
```

## Cómo Ejecutar

### Requisitos Previos

- Java 21
- Maven 3.9+

### Pasos

```bash
# 1. Asegúrate de que el módulo common esté compilado
cd ../../common
mvn clean install

# 2. Compila el proyecto
cd ../quarkus-demos/transactions
mvn clean compile

# 3. Ejecuta el demo en modo desarrollo
mvn quarkus:dev
```

La aplicación estará disponible en: `http://localhost:8080`

## Endpoints Disponibles

### Heroes

- `GET /api/transactions/hero/create?name=Superman&power=Super strength&powerLevel=95` - Crea un héroe con transacción básica

### Power Transfers

- `POST /api/transactions/power-transfer` - Transfiere poder entre héroes (transacción atómica)
- `POST /api/transactions/power-transfer/fail` - Transfiere poder que falla (demuestra rollback automático)
- `POST /api/transactions/power-transfer/manual-rollback` - Transfiere poder con rollback manual
- `GET /api/transactions/transfers` - Obtiene todas las transferencias
- `GET /api/transactions/transfers/{id}` - Obtiene una transferencia por ID

### Battles

- `POST /api/transactions/battle` - Ejecuta una batalla entre héroe y villano (transacción compleja)
- `GET /api/transactions/battles` - Obtiene todas las batallas
- `GET /api/transactions/battles/{id}` - Obtiene una batalla por ID

### Información

- `GET /api/transactions/info` - Información sobre características de transacciones demostradas

## Ejemplos de Uso

### Crear un Héroe

```bash
curl "http://localhost:8080/api/transactions/hero/create?name=Aquaman&power=Control%20del%20agua&powerLevel=82"
```

### Transferir Poder entre Héroes

```bash
curl -X POST http://localhost:8080/api/transactions/power-transfer \
  -H "Content-Type: application/json" \
  -d '{
    "fromHeroId": 1,
    "toHeroId": 2,
    "amount": 10
  }'
```

### Transferencia que Falla (Rollback Automático)

```bash
curl -X POST http://localhost:8080/api/transactions/power-transfer/fail \
  -H "Content-Type: application/json" \
  -d '{
    "fromHeroId": 1,
    "toHeroId": 2,
    "amount": 10
  }'
```

### Ejecutar una Batalla

```bash
curl -X POST http://localhost:8080/api/transactions/battle \
  -H "Content-Type: application/json" \
  -d '{
    "heroId": 1,
    "villainId": 1
  }'
```

### Obtener Todas las Transferencias

```bash
curl http://localhost:8080/api/transactions/transfers
```

## Características Clave de Transactions en Quarkus

### ✅ Transacciones Declarativas

- `@Transactional` es suficiente para la mayoría de casos
- No necesitas código adicional para manejar transacciones
- Integración automática con JPA/Hibernate

### ✅ Tipos de Transacciones

- **REQUIRED**: Usa existente o crea nueva (por defecto)
- **REQUIRES_NEW**: Siempre crea nueva transacción
- **MANDATORY**: Requiere transacción existente
- **SUPPORTS**: Usa si existe, sino sin transacción
- **NOT_SUPPORTED**: Suspende transacción existente
- **NEVER**: No permite transacción activa

### ✅ Rollback Automático

- Las excepciones no marcadas causan rollback automático
- Puedes especificar qué excepciones causan rollback
- Puedes especificar qué excepciones NO causan rollback

### ✅ Control Programático

- `TransactionManager` para control manual
- `setRollbackOnly()` para marcar rollback manual
- Timeout configurable

### ✅ Operaciones Atómicas

- Múltiples operaciones en una transacción son atómicas
- Si una falla, todas se revierten
- Garantiza consistencia de datos

## Comparación de Tipos de Transacciones

| Tipo | Transacción Existente | Comportamiento |
|------|----------------------|----------------|
| REQUIRED | Sí | Usa la existente |
| REQUIRED | No | Crea nueva |
| REQUIRES_NEW | Sí | Suspende y crea nueva |
| REQUIRES_NEW | No | Crea nueva |
| MANDATORY | Sí | Usa la existente |
| MANDATORY | No | Lanza excepción |
| SUPPORTS | Sí | Usa la existente |
| SUPPORTS | No | Sin transacción |
| NOT_SUPPORTED | Sí | Suspende y ejecuta sin transacción |
| NOT_SUPPORTED | No | Sin transacción |
| NEVER | Sí | Lanza excepción |
| NEVER | No | Sin transacción |

## Notas

- Quarkus usa Narayana JTA para manejar transacciones
- Las transacciones se propagan automáticamente entre métodos
- El rollback automático ocurre con excepciones no marcadas
- Las transacciones anidadas con REQUIRES_NEW son independientes
- El timeout se especifica en segundos

## Referencias

- [Jakarta Transactions Specification](https://jakarta.ee/specifications/transactions/)
- [Quarkus Transactions Guide](https://quarkus.io/guides/transaction)
- [Narayana JTA Documentation](https://narayana.io/)

