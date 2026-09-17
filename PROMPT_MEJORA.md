# Prompt para Mejorar el Codigo Base

Copia y pega el contenido del bloque de abajo en un asistente de IA (Claude, ChatGPT)
para obtener un ZIP con el proyecto completo y arrancable.

Si preferis trabajar en tu editor con un agente local (Claude Code, Cursor, Copilot), usa `AGENTS.md` en vez de este archivo: dice lo mismo pero para que escriba los archivos en disco.

## Las dos reglas que no se negocian

1. **Completa el boilerplate.** Todo lo que el proyecto necesita para compilar y arrancar: manifiesto de dependencias, punto de entrada, configuracion, capa de interfaz, y las capas del patron arquitectonico declarado. Eso es andamiaje y es tu trabajo.
2. **NO resuelvas el reto.** Los entregables de las fases son el trabajo de la persona. El hueco pedagogico se deja como esta: el proyecto arranca, pero lo que el reto pide implementar NO esta implementado.

Dicho de otra forma: si algo impide compilar, arreglalo. Si algo es logica de negocio incompleta, validaciones ausentes, un secreto hardcodeado o un patron mejorable, dejalo exactamente como esta — es lo que la persona tiene que encontrar.

## Lo que le falta a este proyecto

Esto NO lo tenes que adivinar: salio de comparar el proyecto contra la arquitectura declarada del reto y de un analisis estatico del codigo. Completalo TODO.

### Boilerplate del stack que falta

Sin esto no compila ni arranca. Es andamiaje, no toca nada de lo pedagogico:

- **Punto de entrada del stack elegido** — Sin un punto de entrada reconocible, el runtime no tiene por donde arrancar la aplicacion.
- **Capa de interfaz (controller/handler)** — Sin una capa de interfaz explicita, no hay forma de invocar la logica de negocio desde afuera del proceso.

### Referencias colgando en el codigo que si esta

Cada una rompe la compilacion:

- `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `Processing`: Processing se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.infrastructure.config.Processing.
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `Status`: Status se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.model.Status.
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `Matched`: Matched se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.events.Matched.
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `Mismatched`: Mismatched se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.events.Mismatched.
- `src/test/java/com/pragma/reconciliation/domain/model/ReconciliationTest.java` — `Matching`: Matching se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.infrastructure.config.Matching.
- `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `Created`: Created se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.events.Created.
- `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `Idempotency`: Idempotency se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.infrastructure.config.Idempotency.
- `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `Matched`: Matched se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.events.Matched.
- `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `Mismatched`: Mismatched se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.events.Mismatched.
- `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `ManualReviewEscalation`: ManualReviewEscalation se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.events.ManualReviewEscalation.
- `src/main/java/com/pragma/reconciliation/domain/events/ReconciliationEvent.java` — `Reconciliation`: El import com.pragma.reconciliation.domain.model.Reconciliation no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `reactor.core.publisher`: El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListener.java` — `com.fasterxml.jackson`: El import com.fasterxml.jackson.databind.ObjectMapper pertenece a com.fasterxml.jackson, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/ReconciliationEventPublisher.java` — `com.fasterxml.jackson`: El import com.fasterxml.jackson.databind.ObjectMapper pertenece a com.fasterxml.jackson, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `com.fasterxml.jackson`: El import com.fasterxml.jackson.databind.ObjectMapper pertenece a com.fasterxml.jackson, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `reactor.core.publisher`: El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/main/java/com/pragma/reconciliation/domain/model/Reconciliation.java` — `Status.canTransitionTo`: Se invoca `canTransitionTo` sobre `Status`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java` — `Status.name`: Se invoca `name` sobre `Status`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java` — `MovementMatch.source`: Se invoca `source` sobre `MovementMatch`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java` — `MovementMatch.reference`: Se invoca `reference` sobre `MovementMatch`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java` — `MovementMatch.amount`: Se invoca `amount` sobre `MovementMatch`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.sourceReference`: Se invoca `sourceReference` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.sourceSystem`: Se invoca `sourceSystem` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.amount`: Se invoca `amount` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `PostgreSQLReconciliationRepository.findBySourceReferenceAndSystem`: Se invoca `findBySourceReferenceAndSystem` sobre `PostgreSQLReconciliationRepository`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.eventId`: Se invoca `eventId` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.version`: Se invoca `version` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.currency`: Se invoca `currency` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.transactionDate`: Se invoca `transactionDate` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ReconciliationEvent.getClass`: Se invoca `getClass` sobre `ReconciliationEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.sourceReference`: Se invoca `sourceReference` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.sourceSystem`: Se invoca `sourceSystem` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.eventId`: Se invoca `eventId` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.version`: Se invoca `version` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.amount`: Se invoca `amount` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.currency`: Se invoca `currency` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.transactionDate`: Se invoca `transactionDate` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/ReconciliationEventPublisher.java` — `IdempotentProcessed.getClass`: Se invoca `getClass` sobre `IdempotentProcessed`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `LagMetrics.getCurrentLag`: Se invoca `getCurrentLag` sobre `LagMetrics`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `LagMetrics.getSource`: Se invoca `getSource` sobre `LagMetrics`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `LagMetrics.getPendingCount`: Se invoca `getPendingCount` sobre `LagMetrics`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `LagAlertPublisher.publishSlaAlert`: Se invoca `publishSlaAlert` sobre `LagAlertPublisher`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `LagAlertPublisher.publishSystemLagAlert`: Se invoca `publishSystemLagAlert` sobre `LagAlertPublisher`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `LagMetrics.getProcessedCount`: Se invoca `getProcessedCount` sobre `LagMetrics`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `ProcessMovementCommand.sourceReference`: Se invoca `sourceReference` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `ProcessMovementCommand.amount`: Se invoca `amount` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `PostgreSQLReconciliationRepository.findUnmatchedWithinWindow`: Se invoca `findUnmatchedWithinWindow` sobre `PostgreSQLReconciliationRepository`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `ReconciliationService.resolveReconciliation`: Se invoca `resolveReconciliation` sobre `ReconciliationService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `ReconciliationService.checkAndEscalateIfNeeded`: Se invoca `checkAndEscalateIfNeeded` sobre `ReconciliationService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `ProcessMovementCommand.sourceReference`: Se invoca `sourceReference` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `ProcessMovementCommand.sourceSystem`: Se invoca `sourceSystem` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `ProcessMovementCommand.amount`: Se invoca `amount` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `ProcessMovementCommand.currency`: Se invoca `currency` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `ProcessMovementCommand.transactionDate`: Se invoca `transactionDate` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

## Como saber que terminaste

```bash
el comando de build o arranque canonico del stack elegido
```

Ese comando corriendo sin errores es la definicion de "listo".

---

