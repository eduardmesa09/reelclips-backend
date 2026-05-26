# ADR-0009: Usar PostgreSQL como base de datos principal

- **Status:** Accepted
- **Fecha:** 2026-05-24
- **Autores:** Equipo ReelClips

---

## Contexto

ReelClips maneja entidades relacionales y reglas de negocio consistentes, como usuarios, canales, reels, comentarios, reacciones, conversaciones y mensajes. Este tipo de información requiere integridad, relaciones claras y consultas estructuradas.

---

## Decisión

Se utilizará **PostgreSQL** como base de datos principal para toda la información transaccional del sistema. Los datos estructurados del dominio se almacenarán allí, mientras que los archivos multimedia se delegarán a Supabase Storage.

---

## Consecuencias

### Positivas

- Buen soporte para relaciones entre entidades.
- Integridad referencial y consistencia transaccional.
- Permite modelar bien el dominio del sistema.
- Es una solución sólida para datos estructurados.

### Negativas

- No es ideal para archivos multimedia grandes.
- Algunas consultas del feed pueden requerir optimización adicional.
- A medida que crezca la plataforma, pueden aparecer cuellos de botella de lectura.
