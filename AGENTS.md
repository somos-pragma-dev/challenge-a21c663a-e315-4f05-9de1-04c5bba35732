# AGENTS.md

Instrucciones para el agente de IA que abra este repositorio (Claude Code, Cursor, Codex, Copilot, Gemini). Se cargan solas: no hay que pegar nada en ningun chat.

## Que es este repositorio

Es el codigo base de un reto de aprendizaje de Pragma: **Diseño de Motor de Conciliación Bancaria en Tiempo Real**.

| | |
|---|---|
| Tema | TEST-CT |
| Nivel | senior-l2 |
| Chapter | Generico |
| Especialidad | Inferido del contexto |
| Stack | Java / Spring Boot 3.4 |
| Patron arquitectonico | hexagonal/clean con CQRS y Event Sourcing |
| Tiempo estimado | 12 horas |

## Tu tarea

Dejar este proyecto en estado **verificable**: que el comando de verificacion corra sin errores. Escribi los archivos en disco, en este repositorio. No generes ZIPs ni archivos adjuntos.

En orden:

1. Corre `el comando de build o arranque canonico del stack elegido` y mira que falla.
2. Completa lo que falte de la lista de abajo: manifiesto de dependencias, punto de entrada, capa de interfaz y las capas del patron declarado.
3. Arregla SOLO los errores que impiden compilar o arrancar.
4. Volve a correr `el comando de build o arranque canonico del stack elegido` hasta que pase.
5. Pará ahí.

## Regla dura: las fases son trabajo del humano

**PROHIBIDO implementar los entregables de las fases.** El valor del reto esta en que la persona los resuelva. Tu trabajo es que tenga un proyecto que arranca; el hueco pedagogico se queda como esta.

No resuelvas nada de esto:

- **Fase 1 — Exploración del Sistema y Definición de Requisitos**: Documento de requisitos y restricciones del sistema de conciliación.
- **Fase 2 — Diseño de la Máquina de Estados y Ventana de Matching**: Diagrama de la máquina de estados y documento justificando la ventana de matching.
- **Fase 3 — Manejo de Idempotencia y Estrategia de Reprocesamiento**: Documento describiendo el manejo de idempotencia y la estrategia de reprocesamiento elegida.
- **Fase 4 — Alerta de Lag de Conciliación**: Documento describiendo la estrategia de alertas y los criterios para alertar al equipo de operaciones.

Distincion operativa:

- **Arreglar** (si): import faltante, tipo que no existe, dependencia sin declarar, error de sintaxis, archivo referenciado que no existe.
- **No tocar** (no): logica de negocio incompleta, validaciones ausentes, secretos hardcodeados, APIs deprecadas que funcionan, concurrencia insegura, patrones mejorables. Eso es lo que la persona tiene que encontrar.

## Lo que falta y tenes que completar

### 1. Boilerplate del stack (2)

Sin esto el proyecto no compila ni arranca. **Es tu trabajo crearlo**, y no toca nada de lo pedagogico: es andamiaje del stack.

- [ ] **Punto de entrada del stack elegido** — Sin un punto de entrada reconocible, el runtime no tiene por donde arrancar la aplicacion.
- [ ] **Capa de interfaz (controller/handler)** — Sin una capa de interfaz explicita, no hay forma de invocar la logica de negocio desde afuera del proceso.

### 2. Referencias colgando (57)

Salieron de un analisis estatico del codigo que SI esta en el repo. Cada una rompe la compilacion:

- [ ] `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `Processing`
      Processing se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.infrastructure.config.Processing.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `Status`
      Status se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.model.Status.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `Matched`
      Matched se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.events.Matched.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `Mismatched`
      Mismatched se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.events.Mismatched.
- [ ] `src/test/java/com/pragma/reconciliation/domain/model/ReconciliationTest.java` — `Matching`
      Matching se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.infrastructure.config.Matching.
- [ ] `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `Created`
      Created se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.events.Created.
- [ ] `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `Idempotency`
      Idempotency se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.infrastructure.config.Idempotency.
- [ ] `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `Matched`
      Matched se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.events.Matched.
- [ ] `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `Mismatched`
      Mismatched se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.events.Mismatched.
- [ ] `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `ManualReviewEscalation`
      ManualReviewEscalation se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.pragma.reconciliation.domain.events.ManualReviewEscalation.
