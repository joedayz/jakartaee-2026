# Jakarta Batch Demo

## Descripción

Este demo muestra cómo usar **Jakarta Batch** para procesar lotes de datos relacionados con Heroes y Villanos de DC Comics.

## Objetivo

Aprender a:
- Crear jobs batch con Jakarta Batch
- Implementar `ItemReader`, `ItemProcessor`, `ItemWriter`
- Usar `Batchlet` para tareas simples
- Ejecutar y monitorear jobs batch

## Tema DC

Procesamiento por lotes de:
- **Importar Heroes desde CSV**: Leer un archivo CSV e importar héroes a la base de datos
- **Calcular Estadísticas**: Procesar todos los héroes y calcular estadísticas de poder
- **Generar Reportes**: Crear reportes de actividad de héroes y villanos

## Jobs Batch Incluidos

### 1. Import Heroes Job
Importa héroes desde un archivo CSV.

### 2. Power Statistics Job
Calcula estadísticas de poder de todos los héroes.

### 3. Hero Report Job
Genera un reporte de todos los héroes activos.

## Dependencias

```xml
<!-- Jakarta REST para endpoints de control -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- Jakarta Batch usando Quarkiverse JBeret -->
<dependency>
    <groupId>io.quarkiverse.jberet</groupId>
    <artifactId>quarkus-jberet</artifactId>
    <version>2.4.0</version>
</dependency>

<!-- H2 Database (requerido para JBeret) -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-h2</artifactId>
</dependency>
```

**Versión**: Quarkus 3.28.2 con `quarkus-jberet` 2.6.0

**Nota**: Esta demo usa Quarkus 3.28.2 (en lugar de 3.30.2) porque es la versión probada y compatible con `quarkus-jberet` 2.6.0. La versión 2.6.0 de `quarkus-jberet` resuelve los problemas de compatibilidad con Quarkus 3.x.

## Cómo Ejecutar

```bash
cd quarkus-demos/batch
mvn quarkus:dev
```

## Ejemplos de Uso

### Ejecutar Job de Importación
```bash
curl -X POST http://localhost:8080/api/batch/jobs/import-heroes/start
```

### Ver Estado del Job
```bash
curl http://localhost:8080/api/batch/jobs/executions/{executionId}
```

### Listar Todos los Jobs
```bash
curl http://localhost:8080/api/batch/jobs
```

## Validación

Para validar que Jakarta Batch está funcionando:

1. Ejecutar un job y verificar que completa correctamente
2. Verificar el estado del job con el endpoint de ejecuciones
3. Verificar que los datos se procesan correctamente

```bash
# Listar jobs disponibles
curl http://localhost:8080/api/batch/jobs

# Ejecutar un job
curl -X POST http://localhost:8080/api/batch/jobs/import-heroes/start

# Ver estado de la ejecución
curl http://localhost:8080/api/batch/jobs/executions/1
```

## Estructura del Código

```
batch/
├── src/main/java/com/jakartaee/batch/
│   ├── batchlet/
│   │   └── HeroReportBatchlet.java        # Batchlet para generar reportes
│   ├── config/
│   │   └── DataInitializer.java           # Inicialización de datos
│   ├── reader/
│   │   ├── HeroImportReader.java          # Lee héroes desde CSV
│   │   └── HeroItemReader.java            # Lee héroes desde BD
│   ├── processor/
│   │   ├── HeroImportProcessor.java       # Procesa héroes importados
│   │   └── PowerStatisticsProcessor.java  # Calcula estadísticas de poder
│   ├── writer/
│   │   ├── HeroImportWriter.java          # Escribe héroes en BD
│   │   └── PowerStatisticsWriter.java     # Escribe estadísticas
│   └── resource/
│       └── BatchResource.java             # Endpoint REST para controlar jobs
├── src/main/resources/
│   ├── application.properties
│   └── META-INF/batch-jobs/
│       ├── hero-report.xml                # Definición del job de reportes
│       ├── import-heroes.xml              # Definición del job de importación
│       └── power-statistics.xml           # Definición del job de estadísticas
└── pom.xml
```

