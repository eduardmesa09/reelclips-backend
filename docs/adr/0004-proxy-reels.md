# ADR-0004: Aplicar patrón Proxy para acceso a reels

- **Status:** Accepted
- **Fecha:** 2026-05-24
- **Autores:** Equipo ReelClips

---

## Contexto

La reproducción de reels debe ser rápida y eficiente para soportar el scroll infinito y una buena experiencia de usuario. Acceder directamente al almacenamiento multimedia en cada solicitud incrementaría la latencia y el consumo de recursos.

---

## Decisión

Se implementa el patrón **Proxy** para controlar el acceso a los reels, gestionar caché y delegar el acceso al almacenamiento multimedia únicamente cuando sea necesario.

---

## Opciones evaluadas

| Opción                    | Ventajas                           | Desventajas                          | Decisión   |
|---------------------------|------------------------------------|--------------------------------------|------------|
| Acceso directo al storage | Simple                             | Baja performance y sin control       | Rechazada  |
| **Proxy + Caché**         | Mejora rendimiento y seguridad     | Agrega una capa de complejidad       | **Aceptada** |
| CDN externo               | Excelente rendimiento              | Mayor costo y complejidad            | Rechazada  |

---
## Consecuencias

### Positivas

- Mejora el rendimiento percibido.
- Permite integrar mecanismos de caché.
- Centraliza validaciones de acceso y visibilidad.
- Reduce accesos innecesarios al almacenamiento externo.

### Negativas

- Se agrega una capa adicional de complejidad.
- La gestión incorrecta de caché puede generar inconsistencias.
- El proxy puede convertirse en un punto crítico del sistema.