- [ ] `src/main/java/com/pragma/reconciliation/domain/events/ReconciliationEvent.java` — `Reconciliation`
      El import com.pragma.reconciliation.domain.model.Reconciliation no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Flux pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListener.java` — `com.fasterxml.jackson`
      El import com.fasterxml.jackson.databind.ObjectMapper pertenece a com.fasterxml.jackson, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/ReconciliationEventPublisher.java` — `com.fasterxml.jackson`
      El import com.fasterxml.jackson.databind.ObjectMapper pertenece a com.fasterxml.jackson, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `com.fasterxml.jackson`
      El import com.fasterxml.jackson.databind.ObjectMapper pertenece a com.fasterxml.jackson, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `reactor.core.publisher`
      El import reactor.core.publisher.Mono pertenece a reactor.core.publisher, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/pragma/reconciliation/domain/model/Reconciliation.java` — `Status.canTransitionTo`
      Se invoca `canTransitionTo` sobre `Status`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java` — `Status.name`
      Se invoca `name` sobre `Status`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java` — `MovementMatch.source`
      Se invoca `source` sobre `MovementMatch`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java` — `MovementMatch.reference`
      Se invoca `reference` sobre `MovementMatch`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java` — `MovementMatch.amount`
      Se invoca `amount` sobre `MovementMatch`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.sourceReference`
      Se invoca `sourceReference` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.sourceSystem`
      Se invoca `sourceSystem` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.amount`
      Se invoca `amount` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `PostgreSQLReconciliationRepository.findBySourceReferenceAndSystem`
      Se invoca `findBySourceReferenceAndSystem` sobre `PostgreSQLReconciliationRepository`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.eventId`
      Se invoca `eventId` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.version`
      Se invoca `version` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.currency`
      Se invoca `currency` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ProcessMovementCommand.transactionDate`
      Se invoca `transactionDate` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java` — `ReconciliationEvent.getClass`
      Se invoca `getClass` sobre `ReconciliationEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.sourceReference`
      Se invoca `sourceReference` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.sourceSystem`
      Se invoca `sourceSystem` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.eventId`
      Se invoca `eventId` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.version`
      Se invoca `version` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.amount`
      Se invoca `amount` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.currency`
      Se invoca `currency` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java` — `ProcessMovementCommand.transactionDate`
      Se invoca `transactionDate` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/ReconciliationEventPublisher.java` — `IdempotentProcessed.getClass`
      Se invoca `getClass` sobre `IdempotentProcessed`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `LagMetrics.getCurrentLag`
      Se invoca `getCurrentLag` sobre `LagMetrics`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `LagMetrics.getSource`
      Se invoca `getSource` sobre `LagMetrics`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `LagMetrics.getPendingCount`
      Se invoca `getPendingCount` sobre `LagMetrics`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `LagAlertPublisher.publishSlaAlert`
      Se invoca `publishSlaAlert` sobre `LagAlertPublisher`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `LagAlertPublisher.publishSystemLagAlert`
      Se invoca `publishSystemLagAlert` sobre `LagAlertPublisher`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java` — `LagMetrics.getProcessedCount`
      Se invoca `getProcessedCount` sobre `LagMetrics`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `ProcessMovementCommand.sourceReference`
      Se invoca `sourceReference` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `ProcessMovementCommand.amount`
      Se invoca `amount` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `PostgreSQLReconciliationRepository.findUnmatchedWithinWindow`
      Se invoca `findUnmatchedWithinWindow` sobre `PostgreSQLReconciliationRepository`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `ReconciliationService.resolveReconciliation`
      Se invoca `resolveReconciliation` sobre `ReconciliationService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java` — `ReconciliationService.checkAndEscalateIfNeeded`
      Se invoca `checkAndEscalateIfNeeded` sobre `ReconciliationService`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `ProcessMovementCommand.sourceReference`
      Se invoca `sourceReference` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `ProcessMovementCommand.sourceSystem`
      Se invoca `sourceSystem` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `ProcessMovementCommand.amount`
      Se invoca `amount` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `ProcessMovementCommand.currency`
      Se invoca `currency` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java` — `ProcessMovementCommand.transactionDate`
      Se invoca `transactionDate` sobre `ProcessMovementCommand`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

### Presentes (19)

