# Diseño de Motor de Conciliación Bancaria en Tiempo Real

El sistema de conciliación bancaria en tiempo real consume streams de movimientos desde tres fuentes (core bancario, gateway de pagos, sistema de liquidación) y detecta discrepancias en ventanas móviles. Cada movimiento debe reconciliarse contra las tres fuentes con tolerancia a mensajes fuera de orden y llegadas duplicadas. El objetivo es diseñar la máquina de estados de cada Reconciliation (Pending, Matched, Mismatched, Manual), justificar la ventana de matching (5 min vs 1 hora), definir cómo manejar idempotencia con eventId + version, y elegir entre reprocesamiento por replay de Kafka vs snapshot desde PostgreSQL. Adicionalmente, se debe explicar cómo alertar al equipo de operaciones cuando el lag de conciliación supera un SLA.

## Informacion General

| Campo | Valor |
|-------|-------|
| **Tema** | TEST-CT |
| **Nivel** | senior-l2 |
| **Tipo** | mixed |
| **Tiempo estimado** | 12 horas |

## Fases del Reto

### Fase 0: Configuración del Proyecto

**Objetivo:** Obtener el proyecto base funcional enviando el Código Base a un asistente de IA, que lo analizará, corregirá errores y generará un ZIP listo para usar.

**Tiempo estimado:** 15-30 minutos

**Instrucciones:**

- Asegúrate de tener instalado para ejecutar el proyecto: JDK 17+, Maven 3.9+, IDE con soporte Java.
- Copia todo el contenido del campo **Código Base** de este reto — incluyendo el texto de instrucciones que aparece al inicio.
- Abre un asistente de IA (Claude en claude.ai, ChatGPT o Gemini — se recomienda Claude), pega el contenido copiado en el chat y envíalo.
- El asistente analizará los archivos, corregirá errores y generará un archivo ZIP descargable. Descárgalo y extráelo en la carpeta donde quieras trabajar.
- Ejecuta `mvn compile` en la raíz. Si no hay errores, estás listo.

**Entregable:** El proyecto compila/arranca sin errores.

<details>
<summary>Pistas de conocimiento</summary>

- Copia el Código Base completo incluyendo el texto de instrucciones al inicio — esas instrucciones le indican al asistente exactamente qué hacer con los archivos.
- Si el asistente no genera el ZIP automáticamente al terminar el análisis, escríbele: "genera el ZIP ahora".
- Si el proyecto tiene errores al arrancar, comparte el mensaje de error con el mismo asistente para que lo corrija.

</details>

### Fase 1: Exploración del Sistema y Definición de Requisitos

**Objetivo:** Identificar y documentar los requisitos y restricciones del sistema de conciliación.

**Tiempo estimado:** 3 horas

**Instrucciones:**

- Identificar las fuentes de movimientos y sus características.
- Documentar las restricciones y ambigüedades del sistema.
- Definir los criterios de aceptación para la fase.

**Entregable:** Documento de requisitos y restricciones del sistema de conciliación.

<details>
<summary>Pistas de conocimiento</summary>

- Considerar los tipos de movimientos y sus fuentes.
- Identificar posibles ambigüedades en la documentación existente.

</details>

### Fase 2: Diseño de la Máquina de Estados y Ventana de Matching

**Objetivo:** Diseñar la máquina de estados de cada Reconciliation y justificar la ventana de matching.

**Tiempo estimado:** 4 horas

**Instrucciones:**

- Diseñar la máquina de estados para cada Reconciliation (Pending, Matched, Mismatched, Manual).
- Justificar la elección de la ventana de matching (5 min vs 1 hora).
- Definir los criterios de aceptación para la fase.

**Entregable:** Diagrama de la máquina de estados y documento justificando la ventana de matching.

<details>
<summary>Pistas de conocimiento</summary>

- Considerar los posibles estados de una Reconciliation y las transiciones entre ellos.
- Evaluar los pros y contras de diferentes ventanas de matching.

</details>

### Fase 3: Manejo de Idempotencia y Estrategia de Reprocesamiento

**Objetivo:** Definir cómo manejar idempotencia y elegir la estrategia de reprocesamiento.

**Tiempo estimado:** 3 horas

**Instrucciones:**

- Definir cómo manejar idempotencia con eventId + version.
- Elegir entre reprocesamiento por replay de Kafka vs snapshot desde PostgreSQL.
- Definir los criterios de aceptación para la fase.

**Entregable:** Documento describiendo el manejo de idempotencia y la estrategia de reprocesamiento elegida.

<details>
<summary>Pistas de conocimiento</summary>

- Considerar los pros y contras de cada estrategia de reprocesamiento.
- Evaluar cómo manejar eventos duplicados y fuera de orden.

</details>

### Fase 4: Alerta de Lag de Conciliación

**Objetivo:** Definir cómo alertar al equipo de operaciones cuando el lag de conciliación supera un SLA.

**Tiempo estimado:** 2 horas

**Instrucciones:**

- Definir los criterios para alertar al equipo de operaciones cuando el lag de conciliación supera un SLA.
- Documentar la estrategia de alertas y los criterios de aceptación para la fase.

**Entregable:** Documento describiendo la estrategia de alertas y los criterios para alertar al equipo de operaciones.

<details>
<summary>Pistas de conocimiento</summary>

- Considerar los umbrales de lag aceptables y las consecuencias de superarlos.
- Evaluar diferentes mecanismos de alerta.

</details>

## Dimensiones Evaluadas

- **queEs**: ¿Qué es una Reconciliation y cuáles son sus estados posibles?
- **paraQueSirve**: ¿Para qué sirve la ventana de matching en el sistema de conciliación?
- **comoSeUsa**: ¿Cómo se usa eventId + version para manejar idempotencia?
- **erroresComunes**: ¿Cuáles son los errores comunes al diseñar un sistema de conciliación?
- **queDecisionesImplica**: ¿Qué decisiones implica elegir entre reprocesamiento por replay de Kafka vs snapshot desde PostgreSQL?

## Criterios de Evaluacion

- Identificar y documentar los requisitos y restricciones del sistema de conciliación.
- Diseñar la máquina de estados para cada Reconciliation y justificar la ventana de matching.
- Definir cómo manejar idempotencia y elegir la estrategia de reprocesamiento.
- Definir los criterios para alertar al equipo de operaciones cuando el lag de conciliación supera un SLA.

## Como trabajar con un asistente de IA

Hay dos caminos, elegi uno:

- **AGENTS.md** (recomendado) — instrucciones nativas del repo. Abri esta carpeta con tu agente local (Claude Code, Cursor, Codex, Copilot, Gemini) y las carga solo. Sabe que archivos faltan y con que comando se verifica, y completa el scaffold escribiendo en disco.
- **PROMPT_MEJORA.md** — para copiar y pegar en un chat (claude.ai, ChatGPT). Devuelve un ZIP con el proyecto. Sirve si no tenes un agente en el IDE.

Ninguno de los dos resuelve las fases del reto: eso es tu trabajo.

## Verificacion

El proyecto esta listo para trabajar cuando este comando corre sin errores:

```bash
el comando de build o arranque canonico del stack elegido
```

---

*Reto generado automaticamente por Challenge Generator - Pragma*
