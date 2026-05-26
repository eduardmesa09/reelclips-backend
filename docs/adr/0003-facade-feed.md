# ADR-0003: Aplicar patrón Facade para el feed

- **Status:** Accepted
- **Fecha:** 2026-05-24
- **Autores:** Equipo ReelClips

---

## Contexto

La construcción del feed requiere coordinar múltiples operaciones: recuperar reels, aplicar filtros, validar visibilidad, ordenar resultados y paginar contenido para soportar scroll infinito. Exponer toda esta lógica directamente al frontend aumentaría el acoplamiento entre capas.

---

## Decisión

Se implementa el patrón **Facade** mediante un componente central encargado de coordinar la generación del feed y ocultar la complejidad interna del proceso.

---

## Consecuencias

### Positivas

- La interfaz consume un único punto de acceso.
- La lógica del feed queda centralizada.
- Se reduce el acoplamiento entre frontend y backend.
- Facilita futuras modificaciones en reglas de recomendación.

### Negativas

- La fachada puede crecer demasiado si acumula responsabilidades.
- Requiere control arquitectónico para evitar lógica excesiva en un solo componente.