- `pom.xml`
- `src/main/resources/application.yml`
- `src/main/java/com/pragma/reconciliation/ReconciliationApplication.java`
- `src/main/java/com/pragma/reconciliation/domain/model/Reconciliation.java`
- `src/main/java/com/pragma/reconciliation/domain/events/ReconciliationEvent.java`
- `src/main/java/com/pragma/reconciliation/domain/commands/ProcessMovementCommand.java`
- `src/main/java/com/pragma/reconciliation/domain/queries/GetReconciliationStatusQuery.java`
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/PostgreSQLReconciliationRepository.java`
- `src/main/java/com/pragma/reconciliation/application/services/ReconciliationService.java`
- `src/main/java/com/pragma/reconciliation/application/usecases/ProcessMovementUseCase.java`
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListener.java`
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound/ReconciliationEventPublisher.java`
- `src/main/java/com/pragma/reconciliation/infrastructure/config/KafkaConfig.java`
- `src/main/java/com/pragma/reconciliation/infrastructure/config/ReconciliationConfig.java`
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring/ReconciliationLagMonitor.java`
- `src/test/java/com/pragma/reconciliation/domain/model/ReconciliationTest.java`
- `src/test/java/com/pragma/reconciliation/application/services/ReconciliationServiceTest.java`
- `src/test/java/com/pragma/reconciliation/infrastructure/adapters/inbound/KafkaMovementListenerTest.java`
- `src/main/java/com/pragma/reconciliation/domain/model/MovementMatch.java`

### Capas del patron declarado

Cada una tiene que existir como directorio real con al menos un archivo. Codigo plano en la raiz no satisface el patron.

- `src/main/java/com/pragma/reconciliation`
- `src/main/java/com/pragma/reconciliation/domain`
- `src/main/java/com/pragma/reconciliation/domain/model`
- `src/main/java/com/pragma/reconciliation/domain/events`
- `src/main/java/com/pragma/reconciliation/domain/commands`
- `src/main/java/com/pragma/reconciliation/domain/queries`
- `src/main/java/com/pragma/reconciliation/application`
- `src/main/java/com/pragma/reconciliation/application/services`
- `src/main/java/com/pragma/reconciliation/application/usecases`
- `src/main/java/com/pragma/reconciliation/infrastructure`
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters`
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/inbound`
- `src/main/java/com/pragma/reconciliation/infrastructure/adapters/outbound`
- `src/main/java/com/pragma/reconciliation/infrastructure/config`
- `src/main/java/com/pragma/reconciliation/infrastructure/monitoring`
- `src/test/java/com/pragma/reconciliation`

## Verificacion

```bash
el comando de build o arranque canonico del stack elegido
```

Ese comando pasando es la definicion de "terminado" para vos.

## Convenciones que tenes que respetar

- Un solo ecosistema: no declares librerias de otro lenguaje ni mezcles gestores de paquetes.
- Toda libreria que uses tiene que estar declarada en el manifiesto de dependencias.
- Todo import declarado tiene que usarse; todo tipo usado tiene que existir o venir de una dependencia declarada.
- El patron es **hexagonal/clean con CQRS y Event Sourcing**: los contratos (interfaces, puertos) los define la capa interna y los implementa la externa, nunca al revés.
- Los archivos que crees llevan implementacion real, no stubs: sin `TODO`, sin cuerpos vacios, sin `// getters y setters`.

## Contexto del candidato

Sirve para calibrar el nivel del codigo, no para resolver las fases.

- Brecha que el reto ataca: Diseño de motor de conciliación que consume streams de movimientos desde 3 fuentes (core bancario, gateway de pagos, sistema de liquidación) y detecta discrepancias en ventanas móviles. Cada movimiento debe reconciliarse contra las 3 fuentes con tolerancia a mensajes fuera de orden y llegadas duplicadas. El desarrollador senior debe diseñar la máquina de estados de cada Reconciliation (Pending, Matched, Mismatched, Manual), justificar la ventana de matching (5 min vs 1 hora), definir cómo maneja idempotencia con eventId + version, y elegir entre reprocesamiento por replay de Kafka vs snapshot desde PostgreSQL. Adicionalmente debe explicar cómo alertar al equipo de operaciones cuando el lag de conciliación supera un SLA.

---

*Generado por Challenge Generator — Pragma. `README.md` tiene el enunciado completo del reto para la persona. `PROMPT_MEJORA.md` es la variante para pegar en un chat, si se prefiere ese flujo.*
