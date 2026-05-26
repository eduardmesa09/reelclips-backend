# ADR-0005: Aplicar patrón Observer para interacciones

- **Status:** Accepted
- **Fecha:** 2026-05-24
- **Autores:** Equipo ReelClips

---

## Contexto

Las interacciones sobre reels, como likes y comentarios, pueden generar múltiples acciones secundarias: actualización de métricas, notificaciones y registro de actividad. Implementar todas estas operaciones directamente dentro del flujo principal produciría fuerte acoplamiento.

---

## Decisión

Se implementa el patrón **Observer**, permitiendo que diferentes observadores reaccionen a eventos generados por el módulo de interacciones.

---

## Consecuencias

### Positivas

- Se desacoplan procesos secundarios del flujo principal.
- Facilita agregar nuevas funcionalidades basadas en eventos.
- Mejora la extensibilidad del sistema.

### Negativas

- El flujo de ejecución es menos explícito.
- Puede dificultar el seguimiento de eventos durante depuración.
- Un número elevado de observadores podría afectar el rendimiento.