```
## Briefing del reto (autoridad)
Este bloque manda sobre los archivos adjuntos. El stack y el rol salen de AQUÍ, no de un topic genérico ni de markdown placeholder.

### Contexto técnico original
Diseño de motor de conciliación que consume streams de movimientos desde 3 fuentes (core bancario, gateway de pagos, sistema de liquidación) y detecta discrepancias en ventanas móviles. Cada movimiento debe reconciliarse contra las 3 fuentes con tolerancia a mensajes fuera de orden y llegadas duplicadas. El desarrollador senior debe diseñar la máquina de estados de cada Reconciliation (Pending, Matched, Mismatched, Manual), justificar la ventana de matching (5 min vs 1 hora), definir cómo maneja idempotencia con eventId + version, y elegir entre reprocesamiento por replay de Kafka vs snapshot desde PostgreSQL. Adicionalmente debe explicar cómo alertar al equipo de operaciones cuando el lag de conciliación supera un SLA.

### Reto
- Tema: TEST-CT
- Seniority: senior-l2
- Tipo: mixed
- Título: Diseño de Motor de Conciliación Bancaria en Tiempo Real
- Tiempo estimado: 12 horas

### Fases (trabajo del HUMANO — PROHIBIDO completarlas)
No implementes estos entregables. Dejalos como hueco pedagógico. El asistente solo materializa el proyecto arrancable para que el participante pueda trabajar.
- Fase 1: Exploración del Sistema y Definición de Requisitos — objetivo: Identificar y documentar los requisitos y restricciones del sistema de conciliación. — entregable (NO resolver): Documento de requisitos y restricciones del sistema de conciliación.
- Fase 2: Diseño de la Máquina de Estados y Ventana de Matching — objetivo: Diseñar la máquina de estados de cada Reconciliation y justificar la ventana de matching. — entregable (NO resolver): Diagrama de la máquina de estados y documento justificando la ventana de matching.
- Fase 3: Manejo de Idempotencia y Estrategia de Reprocesamiento — objetivo: Definir cómo manejar idempotencia y elegir la estrategia de reprocesamiento. — entregable (NO resolver): Documento describiendo el manejo de idempotencia y la estrategia de reprocesamiento elegida.
- Fase 4: Alerta de Lag de Conciliación — objetivo: Definir cómo alertar al equipo de operaciones cuando el lag de conciliación supera un SLA. — entregable (NO resolver): Documento describiendo la estrategia de alertas y los criterios para alertar al equipo de operaciones.

Eres un asistente experto en análisis, corrección y generación de archivos de cualquier tipo:
código fuente, documentación, hojas de cálculo, documentos Word, configuraciones, entre otros.
Voy a enviarte una cadena de texto que contiene uno o más archivos. Cada archivo está delimitado por un marcador con el siguiente formato:
// === ARCHIVO: ruta/del/archivo.extension ===
o también puede aparecer como:
## === ARCHIVO: ruta/del/archivo.extension ===
Lo que sigue al marcador puede ser:

El contenido real del archivo (código, texto, YAML, etc.)
Una descripción en lenguaje natural de lo que debe contener el archivo


TU TAREA
PASO 0 — ¿Esto es un proyecto o una carcasa?
Antes de extraer archivos, leé el Briefing (si está) y diagnosticá el adjunto.

Es CARCASA si ocurre CUALQUIERA de estas:
- No hay manifiesto de dependencias del stack del briefing (manifest.json de VTEX IO / package.json / pom.xml / build.gradle / requirements.txt / go.mod / *.tf / *.csproj, según corresponda)
- Hay un "binario" que en realidad es un comentario ("no puede ser mostrado como texto plano", placeholder .fig/.docx vacío)
- Los markdowns ya completan entregables de fases posteriores ("se implementó fade-in", lista de áreas ya resuelta)

Si es CARCASA:
- MATERIALIZÁ un proyecto que arranca en el stack del briefing (VTEX IO Store Framework, Angular, Terraform, pytest, Nest, etc.). Incluí manifiesto, punto de entrada y capa de interfaz reales.
- NO copies los markdowns de "solución" como si fueran el producto. Son ruido de generación.
- NO resuelvas las fases del briefing (están marcadas PROHIBIDO). Dejá el hueco pedagógico: el flujo existe, las microinteracciones/calidad/infra que el reto pide NO están hechas.
- Después seguí al PASO 5 (ZIP).

Si es un proyecto REAL (manifiesto + código que compila o arranca):
- Seguí PASO 1 en adelante. 🔴 compilación sí. 🟡 pedagógico no.

PASO 1 — Detección y extracción
Identifica todos los archivos presentes en la cadena. Para cada archivo extrae:

Su ruta completa (ej: src/main/java/com/pragma/Service.java)
Su contenido o descripción

PASO 2 — Clasificación por tipo
Clasifica cada archivo en una de estas categorías:
A) Código fuente (Java, Python, TypeScript, JavaScript, Kotlin, etc.)
B) Configuración / documentación (YAML, properties, Markdown, JSON, txt, etc.)
C) Excel (.xlsx, .xls, .csv)
D) Word (.docx, .doc)
E) Otro tipo de archivo binario o especial
PASO 3 — Clasificación de errores en código fuente

Objetivo prioritario: que el proyecto compile. No corrijas flujo de negocio ni lógica funcional.

Antes de modificar cualquier archivo de código fuente, clasifica cada problema encontrado en una de estas dos categorías:
🔴 ERROR DE COMPILACIÓN — corregir siempre
Son errores que impiden que el proyecto arranque, sin valor pedagógico:

Import faltante o incorrecto
Clase, método o variable referenciada que no existe en ningún archivo del proyecto
Error de sintaxis
Anotación con atributos inválidos
Dependencia ausente en pom.xml, package.json, etc.
Archivo referenciado que no existe y debe ser creado con implementación mínima

→ CORREGIR estos errores.
🟡 PROBLEMA FUNCIONAL O DE CALIDAD — preservar siempre
Son problemas que no impiden compilar. Pueden ser intencionales para el aprendizaje:

Clave secreta hardcodeada ("secret", "password123")
API deprecada que funciona pero tiene reemplazo moderno
Lógica de negocio incorrecta o incompleta
Código redundante o de baja legibilidad
Falta de validaciones en flujo de negocio
Patrones de diseño incorrectos pero funcionales
Concurrencia no segura
Configuración funcional pero no óptima

→ PRESERVAR tal cual. No corregir, no mejorar, no comentar.
PASO 4 — Procesamiento según tipo de archivo
Tipo A — Código fuente
Aplica únicamente las correcciones clasificadas como 🔴 ERROR DE COMPILACIÓN.
No alteres ningún elemento clasificado como 🟡 PROBLEMA FUNCIONAL O DE CALIDAD.
Si falta un archivo referenciado, créalo con la implementación mínima necesaria para compilar.
Tipo B — Configuración / documentación
Extrae el contenido tal cual, sin modificaciones salvo errores evidentes de sintaxis
(ej: YAML mal indentado).
Tipo C — Excel (.xlsx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un archivo Excel funcional con:

Fila de encabezados en negrita con color de fondo distintivo
Columnas con ancho ajustado al contenido
Tipos de dato correctos por columna
Validaciones si la descripción lo indica
Hojas nombradas descriptivamente si hay más de una
Filas de ejemplo si no hay datos reales

Tipo D — Word (.docx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un documento Word funcional con:

Estilos de título (Título 1, Título 2) para jerarquía de secciones
Fuente legible (Calibri o equivalente), tamaño 11-12pt para cuerpo
Márgenes estándar
Tabla de contenido si tiene múltiples secciones
Tablas con encabezados en negrita si aplica

Tipo E — Otro
Genera el archivo con el contenido o estructura más apropiada según la descripción.
PASO 5 — Exportación en ZIP
Empaqueta todos los archivos en un único archivo ZIP descargable respetando exactamente
la estructura de rutas indicada por los marcadores.
El ZIP debe incluir:

Archivos de código con únicamente los errores de compilación corregidos
Archivos de configuración y documentación sin cambios
Archivos nuevos creados para resolver dependencias de compilación faltantes
Archivos Excel y Word generados desde descripción

IMPORTANTE: El ZIP debe estar listo para descargar al finalizar. No preguntes si el usuario
quiere generarlo. Simplemente genera el archivo y proporciona el enlace de descarga; No debes desplegar en el chat el resumen de lo que arreglaste al Zip, solo entregalo.

REGLAS IMPORTANTES

No omitas ningún archivo aunque no tenga errores ni modificaciones
Respeta los nombres y rutas exactas indicadas por los marcadores
Si un archivo no tiene marcador claro, infiere el nombre desde su contenido
Si la cadena contiene solo documentación, placeholders o binarios fake, NO la reproduzcas:
aplicá PASO 0 (materializar el proyecto del briefing). Reproducir la carcasa es un fallo.
No agregues texto después del enlace de descarga del ZIP
No preguntes si el usuario quiere el ZIP: simplemente generalo siempre
Si detectas que falta un archivo de configuración necesario para compilar
(pom.xml, package.json, requirements.txt, build.gradle, etc.), créalo e inclúyelo
inferiendo su contenido desde los imports y frameworks detectados en el código
Nunca corrijas problemas 🟡 aunque parezcan obvios o fáciles de mejorar.
El participante que recibirá este proyecto los debe encontrar y resolver él mismo.


INPUT
Aquí está la cadena con los archivos:

// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version>
        <relativePath/>
    </parent>

    <groupId>com.pragma</groupId>
    <artifactId>reconciliation</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>reconciliation</name>
    <description>Motor de Conciliación Bancaria en Tiempo Real</description>

    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <reactor.version>3.6.3</reactor.version>
        <testcontainers.version>1.19.7</testcontainers.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.testcontainers</groupId>
                <artifactId>testcontainers-bom</artifactId>
                <version>${testcontainers.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <!-- Spring Boot WebFlux (Reactive) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>

        <!-- Spring Kafka -->
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
            <version>3.4.0</version>
        </dependency>

        <!-- PostgreSQL Driver -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.3</version>
        </dependency>

        <!-- Reactor Core (ya incluido por webflux, pero explicitamos versión) -->
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId>
            <version>${reactor.version}</version>
        </dependency>

        <!-- Spring Boot Actuator -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- R2DBC PostgreSQL para acceso reactivo -->
        <dependency>
            <groupId>io.r2dbc</groupId>
            <artifactId>r2dbc-postgresql</artifactId>
            <version>1.0.0.RELEASE</version>
        </dependency>

        <!-- Spring Boot Starter Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Lombok (opcional para reducir boilerplate) -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Testing: Spring Boot Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Testing: Testcontainers JUnit Jupiter -->
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Testing: Testcontainers Kafka -->
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>kafka</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Testing: Testcontainers PostgreSQL -->
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Testing: Spring Kafka Test -->
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Testing: Reactor Test -->
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

// === ARCHIVO: src/main/resources/application.yml ===
# Configuración Principal del Motor de Conciliación Bancaria
# ============================================================
# Este archivo contiene la configuración de la aplicación incluyendo:
# - Parámetros de conexión a PostgreSQL y Kafka
# - Configuración de ventanas de matching y umbrales de SLA
# - Endpoints de actuator para monitoreo

server:
  port: ${APP_PORT:8080}

spring:
  application:
    name: reconciliation-service

  # Configuración de PostgreSQL (bloqueante para consultas de estado)
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:reconciliation}
    username: ${DB_USER:reconciliation_user}
    password: ${DB_PASSWORD:reconciliation_pass}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000

  # Configuración de R2DBC (reactivo para operaciones de lectura)
  r2dbc:
    url: r2dbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:reconciliation}
    username: ${DB_USER:reconciliation_user}
    password: ${DB_PASSWORD:reconciliation_pass}
    pool:
      max-size: 20
      min-idle: 5
      acquire-timeout: 30s
      max-idle-time: 30m

  # Configuración de Kafka
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    consumer:
      group-id: reconciliation-consumer-group
      auto-offset-reset: earliest
      enable-auto-commit: false
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      properties:
        spring.json.trusted.packages: com.pragma.reconciliation.*
        spring.json.value.default.type: com.pragma.reconciliation.infrastructure.adapters.inbound.dto.MovementDto
        max.poll.records: 500
        max.poll.interval.ms: 300000
        session.timeout.ms: 45000
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      acks: all
      retries: 3
      properties:
        enable.idempotence: true
        max.in.flight.requests.per.connection: 5

# Configuración Específica de Conciliación
reconciliation:
  # Fuentes de datos que se concilian
  sources:
    - name: core-bancario
      topic: movements.core
      priority: 1
    - name: gateway-pagos
      topic: movements.payments
      priority: 2
    - name: sistema-liquidacion
      topic: movements.settlement
      priority: 3

  # Ventana de matching: tiempo máximo para considerar movimientos como
  # potencialmente reconciliables. Valor en minutos.
  matching-window:
    minutes: 5
    # Para entornos de alta frecuencia, usar ventana más pequeña (1-5 min)
    # Para entornos batch, considerar hasta 60 min

  # Estados posibles de una conciliación
  states:
    pending: PENDING
    matched: MATCHED
    mismatched: MISMATCHED
    manual-review: MANUAL_REVIEW

  # Umbrales de SLA para alertas (en milisegundos)
  sla:
    warning-threshold-ms: 300000    # 5 minutos
    critical-threshold-ms: 600000   # 10 minutos
    max-acceptable-lag-ms: 900000   # 15 minutos

  # Configuración de idempotencia
  idempotency:
    # Tiempo de retención para claves de idempotencia (en días)
    key-ttl-days: 90
    # Habilitar verificación de versión para reprocesamiento
    version-checking-enabled: true

  # Configuración de reprocesamiento
  reprocessing:
    # Estrategia: KAFKA_REPLAY o POSTGRES_SNAPSHOT
    strategy: KAFKA_REPLAY
    # Número máximo de reintentos por movimiento
    max-retries: 3
    # Intervalo entre reintentos (en milisegundos)
    retry-interval-ms: 1000

  # Configuración de métricas y monitoreo
  monitoring:
    enabled: true
    metrics-interval-seconds: 30
    lag-calculation-window: 100

# Configuración de Actuator para monitoreo
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
      base-path: /actuator
  endpoint:
    health:
      show-details: always
      probes:
        enabled: true
  health:
    livenessState:
      enabled: true
    readinessState:
      enabled: true
    kafka:
      enabled: true
    db:
      enabled: true
  metrics:
    export:
      prometheus:
        enabled: true

# Configuración de Logging
logging:
  level:
    root: INFO
    com.pragma.reconciliation: DEBUG
    org.springframework.kafka: INFO
    org.apache.kafka: WARN
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"

// === ARCHIVO: src/main/java/com/pragma/reconciliation/ReconciliationApplication.java ===
package com.pragma.reconciliation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Punto de entrada principal del Motor de Conciliación Bancaria en Tiempo Real.
 * 
 * Esta aplicación consume streams de movimientos desde tres fuentes:
 * - Core bancario
 * - Gateway de pagos  
 * - Sistema de liquidación
 * 
 * Detecta discrepancias en ventanas móviles y gestiona el ciclo de vida
 * de cada conciliación (Pending → Matched/Mismatched/Manual).
 * 
 * Arquitectura: Hexagonal/Clean con CQRS y Event Sourcing
 * Stack: Spring Boot 3.4 + WebFlux + Kafka + PostgreSQL (R2DBC)
 */
@SpringBootApplication
@EnableKafka
@EnableR2dbcRepositories(basePackages = "com.pragma.reconciliation.infrastructure.adapters.outbound")
@EntityScan(basePackages = "com.pragma.reconciliation.domain.model")
public class ReconciliationApplication {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationApplication.class);

    public static void main(String[] args) {
        log.info("========================================================");
        log.info("Iniciando Motor de Conciliación Bancaria en Tiempo Real");
        log.info("Versión: 1.0.0-SNAPSHOT");
        log.info("Java Version: {}", System.getProperty("java.version"));
        log.info("========================================================");
        
        SpringApplication.run(ReconciliationApplication.class, args);
        
        log.info("========================================================");
        log.info("Aplicación iniciada correctamente");
        log.info("Endpoints de actuator disponibles en: /actuator/*");
        log.info("========================================================");
    }

    /**
     * Configuración del factory de listeners de Kafka para procesamiento reactivo.
     * Configura manejo de errores con reintentos y dead-letter topic.
     */
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            ProducerFactory<String, String> producerFactory) {
        
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        
        // Configuración para procesamiento concurrente
        factory.setConcurrency(3);
        
        // Configuración de acknowledgement
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        
        // Manejo de errores con reintentos
        FixedBackOff backOff = new FixedBackOff(1000L, 3L);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(backOff);
        factory.setCommonErrorHandler(errorHandler);
        
        log.info("Kafka listener container factory configurado con concurrencia=3 y reintentos=3");
        
        return factory;
    }
}


// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/model/Reconciliation.java ===
package com.pragma.reconciliation.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Reconciliation {

    public enum Status {
        PENDING,
        MATCHED,
        MISMATCHED,
        MANUAL_REVIEW;

        public boolean canTransitionTo(Status target) {
            return switch (this) {
                case PENDING -> target == MATCHED || target == MISMATCHED || target == MANUAL_REVIEW;
                case MATCHED -> false;
                case MISMATCHED -> target == MANUAL_REVIEW || target == MATCHED;
                case MANUAL_REVIEW -> target == MATCHED || target == MISMATCHED;
            };
        }
    }

    private final String id;
    private final String eventId;
    private final int version;
    private Status status;
    private final String sourceReference;
    private final String sourceSystem;
    private final BigDecimal amount;
    private final String currency;
    private final Instant transactionDate;
    private final Instant createdAt;
    private Instant updatedAt;
    private List<MovementMatch> matchedMovements;
    private String mismatchReason;
    private boolean idempotencyProcessed;

    public Reconciliation(String sourceReference, String sourceSystem, BigDecimal amount,
                          String currency, Instant transactionDate) {
        this.id = UUID.randomUUID().toString();
        this.eventId = UUID.randomUUID().toString();
        this.version = 1;
        this.status = Status.PENDING;
        this.sourceReference = Objects.requireNonNull(sourceReference, "sourceReference no puede ser null");
        this.sourceSystem = Objects.requireNonNull(sourceSystem, "sourceSystem no puede ser null");
        this.amount = Objects.requireNonNull(amount, "amount no puede ser null");
        this.currency = Objects.requireNonNull(currency, "currency no puede ser null");
        this.transactionDate = Objects.requireNonNull(transactionDate, "transactionDate no puede ser null");
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.matchedMovements = List.of();
        this.idempotencyProcessed = false;
    }

    public Reconciliation(String id, String eventId, int version, Status status,
                          String sourceReference, String sourceSystem, BigDecimal amount,
                          String currency, Instant transactionDate, Instant createdAt,
                          Instant updatedAt, List<MovementMatch> matchedMovements,
                          String mismatchReason, boolean idempotencyProcessed) {
        this.id = id;
        this.eventId = eventId;
        this.version = version;
        this.status = status;
        this.sourceReference = sourceReference;
        this.sourceSystem = sourceSystem;
        this.amount = amount;
        this.currency = currency;
        this.transactionDate = transactionDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.matchedMovements = matchedMovements != null ? matchedMovements : List.of();
        this.mismatchReason = mismatchReason;
        this.idempotencyProcessed = idempotencyProcessed;
    }

    public boolean isIdempotentWith(String incomingEventId) {
        if (incomingEventId == null) {
            return false;
        }
        if (this.eventId.equals(incomingEventId)) {
            return true;
        }
        return false;
    }

    public boolean isDuplicateVersion(int incomingVersion) {
        return incomingVersion <= this.version;
    }

    public Reconciliation applyIdempotent(String incomingEventId, int incomingVersion) {
        if (isIdempotentWith(incomingEventId) && isDuplicateVersion(incomingVersion)) {
            this.idempotencyProcessed = true;
            return this;
        }
        throw new IllegalArgumentException("Evento no idempotente: eventId=" + incomingEventId
                + ", incomingVersion=" + incomingVersion + ", currentVersion=" + this.version);
    }

    public boolean transitionTo(Status newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            return false;
        }
        this.status = newStatus;
        this.updatedAt = Instant.now();
        this.version++;
        return true;
    }

    public void markAsMatched(List<MovementMatch> matches) {
        if (!transitionTo(Status.MATCHED)) {
            throw new IllegalStateException("Transición a MATCHED no permitida desde " + this.status);
        }
        this.matchedMovements = List.copyOf(matches);
        this.mismatchReason = null;
    }

    public void markAsMismatched(String reason) {
        if (!transitionTo(Status.MISMATCHED)) {
            throw new IllegalStateException("Transición a MISMATCHED no permitida desde " + this.status);
        }
        this.mismatchReason = Objects.requireNonNull(reason, "mismatchReason no puede ser null");
    }

    public void escalateToManualReview() {
        if (!transitionTo(Status.MANUAL_REVIEW)) {
            throw new IllegalStateException("Transición a MANUAL_REVIEW no permitida desde " + this.status);
        }
    }

    public void resolveFromManualReview(boolean matched, List<MovementMatch> matches) {
        if (this.status != Status.MANUAL_REVIEW) {
            throw new IllegalStateException("Solo se puede resolver desde MANUAL_REVIEW");
        }
        if (matched) {
            this.status = Status.MATCHED;
            this.matchedMovements = List.copyOf(matches);
            this.mismatchReason = null;
        } else {
            this.status = Status.MISMATCHED;
        }
        this.updatedAt = Instant.now();
        this.version++;
    }

    public boolean isWithinMatchingWindow(Instant now, long windowMinutes) {
        return this.transactionDate.plusSeconds(windowMinutes * 60).isAfter(now);
    }

    public boolean requiresManualIntervention(long slaMinutes, Instant now) {
        return this.status == Status.PENDING
                && this.createdAt.plusSeconds(slaMinutes * 60).isBefore(now);
    }

    public String getId() { return id; }
    public String getEventId() { return eventId; }
    public int getVersion() { return version; }
    public Status getStatus() { return status; }
    public String getSourceReference() { return sourceReference; }
    public String getSourceSystem() { return sourceSystem; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public Instant getTransactionDate() { return transactionDate; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<MovementMatch> getMatchedMovements() { return matchedMovements; }
    public String getMismatchReason() { return mismatchReason; }
    public boolean isIdempotencyProcessed() { return idempotencyProcessed; }

    public record MovementMatch(
        String sourceReference,
        String sourceSystem,
        BigDecimal amount,
        Instant matchedAt
    ) {}
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/events/ReconciliationEvent.java ===
package com.pragma.reconciliation.domain.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public sealed interface ReconciliationEvent permits
        ReconciliationEvent.Created,
        ReconciliationEvent.Matched,
        ReconciliationEvent.Mismatched,
        ReconciliationEvent.ManualReviewEscalation,
        ReconciliationEvent.ManualReviewResolved,
        ReconciliationEvent.IdempotentProcessed {

    String eventId();
    String reconciliationId();
    Instant occurredAt();

    record Created(
        String eventId,
        String reconciliationId,
        String sourceReference,
        String sourceSystem,
        BigDecimal amount,
        String currency,
        Instant transactionDate,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static Created create(String reconciliationId, String sourceReference,
                                     String sourceSystem, BigDecimal amount, String currency,
                                     Instant transactionDate) {
            return new Created(
                UUID.randomUUID().toString(),
                reconciliationId,
                sourceReference,
                sourceSystem,
                amount,
                currency,
                transactionDate,
                Instant.now()
            );
        }
    }

    record Matched(
        String eventId,
        String reconciliationId,
        List<Reconciliation.MovementMatch> matchedMovements,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static Matched create(String reconciliationId,
                                     List<Reconciliation.MovementMatch> matchedMovements) {
            return new Matched(
                UUID.randomUUID().toString(),
                reconciliationId,
                matchedMovements,
                Instant.now()
            );
        }
    }

    record Mismatched(
        String eventId,
        String reconciliationId,
        String reason,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static Mismatched create(String reconciliationId, String reason) {
            return new Mismatched(
                UUID.randomUUID().toString(),
                reconciliationId,
                reason,
                Instant.now()
            );
        }
    }

    record ManualReviewEscalation(
        String eventId,
        String reconciliationId,
        String reason,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static ManualReviewEscalation create(String reconciliationId, String reason) {
            return new ManualReviewEscalation(
                UUID.randomUUID().toString(),
                reconciliationId,
                reason,
                Instant.now()
            );
        }
    }

    record ManualReviewResolved(
        String eventId,
        String reconciliationId,
        boolean matched,
        List<Reconciliation.MovementMatch> matchedMovements,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static ManualReviewResolved create(String reconciliationId, boolean matched,
                                                   List<Reconciliation.MovementMatch> matchedMovements) {
            return new ManualReviewResolved(
                UUID.randomUUID().toString(),
                reconciliationId,
                matched,
                matchedMovements,
                Instant.now()
            );
        }
    }

    record IdempotentProcessed(
        String eventId,
        String reconciliationId,
        String originalEventId,
        int version,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static IdempotentProcessed create(String reconciliationId, String originalEventId, int version) {
            return new IdempotentProcessed(
                UUID.randomUUID().toString(),
                reconciliationId,
                originalEventId,
                version,
                Instant.now()
            );
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/commands/ProcessMovementCommand.java ===
package com.pragma.reconciliation.domain.commands;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public record ProcessMovementCommand(
    String sourceReference,
    String sourceSystem,
    BigDecimal amount,
    String currency,
    Instant transactionDate,
    String eventId,
    int version,
    Instant receivedAt
) {

    public ProcessMovementCommand {
        Objects.requireNonNull(sourceReference, "sourceReference no puede ser null");
        Objects.requireNonNull(sourceSystem, "sourceSystem no puede ser null");
        Objects.requireNonNull(amount, "amount no puede ser null");
        Objects.requireNonNull(currency, "currency no puede ser null");
        Objects.requireNonNull(transactionDate, "transactionDate no puede ser null");
        Objects.requireNonNull(eventId, "eventId no puede ser null");
        Objects.requireNonNull(receivedAt, "receivedAt no puede ser null");

        if (version < 1) {
            throw new IllegalArgumentException("version debe ser >= 1");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount debe ser mayor que cero");
        }
    }

    public static ProcessMovementCommand create(String sourceReference, String sourceSystem,
                                                 BigDecimal amount, String currency,
                                                 Instant transactionDate) {
        return new ProcessMovementCommand(
            sourceReference,
            sourceSystem,
            amount,
            currency,
            transactionDate,
            java.util.UUID.randomUUID().toString(),
            1,
            Instant.now()
        );
    }

    public boolean isValidForMatchingWindow(long windowMinutes) {
        long windowSeconds = windowMinutes * 60L;
        long nowEpoch = Instant.now().getEpochSecond();
        long transactionEpoch = transactionDate.getEpochSecond();
        return (nowEpoch - transactionEpoch) <= windowSeconds;
    }

    public boolean isDuplicateOf(ProcessMovementCommand other) {
        if (other == null) return false;
        return this.sourceReference().equals(other.sourceReference())
            && this.sourceSystem().equals(other.sourceSystem())
            && this.eventId().equals(other.eventId())
            && this.version() == other.version();
    }

    public String getCompositeKey() {
        return sourceSystem + "|" + sourceReference;
    }

    public record MovementSource(
        String systemName,
        String priority,
        boolean requiresRealTimeProcessing
    ) {
        public static MovementSource fromSystem(String systemName) {
            return switch (systemName.toUpperCase()) {
                case "CORE_BANKING" -> new MovementSource("CORE_BANKING", "PRIMARY", true);
                case "PAYMENT_GATEWAY" -> new MovementSource("PAYMENT_GATEWAY", "SECONDARY", true);
                case "LIQUIDATION_SYSTEM" -> new MovementSource("LIQUIDATION_SYSTEM", "TERTIARY", false);
                default -> new MovementSource(systemName, "UNKNOWN", false);
            };
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/queries/GetReconciliationStatusQuery.java ===
package com.pragma.reconciliation.domain.queries;

import java.time.Instant;

/**
 * Query de CQRS para obtener el estado actual de una conciliación por su identificador.
 * Representa una operación de lectura que no modifica el estado del sistema.
 */
public record GetReconciliationStatusQuery(
    String reconciliationId,
    Instant requestedAt
) {
    public GetReconciliationStatusQuery {
        if (reconciliationId == null || reconciliationId.isBlank()) {
            throw new IllegalArgumentException("El identificador de conciliación no puede ser nulo o vacío");
        }
        if (requestedAt == null) {
            throw new IllegalArgumentException("La marca de tiempo de la solicitud no puede ser nula");
        }
    }

    public static GetReconciliationStatusQuery create(String reconciliationId) {
        return new GetReconciliationStatusQuery(reconciliationId, Instant.now());
    }

    public String getReconciliationId() {
        return reconciliationId;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }
}
// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java ===
package com.pragma.reconciliation.infrastructure.adapters.outbound;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.Reconciliation.Status;
import com.pragma.reconciliation.domain.queries.GetReconciliationStatusQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida que persiste el estado de las conciliaciones en PostgreSQL
 * utilizando R2DBC para operaciones reactivas. Permite la recuperación de snapshots
 * para reprocesamiento y consultas de estado.
 */
@Repository
public class PostgreSQLReconciliationRepository {

    private static final Logger log = LoggerFactory.getLogger(PostgreSQLReconciliationRepository.class);

    private final DatabaseClient databaseClient;

    public PostgreSQLReconciliationRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
        initializeSchema();
    }

    private void initializeSchema() {
        databaseClient.sql("""
            CREATE TABLE IF NOT EXISTS reconciliations (
                id VARCHAR(255) PRIMARY KEY,
                event_id VARCHAR(255) NOT NULL,
                version INTEGER NOT NULL DEFAULT 1,
                status VARCHAR(50) NOT NULL,
                source_reference VARCHAR(255) NOT NULL,
                source_system VARCHAR(100) NOT NULL,
                amount DECIMAL(19,4) NOT NULL,
                currency VARCHAR(3) NOT NULL,
                transaction_date TIMESTAMP WITH TIME ZONE NOT NULL,
                created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                matched_movements JSONB,
                mismatch_reason TEXT,
                idempotency_processed BOOLEAN NOT NULL DEFAULT FALSE
            )
            """)
            .fetch()
            .rowsUpdated()
            .subscribe(
                rows -> log.debug("Esquema de reconciliations inicializado: {} filas afectadas", rows),
                error -> log.error("Error al inicializar esquema de reconciliations", error)
            );
    }

    public Mono<Reconciliation> save(Reconciliation reconciliation) {
        log.info("Persistiendo conciliación con ID: {} y estado: {}", reconciliation.getId(), reconciliation.getStatus());
        
        String matchedMovementsJson = serializeMatchedMovements(reconciliation.getMatchedMovements());
        
        return databaseClient.sql("""
            INSERT INTO reconciliations 
                (id, event_id, version, status, source_reference, source_system, 
                 amount, currency, transaction_date, created_at, updated_at, 
                 matched_movements, mismatch_reason, idempotency_processed)
            VALUES 
                (:id, :eventId, :version, :status, :sourceReference, :sourceSystem,
                 :amount, :currency, :transactionDate, :createdAt, :updatedAt,
                 :matchedMovements, :mismatchReason, :idempotencyProcessed)
            ON CONFLICT (id) DO UPDATE SET
                event_id = EXCLUDED.event_id,
                version = EXCLUDED.version,
                status = EXCLUDED.status,
                source_reference = EXCLUDED.source_reference,
                source_system = EXCLUDED.source_system,
                amount = EXCLUDED.amount,
                currency = EXCLUDED.currency,
                transaction_date = EXCLUDED.transaction_date,
                updated_at = EXCLUDED.updated_at,
                matched_movements = EXCLUDED.matched_movements,
                mismatch_reason = EXCLUDED.mismatch_reason,
                idempotency_processed = EXCLUDED.idempotency_processed
            RETURNING *
            """)
            .bind("id", reconciliation.getId())
            .bind("eventId", reconciliation.getEventId())
            .bind("version", reconciliation.getVersion())
            .bind("status", reconciliation.getStatus().name())
            .bind("sourceReference", reconciliation.getSourceReference())
            .bind("sourceSystem", reconciliation.getSourceSystem())
            .bind("amount", reconciliation.getAmount())
            .bind("currency", reconciliation.getCurrency())
            .bind("transactionDate", reconciliation.getTransactionDate())
            .bind("createdAt", reconciliation.getCreatedAt())
            .bind("updatedAt", reconciliation.getUpdatedAt())
            .bind("matchedMovements", matchedMovementsJson)
            .bind("mismatchReason", reconciliation.getMismatchReason() != null ? reconciliation.getMismatchReason() : "")
            .bind("idempotencyProcessed", reconciliation.isIdempotencyProcessed())
            .map((row, metadata) -> mapRowToReconciliation(row))
            .first()
            .doOnSuccess(saved -> log.info("Conciliación persistida exitosamente: {}", saved.getId()))
            .doOnError(error -> log.error("Error al persistir conciliación: {}", error.getMessage()));
    }

    public Mono<Reconciliation> findById(String id) {
        log.debug("Buscando conciliación por ID: {}", id);
        
        return databaseClient.sql("SELECT * FROM reconciliations WHERE id = :id")
            .bind("id", id)
            .map((row, metadata) -> mapRowToReconciliation(row))
            .first()
            .switchIfEmpty(Mono.defer(() -> {
                log.warn("No se encontró conciliación con ID: {}", id);
                return Mono.empty();
            }));
    }

    public Mono<Reconciliation> findBySourceReferenceAndSystem(String sourceReference, String sourceSystem) {
        log.debug("Buscando conciliación por referencia: {} y sistema: {}", sourceReference, sourceSystem);
        
        return databaseClient.sql("""
            SELECT * FROM reconciliations 
            WHERE source_reference = :sourceReference AND source_system = :sourceSystem
            ORDER BY version DESC
            LIMIT 1
            """)
            .bind("sourceReference", sourceReference)
            .bind("sourceSystem", sourceSystem)
            .map((row, metadata) -> mapRowToReconciliation(row))
            .first()
            .switchIfEmpty(Mono.empty());
    }

    public Flux<Reconciliation> findByStatus(Status status) {
        log.debug("Buscando conciliaciones con estado: {}", status);
        
        return databaseClient.sql("SELECT * FROM reconciliations WHERE status = :status ORDER BY created_at DESC")
            .bind("status", status.name())
            .map((row, metadata) -> mapRowToReconciliation(row))
            .all();
    }

    public Mono<List<Reconciliation>> findByStatusPaginated(Status status, int limit, int offset) {
        log.debug("Buscando conciliaciones con estado: {}, límite: {}, offset: {}", status, limit, offset);
        
        return databaseClient.sql("""
            SELECT * FROM reconciliations 
            WHERE status = :status 
            ORDER BY created_at DESC
            LIMIT :limit OFFSET :offset
            """)
            .bind("status", status.name())
            .bind("limit", limit)
            .bind("offset", offset)
            .map((row, metadata) -> mapRowToReconciliation(row))
            .all()
            .collectList();
    }

    public Mono<Long> countByStatus(Status status) {
        return databaseClient.sql("SELECT COUNT(*) FROM reconciliations WHERE status = :status")
            .bind("status", status.name())
            .map((row, metadata) -> ((Number) row.get("count")).longValue())
            .first();
    }

    public Mono<Boolean> existsById(String id) {
        return databaseClient.sql("SELECT 1 FROM reconciliations WHERE id = :id")
            .bind("id", id)
            .fetch()
            .rowsUpdated()
            .map(rows -> rows > 0);
    }

    public Mono<Void> deleteById(String id) {
        log.info("Eliminando conciliación con ID: {}", id);
        
        return databaseClient.sql("DELETE FROM reconciliations WHERE id = :id")
            .bind("id", id)
            .fetch()
            .rowsUpdated()
            .then()
            .doOnSuccess(v -> log.info("Conciliación eliminada: {}", id))
            .doOnError(error -> log.error("Error al eliminar conciliación: {}", error.getMessage()));
    }

    private Reconciliation mapRowToReconciliation(java.util.Map<String, Object> row) {
        String id = (String) row.get("id");
        String eventId = (String) row.get("event_id");
        Integer version = ((Number) row.get("version")).intValue();
        Status status = Status.valueOf((String) row.get("status"));
        String sourceReference = (String) row.get("source_reference");
        String sourceSystem = (String) row.get("source_system");
        BigDecimal amount = (BigDecimal) row.get("amount");
        String currency = (String) row.get("currency");
        Instant transactionDate = (Instant) row.get("transaction_date");
        Instant createdAt = (Instant) row.get("created_at");
        Instant updatedAt = (Instant) row.get("updated_at");
        Boolean idempotencyProcessed = (Boolean) row.get("idempotency_processed");
        
        return new Reconciliation(
            id, eventId, version, status,
            sourceReference, sourceSystem, amount, currency,
            transactionDate, createdAt, updatedAt
        );
    }

    private String serializeMatchedMovements(List<Reconciliation.MovementMatch> matchedMovements) {
        if (matchedMovements == null || matchedMovements.isEmpty()) {
            return "[]";
        }
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < matchedMovements.size(); i++) {
            Reconciliation.MovementMatch match = matchedMovements.get(i);
            json.append(String.format("{\"source\":\"%s\",\"reference\":\"%s\",\"amount\":%s}",
                match.source(), match.reference(), match.amount()));
            if (i < matchedMovements.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
}
// === ARCHIVO: src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java ===
package com.pragma.reconciliation.application.services;

import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.events.ReconciliationEvent;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.ManualReviewEscalation;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Matched;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Mismatched;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.Reconciliation.MovementMatch;
import com.pragma.reconciliation.domain.model.Reconciliation.Status;
import com.pragma.reconciliation.domain.queries.GetReconciliationStatusQuery;
import com.pragma.reconciliation.infrastructure.adapters.outbound.PostgreSQLReconciliationRepository;
import com.pragma.reconciliation.infrastructure.adapters.outbound.ReconciliationEventPublisher;
import com.pragma.reconciliation.infrastructure.config.ReconciliationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de aplicación que coordina los casos de uso de conciliación,
 * incluyendo el procesamiento de movimientos y la gestión de ventanas de matching.
 * Implementa la lógica de orchestación entre el dominio y la infraestructura.
 */
@Service
public class ReconciliationService {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationService.class);

    private final PostgreSQLReconciliationRepository reconciliationRepository;
    private final ReconciliationEventPublisher eventPublisher;
    private final ReconciliationConfig config;

    public ReconciliationService(
            PostgreSQLReconciliationRepository reconciliationRepository,
            ReconciliationEventPublisher eventPublisher,
            ReconciliationConfig config) {
        this.reconciliationRepository = reconciliationRepository;
        this.eventPublisher = eventPublisher;
        this.config = config;
    }

    public Mono<Reconciliation> processMovement(ProcessMovementCommand command) {
        log.info("Procesando movimiento: referencia={}, sistema={}, monto={}",
            command.sourceReference(), command.sourceSystem(), command.amount());

        if (!command.isValidForMatchingWindow(config.getMatchingWindowMinutes())) {
            log.warn("Movimiento fuera de la ventana de matching: {}", command.getCompositeKey());
            return Mono.error(new IllegalArgumentException(
                "El movimiento está fuera de la ventana de matching permitida"));
        }

        return reconciliationRepository.findBySourceReferenceAndSystem(
                command.sourceReference(), command.sourceSystem())
            .flatMap(existingReconciliation -> handleExistingReconciliation(existingReconciliation, command))
            .switchIfEmpty(Mono.defer(() -> createNewReconciliation(command)))
            .flatMap(reconciliation -> reconciliationRepository.save(reconciliation))
            .flatMap(savedReconciliation -> publishReconciliationEvent(savedReconciliation)
                .thenReturn(savedReconciliation))
            .doOnSuccess(reconciliation -> log.info("Movimiento procesado exitosamente: {}", reconciliation.getId()))
            .doOnError(error -> log.error("Error al procesar movimiento: {}", error.getMessage()));
    }

    private Mono<Reconciliation> handleExistingReconciliation(
            Reconciliation existing, ProcessMovementCommand command) {
        
        log.debug("Conciliación existente encontrada: {}, version={}, eventId={}",
            existing.getId(), existing.getVersion(), existing.getEventId());

        if (existing.isIdempotentWith(command.eventId())) {
            log.info("Procesamiento idempotente detectado para conciliación: {}", existing.getId());
            return Mono.just(existing.applyIdempotent(command.eventId(), command.version()));
        }

        if (existing.isDuplicateVersion(command.version())) {
            log.warn("Versión duplicada detectada: {} vs {}", existing.getVersion(), command.version());
            return Mono.error(new IllegalStateException(
                "Versión duplicada recibida para la misma conciliación"));
        }

        return performMatching(existing, command);
    }

    private Mono<Reconciliation> createNewReconciliation(ProcessMovementCommand command) {
        log.debug("Creando nueva conciliación para movimiento: {}", command.getCompositeKey());
        
        String reconciliationId = UUID.randomUUID().toString();
        String eventId = command.eventId();
        int version = 1;

        Reconciliation reconciliation = new Reconciliation(
            reconciliationId,
            eventId,
            version,
            Status.PENDING,
            command.sourceReference(),
            command.sourceSystem(),
            command.amount(),
            command.currency(),
            command.transactionDate(),
            Instant.now(),
            Instant.now()
        );

        return performMatching(reconciliation, command);
    }

    private Mono<Reconciliation> performMatching(Reconciliation reconciliation, ProcessMovementCommand command) {
        log.debug("Ejecutandomatching para conciliación: {}", reconciliation.getId());

        List<MovementMatch> matches = findMatchingMovements(reconciliation, command);
        
        if (!matches.isEmpty()) {
            reconciliation.markAsMatched(matches);
            log.info("Conciliación {} marcada comoMatched con {} movimientos",
                reconciliation.getId(), matches.size());
        } else if (reconciliation.requiresManualIntervention(config.getSlaMinutes(), Instant.now())) {
            reconciliation.escalateToManualReview();
            log.warn("Conciliación {} escalada a revisión manual por SLA", reconciliation.getId());
        } else {
            reconciliation.markAsMismatched("No se encontraron movimientos coincidentes en las fuentes");
            log.info("Conciliación {} marcada como Mismatched", reconciliation.getId());
        }

        return Mono.just(reconciliation);
    }

    private List<MovementMatch> findMatchingMovements(Reconciliation reconciliation, ProcessMovementCommand command) {
        BigDecimal tolerance = config.getAmountTolerance();
        BigDecimal amount = command.amount();
        
        return List.of(
            new MovementMatch(command.sourceSystem(), command.sourceReference(), amount)
        );
    }

    public Mono<Reconciliation> getReconciliationStatus(GetReconciliationStatusQuery query) {
        log.debug("Consultando estado de conciliación: {}", query.getReconciliationId());
        
        return reconciliationRepository.findById(query.getReconciliationId())
            .doOnSuccess(reconciliation -> {
                if (reconciliation != null) {
                    log.debug("Estado de conciliación {}: {}", 
                        query.getReconciliationId(), reconciliation.getStatus());
                }
            })
            .doOnError(error -> log.error("Error al consultar conciliación: {}", error.getMessage()));
    }

    public Mono<Boolean> resolveManualReview(String reconciliationId, boolean matched, List<MovementMatch> matches) {
        log.info("Resolviendo revisión manual para conciliación: {}, matched={}", reconciliationId, matched);
        
        return reconciliationRepository.findById(reconciliationId)
            .flatMap(reconciliation -> {
                reconciliation.resolveFromManualReview(matched, matches);
                return reconciliationRepository.save(reconciliation)
                    .flatMap(saved -> publishReconciliationEvent(saved).thenReturn(true));
            })
            .switchIfEmpty(Mono.error(new IllegalArgumentException(
                "Conciliación no encontrada: " + reconciliationId)));
    }

    public Flux<Reconciliation> getReconciliationsByStatus(Status status) {
        log.debug("Consultando conciliaciones con estado: {}", status);
        return reconciliationRepository.findByStatus(status);
    }

    public Mono<Long> countReconciliationsByStatus(Status status) {
        return reconciliationRepository.countByStatus(status);
    }

    private Mono<Void> publishReconciliationEvent(Reconciliation reconciliation) {
        ReconciliationEvent event;
        
        switch (reconciliation.getStatus()) {
            case MATCHED -> {
                event = new Matched(
                    reconciliation.getEventId(),
                    reconciliation.getId(),
                    Instant.now(),
                    reconciliation.getMatchedMovements()
                );
            }
            case MISMATCHED -> {
                event = new Mismatched(
                    reconciliation.getEventId(),
                    reconciliation.getId(),
                    Instant.now(),
                    reconciliation.getMismatchReason()
                );
            }
            case MANUAL_REVIEW -> {
                event = new ManualReviewEscalation(
                    reconciliation.getEventId(),
                    reconciliation.getId(),
                    Instant.now(),
                    reconciliation.getMismatchReason()
                );
            }
            default -> {
                return Mono.empty();
            }
        }

        return eventPublisher.publish(event)
            .doOnSuccess(v -> log.debug("Evento publicado: {}", event.getClass().getSimpleName()))
            .doOnError(error -> log.error("Error al publicar evento: {}", error.getMessage()));
    }
}


// === ARCHIVO: src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java ===
package com.pragma.reconciliation.application.usecases;

import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.events.ReconciliationEvent;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.infrastructure.adapters.outbound.ReconciliationEventPublisher;
import com.pragma.reconciliation.infrastructure.adapters.outbound.PostgreSQLReconciliationRepository;
import com.pragma.reconciliation.infrastructure.config.ReconciliationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Component
public class ProcessMovementUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessMovementUseCase.class);

    private final PostgreSQLReconciliationRepository reconciliationRepository;
    private final ReconciliationEventPublisher eventPublisher;
    private final ReconciliationConfig reconciliationConfig;

    public ProcessMovementUseCase(
            PostgreSQLReconciliationRepository reconciliationRepository,
            ReconciliationEventPublisher eventPublisher,
            ReconciliationConfig reconciliationConfig) {
        this.reconciliationRepository = reconciliationRepository;
        this.eventPublisher = eventPublisher;
        this.reconciliationConfig = reconciliationConfig;
    }

    public Mono<Reconciliation> execute(ProcessMovementCommand command) {
        log.info("Processing movement: sourceRef={}, sourceSystem={}, eventId={}",
                command.sourceReference(), command.sourceSystem(), command.eventId());

        if (!command.isValidForMatchingWindow(reconciliationConfig.getMatchingWindowMinutes())) {
            log.warn("Movement {} is outside matching window, escalating to manual review",
                    command.eventId());
            return handleOutsideWindow(command);
        }

        return reconciliationRepository.findBySourceReferenceAndSourceSystem(
                        command.sourceReference(), command.sourceSystem())
                .flatMap(existing -> processExistingReconciliation(existing, command))
                .switchIfEmpty(Mono.defer(() -> createNewReconciliation(command)));
    }

    private Mono<Reconciliation> processExistingReconciliation(Reconciliation existing, ProcessMovementCommand command) {
        if (existing.isIdempotentWith(command.eventId())) {
            log.info("Idempotent processing: event {} already handled for reconciliation {}",
                    command.eventId(), existing.getId());
            return reconciliationRepository.save(existing)
                    .flatMap(saved -> publishEventAndReturn(
                            new ReconciliationEvent.IdempotentProcessed(
                                    saved.getId(),
                                    command.eventId(),
                                    Instant.now()),
                            saved));
        }

        if (existing.isDuplicateVersion(command.version())) {
            log.warn("Duplicate version {} for reconciliation {}, skipping",
                    command.version(), existing.getId());
            return Mono.just(existing);
        }

        if (existing.getStatus() == Reconciliation.Status.MANUAL_REVIEW) {
            return handleManualReviewReception(existing, command);
        }

        return attemptMatching(existing, command);
    }

    private Mono<Reconciliation> attemptMatching(Reconciliation reconciliation, ProcessMovementCommand command) {
        Instant now = Instant.now();
        List<Reconciliation.MovementMatch> matches = performMatching(reconciliation, command);

        if (!matches.isEmpty() && matchesComplete(reconciliation, matches)) {
            reconciliation.markAsMatched(matches);
            log.info("Reconciliation {} matched with {} movements",
                    reconciliation.getId(), matches.size());
        } else {
            reconciliation.markAsMismatched("No complete match found across all source systems");
            log.warn("Reconciliation {} marked as mismatched", reconciliation.getId());
        }

        return reconciliationRepository.save(reconciliation)
                .flatMap(saved -> {
                    ReconciliationEvent event = saved.getStatus() == Reconciliation.Status.MATCHED
                            ? new ReconciliationEvent.Matched(saved.getId(), saved.getEventId(),
                                    saved.getAmount(), saved.getCurrency(), now)
                            : new ReconciliationEvent.Mismatched(saved.getId(), saved.getEventId(),
                                    saved.getMismatchReason(), now);
                    return publishEventAndReturn(event, saved);
                });
    }

    private List<Reconciliation.MovementMatch> performMatching(Reconciliation reconciliation, ProcessMovementCommand command) {
        return List.of(new Reconciliation.MovementMatch(
                command.sourceSystem(),
                command.sourceReference(),
                command.amount(),
                command.eventId()));
    }

    private boolean matchesComplete(Reconciliation reconciliation, List<Reconciliation.MovementMatch> matches) {
        return matches.size() >= 3;
    }

    private Mono<Reconciliation> handleManualReviewReception(Reconciliation reconciliation, ProcessMovementCommand command) {
        List<Reconciliation.MovementMatch> matches = performMatching(reconciliation, command);
        boolean resolved = matchesComplete(reconciliation, matches);

        reconciliation.resolveFromManualReview(resolved, matches);
        log.info("Manual review reconciliation {} resolved: matched={}", reconciliation.getId(), resolved);

        return reconciliationRepository.save(reconciliation)
                .flatMap(saved -> publishEventAndReturn(
                        new ReconciliationEvent.ManualReviewResolved(
                                saved.getId(),
                                saved.getEventId(),
                                resolved,
                                Instant.now()),
                        saved));
    }

    private Mono<Reconciliation> handleOutsideWindow(ProcessMovementCommand command) {
        Reconciliation reconciliation = new Reconciliation(
                command.sourceReference(),
                command.sourceSystem(),
                command.amount(),
                command.currency(),
                command.transactionDate());

        reconciliation.escalateToManualReview();
        log.warn("Movement {} escalated to manual review: outside matching window", command.eventId());

        return reconciliationRepository.save(reconciliation)
                .flatMap(saved -> publishEventAndReturn(
                        new ReconciliationEvent.ManualReviewEscalation(
                                saved.getId(),
                                saved.getEventId(),
                                "Outside matching window",
                                Instant.now()),
                        saved));
    }

    private Mono<Reconciliation> createNewReconciliation(ProcessMovementCommand command) {
        Reconciliation reconciliation = new Reconciliation(
                command.sourceReference(),
                command.sourceSystem(),
                command.amount(),
                command.currency(),
                command.transactionDate());

        reconciliation.applyIdempotent(command.eventId(), command.version());

        return reconciliationRepository.save(reconciliation)
                .flatMap(saved -> publishEventAndReturn(
                        new ReconciliationEvent.Created(
                                saved.getId(),
                                saved.getEventId(),
                                saved.getAmount(),
                                saved.getCurrency(),
                                saved.getTransactionDate(),
                                Instant.now()),
                        saved));
    }

    private Mono<Reconciliation> publishEventAndReturn(ReconciliationEvent event, Reconciliation reconciliation) {
        return eventPublisher.publish(event)
                .then(Mono.just(reconciliation))
                .onErrorResume(e -> {
                    log.error("Failed to publish event for reconciliation {}: {}",
                            reconciliation.getId(), e.getMessage());
                    return Mono.just(reconciliation);
                });
    }

    public Mono<Reconciliation> processWithSlaCheck(ProcessMovementCommand command) {
        return execute(command)
                .flatMap(reconciliation -> {
                    if (reconciliation.requiresManualIntervention(
                            reconciliationConfig.getSlaMinutes(), Instant.now())) {
                        log.warn("Reconciliation {} requires manual intervention - SLA breached",
                                reconciliation.getId());
                        return eventPublisher.publish(new ReconciliationEvent.ManualReviewEscalation(
                                reconciliation.getId(),
                                reconciliation.getEventId(),
                                "SLA breach",
                                Instant.now())).thenReturn(reconciliation);
                    }
                    return Mono.just(reconciliation);
                });
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListener.java ===
package com.pragma.reconciliation.infrastructure.adapters.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.reconciliation.application.usecases.ProcessMovementUseCase;
import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Component
public class KafkaMovementListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaMovementListener.class);

    private final ProcessMovementUseCase processMovementUseCase;
    private final ObjectMapper objectMapper;

    public KafkaMovementListener(ProcessMovementUseCase processMovementUseCase, ObjectMapper objectMapper) {
        this.processMovementUseCase = processMovementUseCase;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${reconciliation.kafka.topics.movements:banking.movements}",
            groupId = "${reconciliation.kafka.consumer.group-id:reconciliation-processor}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenMovement(String message, Acknowledgment acknowledgment) {
        log.debug("Received movement message: {}", message);

        try {
            ProcessMovementCommand command = parseAndValidateMessage(message);
            if (command == null) {
                log.warn("Invalid message format, acknowledging to avoid redelivery: {}", message);
                acknowledgment.acknowledge();
                return;
            }

            processMovementUseCase.execute(command)
                    .doOnSuccess(reconciliation -> {
                        log.info("Movement processed successfully: reconciliationId={}, status={}",
                                reconciliation.getId(), reconciliation.getStatus());
                        acknowledgment.acknowledge();
                    })
                    .doOnError(error -> {
                        log.error("Error processing movement: {}", error.getMessage(), error);
                        acknowledgment.acknowledge();
                    })
                    .subscribe();

        } catch (Exception e) {
            log.error("Fatal error parsing movement message: {}", e.getMessage(), e);
            acknowledgment.acknowledge();
        }
    }

    @KafkaListener(
            topics = "${reconciliation.kafka.topics.movements:banking.movements}",
            groupId = "${reconciliation.kafka.consumer.group-id:reconciliation-processor-sla}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenMovementWithSla(String message, Acknowledgment acknowledgment) {
        log.debug("Received movement for SLA processing: {}", message);

        try {
            ProcessMovementCommand command = parseAndValidateMessage(message);
            if (command == null) {
                acknowledgment.acknowledge();
                return;
            }

            processMovementUseCase.processWithSlaCheck(command)
                    .doOnSuccess(reconciliation -> {
                        log.debug("Movement processed with SLA check: reconciliationId={}",
                                reconciliation.getId());
                        acknowledgment.acknowledge();
                    })
                    .doOnError(error -> {
                        log.error("Error in SLA processing: {}", error.getMessage(), error);
                        acknowledgment.acknowledge();
                    })
                    .subscribe();

        } catch (Exception e) {
            log.error("Error in SLA listener: {}", e.getMessage(), e);
            acknowledgment.acknowledge();
        }
    }

    private ProcessMovementCommand parseAndValidateMessage(String message) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> payload = objectMapper.readValue(message, Map.class);

            String sourceReference = (String) payload.get("sourceReference");
            String sourceSystem = (String) payload.get("sourceSystem");
            String eventId = (String) payload.get("eventId");

            if (sourceReference == null || sourceSystem == null || eventId == null) {
                log.warn("Missing required fields in message: {}", message);
                return null;
            }

            BigDecimal amount = new BigDecimal(payload.get("amount").toString());
            String currency = (String) payload.get("currency");
            String transactionDateStr = (String) payload.get("transactionDate");
            int version = payload.containsKey("version")
                    ? ((Number) payload.get("version")).intValue()
                    : 1;

            Instant transactionDate = transactionDateStr != null
                    ? Instant.parse(transactionDateStr)
                    : Instant.now();

            ProcessMovementCommand.MovementSource source = ProcessMovementCommand.MovementSource
                    .valueOf(sourceSystem.toUpperCase());

            return ProcessMovementCommand.create(sourceReference, sourceSystem, eventId,
                    amount, currency, transactionDate, version, source);

        } catch (Exception e) {
            log.error("Failed to parse movement message: {}", e.getMessage(), e);
            return null;
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/ReconciliationEventPublisher.java ===
package com.pragma.reconciliation.infrastructure.adapters.outbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.reconciliation.domain.events.ReconciliationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ReconciliationEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationEventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String eventsTopic;

    public ReconciliationEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            @Value("${reconciliation.kafka.topics.events:reconciliation.events}") String eventsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.eventsTopic = eventsTopic;
    }

    public reactor.core.publisher.Mono<Void> publish(ReconciliationEvent event) {
        return reactor.core.publisher.Mono.fromCallable(() -> serializeEvent(event))
                .flatMap(this::sendToKafka)
                .doOnSuccess(result -> log.debug("Event published successfully: type={}, reconciliationId={}",
                        event.getClass().getSimpleName(), event.reconciliationId()))
                .doOnError(error -> log.error("Failed to publish event: type={}, reconciliationId={}, error={}",
                        event.getClass().getSimpleName(), event.reconciliationId(), error.getMessage()));
    }

    private String serializeEvent(ReconciliationEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event: " + event.reconciliationId(), e);
        }
    }

    private reactor.core.publisher.Mono<Void> sendToKafka(String serializedEvent) {
        return reactor.core.publisher.Mono.create(sink -> {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(eventsTopic,
                    extractReconciliationId(serializedEvent), serializedEvent);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Kafka send failed: {}", ex.getMessage(), ex);
                    sink.error(ex);
                } else {
                    log.trace("Kafka send success: partition={}, offset={}",
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                    sink.success();
                }
            });
        });
    }

    private String extractReconciliationId(String serializedEvent) {
        try {
            return objectMapper.readTree(serializedEvent).get("reconciliationId").asText();
        } catch (Exception e) {
            log.warn("Could not extract reconciliationId from event, using default key");
            return "unknown";
        }
    }

    public reactor.core.publisher.Mono<Void> publishCreated(ReconciliationEvent.Created event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishMatched(ReconciliationEvent.Matched event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishMismatched(ReconciliationEvent.Mismatched event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishManualReviewEscalation(ReconciliationEvent.ManualReviewEscalation event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishManualReviewResolved(ReconciliationEvent.ManualReviewResolved event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishIdempotentProcessed(ReconciliationEvent.IdempotentProcessed event) {
        return publish(event);
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/config/KafkaConfig.java ===
package com.pragma.reconciliation.infrastructure.config;

import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.events.ReconciliationEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:reconciliation-group}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset:earliest}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.max-poll-records:500}")
    private int maxPollRecords;

    @Value("${spring.kafka.consumer.fetch-min-size:1}")
    private int fetchMinSize;

    @Value("${spring.kafka.consumer.fetch-max-wait-ms:500}")
    private int fetchMaxWaitMs;

    @Value("${spring.kafka.listener.concurrency:3}")
    private int concurrency;

    @Value("${spring.kafka.listener.max-retry:3}")
    private int maxRetry;

    @Value("${spring.kafka.listener.retry-interval-ms:1000}")
    private long retryIntervalMs;

    @Value("${spring.kafka.producer.acks:all}")
    private String acks;

    @Value("${spring.kafka.producer.retries:3}")
    private int retries;

    @Value("${spring.kafka.producer.batch-size:16384}")
    private int batchSize;

    @Value("${spring.kafka.producer.linger-ms:5}")
    private int lingerMs;

    @Value("${spring.kafka.producer.buffer-memory:33554432}")
    private int bufferMemory;

    @Value("${spring.kafka.topics.movements:movements}")
    private String movementsTopic;

    @Value("${spring.kafka.topics.events:reconciliation-events}")
    private String eventsTopic;

    @Value("${spring.kafka.topics.dlq:reconciliation-dlq}")
    private String dlqTopic;

    @Value("${spring.kafka.security.protocol:PLAINTEXT}")
    private String securityProtocol;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, fetchMinSize);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWaitMs);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
        props.put(ConsumerConfig.HEART_BEAT_INTERVAL_MS_CONFIG, 10000);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        props.put("security.protocol", securityProtocol);
        log.info("Consumer config initialized for group: {}, bootstrap servers: {}", groupId, bootstrapServers);
        return props;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 60000);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        props.put("security.protocol", securityProtocol);
        log.info("Producer config initialized with acks: {}, retries: {}", acks, retries);
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setSyncCommits(true);
        factory.setCommonErrorHandler(createErrorHandler());
        log.info("Kafka listener container factory initialized with concurrency: {}", concurrency);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> movementsListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(createErrorHandler());
        JsonDeserializer<ProcessMovementCommand> deserializer = new JsonDeserializer<>(ProcessMovementCommand.class);
        deserializer.addTrustedPackages("com.pragma.reconciliation.domain.commands");
        deserializer.setUseTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(false);
        factory.setRecordMessageConverter(new org.springframework.kafka.support.mapping.JsonMessageConverter());
        log.info("Movements listener container factory initialized");
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> eventsListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(createErrorHandler());
        log.info("Events listener container factory initialized");
        return factory;
    }

    private DefaultErrorHandler createErrorHandler() {
        FixedBackOff backOff = new FixedBackOff(retryIntervalMs, maxRetry);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(backOff);
        errorHandler.addNotRetryableExceptions(
                IllegalArgumentException.class,
                NullPointerException.class
        );
        log.info("Error handler created with maxRetry: {} and retryInterval: {}ms", maxRetry, retryIntervalMs);
        return errorHandler;
    }

    @Bean
    public NewTopic movementsTopic() {
        return TopicBuilder.name(movementsTopic)
                .partitions(6)
                .replicas(3)
                .config("retention.ms", "604800000")
                .config("cleanup.policy", "compact")
                .build();
    }

    @Bean
    public NewTopic eventsTopic() {
        return TopicBuilder.name(eventsTopic)
                .partitions(6)
                .replicas(3)
                .config("retention.ms", "259200000")
                .config("cleanup.policy", "compact")
                .build();
    }

    @Bean
    public NewTopic dlqTopic() {
        return TopicBuilder.name(dlqTopic)
                .partitions(3)
                .replicas(3)
                .config("retention.ms", "604800000")
                .config("cleanup.policy", "delete")
                .build();
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put("security.protocol", securityProtocol);
        return new KafkaAdmin(configs);
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getMovementsTopic() {
        return movementsTopic;
    }

    public String getEventsTopic() {
        return eventsTopic;
    }

    public String getDlqTopic() {
        return dlqTopic;
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/config/ReconciliationConfig.java ===
package com.pragma.reconciliation.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "reconciliation")
public class ReconciliationConfig {
    private static final Logger log = LoggerFactory.getLogger(ReconciliationConfig.class);

    private Matching matching = new Matching();
    private Sla sla = new Sla();
    private Processing processing = new Processing();
    private Alerting alerting = new Alerting();
    private Idempotency idempotency = new Idempotency();

    public static class Matching {
        private int windowMinutes = 30;
        private int maxDistanceMinutes = 60;
        private int toleranceSeconds = 30;
        private String amountToleranceStrategy = "EXACT";
        private double amountTolerancePercentage = 0.0;
        private int maxCandidates = 10;
        private boolean allowPartialMatch = false;
        private boolean enableMultiSourceMatching = true;

        public int getWindowMinutes() {
            return windowMinutes;
        }

        public void setWindowMinutes(int windowMinutes) {
            this.windowMinutes = windowMinutes;
        }

        public int getMaxDistanceMinutes() {
            return maxDistanceMinutes;
        }

        public void setMaxDistanceMinutes(int maxDistanceMinutes) {
            this.maxDistanceMinutes = maxDistanceMinutes;
        }

        public int getToleranceSeconds() {
            return toleranceSeconds;
        }

        public void setToleranceSeconds(int toleranceSeconds) {
            this.toleranceSeconds = toleranceSeconds;
        }

        public String getAmountToleranceStrategy() {
            return amountToleranceStrategy;
        }

        public void setAmountToleranceStrategy(String amountToleranceStrategy) {
            this.amountToleranceStrategy = amountToleranceStrategy;
        }

        public double getAmountTolerancePercentage() {
            return amountTolerancePercentage;
        }

        public void setAmountTolerancePercentage(double amountTolerancePercentage) {
            this.amountTolerancePercentage = amountTolerancePercentage;
        }

        public int getMaxCandidates() {
            return maxCandidates;
        }

        public void setMaxCandidates(int maxCandidates) {
            this.maxCandidates = maxCandidates;
        }

        public boolean isAllowPartialMatch() {
            return allowPartialMatch;
        }

        public void setAllowPartialMatch(boolean allowPartialMatch) {
            this.allowPartialMatch = allowPartialMatch;
        }

        public boolean isEnableMultiSourceMatching() {
            return enableMultiSourceMatching;
        }

        public void setEnableMultiSourceMatching(boolean enableMultiSourceMatching) {
            this.enableMultiSourceMatching = enableMultiSourceMatching;
        }

        public Duration getWindowDuration() {
            return Duration.ofMinutes(windowMinutes);
        }
    }

    public static class Sla {
        private int warningMinutes = 15;
        private int criticalMinutes = 30;
        private int maxPendingAgeMinutes = 120;
        private int alertCheckIntervalSeconds = 60;
        private int maxReconciliationAgeHours = 24;
        private boolean enableAutoEscalation = true;
        private int escalationThresholdPercent = 80;

        public int getWarningMinutes() {
            return warningMinutes;
        }

        public void setWarningMinutes(int warningMinutes) {
            this.warningMinutes = warningMinutes;
        }

        public int getCriticalMinutes() {
            return criticalMinutes;
        }

        public void setCriticalMinutes(int criticalMinutes) {
            this.criticalMinutes = criticalMinutes;
        }

        public int getMaxPendingAgeMinutes() {
            return maxPendingAgeMinutes;
        }

        public void setMaxPendingAgeMinutes(int maxPendingAgeMinutes) {
            this.maxPendingAgeMinutes = maxPendingAgeMinutes;
        }

        public int getAlertCheckIntervalSeconds() {
            return alertCheckIntervalSeconds;
        }

        public void setAlertCheckIntervalSeconds(int alertCheckIntervalSeconds) {
            this.alertCheckIntervalSeconds = alertCheckIntervalSeconds;
        }

        public int getMaxReconciliationAgeHours() {
            return maxReconciliationAgeHours;
        }

        public void setMaxReconciliationAgeHours(int maxReconciliationAgeHours) {
            this.maxReconciliationAgeHours = maxReconciliationAgeHours;
        }

        public boolean isEnableAutoEscalation() {
            return enableAutoEscalation;
        }

        public void setEnableAutoEscalation(boolean enableAutoEscalation) {
            this.enableAutoEscalation = enableAutoEscalation;
        }

        public int getEscalationThresholdPercent() {
            return escalationThresholdPercent;
        }

        public void setEscalationThresholdPercent(int escalationThresholdPercent) {
            this.escalationThresholdPercent = escalationThresholdPercent;
        }

        public Duration getWarningDuration() {
            return Duration.ofMinutes(warningMinutes);
        }

        public Duration getCriticalDuration() {
            return Duration.ofMinutes(criticalMinutes);
        }
    }

    public static class Processing {
        private int batchSize = 100;
        private int maxConcurrency = 10;
        private long pollTimeoutMs = 1000;
        private int retryAttempts = 3;
        private long retryDelayMs = 500;
        private boolean enableParallelProcessing = true;
        private int maxParallelism = 20;
        private boolean enableSnapshotRecovery = true;
        private long snapshotIntervalMs = 300000;

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public int getMaxConcurrency() {
            return maxConcurrency;
        }

        public void setMaxConcurrency(int maxConcurrency) {
            this.maxConcurrency = maxConcurrency;
        }

        public long getPollTimeoutMs() {
            return pollTimeoutMs;
        }

        public void setPollTimeoutMs(long pollTimeoutMs) {
            this.pollTimeoutMs = pollTimeoutMs;
        }

        public int getRetryAttempts() {
            return retryAttempts;
        }

        public void setRetryAttempts(int retryAttempts) {
            this.retryAttempts = retryAttempts;
        }

        public long getRetryDelayMs() {
            return retryDelayMs;
        }

        public void setRetryDelayMs(long retryDelayMs) {
            this.retryDelayMs = retryDelayMs;
        }

        public boolean isEnableParallelProcessing() {
            return enableParallelProcessing;
        }

        public void setEnableParallelProcessing(boolean enableParallelProcessing) {
            this.enableParallelProcessing = enableParallelProcessing;
        }

        public int getMaxParallelism() {
            return maxParallelism;
        }

        public void setMaxParallelism(int maxParallelism) {
            this.maxParallelism = maxParallelism;
        }

        public boolean isEnableSnapshotRecovery() {
            return enableSnapshotRecovery;
        }

        public void setEnableSnapshotRecovery(boolean enableSnapshotRecovery) {
            this.enableSnapshotRecovery = enableSnapshotRecovery;
        }

        public long getSnapshotIntervalMs() {
            return snapshotIntervalMs;
        }

        public void setSnapshotIntervalMs(long snapshotIntervalMs) {
            this.snapshotIntervalMs = snapshotIntervalMs;
        }
    }

    public static class Alerting {
        private boolean enabled = true;
        private String channel = "SLACK";
        private String webhookUrl = "";
        private String emailRecipients = "";
        private boolean alertOnMatch = false;
        private boolean alertOnMismatch = true;
        private boolean alertOnSlaBreach = true;
        private boolean alertOnManualEscalation = true;
        private int alertCooldownMinutes = 15;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getWebhookUrl() {
            return webhookUrl;
        }

        public void setWebhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
        }

        public String getEmailRecipients() {
            return emailRecipients;
        }

        public void setEmailRecipients(String emailRecipients) {
            this.emailRecipients = emailRecipients;
        }

        public boolean isAlertOnMatch() {
            return alertOnMatch;
        }

        public void setAlertOnMatch(boolean alertOnMatch) {
            this.alertOnMatch = alertOnMatch;
        }

        public boolean isAlertOnMismatch() {
            return alertOnMismatch;
        }

        public void setAlertOnMismatch(boolean alertOnMismatch) {
            this.alertOnMismatch = alertOnMismatch;
        }

        public boolean isAlertOnSlaBreach() {
            return alertOnSlaBreach;
        }

        public void setAlertOnSlaBreach(boolean alertOnSlaBreach) {
            this.alertOnSlaBreach = alertOnSlaBreach;
        }

        public boolean isAlertOnManualEscalation() {
            return alertOnManualEscalation;
        }

        public void setAlertOnManualEscalation(boolean alertOnManualEscalation) {
            this.alertOnManualEscalation = alertOnManualEscalation;
        }

        public int getAlertCooldownMinutes() {
            return alertCooldownMinutes;
        }

        public void setAlertCooldownMinutes(int alertCooldownMinutes) {
            this.alertCooldownMinutes = alertCooldownMinutes;
        }
    }

    public static class Idempotency {
        private boolean enabled = true;
        private int maxVersionHistory = 100;
        private long ttlDays = 90;
        private boolean enableVersionCheck = true;
        private boolean allowDowngrade = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getMaxVersionHistory() {
            return maxVersionHistory;
        }

        public void setMaxVersionHistory(int maxVersionHistory) {
            this.maxVersionHistory = maxVersionHistory;
        }

        public long getTtlDays() {
            return ttlDays;
        }

        public void setTtlDays(long ttlDays) {
            this.ttlDays = ttlDays;
        }

        public boolean isEnableVersionCheck() {
            return enableVersionCheck;
        }

        public void setEnableVersionCheck(boolean enableVersionCheck) {
            this.enableVersionCheck = enableVersionCheck;
        }

        public boolean isAllowDowngrade() {
            return allowDowngrade;
        }

        public void setAllowDowngrade(boolean allowDowngrade) {
            this.allowDowngrade = allowDowngrade;
        }
    }

    public Matching getMatching() {
        return matching;
    }

    public void setMatching(Matching matching) {
        this.matching = matching;
    }

    public Sla getSla() {
        return sla;
    }

    public void setSla(Sla sla) {
        this.sla = sla;
    }

    public Processing getProcessing() {
        return processing;
    }

    public void setProcessing(Processing processing) {
        this.processing = processing;
    }

    public Alerting getAlerting() {
        return alerting;
    }

    public void setAlerting(Alerting alerting) {
        this.alerting = alerting;
    }

    public Idempotency getIdempotency() {
        return idempotency;
    }

    public void setIdempotency(Idempotency idempotency) {
        this.idempotency = idempotency;
    }

    public void logConfiguration() {
        log.info("=== Reconciliation Configuration ===");
        log.info("Matching: windowMinutes={}, maxDistanceMinutes={}, toleranceSeconds={}",
                matching.windowMinutes, matching.maxDistanceMinutes, matching.toleranceSeconds);
        log.info("SLA: warningMinutes={}, criticalMinutes={}, maxPendingAgeMinutes={}",
                sla.warningMinutes, sla.criticalMinutes, sla.maxPendingAgeMinutes);
        log.info("Processing: batchSize={}, maxConcurrency={}, enableParallelProcessing={}",
                processing.batchSize, processing.maxConcurrency, processing.enableParallelProcessing);
        log.info("Alerting: enabled={}, channel={}, alertOnSlaBreach={}",
                alerting.enabled, alerting.channel, alerting.alertOnSlaBreach);
        log.info("Idempotency: enabled={}, maxVersionHistory={}, ttlDays={}",
                idempotency.enabled, idempotency.maxVersionHistory, idempotency.ttlDays);
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java ===
package com.pragma.reconciliation.infrastructure.monitoring;

import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.infrastructure.config.ReconciliationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ReconciliationLagMonitor {
    private static final Logger log = LoggerFactory.getLogger(ReconciliationLagMonitor.class);

    private final ReconciliationConfig config;
    private final LagAlertPublisher alertPublisher;
    private final Map<String, LagMetrics> metricsBySource = new ConcurrentHashMap<>();
    private final AtomicInteger totalProcessed = new AtomicInteger(0);
    private final AtomicInteger totalPending = new AtomicInteger(0);
    private final AtomicInteger totalMatched = new AtomicInteger(0);
    private final AtomicInteger totalMismatched = new AtomicInteger(0);
    private final AtomicInteger totalManualReview = new AtomicInteger(0);
    private final AtomicInteger slaWarnings = new AtomicInteger(0);
    private final AtomicInteger slaCritical = new AtomicInteger(0);
    private final AtomicLong lastAlertTime = new AtomicLong(0);

    public ReconciliationLagMonitor(ReconciliationConfig config, LagAlertPublisher alertPublisher) {
        this.config = config;
        this.alertPublisher = alertPublisher;
        initializeSourceMetrics();
    }

    private void initializeSourceMetrics() {
        metricsBySource.put("CORE_BANKING", new LagMetrics("CORE_BANKING"));
        metricsBySource.put("PAYMENT_GATEWAY", new LagMetrics("PAYMENT_GATEWAY"));
        metricsBySource.put("LIQUIDATION_SYSTEM", new LagMetrics("LIQUIDATION_SYSTEM"));
    }

    public void recordProcessing(String sourceSystem, int processedCount, int pendingCount) {
        LagMetrics metrics = metricsBySource.get(sourceSystem);
        if (metrics != null) {
            metrics.recordProcessing(processedCount, pendingCount);
        }
        totalProcessed.addAndGet(processedCount);
        totalPending.set(pendingCount);
    }

    public void recordReconciliationStatus(Reconciliation.Status status) {
        switch (status) {
            case PENDING -> totalPending.incrementAndGet();
            case MATCHED -> totalMatched.incrementAndGet();
            case MISMATCHED -> totalMismatched.incrementAndGet();
            case MANUAL_REVIEW -> totalManualReview.incrementAndGet();
            default -> log.warn("Unknown reconciliation status: {}", status);
        }
    }

    public void recordSlaBreach(String reconciliationId, Duration age, boolean isCritical) {
        if (isCritical) {
            slaCritical.incrementAndGet();
            log.warn("CRITICAL SLA breach for reconciliation: {}, age: {} minutes",
                    reconciliationId, age.toMinutes());
        } else {
            slaWarnings.incrementAndGet();
            log.warn("SLA warning for reconciliation: {}, age: {} minutes",
                    reconciliationId, age.toMinutes());
        }
        checkAndPublishAlert(reconciliationId, age, isCritical);
    }

    @Scheduled(fixedRateString = "${reconciliation.sla.alert-check-interval-seconds:60}000")
    public void performScheduledLagCheck() {
        if (!config.getAlerting().isEnabled() || !config.getSla().isEnableAutoEscalation()) {
            return;
        }
        log.debug("Performing scheduled lag check");
        checkLagThresholds();
    }

    private void checkLagThresholds() {
        Duration warningThreshold = config.getSla().getWarningDuration();
        Duration criticalThreshold = config.getSla().getCriticalDuration();

        metricsBySource.values().forEach(metrics -> {
            Duration currentLag = metrics.getCurrentLag();
            if (currentLag.compareTo(criticalThreshold) >= 0) {
                publishSystemAlert("CRITICAL", metrics.getSource(), currentLag, metrics.getPendingCount());
            } else if (currentLag.compareTo(warningThreshold) >= 0) {
                publishSystemAlert("WARNING", metrics.getSource(), currentLag, metrics.getPendingCount());
            }
        });

        logLagMetrics();
    }

    private void checkAndPublishAlert(String reconciliationId, Duration age, boolean isCritical) {
        long currentTime = System.currentTimeMillis();
        long cooldownMs = config.getAlerting().getAlertCooldownMinutes() * 60 * 1000L;

        if (currentTime - lastAlertTime.get() < cooldownMs) {
            log.debug("Alert cooldown active, skipping SLA alert for: {}", reconciliationId);
            return;
        }

        lastAlertTime.set(currentTime);

        if (alertPublisher != null) {
            alertPublisher.publishSlaAlert(reconciliationId, age, isCritical);
        }
    }

    private void publishSystemAlert(String severity, String source, Duration lag, int pendingCount) {
        log.warn("System alert: {} - Source: {}, Lag: {} minutes, Pending: {}",
                severity, source, lag.toMinutes(), pendingCount);

        if (alertPublisher != null) {
            alertPublisher.publishSystemLagAlert(severity, source, lag, pendingCount);
        }
    }

    public LagSnapshot getCurrentSnapshot() {
        return new LagSnapshot(
                Instant.now(),
                totalProcessed.get(),
                totalPending.get(),
                totalMatched.get(),
                totalMismatched.get(),
                totalManualReview.get(),
                slaWarnings.get(),
                slaCritical.get(),
                Map.copyOf(metricsBySource)
        );
    }

    public Map<String, LagMetrics> getMetricsBySource() {
        return Map.copyOf(metricsBySource);
    }

    public int getTotalProcessed() {
        return totalProcessed.get();
    }

    public int getTotalPending() {
        return totalPending.get();
    }

    public int getTotalMatched() {
        return totalMatched.get();
    }

    public int getTotalMismatched() {
        return totalMismatched.get();
    }

    public int getTotalManualReview() {
        return totalManualReview.get();
    }

    public int getSlaWarnings() {
        return slaWarnings.get();
    }

    public int getSlaCritical() {
        return slaCritical.get();
    }

    private void logLagMetrics() {
        log.info("=== Reconciliation Lag Metrics ===");
        log.info("Total Processed: {}, Pending: {}, Matched: {}, Mismatched: {}, Manual: {}",
                totalProcessed.get(), totalPending.get(), totalMatched.get(),
                totalMismatched.get(), totalManualReview.get());
        log.info("SLA Warnings: {}, Critical: {}", slaWarnings.get(), slaCritical.get());
        metricsBySource.forEach((source, metrics) ->
                log.info("Source {}: Lag={}min, Processed={}, Pending={}",
                        source, metrics.getCurrentLag().toMinutes(),
                        metrics.getProcessedCount(), metrics.getPendingCount()));
    }

    public static class LagMetrics {
        private final String source;
        private final AtomicInteger processedCount = new AtomicInteger(0);
        private final AtomicInteger pendingCount = new AtomicInteger(0);
        private volatile Instant lastProcessingTime;
        private volatile Duration currentLag = Duration.ZERO;

        public LagMetrics(String source) {
            this.source = source;
        }

        public void recordProcessing(int processed, int pending) {
            processedCount.addAndGet(processed);
            pendingCount.set(pending);
            lastProcessingTime = Instant.now();
            calculateLag();
        }

        private void calculateLag() {
            if (lastProcessingTime != null) {
                currentLag = Duration.between(lastProcessingTime, Instant.now());
            }
        }

        public String getSource() {
            return source;
        }

        public int getProcessedCount() {
            return processedCount.get();
        }

        public int getPendingCount() {
            return pendingCount.get();
        }

        public Instant getLastProcessingTime() {
            return lastProcessingTime;
        }

        public Duration getCurrentLag() {
            calculateLag();
            return currentLag;
        }
    }

    public record LagSnapshot(
            Instant timestamp,
            int totalProcessed,
            int totalPending,
            int totalMatched,
            int totalMismatched,
            int totalManualReview,
            int slaWarnings,
            int slaCritical,
            Map<String, LagMetrics> metricsBySource
    ) {}

    public interface LagAlertPublisher {
        void publishSlaAlert(String reconciliationId, Duration age, boolean isCritical);
        void publishSystemLagAlert(String severity, String source, Duration lag, int pendingCount);
    }
}

// === ARCHIVO: src/test/java/com/pragma/reconciliation/domain/model/ReconciliationTest.java ===
package com.pragma.reconciliation.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReconciliationTest {

    private static final String SOURCE_REF = "TXN-2024-001";
    private static final String SOURCE_SYSTEM = "CORE_BANK";
    private static final BigDecimal AMOUNT = new BigDecimal("1500.50");
    private static final String CURRENCY = "USD";

    @Nested
    @DisplayName("Creación de instancia")
    class Creation {

        @Test
        @DisplayName("Debe crear instancia con estado PENDING por defecto")
        void shouldCreateWithPendingStatus() {
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );

            assertNotNull(reconciliation.getId());
            assertNotNull(reconciliation.getEventId());
            assertEquals(0, reconciliation.getVersion());
            assertEquals(Reconciliation.Status.PENDING, reconciliation.getStatus());
            assertEquals(SOURCE_REF, reconciliation.getSourceReference());
            assertEquals(SOURCE_SYSTEM, reconciliation.getSourceSystem());
            assertEquals(AMOUNT, reconciliation.getAmount());
            assertEquals(CURRENCY, reconciliation.getCurrency());
            assertNotNull(reconciliation.getCreatedAt());
            assertFalse(reconciliation.isIdempotencyProcessed());
        }

        @Test
        @DisplayName("Debe crear instancia con parámetros completos")
        void shouldCreateWithFullParameters() {
            String id = "recon-123";
            String eventId = "evt-456";
            int version = 5;
            Reconciliation.Status status = Reconciliation.Status.MATCHED;
            Instant transactionDate = Instant.now().minus(1, ChronoUnit.HOURS);

            Reconciliation reconciliation = new Reconciliation(
                id, eventId, version, status,
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY,
                transactionDate, Instant.now(), Instant.now()
            );

            assertEquals(id, reconciliation.getId());
            assertEquals(eventId, reconciliation.getEventId());
            assertEquals(version, reconciliation.getVersion());
            assertEquals(status, reconciliation.getStatus());
            assertEquals(transactionDate, reconciliation.getTransactionDate());
        }
    }

    @Nested
    @DisplayName("Transiciones de estado")
    class StateTransitions {

        private Reconciliation reconciliation;

        @BeforeEach
        void setUp() {
            reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );
        }

        @ParameterizedTest
        @EnumSource(
            value = Reconciliation.Status.class,
            names = {"MATCHED", "MISMATCHED"}
        )
        @DisplayName("Debe permitir transición desde PENDING a estados terminales")
        void shouldTransitionFromPendingToTerminal(Reconciliation.Status targetStatus) {
            boolean result = reconciliation.transitionTo(targetStatus);

            assertTrue(result);
            assertEquals(targetStatus, reconciliation.getStatus());
        }

        @Test
        @DisplayName("No debe permitir transición desde MATCHED a PENDING")
        void shouldNotAllowTransitionFromMatchedToPending() {
            reconciliation.transitionTo(Reconciliation.Status.MATCHED);
            boolean result = reconciliation.transitionTo(Reconciliation.Status.PENDING);

            assertFalse(result);
            assertEquals(Reconciliation.Status.MATCHED, reconciliation.getStatus());
        }

        @Test
        @DisplayName("No debe permitir transición desde MISMATCHED a PENDING")
        void shouldNotAllowTransitionFromMismatchedToPending() {
            reconciliation.transitionTo(Reconciliation.Status.MISMATCHED);
            boolean result = reconciliation.transitionTo(Reconciliation.Status.PENDING);

            assertFalse(result);
            assertEquals(Reconciliation.Status.MISMATCHED, reconciliation.getStatus());
        }

        @Test
        @DisplayName("Debe permitir transición a MANUAL_REVIEW desde cualquier estado no terminal")
        void shouldAllowTransitionToManualReview() {
            boolean result = reconciliation.transitionTo(Reconciliation.Status.MANUAL_REVIEW);

            assertTrue(result);
            assertEquals(Reconciliation.Status.MANUAL_REVIEW, reconciliation.getStatus());
        }

        @Test
        @DisplayName("Debe resolver desde MANUAL_REVIEW con resultado MATCHED")
        void shouldResolveFromManualReviewMatched() {
            reconciliation.transitionTo(Reconciliation.Status.MANUAL_REVIEW);
            List<Reconciliation.MovementMatch> matches = List.of(
                new Reconciliation.MovementMatch("src-1", "CORE_BANK", new BigDecimal("1500.50")),
                new Reconciliation.MovementMatch("src-2", "PAYMENT_GW", new BigDecimal("1500.50"))
            );

            reconciliation.resolveFromManualReview(true, matches);

            assertEquals(Reconciliation.Status.MATCHED, reconciliation.getStatus());
            assertEquals(matches, reconciliation.getMatchedMovements());
        }

        @Test
        @DisplayName("Debe resolver desde MANUAL_REVIEW con resultado MISMATCHED")
        void shouldResolveFromManualReviewMismatched() {
            reconciliation.transitionTo(Reconciliation.Status.MANUAL_REVIEW);

            reconciliation.resolveFromManualReview(false, List.of());

            assertEquals(Reconciliation.Status.MISMATCHED, reconciliation.getStatus());
            assertNotNull(reconciliation.getMismatchReason());
        }
    }

    @Nested
    @DisplayName("Manejo de idempotencia")
    class IdempotencyHandling {

        private Reconciliation reconciliation;

        @BeforeEach
        void setUp() {
            reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );
        }

        @Test
        @DisplayName("Debe detectar evento duplicado con mismo eventId")
        void shouldDetectDuplicateEventId() {
            String eventId = "evt-001";
            reconciliation = new Reconciliation(
                reconciliation.getId(), eventId, 0,
                Reconciliation.Status.PENDING, SOURCE_REF, SOURCE_SYSTEM,
                AMOUNT, CURRENCY, Instant.now(), Instant.now(), Instant.now()
            );

            boolean isIdempotent = reconciliation.isIdempotentWith(eventId);

            assertTrue(isIdempotent);
        }

        @Test
        @DisplayName("Debe rechazar evento con eventId diferente")
        void shouldRejectDifferentEventId() {
            String existingEventId = "evt-001";
            reconciliation = new Reconciliation(
                reconciliation.getId(), existingEventId, 0,
                Reconciliation.Status.PENDING, SOURCE_REF, SOURCE_SYSTEM,
                AMOUNT, CURRENCY, Instant.now(), Instant.now(), Instant.now()
            );

            boolean isIdempotent = reconciliation.isIdempotentWith("evt-002");

            assertFalse(isIdempotent);
        }

        @Test
        @DisplayName("Debe detectar versión duplicada")
        void shouldDetectDuplicateVersion() {
            reconciliation = new Reconciliation(
                reconciliation.getId(), "evt-001", 3,
                Reconciliation.Status.PENDING, SOURCE_REF, SOURCE_SYSTEM,
                AMOUNT, CURRENCY, Instant.now(), Instant.now(), Instant.now()
            );

            boolean isDuplicate = reconciliation.isDuplicateVersion(3);

            assertTrue(isDuplicate);
        }

        @Test
        @DisplayName("Debe aceptar versión mayor como reprocesamiento válido")
        void shouldAcceptHigherVersionAsReprocessing() {
            reconciliation = new Reconciliation(
                reconciliation.getId(), "evt-001", 3,
                Reconciliation.Status.PENDING, SOURCE_REF, SOURCE_SYSTEM,
                AMOUNT, CURRENCY, Instant.now(), Instant.now(), Instant.now()
            );

            boolean isDuplicate = reconciliation.isDuplicateVersion(5);

            assertFalse(isDuplicate);
        }

        @Test
        @DisplayName("Debe aplicar procesamiento idempotente correctamente")
        void shouldApplyIdempotentProcessing() {
            String incomingEventId = "evt-002";
            int incomingVersion = 2;

            Reconciliation result = reconciliation.applyIdempotent(incomingEventId, incomingVersion);

            assertEquals(incomingEventId, result.getEventId());
            assertEquals(incomingVersion, result.getVersion());
            assertTrue(result.isIdempotencyProcessed());
        }
    }

    @Nested
    @DisplayName("Matching y verificación de ventana")
    class MatchingAndWindow {

        @Test
        @DisplayName("Debe marcar comoMatched con lista de movimientos")
        void shouldMarkAsMatched() {
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );

            List<Reconciliation.MovementMatch> matches = List.of(
                new Reconciliation.MovementMatch("core-001", "CORE_BANK", new BigDecimal("1500.50")),
                new Reconciliation.MovementMatch("gw-001", "PAYMENT_GW", new BigDecimal("1500.50")),
                new Reconciliation.MovementMatch("liq-001", "LIQUIDATION", new BigDecimal("1500.50"))
            );

            reconciliation.markAsMatched(matches);

            assertEquals(Reconciliation.Status.MATCHED, reconciliation.getStatus());
            assertEquals(matches, reconciliation.getMatchedMovements());
            assertNull(reconciliation.getMismatchReason());
        }

        @Test
        @DisplayName("Debe marcar comoMismatched con razón")
        void shouldMarkAsMismatched() {
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );
            String reason = "Monto discrepancy: 1500.50 vs 1400.00";

            reconciliation.markAsMismatched(reason);

            assertEquals(Reconciliation.Status.MISMATCHED, reconciliation.getStatus());
            assertEquals(reason, reconciliation.getMismatchReason());
        }

        @Test
        @DisplayName("Debe verificar que está dentro de la ventana de matching")
        void shouldBeWithinMatchingWindow() {
            Instant transactionDate = Instant.now().minus(2, ChronoUnit.MINUTES);
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );

            boolean withinWindow = reconciliation.isWithinMatchingWindow(Instant.now(), 5);

            assertTrue(withinWindow);
        }

        @Test
        @DisplayName("Debe detectar que está fuera de la ventana de matching")
        void shouldBeOutsideMatchingWindow() {
            Instant transactionDate = Instant.now().minus(10, ChronoUnit.MINUTES);
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );

            boolean withinWindow = reconciliation.isWithinMatchingWindow(Instant.now(), 5);

            assertFalse(withinWindow);
        }

        @ParameterizedTest
        @CsvSource({
            "30, true",
            "45, true",
            "50, false"
        })
        @DisplayName("Debe verificar requiere intervención manual según SLA")
        void shouldRequireManualIntervention(long minutesAgo, boolean expected) {
            Instant transactionDate = Instant.now().minus(minutesAgo, ChronoUnit.MINUTES);
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );

            boolean requires = reconciliation.requiresManualIntervention(40, Instant.now());

            assertEquals(expected, requires);
        }

        @Test
        @DisplayName("Debe escalar a revisión manual")
        void shouldEscalateToManualReview() {
            Reconciliation reconciliation = new Reconciliation(
                SOURCE_REF, SOURCE_SYSTEM, AMOUNT, CURRENCY
            );

            reconciliation.escalateToManualReview();

            assertEquals(Reconciliation.Status.MANUAL_REVIEW, reconciliation.getStatus());
        }
    }
}
// === ARCHIVO: src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java ===
package com.pragma.reconciliation.application.services;

import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.events.ReconciliationEvent;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.Reconciliation.Status;
import com.pragma.reconciliation.domain.model.Reconciliation.MovementMatch;
import com.pragma.reconciliation.infrastructure.adapters.outbound.PostgreSQLReconciliationRepository;
import com.pragma.reconciliation.infrastructure.adapters.outbound.ReconciliationEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReconciliationServiceTest {

    @Mock
    private PostgreSQLReconciliationRepository repository;

    @Mock
    private ReconciliationEventPublisher eventPublisher;

    private ReconciliationService service;

    @BeforeEach
    void setUp() {
        service = new ReconciliationService(repository, eventPublisher, 5, 40);
    }

    @Nested
    @DisplayName("Procesamiento de movimientos")
    class ProcessMovement {

        @Test
        @DisplayName("Debe crear nueva reconciliación cuando no existe")
        void shouldCreateNewReconciliationWhenNotExists() {
            ProcessMovementCommand command = ProcessMovementCommand.create(
                "TXN-001", "CORE_BANK", new BigDecimal("1000.00"), "USD"
            );

            when(repository.findBySourceReferenceAndSourceSystem(anyString(), anyString()))
                .thenReturn(Mono.empty());
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.processMovement(command))
                .assertNext(reconciliation -> {
                    assertNotNull(reconciliation.getId());
                    assertEquals(Status.PENDING, reconciliation.getStatus());
                    assertEquals(command.sourceReference(), reconciliation.getSourceReference());
                    assertEquals(command.amount(), reconciliation.getAmount());
                })
                .verifyComplete();

            verify(repository).save(any(Reconciliation.class));
            verify(eventPublisher, times(1)).publish(any(ReconciliationEvent.Created.class));
        }

        @Test
        @DisplayName("Debe rechazar movimiento duplicado por idempotencia")
        void shouldRejectDuplicateByIdempotency() {
            String eventId = "evt-" + UUID.randomUUID();
            ProcessMovementCommand command = ProcessMovementCommand.create(
                "TXN-001", "CORE_BANK", new BigDecimal("1000.00"), "USD"
            );

            Reconciliation existing = new Reconciliation(
                "recon-123", eventId, 1, Status.PENDING,
                "TXN-001", "CORE_BANK", new BigDecimal("1000.00"), "USD",
                Instant.now(), Instant.now(), Instant.now()
            );

            when(repository.findBySourceReferenceAndSourceSystem(anyString(), anyString()))
                .thenReturn(Mono.just(existing));

            StepVerifier.create(service.processMovement(command))
                .verifyErrorMatches(e -> e.getMessage().contains("Idempotency"));

            verify(repository, never()).save(any());
            verify(eventPublisher, never()).publish(any());
        }

        @Test
        @DisplayName("Debe aceptar reprocesamiento con versión mayor")
        void shouldAcceptReprocessingWithHigherVersion() {
            ProcessMovementCommand command = ProcessMovementCommand.create(
                "TXN-001", "CORE_BANK", new BigDecimal("1000.00"), "USD"
            );

            Reconciliation existing = new Reconciliation(
                "recon-123", "evt-old", 1, Status.PENDING,
                "TXN-001", "CORE_BANK", new BigDecimal("1000.00"), "USD",
                Instant.now(), Instant.now(), Instant.now()
            );

            when(repository.findBySourceReferenceAndSourceSystem(anyString(), anyString()))
                .thenReturn(Mono.just(existing));
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.processMovement(command))
                .assertNext(reconciliation -> {
                    assertEquals(2, reconciliation.getVersion());
                    assertTrue(reconciliation.isIdempotencyProcessed());
                })
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Gestión de ventana de matching")
    class MatchingWindow {

        @Test
        @DisplayName("Debe ejecutar matching dentro de la ventana")
        void shouldExecuteMatchingWithinWindow() {
            ProcessMovementCommand command = ProcessMovementCommand.create(
                "TXN-002", "CORE_BANK", new BigDecimal("2000.00"), "USD"
            );

            when(repository.findBySourceReferenceAndSourceSystem(anyString(), anyString()))
                .thenReturn(Mono.empty());
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.processMovement(command))
                .assertNext(reconciliation -> {
                    assertTrue(reconciliation.isWithinMatchingWindow(Instant.now(), 5));
                })
                .verifyComplete();
        }

        @Test
        @DisplayName("Debe rechazar movimiento fuera de ventana para matching")
        void shouldRejectMovementOutsideMatchingWindow() {
            ProcessMovementCommand command = ProcessMovementCommand.create(
                "TXN-003", "CORE_BANK", new BigDecimal("3000.00"), "USD"
            );

            when(repository.findBySourceReferenceAndSourceSystem(anyString(), anyString()))
                .thenReturn(Mono.empty());
            when(repository.findUnmatchedWithinWindow(any(Instant.class), anyLong()))
                .thenReturn(Mono.empty());
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.processMovement(command))
                .assertNext(reconciliation -> {
                    assertEquals(Status.PENDING, reconciliation.getStatus());
                    assertFalse(reconciliation.isWithinMatchingWindow(Instant.now(), 5));
                })
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Resolución de discrepancias")
    class DiscrepancyResolution {

        @Test
        @DisplayName("Debe marcar como matched cuando encuentra coincidencias")
        void shouldMarkAsMatchedWhenMatchesFound() {
            Reconciliation reconciliation = new Reconciliation(
                "TXN-004", "CORE_BANK", new BigDecimal("4000.00"), "USD"
            );

            List<MovementMatch> matches = List.of(
                new MovementMatch("core-001", "CORE_BANK", new BigDecimal("4000.00")),
                new MovementMatch("gw-001", "PAYMENT_GW", new BigDecimal("4000.00"))
            );

            when(repository.findById(anyString())).thenReturn(Mono.just(reconciliation));
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.Matched.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.resolveReconciliation(reconciliation.getId(), true, matches))
                .assertNext(r -> assertEquals(Status.MATCHED, r.getStatus()))
                .verifyComplete();

            verify(eventPublisher).publish(any(ReconciliationEvent.Matched.class));
        }

        @Test
        @DisplayName("Debe marcar como mismatched cuando no hay coincidencias")
        void shouldMarkAsMismatchedWhenNoMatches() {
            Reconciliation reconciliation = new Reconciliation(
                "TXN-005", "CORE_BANK", new BigDecimal("5000.00"), "USD"
            );

            when(repository.findById(anyString())).thenReturn(Mono.just(reconciliation));
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.Mismatched.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.resolveReconciliation(reconciliation.getId(), false, List.of()))
                .assertNext(r -> {
                    assertEquals(Status.MISMATCHED, r.getStatus());
                    assertNotNull(r.getMismatchReason());
                })
                .verifyComplete();
        }

        @Test
        @DisplayName("Debe escalar a revisión manual cuando supera SLA")
        void shouldEscalateToManualReviewWhenSlaExceeded() {
            Reconciliation reconciliation = new Reconciliation(
                "TXN-006", "CORE_BANK", new BigDecimal("6000.00"), "USD"
            );

            when(repository.findById(anyString())).thenReturn(Mono.just(reconciliation));
            when(repository.save(any(Reconciliation.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(eventPublisher.publish(any(ReconciliationEvent.ManualReviewEscalation.class)))
                .thenReturn(Mono.empty());

            StepVerifier.create(service.checkAndEscalateIfNeeded(reconciliation.getId()))
                .assertNext(r -> {
                    assertTrue(r.requiresManualIntervention(40, Instant.now()));
                })
                .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Consultas de estado")
    class StatusQueries {

        @Test
        @DisplayName("Debe retornar estado de reconciliación por ID")
        void shouldReturnStatusById() {
            Reconciliation reconciliation = new Reconciliation(
                "TXN-007", "CORE_BANK", new BigDecimal("7000.00"), "USD"
            );
            reconciliation.markAsMatched(List.of(
                new MovementMatch("m1", "CORE_BANK", new BigDecimal("7000.00"))
            ));

            when(repository.findById("TXN-007")).thenReturn(Mono.just(reconciliation));

            StepVerifier.create(service.getReconciliationStatus("TXN-007"))
                .assertNext(status -> {
                    assertEquals(Status.MATCHED, status.getStatus());
                    assertNotNull(status.getMatchedAt());
                })
                .verifyComplete();
        }

        @Test
        @DisplayName("Debe retornar vacío cuando reconciliación no existe")
        void shouldReturnEmptyWhenNotFound() {
            when(repository.findById(anyString())).thenReturn(Mono.empty());

            StepVerifier.create(service.getReconciliationStatus("NON-EXISTENT"))
                .verifyComplete();
        }
    }
}
// === ARCHIVO: src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java ===
package com.pragma.reconciliation.infrastructure.adapters.inbound;

import com.pragma.reconciliation.application.usecases.ProcessMovementUseCase;
import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.model.Reconciliation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMovementListenerTest {

    @Mock
    private ProcessMovementUseCase processMovementUseCase;

    private KafkaMovementListener listener;

    @BeforeEach
    void setUp() {
        listener = new KafkaMovementListener(processMovementUseCase);
    }

    @Nested
    @DisplayName("Consumo de mensajes desde Kafka")
    class MessageConsumption {

        @Test
        @DisplayName("Debe procesar mensaje válido correctamente")
        void shouldProcessValidMessage() {
            String json = """
                {
                    "sourceReference": "TXN-KAFKA-001",
                    "sourceSystem": "CORE_BANK",
                    "amount": 2500.75,
                    "currency": "USD"
                }
                """;

            ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "movements", 0, 100L, "TXN-KAFKA-001", json
            );

            Reconciliation expectedResult = new Reconciliation(
                "TXN-KAFKA-001", "CORE_BANK", new BigDecimal("2500.75"), "USD"
            );

            when(processMovementUseCase.execute(any(ProcessMovementCommand.class)))
                .thenReturn(Mono.just(expectedResult));

            CountDownLatch latch = new CountDownLatch(1);

            listener.receive(List.of(record)).doOnTerminate(latch::countDown).subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for message processing");
            }

            ArgumentCaptor<ProcessMovementCommand> captor = ArgumentCaptor.forClass(ProcessMovementCommand.class);
            verify(processMovementUseCase, times(1)).execute(captor.capture());

            ProcessMovementCommand captured = captor.getValue();
            assertEquals("TXN-KAFKA-001", captured.sourceReference());
            assertEquals("CORE_BANK", captured.sourceSystem());
            assertEquals(new BigDecimal("2500.75"), captured.amount());
        }

        @Test
        @DisplayName("Debe manejar error de procesamiento gracefully")
        void shouldHandleProcessingError() {
            String json = """
                {
                    "sourceReference": "TXN-ERROR-001",
                    "sourceSystem": "INVALID_SYSTEM",
                    "amount": 100.00,
                    "currency": "USD"
                }
                """;

            ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "movements", 0, 100L, "TXN-ERROR-001", json
            );

            when(processMovementUseCase.execute(any(ProcessMovementCommand.class)))
                .thenReturn(Mono.error(new RuntimeException("Invalid source system")));

            CountDownLatch latch = new CountDownLatch(1);

            listener.receive(List.of(record))
                .doOnError(e -> latch.countDown())
                .subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for error handling");
            }

            verify(processMovementUseCase, times(1)).execute(any(ProcessMovementCommand.class));
        }

        @Test
        @DisplayName("Debe procesar lote de mensajes correctamente")
        void shouldProcessBatchOfMessages() {
            List<ConsumerRecord<String, String>> records = List.of(
                new ConsumerRecord<>("movements", 0, 100L, "TXN-001",
                    """{"sourceReference":"TXN-001","sourceSystem":"CORE_BANK","amount":100,"currency":"USD"}"""),
                new ConsumerRecord<>("movements", 1, 200L, "TXN-002",
                    """{"sourceReference":"TXN-002","sourceSystem":"PAYMENT_GW","amount":200,"currency":"USD"}"""),
                new ConsumerRecord<>("movements", 2, 300L, "TXN-003",
                    """{"sourceReference":"TXN-003","sourceSystem":"LIQUIDATION","amount":300,"currency":"USD"}""")
            );

            when(processMovementUseCase.execute(any(ProcessMovementCommand.class)))
                .thenAnswer(inv -> {
                    ProcessMovementCommand cmd = inv.getArgument(0);
                    Reconciliation r = new Reconciliation(
                        cmd.sourceReference(), cmd.sourceSystem(), cmd.amount(), cmd.currency()
                    );
                    return Mono.just(r);
                });

            CountDownLatch latch = new CountDownLatch(1);

            listener.receive(records).doOnTerminate(latch::countDown).subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for batch processing");
            }

            verify(processMovementUseCase, times(3)).execute(any(ProcessMovementCommand.class));
        }

        @Test
        @DisplayName("Debe extraer correctamente los campos del JSON")
        void shouldExtractFieldsFromJson() {
            String json = """
                {
                    "sourceReference": "TXN-FIELDS-001",
                    "sourceSystem": "CORE_BANK",
                    "amount": 4500.25,
                    "currency": "EUR",
                    "eventId": "evt-12345",
                    "timestamp": "2024-01-15T10:30:00Z"
                }
                """;

            ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "movements", 0, 100L, "TXN-FIELDS-001", json
            );

            Reconciliation result = new Reconciliation(
                "TXN-FIELDS-001", "CORE_BANK", new BigDecimal("4500.25"), "EUR"
            );

            when(processMovementUseCase.execute(any(ProcessMovementCommand.class)))
                .thenReturn(Mono.just(result));

            CountDownLatch latch = new CountDownLatch(1);

            listener.receive(List.of(record)).doOnTerminate(latch::countDown).subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for field extraction");
            }

            ArgumentCaptor<ProcessMovementCommand> captor = ArgumentCaptor.forClass(ProcessMovementCommand.class);
            verify(processMovementUseCase).execute(captor.capture());

            ProcessMovementCommand cmd = captor.getValue();
            assertEquals("EUR", cmd.currency());
            assertEquals(0, new BigDecimal("4500.25").compareTo(cmd.amount()));
        }
    }

    @Nested
    @DisplayName("Integración con validación")
    class ValidationIntegration {

        @Test
        @DisplayName("Debe rechazar mensaje con JSON inválido")
        void shouldRejectInvalidJson() {
            String invalidJson = "{ invalid json structure ";

            ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "movements", 0, 100L, "TXN-INVALID", invalidJson
            );

            CountDownLatch latch = new CountDownLatch(1);

            listener.receive(List.of(record))
                .doOnError(e -> latch.countDown())
                .subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for invalid JSON handling");
            }

            verify(processMovementUseCase, never()).execute(any());
        }

        @Test
        @DisplayName("Debe manejar mensaje con campos faltantes")
        void shouldHandleMissingFields() {
            String json = """
                {
                    "sourceReference": "TXN-MISSING-001"
                }
                """;

            ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "movements", 0, 100L, "TXN-MISSING-001", json
            );

            CountDownLatch latch = new CountDownLatch(1);

            listener.receive(List.of(record))
                .doOnError(e -> latch.countDown())
                .subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for missing fields handling");
            }

            verify(processMovementUseCase, never()).execute(any());
        }
    }

    @Nested
    @DisplayName("Control de flujo y backpressure")
    class FlowControl {

        @Test
        @DisplayName("Debe procesar mensajes en orden de arrival")
        void shouldProcessMessagesInOrder() {
            int[] callOrder = {0};

            when(processMovementUseCase.execute(any(ProcessMovementCommand.class)))
                .thenAnswer(inv -> {
                    int current = callOrder[0]++;
                    Thread.sleep(50 * (3 - current));
                    ProcessMovementCommand cmd = inv.getArgument(0);
                    return Mono.just(new Reconciliation(
                        cmd.sourceReference(), cmd.sourceSystem(), cmd.amount(), cmd.currency()
                    ));
                });

            List<ConsumerRecord<String, String>> records = List.of(
                new ConsumerRecord<>("movements", 0, 100L, "TXN-1",
                    """{"sourceReference":"TXN-1","sourceSystem":"A","amount":1,"currency":"USD"}"""),
                new ConsumerRecord<>("movements", 1, 200L, "TXN-2",
                    """{"sourceReference":"TXN-2","sourceSystem":"B","amount":2,"currency":"USD"}"""),
                new ConsumerRecord<>("movements", 2, 300L, "TXN-3",
                    """{"sourceReference":"TXN-3","sourceSystem":"C","amount":3,"currency":"USD"}""")
            );

            CountDownLatch latch = new CountDownLatch(1);

            listener.receive(records).doOnTerminate(latch::countDown).subscribe();

            try {
                assertTrue(latch.await(10, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for ordered processing");
            }

            verify(processMovementUseCase, times(3)).execute(any(ProcessMovementCommand.class));
        }

        @Test
        @DisplayName("Debe completar incluso con lista vacía de mensajes")
        void shouldCompleteWithEmptyList() {
            CountDownLatch latch = new CountDownLatch(1);

            listener.receive(List.of())
                .doOnTerminate(latch::countDown)
                .subscribe();

            try {
                assertTrue(latch.await(2, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for empty list");
            }

            verifyNoInteractions(processMovementUseCase);
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/model/MovementMatch.java ===
package com.pragma.reconciliation.domain.model;

import java.math.BigDecimal;

public record MovementMatch(
    String source,
    String reference,
    BigDecimal amount,
    String matchedWith
) {
    public static MovementMatch create(String source, String reference, BigDecimal amount) {
        return new MovementMatch(source, reference, amount, null);
    }

    public static MovementMatch createWithMatch(String source, String reference, BigDecimal amount, String matchedWith) {
        return new MovementMatch(source, reference, amount, matchedWith);
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/events/ReconciliationEvent.java ===
package com.pragma.reconciliation.domain.events;

import com.pragma.reconciliation.domain.model.MovementMatch;
import com.pragma.reconciliation.domain.model.Reconciliation;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public sealed interface ReconciliationEvent permits
        ReconciliationEvent.Created,
        ReconciliationEvent.Matched,
        ReconciliationEvent.Mismatched,
        ReconciliationEvent.ManualReviewEscalation,
        ReconciliationEvent.ManualReviewResolved,
        ReconciliationEvent.IdempotentProcessed {

    String eventId();
    String reconciliationId();
    Instant occurredAt();

    record Created(
        String eventId,
        String reconciliationId,
        String sourceReference,
        String sourceSystem,
        BigDecimal amount,
        String currency,
        Instant transactionDate,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static Created create(String reconciliationId, String sourceReference,
                                     String sourceSystem, BigDecimal amount, String currency,
                                     Instant transactionDate) {
            return new Created(
                UUID.randomUUID().toString(),
                reconciliationId,
                sourceReference,
                sourceSystem,
                amount,
                currency,
                transactionDate,
                Instant.now()
            );
        }
    }

    record Matched(
        String eventId,
        String reconciliationId,
        List<MovementMatch> matchedMovements,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static Matched create(String reconciliationId,
                                     List<MovementMatch> matchedMovements) {
            return new Matched(
                UUID.randomUUID().toString(),
                reconciliationId,
                matchedMovements,
                Instant.now()
            );
        }
    }

    record Mismatched(
        String eventId,
        String reconciliationId,
        String reason,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static Mismatched create(String reconciliationId, String reason) {
            return new Mismatched(
                UUID.randomUUID().toString(),
                reconciliationId,
                reason,
                Instant.now()
            );
        }
    }

    record ManualReviewEscalation(
        String eventId,
        String reconciliationId,
        String reason,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static ManualReviewEscalation create(String reconciliationId, String reason) {
            return new ManualReviewEscalation(
                UUID.randomUUID().toString(),
                reconciliationId,
                reason,
                Instant.now()
            );
        }
    }

    record ManualReviewResolved(
        String eventId,
        String reconciliationId,
        boolean matched,
        List<MovementMatch> matchedMovements,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static ManualReviewResolved create(String reconciliationId, boolean matched,
                                                   List<MovementMatch> matchedMovements) {
            return new ManualReviewResolved(
                UUID.randomUUID().toString(),
                reconciliationId,
                matched,
                matchedMovements,
                Instant.now()
            );
        }
    }

    record IdempotentProcessed(
        String eventId,
        String reconciliationId,
        String originalEventId,
        int version,
        Instant occurredAt
    ) implements ReconciliationEvent {

        public static IdempotentProcessed create(String reconciliationId, String originalEventId, int version) {
            return new IdempotentProcessed(
                UUID.randomUUID().toString(),
                reconciliationId,
                originalEventId,
                version,
                Instant.now()
            );
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java ===
package com.pragma.reconciliation.infrastructure.adapters.outbound;

import com.pragma.reconciliation.domain.model.MovementMatch;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.Reconciliation.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Repository
public class PostgreSQLReconciliationRepository {

    private static final Logger log = LoggerFactory.getLogger(PostgreSQLReconciliationRepository.class);

    private final DatabaseClient databaseClient;

    public PostgreSQLReconciliationRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
        initializeSchema();
    }

    private void initializeSchema() {
        databaseClient.sql("""
            CREATE TABLE IF NOT EXISTS reconciliations (
                id VARCHAR(255) PRIMARY KEY,
                event_id VARCHAR(255) NOT NULL,
                version INTEGER NOT NULL DEFAULT 1,
                status VARCHAR(50) NOT NULL,
                source_reference VARCHAR(255) NOT NULL,
                source_system VARCHAR(100) NOT NULL,
                amount DECIMAL(19,4) NOT NULL,
                currency VARCHAR(3) NOT NULL,
                transaction_date TIMESTAMP WITH TIME ZONE NOT NULL,
                created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                matched_movements JSONB,
                mismatch_reason TEXT,
                idempotency_processed BOOLEAN NOT NULL DEFAULT FALSE
            )
            """)
            .fetch()
            .rowsUpdated()
            .subscribe(
                rows -> log.debug("Esquema de reconciliations inicializado: {} filas afectadas", rows),
                error -> log.error("Error al inicializar esquema de reconciliations", error)
            );
    }

    public Mono<Reconciliation> save(Reconciliation reconciliation) {
        log.info("Persistiendo conciliación con ID: {} y estado: {}", reconciliation.getId(), reconciliation.getStatus());
        
        String matchedMovementsJson = serializeMatchedMovements(reconciliation.getMatchedMovements());
        
        return databaseClient.sql("""
            INSERT INTO reconciliations 
                (id, event_id, version, status, source_reference, source_system, 
                 amount, currency, transaction_date, created_at, updated_at, 
                 matched_movements, mismatch_reason, idempotency_processed)
            VALUES 
                (:id, :eventId, :version, :status, :sourceReference, :sourceSystem,
                 :amount, :currency, :transactionDate, :createdAt, :updatedAt,
                 :matchedMovements, :mismatchReason, :idempotencyProcessed)
            ON CONFLICT (id) DO UPDATE SET
                event_id = EXCLUDED.event_id,
                version = EXCLUDED.version,
                status = EXCLUDED.status,
                source_reference = EXCLUDED.source_reference,
                source_system = EXCLUDED.source_system,
                amount = EXCLUDED.amount,
                currency = EXCLUDED.currency,
                transaction_date = EXCLUDED.transaction_date,
                updated_at = EXCLUDED.updated_at,
                matched_movements = EXCLUDED.matched_movements,
                mismatch_reason = EXCLUDED.mismatch_reason,
                idempotency_processed = EXCLUDED.idempotency_processed
            RETURNING *
            """)
            .bind("id", reconciliation.getId())
            .bind("eventId", reconciliation.getEventId())
            .bind("version", reconciliation.getVersion())
            .bind("status", reconciliation.getStatus().name())
            .bind("sourceReference", reconciliation.getSourceReference())
            .bind("sourceSystem", reconciliation.getSourceSystem())
            .bind("amount", reconciliation.getAmount())
            .bind("currency", reconciliation.getCurrency())
            .bind("transactionDate", reconciliation.getTransactionDate())
            .bind("createdAt", reconciliation.getCreatedAt())
            .bind("updatedAt", reconciliation.getUpdatedAt())
            .bind("matchedMovements", matchedMovementsJson)
            .bind("mismatchReason", reconciliation.getMismatchReason() != null ? reconciliation.getMismatchReason() : "")
            .bind("idempotencyProcessed", reconciliation.isIdempotencyProcessed())
            .map((row, metadata) -> mapRowToReconciliation(row))
            .first()
            .doOnSuccess(saved -> log.info("Conciliación persistida exitosamente: {}", saved.getId()))
            .doOnError(error -> log.error("Error al persistir conciliación: {}", error.getMessage()));
    }

    public Mono<Reconciliation> findById(String id) {
        log.debug("Buscando conciliación por ID: {}", id);
        
        return databaseClient.sql("SELECT * FROM reconciliations WHERE id = :id")
            .bind("id", id)
            .map((row, metadata) -> mapRowToReconciliation(row))
            .first()
            .switchIfEmpty(Mono.defer(() -> {
                log.warn("No se encontró conciliación con ID: {}", id);
                return Mono.empty();
            }));
    }

    public Mono<Reconciliation> findBySourceReferenceAndSourceSystem(String sourceReference, String sourceSystem) {
        log.debug("Buscando conciliación por referencia: {} y sistema: {}", sourceReference, sourceSystem);
        
        return databaseClient.sql("""
            SELECT * FROM reconciliations 
            WHERE source_reference = :sourceReference AND source_system = :sourceSystem
            ORDER BY version DESC
            LIMIT 1
            """)
            .bind("sourceReference", sourceReference)
            .bind("sourceSystem", sourceSystem)
            .map((row, metadata) -> mapRowToReconciliation(row))
            .first()
            .switchIfEmpty(Mono.empty());
    }

    public Flux<Reconciliation> findByStatus(Status status) {
        log.debug("Buscando conciliaciones con estado: {}", status);
        
        return databaseClient.sql("SELECT * FROM reconciliations WHERE status = :status ORDER BY created_at DESC")
            .bind("status", status.name())
            .map((row, metadata) -> mapRowToReconciliation(row))
            .all();
    }

    public Mono<List<Reconciliation>> findByStatusPaginated(Status status, int limit, int offset) {
        log.debug("Buscando conciliaciones con estado: {}, límite: {}, offset: {}", status, limit, offset);
        
        return databaseClient.sql("""
            SELECT * FROM reconciliations 
            WHERE status = :status 
            ORDER BY created_at DESC
            LIMIT :limit OFFSET :offset
            """)
            .bind("status", status.name())
            .bind("limit", limit)
            .bind("offset", offset)
            .map((row, metadata) -> mapRowToReconciliation(row))
            .all()
            .collectList();
    }

    public Mono<Long> countByStatus(Status status) {
        return databaseClient.sql("SELECT COUNT(*) FROM reconciliations WHERE status = :status")
            .bind("status", status.name())
            .map((row, metadata) -> ((Number) row.get("count")).longValue())
            .first();
    }

    public Mono<Boolean> existsById(String id) {
        return databaseClient.sql("SELECT 1 FROM reconciliations WHERE id = :id")
            .bind("id", id)
            .fetch()
            .rowsUpdated()
            .map(rows -> rows > 0);
    }

    public Mono<Void> deleteById(String id) {
        log.info("Eliminando conciliación con ID: {}", id);
        
        return databaseClient.sql("DELETE FROM reconciliations WHERE id = :id")
            .bind("id", id)
            .fetch()
            .rowsUpdated()
            .then()
            .doOnSuccess(v -> log.info("Conciliación eliminada: {}", id))
            .doOnError(error -> log.error("Error al eliminar conciliación: {}", error.getMessage()));
    }

    private Reconciliation mapRowToReconciliation(java.util.Map<String, Object> row) {
        String id = (String) row.get("id");
        String eventId = (String) row.get("event_id");
        Integer version = ((Number) row.get("version")).intValue();
        Status status = Status.valueOf((String) row.get("status"));
        String sourceReference = (String) row.get("source_reference");
        String sourceSystem = (String) row.get("source_system");
        BigDecimal amount = (BigDecimal) row.get("amount");
        String currency = (String) row.get("currency");
        Instant transactionDate = (Instant) row.get("transaction_date");
        Instant createdAt = (Instant) row.get("created_at");
        Instant updatedAt = (Instant) row.get("updated_at");
        Boolean idempotencyProcessed = (Boolean) row.get("idempotency_processed");
        
        return new Reconciliation(
            id, eventId, version, status,
            sourceReference, sourceSystem, amount, currency,
            transactionDate, createdAt, updatedAt
        );
    }

    private String serializeMatchedMovements(List<MovementMatch> matchedMovements) {
        if (matchedMovements == null || matchedMovements.isEmpty()) {
            return "[]";
        }
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < matchedMovements.size(); i++) {
            MovementMatch match = matchedMovements.get(i);
            json.append(String.format("{\"source\":\"%s\",\"reference\":\"%s\",\"amount\":%s}",
                match.source(), match.reference(), match.amount()));
            if (i < matchedMovements.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java ===
package com.pragma.reconciliation.application.usecases;

import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.events.ReconciliationEvent;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Created;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.IdempotentProcessed;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Matched;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Mismatched;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.ManualReviewEscalation;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.ManualReviewResolved;
import com.pragma.reconciliation.domain.model.MovementMatch;
import com.pragma.reconciliation.domain.model.Reconciliation;
import com.pragma.reconciliation.domain.model.Reconciliation.Status;
import com.pragma.reconciliation.infrastructure.adapters.outbound.ReconciliationEventPublisher;
import com.pragma.reconciliation.infrastructure.adapters.outbound.PostgreSQLReconciliationRepository;
import com.pragma.reconciliation.infrastructure.config.ReconciliationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProcessMovementUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessMovementUseCase.class);

    private final PostgreSQLReconciliationRepository reconciliationRepository;
    private final ReconciliationEventPublisher eventPublisher;
    private final ReconciliationConfig reconciliationConfig;

    public ProcessMovementUseCase(
            PostgreSQLReconciliationRepository reconciliationRepository,
            ReconciliationEventPublisher eventPublisher,
            ReconciliationConfig reconciliationConfig) {
        this.reconciliationRepository = reconciliationRepository;
        this.eventPublisher = eventPublisher;
        this.reconciliationConfig = reconciliationConfig;
    }

    public Mono<Reconciliation> execute(ProcessMovementCommand command) {
        log.info("Processing movement: sourceRef={}, sourceSystem={}, eventId={}",
                command.sourceReference(), command.sourceSystem(), command.eventId());

        if (!command.isValidForMatchingWindow(reconciliationConfig.getMatching().getWindowMinutes())) {
            log.warn("Movement {} is outside matching window, escalating to manual review",
                    command.eventId());
            return handleOutsideWindow(command);
        }

        return reconciliationRepository.findBySourceReferenceAndSourceSystem(
                        command.sourceReference(), command.sourceSystem())
                .flatMap(existing -> processExistingReconciliation(existing, command))
                .switchIfEmpty(Mono.defer(() -> createNewReconciliation(command)));
    }

    private Mono<Reconciliation> processExistingReconciliation(Reconciliation existing, ProcessMovementCommand command) {
        if (existing.isIdempotentWith(command.eventId())) {
            log.info("Idempotent processing: event {} already handled for reconciliation {}",
                    command.eventId(), existing.getId());
            return reconciliationRepository.save(existing)
                    .flatMap(saved -> publishEventAndReturn(
                            IdempotentProcessed.create(
                                    saved.getId(),
                                    command.eventId(),
                                    saved.getVersion()),
                            saved));
        }

        if (existing.isDuplicateVersion(command.version())) {
            log.warn("Duplicate version {} for reconciliation {}, skipping",
                    command.version(), existing.getId());
            return Mono.just(existing);
        }

        if (existing.getStatus() == Status.MANUAL_REVIEW) {
            return handleManualReviewReception(existing, command);
        }

        return attemptMatching(existing, command);
    }

    private Mono<Reconciliation> attemptMatching(Reconciliation reconciliation, ProcessMovementCommand command) {
        Instant now = Instant.now();
        List<MovementMatch> matches = performMatching(reconciliation, command);

        if (!matches.isEmpty() && matchesComplete(reconciliation, matches)) {
            reconciliation.markAsMatched(matches);
            log.info("Reconciliation {} matched with {} movements",
                    reconciliation.getId(), matches.size());
        } else {
            reconciliation.markAsMismatched("No complete match found across all source systems");
            log.warn("Reconciliation {} marked as mismatched", reconciliation.getId());
        }

        return reconciliationRepository.save(reconciliation)
                .flatMap(saved -> {
                    ReconciliationEvent event = saved.getStatus() == Status.MATCHED
                            ? Matched.create(saved.getId(), saved.getMatchedMovements())
                            : Mismatched.create(saved.getId(), saved.getMismatchReason());
                    return publishEventAndReturn(event, saved);
                });
    }

    private List<MovementMatch> performMatching(Reconciliation reconciliation, ProcessMovementCommand command) {
        List<MovementMatch> matches = new ArrayList<>();
        matches.add(MovementMatch.create(
                command.sourceSystem(),
                command.sourceReference(),
                command.amount()));
        return matches;
    }

    private boolean matchesComplete(Reconciliation reconciliation, List<MovementMatch> matches) {
        return matches.size() >= 3;
    }

    private Mono<Reconciliation> handleManualReviewReception(Reconciliation reconciliation, ProcessMovementCommand command) {
        List<MovementMatch> matches = performMatching(reconciliation, command);
        boolean resolved = matchesComplete(reconciliation, matches);

        reconciliation.resolveFromManualReview(resolved, matches);
        log.info("Manual review reconciliation {} resolved: matched={}", reconciliation.getId(), resolved);

        return reconciliationRepository.save(reconciliation)
                .flatMap(saved -> publishEventAndReturn(
                        ManualReviewResolved.create(
                                saved.getId(),
                                resolved,
                                saved.getMatchedMovements()),
                        saved));
    }

    private Mono<Reconciliation> handleOutsideWindow(ProcessMovementCommand command) {
        Reconciliation reconciliation = new Reconciliation(
                command.sourceReference(),
                command.sourceSystem(),
                command.amount(),
                command.currency(),
                command.transactionDate());

        reconciliation.escalateToManualReview();
        log.warn("Movement {} escalated to manual review: outside matching window", command.eventId());

        return reconciliationRepository.save(reconciliation)
                .flatMap(saved -> publishEventAndReturn(
                        ManualReviewEscalation.create(
                                saved.getId(),
                                "Outside matching window"),
                        saved));
    }

    private Mono<Reconciliation> createNewReconciliation(ProcessMovementCommand command) {
        Reconciliation reconciliation = new Reconciliation(
                command.sourceReference(),
                command.sourceSystem(),
                command.amount(),
                command.currency(),
                command.transactionDate());

        reconciliation.applyIdempotent(command.eventId(), command.version());

        return reconciliationRepository.save(reconciliation)
                .flatMap(saved -> publishEventAndReturn(
                        Created.create(
                                saved.getId(),
                                saved.getSourceReference(),
                                saved.getSourceSystem(),
                                saved.getAmount(),
                                saved.getCurrency(),
                                saved.getTransactionDate()),
                        saved));
    }

    private Mono<Reconciliation> publishEventAndReturn(ReconciliationEvent event, Reconciliation reconciliation) {
        return eventPublisher.publish(event)
                .then(Mono.just(reconciliation))
                .onErrorResume(e -> {
                    log.error("Failed to publish event for reconciliation {}: {}",
                            reconciliation.getId(), e.getMessage());
                    return Mono.just(reconciliation);
                });
    }

    public Mono<Reconciliation> processWithSlaCheck(ProcessMovementCommand command) {
        return execute(command)
                .flatMap(reconciliation -> {
                    if (reconciliation.requiresManualIntervention(
                            reconciliationConfig.getSla().getMinutes(), Instant.now())) {
                        log.warn("Reconciliation {} requires manual intervention - SLA breached",
                                reconciliation.getId());
                        return eventPublisher.publish(ManualReviewEscalation.create(
                                reconciliation.getId(),
                                "SLA breach")).thenReturn(reconciliation);
                    }
                    return Mono.just(reconciliation);
                });
    }
}

// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.pragma</groupId>
    <artifactId>reconciliation</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>Reconciliation Service</name>
    <description>Reconciliation event processing service</description>
    
    <properties>
        <java.version>21</java.version>
        <reactor.version>3.6.3</reactor.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
            <version>3.4.0</version>
        </dependency>
        
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.3</version>
        </dependency>
        
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId>
            <version>3.6.3</version>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>1.19.7</version>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>kafka</artifactId>
            <version>1.19.7</version>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <version>1.19.7</version>
            <scope>test</scope>
        </dependency>
        
        <!-- R2DBC PostgreSQL driver -->
        <dependency>
            <groupId>io.r2dbc</groupId>
            <artifactId>r2dbc-postgresql</artifactId>
            <version>1.0.0.RELEASE</version>
        </dependency>
        
        <!-- R2DBC common -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-r2dbc</artifactId>
        </dependency>
        
        <!-- SLF4J is provided by Spring Boot starter-webflux (logback) -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>


// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListener.java ===
package com.pragma.reconciliation.infrastructure.adapters.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.reconciliation.application.usecases.ProcessMovementUseCase;
import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.commands.ProcessMovementCommand.MovementSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Component
public class KafkaMovementListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaMovementListener.class);

    private final ProcessMovementUseCase processMovementUseCase;
    private final ObjectMapper objectMapper;

    public KafkaMovementListener(ProcessMovementUseCase processMovementUseCase, ObjectMapper objectMapper) {
        this.processMovementUseCase = processMovementUseCase;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${reconciliation.kafka.topics.movements:banking.movements}",
            groupId = "${reconciliation.kafka.consumer.group-id:reconciliation-processor}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenMovement(String message, Acknowledgment acknowledgment) {
        log.debug("Received movement message: {}", message);

        try {
            ProcessMovementCommand command = parseAndValidateMessage(message);
            if (command == null) {
                log.warn("Invalid message format, acknowledging to avoid redelivery: {}", message);
                acknowledgment.acknowledge();
                return;
            }

            processMovementUseCase.execute(command)
                    .doOnSuccess(reconciliation -> {
                        log.info("Movement processed successfully: reconciliationId={}, status={}",
                                reconciliation.getId(), reconciliation.getStatus());
                        acknowledgment.acknowledge();
                    })
                    .doOnError(error -> {
                        log.error("Error processing movement: {}", error.getMessage(), error);
                        acknowledgment.acknowledge();
                    })
                    .subscribe();

        } catch (Exception e) {
            log.error("Fatal error parsing movement message: {}", e.getMessage(), e);
            acknowledgment.acknowledge();
        }
    }

    @KafkaListener(
            topics = "${reconciliation.kafka.topics.movements:banking.movements}",
            groupId = "${reconciliation.kafka.consumer.group-id:reconciliation-processor-sla}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenMovementWithSla(String message, Acknowledgment acknowledgment) {
        log.debug("Received movement for SLA processing: {}", message);

        try {
            ProcessMovementCommand command = parseAndValidateMessage(message);
            if (command == null) {
                acknowledgment.acknowledge();
                return;
            }

            processMovementUseCase.processWithSlaCheck(command)
                    .doOnSuccess(reconciliation -> {
                        log.debug("Movement processed with SLA check: reconciliationId={}",
                                reconciliation.getId());
                        acknowledgment.acknowledge();
                    })
                    .doOnError(error -> {
                        log.error("Error in SLA processing: {}", error.getMessage(), error);
                        acknowledgment.acknowledge();
                    })
                    .subscribe();

        } catch (Exception e) {
            log.error("Error in SLA listener: {}", e.getMessage(), e);
            acknowledgment.acknowledge();
        }
    }

    private ProcessMovementCommand parseAndValidateMessage(String message) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> payload = objectMapper.readValue(message, Map.class);

            String sourceReference = (String) payload.get("sourceReference");
            String sourceSystem = (String) payload.get("sourceSystem");
            String eventId = (String) payload.get("eventId");

            if (sourceReference == null || sourceSystem == null || eventId == null) {
                log.warn("Missing required fields in message: {}", message);
                return null;
            }

            BigDecimal amount = new BigDecimal(payload.get("amount").toString());
            String currency = (String) payload.get("currency");
            String transactionDateStr = (String) payload.get("transactionDate");
            int version = payload.containsKey("version")
                    ? ((Number) payload.get("version")).intValue()
                    : 1;

            Instant transactionDate = transactionDateStr != null
                    ? Instant.parse(transactionDateStr)
                    : Instant.now();

            MovementSource source = MovementSource.valueOf(sourceSystem.toUpperCase());

            return ProcessMovementCommand.create(sourceReference, sourceSystem, eventId,
                    amount, currency, transactionDate, version, source);

        } catch (Exception e) {
            log.error("Failed to parse movement message: {}", e.getMessage(), e);
            return null;
        }
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/ReconciliationEventPublisher.java ===
package com.pragma.reconciliation.infrastructure.adapters.outbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.reconciliation.domain.events.ReconciliationEvent;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Created;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Matched;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.Mismatched;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.ManualReviewEscalation;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.ManualReviewResolved;
import com.pragma.reconciliation.domain.events.ReconciliationEvent.IdempotentProcessed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ReconciliationEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationEventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String eventsTopic;

    public ReconciliationEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            @Value("${reconciliation.kafka.topics.events:reconciliation.events}") String eventsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.eventsTopic = eventsTopic;
    }

    public reactor.core.publisher.Mono<Void> publish(ReconciliationEvent event) {
        return reactor.core.publisher.Mono.fromCallable(() -> serializeEvent(event))
                .flatMap(this::sendToKafka)
                .doOnSuccess(result -> log.debug("Event published successfully: type={}, reconciliationId={}",
                        event.getClass().getSimpleName(), event.reconciliationId()))
                .doOnError(error -> log.error("Failed to publish event: type={}, reconciliationId={}, error={}",
                        event.getClass().getSimpleName(), event.reconciliationId(), error.getMessage()));
    }

    private String serializeEvent(ReconciliationEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event: " + event.reconciliationId(), e);
        }
    }

    private reactor.core.publisher.Mono<Void> sendToKafka(String serializedEvent) {
        return reactor.core.publisher.Mono.create(sink -> {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(eventsTopic,
                    extractReconciliationId(serializedEvent), serializedEvent);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Kafka send failed: {}", ex.getMessage(), ex);
                    sink.error(ex);
                } else {
                    log.trace("Kafka send success: partition={}, offset={}",
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                    sink.success();
                }
            });
        });
    }

    private String extractReconciliationId(String serializedEvent) {
        try {
            return objectMapper.readTree(serializedEvent).get("reconciliationId").asText();
        } catch (Exception e) {
            log.warn("Could not extract reconciliationId from event, using default key");
            return "unknown";
        }
    }

    public reactor.core.publisher.Mono<Void> publishCreated(Created event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishMatched(Matched event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishMismatched(Mismatched event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishManualReviewEscalation(ManualReviewEscalation event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishManualReviewResolved(ManualReviewResolved event) {
        return publish(event);
    }

    public reactor.core.publisher.Mono<Void> publishIdempotentProcessed(IdempotentProcessed event) {
        return publish(event);
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/config/ReconciliationConfig.java ===
package com.pragma.reconciliation.infrastructure.config;

import com.pragma.reconciliation.domain.model.Reconciliation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "reconciliation")
public class ReconciliationConfig {
    private static final Logger log = LoggerFactory.getLogger(ReconciliationConfig.class);

    private Matching matching = new Matching();
    private Sla sla = new Sla();
    private Processing processing = new Processing();
    private Alerting alerting = new Alerting();
    private Idempotency idempotency = new Idempotency();

    public static class Matching {
        private int windowMinutes = 30;
        private int maxDistanceMinutes = 60;
        private int toleranceSeconds = 30;
        private String amountToleranceStrategy = "EXACT";
        private double amountTolerancePercentage = 0.0;
        private int maxCandidates = 10;
        private boolean allowPartialMatch = false;
        private boolean enableMultiSourceMatching = true;

        public int getWindowMinutes() {
            return windowMinutes;
        }

        public void setWindowMinutes(int windowMinutes) {
            this.windowMinutes = windowMinutes;
        }

        public int getMaxDistanceMinutes() {
            return maxDistanceMinutes;
        }

        public void setMaxDistanceMinutes(int maxDistanceMinutes) {
            this.maxDistanceMinutes = maxDistanceMinutes;
        }

        public int getToleranceSeconds() {
            return toleranceSeconds;
        }

        public void setToleranceSeconds(int toleranceSeconds) {
            this.toleranceSeconds = toleranceSeconds;
        }

        public String getAmountToleranceStrategy() {
            return amountToleranceStrategy;
        }

        public void setAmountToleranceStrategy(String amountToleranceStrategy) {
            this.amountToleranceStrategy = amountToleranceStrategy;
        }

        public double getAmountTolerancePercentage() {
            return amountTolerancePercentage;
        }

        public void setAmountTolerancePercentage(double amountTolerancePercentage) {
            this.amountTolerancePercentage = amountTolerancePercentage;
        }

        public int getMaxCandidates() {
            return maxCandidates;
        }

        public void setMaxCandidates(int maxCandidates) {
            this.maxCandidates = maxCandidates;
        }

        public boolean isAllowPartialMatch() {
            return allowPartialMatch;
        }

        public void setAllowPartialMatch(boolean allowPartialMatch) {
            this.allowPartialMatch = allowPartialMatch;
        }

        public boolean isEnableMultiSourceMatching() {
            return enableMultiSourceMatching;
        }

        public void setEnableMultiSourceMatching(boolean enableMultiSourceMatching) {
            this.enableMultiSourceMatching = enableMultiSourceMatching;
        }

        public Duration getWindowDuration() {
            return Duration.ofMinutes(windowMinutes);
        }
    }

    public static class Sla {
        private int warningMinutes = 15;
        private int criticalMinutes = 30;
        private int maxPendingAgeMinutes = 120;
        private int alertCheckIntervalSeconds = 60;
        private int maxReconciliationAgeHours = 24;
        private boolean enableAutoEscalation = true;
        private int escalationThresholdPercent = 80;

        public int getWarningMinutes() {
            return warningMinutes;
        }

        public void setWarningMinutes(int warningMinutes) {
            this.warningMinutes = warningMinutes;
        }

        public int getCriticalMinutes() {
            return criticalMinutes;
        }

        public void setCriticalMinutes(int criticalMinutes) {
            this.criticalMinutes = criticalMinutes;
        }

        public int getMaxPendingAgeMinutes() {
            return maxPendingAgeMinutes;
        }

        public void setMaxPendingAgeMinutes(int maxPendingAgeMinutes) {
            this.maxPendingAgeMinutes = maxPendingAgeMinutes;
        }

        public int getAlertCheckIntervalSeconds() {
            return alertCheckIntervalSeconds;
        }

        public void setAlertCheckIntervalSeconds(int alertCheckIntervalSeconds) {
            this.alertCheckIntervalSeconds = alertCheckIntervalSeconds;
        }

        public int getMaxReconciliationAgeHours() {
            return maxReconciliationAgeHours;
        }

        public void setMaxReconciliationAgeHours(int maxReconciliationAgeHours) {
            this.maxReconciliationAgeHours = maxReconciliationAgeHours;
        }

        public boolean isEnableAutoEscalation() {
            return enableAutoEscalation;
        }

        public void setEnableAutoEscalation(boolean enableAutoEscalation) {
            this.enableAutoEscalation = enableAutoEscalation;
        }

        public int getEscalationThresholdPercent() {
            return escalationThresholdPercent;
        }

        public void setEscalationThresholdPercent(int escalationThresholdPercent) {
            this.escalationThresholdPercent = escalationThresholdPercent;
        }

        public Duration getWarningDuration() {
            return Duration.ofMinutes(warningMinutes);
        }

        public Duration getCriticalDuration() {
            return Duration.ofMinutes(criticalMinutes);
        }
    }

    public static class Processing {
        private int batchSize = 100;
        private int maxConcurrency = 10;
        private long pollTimeoutMs = 1000;
        private int retryAttempts = 3;
        private long retryDelayMs = 500;
        private boolean enableParallelProcessing = true;
        private int maxParallelism = 20;
        private boolean enableSnapshotRecovery = true;
        private long snapshotIntervalMs = 300000;

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public int getMaxConcurrency() {
            return maxConcurrency;
        }

        public void setMaxConcurrency(int maxConcurrency) {
            this.maxConcurrency = maxConcurrency;
        }

        public long getPollTimeoutMs() {
            return pollTimeoutMs;
        }

        public void setPollTimeoutMs(long pollTimeoutMs) {
            this.pollTimeoutMs = pollTimeoutMs;
        }

        public int getRetryAttempts() {
            return retryAttempts;
        }

        public void setRetryAttempts(int retryAttempts) {
            this.retryAttempts = retryAttempts;
        }

        public long getRetryDelayMs() {
            return retryDelayMs;
        }

        public void setRetryDelayMs(long retryDelayMs) {
            this.retryDelayMs = retryDelayMs;
        }

        public boolean isEnableParallelProcessing() {
            return enableParallelProcessing;
        }

        public void setEnableParallelProcessing(boolean enableParallelProcessing) {
            this.enableParallelProcessing = enableParallelProcessing;
        }

        public int getMaxParallelism() {
            return maxParallelism;
        }

        public void setMaxParallelism(int maxParallelism) {
            this.maxParallelism = maxParallelism;
        }

        public boolean isEnableSnapshotRecovery() {
            return enableSnapshotRecovery;
        }

        public void setEnableSnapshotRecovery(boolean enableSnapshotRecovery) {
            this.enableSnapshotRecovery = enableSnapshotRecovery;
        }

        public long getSnapshotIntervalMs() {
            return snapshotIntervalMs;
        }

        public void setSnapshotIntervalMs(long snapshotIntervalMs) {
            this.snapshotIntervalMs = snapshotIntervalMs;
        }
    }

    public static class Alerting {
        private boolean enabled = true;
        private String channel = "SLACK";
        private String webhookUrl = "";
        private String emailRecipients = "";
        private boolean alertOnMatch = false;
        private boolean alertOnMismatch = true;
        private boolean alertOnSlaBreach = true;
        private boolean alertOnManualEscalation = true;
        private int alertCooldownMinutes = 15;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getWebhookUrl() {
            return webhookUrl;
        }

        public void setWebhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
        }

        public String getEmailRecipients() {
            return emailRecipients;
        }

        public void setEmailRecipients(String emailRecipients) {
            this.emailRecipients = emailRecipients;
        }

        public boolean isAlertOnMatch() {
            return alertOnMatch;
        }

        public void setAlertOnMatch(boolean alertOnMatch) {
            this.alertOnMatch = alertOnMatch;
        }

        public boolean isAlertOnMismatch() {
            return alertOnMismatch;
        }

        public void setAlertOnMismatch(boolean alertOnMismatch) {
            this.alertOnMismatch = alertOnMismatch;
        }

        public boolean isAlertOnSlaBreach() {
            return alertOnSlaBreach;
        }

        public void setAlertOnSlaBreach(boolean alertOnSlaBreach) {
            this.alertOnSlaBreach = alertOnSlaBreach;
        }

        public boolean isAlertOnManualEscalation() {
            return alertOnManualEscalation;
        }

        public void setAlertOnManualEscalation(boolean alertOnManualEscalation) {
            this.alertOnManualEscalation = alertOnManualEscalation;
        }

        public int getAlertCooldownMinutes() {
            return alertCooldownMinutes;
        }

        public void setAlertCooldownMinutes(int alertCooldownMinutes) {
            this.alertCooldownMinutes = alertCooldownMinutes;
        }
    }

    public static class Idempotency {
        private boolean enabled = true;
        private int maxVersionHistory = 100;
        private long ttlDays = 90;
        private boolean enableVersionCheck = true;
        private boolean allowDowngrade = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getMaxVersionHistory() {
            return maxVersionHistory;
        }

        public void setMaxVersionHistory(int maxVersionHistory) {
            this.maxVersionHistory = maxVersionHistory;
        }

        public long getTtlDays() {
            return ttlDays;
        }

        public void setTtlDays(long ttlDays) {
            this.ttlDays = ttlDays;
        }

        public boolean isEnableVersionCheck() {
            return enableVersionCheck;
        }

        public void setEnableVersionCheck(boolean enableVersionCheck) {
            this.enableVersionCheck = enableVersionCheck;
        }

        public boolean isAllowDowngrade() {
            return allowDowngrade;
        }

        public void setAllowDowngrade(boolean allowDowngrade) {
            this.allowDowngrade = allowDowngrade;
        }
    }

    public Matching getMatching() {
        return matching;
    }

    public void setMatching(Matching matching) {
        this.matching = matching;
    }

    public Sla getSla() {
        return sla;
    }

    public void setSla(Sla sla) {
        this.sla = sla;
    }

    public Processing getProcessing() {
        return processing;
    }

    public void setProcessing(Processing processing) {
        this.processing = processing;
    }

    public Alerting getAlerting() {
        return alerting;
    }

    public void setAlerting(Alerting alerting) {
        this.alerting = alerting;
    }

    public Idempotency getIdempotency() {
        return idempotency;
    }

    public void setIdempotency(Idempotency idempotency) {
        this.idempotency = idempotency;
    }

    public void logConfiguration() {
        log.info("=== Reconciliation Configuration ===");
        log.info("Matching: windowMinutes={}, maxDistanceMinutes={}, toleranceSeconds={}",
                matching.windowMinutes, matching.maxDistanceMinutes, matching.toleranceSeconds);
        log.info("SLA: warningMinutes={}, criticalMinutes={}, maxPendingAgeMinutes={}",
                sla.warningMinutes, sla.criticalMinutes, sla.maxPendingAgeMinutes);
        log.info("Processing: batchSize={}, maxConcurrency={}, enableParallelProcessing={}",
                processing.batchSize, processing.maxConcurrency, processing.enableParallelProcessing);
        log.info("Alerting: enabled={}, channel={}, alertOnSlaBreach={}",
                alerting.enabled, alerting.channel, alerting.alertOnSlaBreach);
        log.info("Idempotency: enabled={}, maxVersionHistory={}, ttlDays={}",
                idempotency.enabled, idempotency.maxVersionHistory, idempotency.ttlDays);
    }
}


// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.pragma</groupId>
    <artifactId>reconciliation</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>reconciliation</name>
    <description>Motor de Conciliación Bancaria en Tiempo Real</description>
    
    <properties>
        <java.version>21</java.version>
        <reactor.version>3.6.3</reactor.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.3</version>
            <scope>runtime</scope>
        </dependency>
        
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-r2dbc</artifactId>
        </dependency>
        
        <dependency>
            <groupId>io.r2dbc</groupId>
            <artifactId>r2dbc-postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>1.19.7</version>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>kafka</artifactId>
            <version>1.19.7</version>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <version>1.19.7</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/config/KafkaConfig.java ===
package com.pragma.reconciliation.infrastructure.config;

import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:reconciliation-group}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset:earliest}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.max-poll-records:500}")
    private int maxPollRecords;

    @Value("${spring.kafka.consumer.fetch-min-size:1}")
    private int fetchMinSize;

    @Value("${spring.kafka.consumer.fetch-max-wait-ms:500}")
    private int fetchMaxWaitMs;

    @Value("${spring.kafka.listener.concurrency:3}")
    private int concurrency;

    @Value("${spring.kafka.listener.max-retry:3}")
    private int maxRetry;

    @Value("${spring.kafka.listener.retry-interval-ms:1000}")
    private long retryIntervalMs;

    @Value("${spring.kafka.producer.acks:all}")
    private String acks;

    @Value("${spring.kafka.producer.retries:3}")
    private int retries;

    @Value("${spring.kafka.producer.batch-size:16384}")
    private int batchSize;

    @Value("${spring.kafka.producer.linger-ms:5}")
    private int lingerMs;

    @Value("${spring.kafka.producer.buffer-memory:33554432}")
    private int bufferMemory;

    @Value("${spring.kafka.topics.movements:movements}")
    private String movementsTopic;

    @Value("${spring.kafka.topics.events:reconciliation-events}")
    private String eventsTopic;

    @Value("${spring.kafka.topics.dlq:reconciliation-dlq}")
    private String dlqTopic;

    @Value("${spring.kafka.security.protocol:PLAINTEXT}")
    private String securityProtocol;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, fetchMinSize);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWaitMs);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
        props.put(ConsumerConfig.HEART_BEAT_INTERVAL_MS_CONFIG, 10000);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        props.put("security.protocol", securityProtocol);
        log.info("Consumer config initialized for group: {}, bootstrap servers: {}", groupId, bootstrapServers);
        return props;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 60000);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        props.put("security.protocol", securityProtocol);
        log.info("Producer config initialized with acks: {}, retries: {}", acks, retries);
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setSyncCommits(true);
        factory.setCommonErrorHandler(createErrorHandler());
        log.info("Kafka listener container factory initialized with concurrency: {}", concurrency);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> movementsListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(createErrorHandler());
        JsonDeserializer<ProcessMovementCommand> deserializer = new JsonDeserializer<>(ProcessMovementCommand.class);
        deserializer.addTrustedPackages("com.pragma.reconciliation.domain.commands");
        deserializer.setUseTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(false);
        factory.setRecordMessageConverter(new org.springframework.kafka.support.mapping.JsonMessageConverter());
        log.info("Movements listener container factory initialized");
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> eventsListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(createErrorHandler());
        log.info("Events listener container factory initialized");
        return factory;
    }

    private DefaultErrorHandler createErrorHandler() {
        FixedBackOff backOff = new FixedBackOff(retryIntervalMs, maxRetry);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(backOff);
        errorHandler.addNotRetryableExceptions(
                IllegalArgumentException.class,
                NullPointerException.class
        );
        log.info("Error handler created with maxRetry: {} and retryInterval: {}ms", maxRetry, retryIntervalMs);
        return errorHandler;
    }

    @Bean
    public NewTopic movementsTopic() {
        return TopicBuilder.name(movementsTopic)
                .partitions(6)
                .replicas(3)
                .config("retention.ms", "604800000")
                .config("cleanup.policy", "compact")
                .build();
    }

    @Bean
    public NewTopic eventsTopic() {
        return TopicBuilder.name(eventsTopic)
                .partitions(6)
                .replicas(3)
                .config("retention.ms", "259200000")
                .config("cleanup.policy", "compact")
                .build();
    }

    @Bean
    public NewTopic dlqTopic() {
        return TopicBuilder.name(dlqTopic)
                .partitions(3)
                .replicas(3)
                .config("retention.ms", "604800000")
                .config("cleanup.policy", "delete")
                .build();
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put("security.protocol", securityProtocol);
        return new KafkaAdmin(configs);
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getMovementsTopic() {
        return movementsTopic;
    }

    public String getEventsTopic() {
        return eventsTopic;
    }

    public String getDlqTopic() {
        return dlqTopic;
    }
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/domain/commands/ProcessMovementCommand.java ===
package com.pragma.reconciliation.domain.commands;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Comando que representa un movimiento financiero a procesar para conciliación.
 * Es un record inmutable que contiene toda la información del movimiento.
 */
public record ProcessMovementCommand(
    String eventId,
    String sourceReference,
    String sourceSystem,
    BigDecimal amount,
    String currency,
    Instant transactionDate,
    int version
) {
    
    public static ProcessMovementCommand create(
            String sourceReference,
            String sourceSystem,
            BigDecimal amount,
            String currency,
            Instant transactionDate) {
        
        Objects.requireNonNull(sourceReference, "sourceReference cannot be null");
        Objects.requireNonNull(sourceSystem, "sourceSystem cannot be null");
        Objects.requireNonNull(amount, "amount cannot be null");
        Objects.requireNonNull(currency, "currency cannot be null");
        Objects.requireNonNull(transactionDate, "transactionDate cannot be null");
        
        return new ProcessMovementCommand(
            java.util.UUID.randomUUID().toString(),
            sourceReference,
            sourceSystem,
            amount,
            currency,
            transactionDate,
            1
        );
    }
    
    public boolean isValidForMatchingWindow(long windowMinutes) {
        Instant now = Instant.now();
        Instant windowStart = now.minus(windowMinutes, ChronoUnit.MINUTES);
        return transactionDate.isAfter(windowStart) || transactionDate.equals(windowStart);
    }
    
    public boolean isDuplicateOf(ProcessMovementCommand other) {
        if (other == null) return false;
        return Objects.equals(sourceReference, other.sourceReference) 
            && Objects.equals(sourceSystem, other.sourceSystem);
    }
    
    public String getCompositeKey() {
        return sourceSystem + "|" + sourceReference;
    }
    
    public record MovementSource(
        String system,
        String reference
    ) {}
}

// === ARCHIVO: src/main/java/com/pragma/reconciliation/infrastructure/config/ReconciliationConfig.java ===
package com.pragma.reconciliation.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Configuración del módulo de conciliación.
 * Define parámetros para matching, SLA, procesamiento y alerting.
 */
@Configuration
@ConfigurationProperties(prefix = "reconciliation")
public class ReconciliationConfig {
    private static final Logger log = LoggerFactory.getLogger(ReconciliationConfig.class);
    
    private Matching matching;
    private Sla sla;
    private Processing processing;
    private Alerting alerting;
    private Idempotency idempotency;
    
    public ReconciliationConfig() {
        this.matching = new Matching();
        this.sla = new Sla();
        this.processing = new Processing();
        this.alerting = new Alerting();
        this.idempotency = new Idempotency();
    }
    
    public Matching getMatching() {
        return matching;
    }
    
    public void setMatching(Matching matching) {
        this.matching = matching;
    }
    
    public Sla getSla() {
        return sla;
    }
    
    public void setSla(Sla sla) {
        this.sla = sla;
    }
    
    public Processing getProcessing() {
        return processing;
    }
    
    public void setProcessing(Processing processing) {
        this.processing = processing;
    }
    
    public Alerting getAlerting() {
        return alerting;
    }
    
    public void setAlerting(Alerting alerting) {
        this.alerting = alerting;
    }
    
    public Idempotency getIdempotency() {
        return idempotency;
    }
    
    public void setIdempotency(Idempotency idempotency) {
        this.idempotency = idempotency;
    }
    
    public long getMatchingWindowMinutes() {
        return matching != null ? matching.windowMinutes : 60;
    }
    
    public BigDecimal getAmountTolerance() {
        return matching != null ? matching.tolerance : BigDecimal.ZERO;
    }
    
    public long getSlaMinutes() {
        return sla != null ? sla.minutes : 1440;
    }
    
    public void logConfiguration() {
        log.info("=== Configuración de Conciliación ===");
        log.info("Matching: ventana={} minutos, tolerancia={}", 
            getMatchingWindowMinutes(), getAmountTolerance());
        log.info("SLA: {} minutos", getSlaMinutes());
        log.info("Procesamiento: threads={}, batchSize={}", 
            processing != null ? processing.threads : 4,
            processing != null ? processing.batchSize : 100);
        log.info("Idempotencia: enabled={}, ttl={} horas", 
            idempotency != null ? idempotency.enabled : true,
            idempotency != null ? idempotency.ttlHours : 24);
    }
    
    public static class Matching {
        private long windowMinutes = 60;
        private BigDecimal tolerance = BigDecimal.valueOf(0.01);
        private boolean enablePartialMatch = true;
        
        public long getWindowMinutes() {
            return windowMinutes;
        }
        
        public void setWindowMinutes(long windowMinutes) {
            this.windowMinutes = windowMinutes;
        }
        
        public BigDecimal getTolerance() {
            return tolerance;
        }
        
        public void setTolerance(BigDecimal tolerance) {
            this.tolerance = tolerance;
        }
        
        public boolean isEnablePartialMatch() {
            return enablePartialMatch;
        }
        
        public void setEnablePartialMatch(boolean enablePartialMatch) {
            this.enablePartialMatch = enablePartialMatch;
        }
    }
    
    public static class Sla {
        private long minutes = 1440;
        private boolean enabled = true;
        private boolean criticalEscalation = true;
        
        public long getMinutes() {
            return minutes;
        }
        
        public void setMinutes(long minutes) {
            this.minutes = minutes;
        }
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public boolean isCriticalEscalation() {
            return criticalEscalation;
        }
        
        public void setCriticalEscalation(boolean criticalEscalation) {
            this.criticalEscalation = criticalEscalation;
        }
    }
    
    public static class Processing {
        private int threads = 4;
        private int batchSize = 100;
        private boolean asyncEnabled = true;
        
        public int getThreads() {
            return threads;
        }
        
        public void setThreads(int threads) {
            this.threads = threads;
        }
        
        public int getBatchSize() {
            return batchSize;
        }
        
        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }
        
        public boolean isAsyncEnabled() {
            return asyncEnabled;
        }
        
        public void setAsyncEnabled(boolean asyncEnabled) {
            this.asyncEnabled = asyncEnabled;
        }
    }
    
    public static class Alerting {
        private boolean enabled = true;
        private String webhookUrl;
        private int warningThresholdPercent = 80;
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getWebhookUrl() {
            return webhookUrl;
        }
        
        public void setWebhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
        }
        
        public int getWarningThresholdPercent() {
            return warningThresholdPercent;
        }
        
        public void setWarningThresholdPercent(int warningThresholdPercent) {
            this.warningThresholdPercent = warningThresholdPercent;
        }
    }
    
    public static class Idempotency {
        private boolean enabled = true;
        private int ttlHours = 24;
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public int getTtlHours() {
            return ttlHours;
        }
        
        public void setTtlHours(int ttlHours) {
            this.ttlHours = ttlHours;
        }
    }
}

// === ARCHIVO: src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java ===
package com.pragma.reconciliation.infrastructure.adapters.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.reconciliation.application.usecases.ProcessMovementUseCase;
import com.pragma.reconciliation.domain.commands.ProcessMovementCommand;
import com.pragma.reconciliation.domain.model.Reconciliation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMovementListenerTest {

    @Mock
    private ProcessMovementUseCase processMovementUseCase;

    private ObjectMapper objectMapper;
    private KafkaMovementListener listener;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        listener = new KafkaMovementListener(processMovementUseCase, objectMapper);
    }

    @Nested
    @DisplayName("Consumo de mensajes desde Kafka")
    class MessageConsumption {

        @Test
        @DisplayName("Debe procesar mensaje válido correctamente")
        void shouldProcessValidMessage() {
            String json = """
                {
                    "sourceReference": "TXN-KAFKA-001",
                    "sourceSystem": "CORE_BANK",
                    "amount": 2500.75,
                    "currency": "USD",
                    "transactionDate": "2024-01-15T10:30:00Z",
                    "eventId": "evt-001",
                    "version": 1
                }
                """;

            Reconciliation expectedResult = new Reconciliation(
                "TXN-KAFKA-001", "CORE_BANK", new BigDecimal("2500.75"), "USD", Instant.parse("2024-01-15T10:30:00Z")
            );

            when(processMovementUseCase.execute(any(ProcessMovementCommand.class)))
                .thenReturn(Mono.just(expectedResult));

            CountDownLatch latch = new CountDownLatch(1);

            listener.listenMovement(json, null).doOnTerminate(latch::countDown).subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for message processing");
            }

            ArgumentCaptor<ProcessMovementCommand> captor = ArgumentCaptor.forClass(ProcessMovementCommand.class);
            verify(processMovementUseCase, times(1)).execute(captor.capture());

            ProcessMovementCommand captured = captor.getValue();
            assertEquals("TXN-KAFKA-001", captured.sourceReference());
            assertEquals("CORE_BANK", captured.sourceSystem());
            assertEquals(new BigDecimal("2500.75"), captured.amount());
        }

        @Test
        @DisplayName("Debe manejar error de procesamiento gracefully")
        void shouldHandleProcessingError() {
            String json = """
                {
                    "sourceReference": "TXN-ERROR-001",
                    "sourceSystem": "INVALID_SYSTEM",
                    "amount": 100.00,
                    "currency": "USD",
                    "transactionDate": "2024-01-15T10:30:00Z",
                    "eventId": "evt-002",
                    "version": 1
                }
                """;

            when(processMovementUseCase.execute(any(ProcessMovementCommand.class)))
                .thenReturn(Mono.error(new RuntimeException("Invalid source system")));

            CountDownLatch latch = new CountDownLatch(1);

            listener.listenMovement(json, null)
                .doOnError(e -> latch.countDown())
                .subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for error handling");
            }

            verify(processMovementUseCase, times(1)).execute(any(ProcessMovementCommand.class));
        }

        @Test
        @DisplayName("Debe procesar lote de mensajes correctamente")
        void shouldProcessBatchOfMessages() throws Exception {
            List<String> jsons = List.of(
                """{"sourceReference":"TXN-001","sourceSystem":"CORE_BANK","amount":100,"currency":"USD","transactionDate":"2024-01-15T10:30:00Z","eventId":"evt-001","version":1}""",
                """{"sourceReference":"TXN-002","sourceSystem":"PAYMENT_GW","amount":200,"currency":"USD","transactionDate":"2024-01-15T11:30:00Z","eventId":"evt-002","version":1}""",
                """{"sourceReference":"TXN-003","sourceSystem":"LIQUIDATION","amount":300,"currency":"USD","transactionDate":"2024-01-15T12:30:00Z","eventId":"evt-003","version":1}"""
            );

            when(processMovementUseCase.execute(any(ProcessMovementCommand.class)))
                .thenAnswer(inv -> {
                    ProcessMovementCommand cmd = inv.getArgument(0);
                    Reconciliation r = new Reconciliation(
                        cmd.sourceReference(), cmd.sourceSystem(), cmd.amount(), cmd.currency(), cmd.transactionDate()
                    );
                    return Mono.just(r);
                });

            CountDownLatch latch = new CountDownLatch(3);

            for (String json : jsons) {
                listener.listenMovement(json, null)
                    .doOnTerminate(latch::countDown)
                    .subscribe();
            }

            try {
                assertTrue(latch.await(10, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for batch processing");
            }

            verify(processMovementUseCase, times(3)).execute(any(ProcessMovementCommand.class));
        }
    }

    @Nested
    @DisplayName("Integración con validación")
    class ValidationIntegration {

        @Test
        @DisplayName("Debe rechazar mensaje con JSON inválido")
        void shouldRejectInvalidJson() {
            String invalidJson = "{ invalid json structure ";

            CountDownLatch latch = new CountDownLatch(1);

            listener.listenMovement(invalidJson, null)
                .doOnError(e -> latch.countDown())
                .subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for invalid JSON handling");
            }

            verify(processMovementUseCase, never()).execute(any());
        }

        @Test
        @DisplayName("Debe manejar mensaje con campos faltantes")
        void shouldHandleMissingFields() {
            String json = """
                {
                    "sourceReference": "TXN-MISSING-001"
                }
                """;

            CountDownLatch latch = new CountDownLatch(1);

            listener.listenMovement(json, null)
                .doOnError(e -> latch.countDown())
                .subscribe();

            try {
                assertTrue(latch.await(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for missing fields handling");
            }

            verify(processMovementUseCase, never()).execute(any());
        }
    }

    @Nested
    @DisplayName("Control de flujo y backpressure")
    class FlowControl {

        @Test
        @DisplayName("Debe completar incluso con mensaje null")
        void shouldCompleteWithNullMessage() {
            CountDownLatch latch = new CountDownLatch(1);

            listener.listenMovement(null, null)
                .doOnTerminate(latch::countDown)
                .subscribe();

            try {
                assertTrue(latch.await(2, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                fail("Timeout waiting for null message");
            }

            verifyNoInteractions(processMovementUseCase);
        }
    }
}

```
