# ADR-0008: Usar Redis como caché para feed y consultas frecuentes

- **Status:** Accepted
- **Fecha:** 2026-05-24
- **Autores:** Equipo ReelClips

---

## Contexto

El feed de ReelClips debe responder rápidamente y soportar scroll infinito. Además, varias consultas del sistema se repiten con frecuencia, como lectura de contenido visible, sesiones o datos consultados de forma continua. Ejecutar siempre estas consultas contra la base de datos principal puede afectar el rendimiento.

---

## Decisión

Se utilizará **Redis** como caché en memoria para almacenar resultados frecuentes, datos temporales y consultas de alta repetición, especialmente en el feed y en operaciones de lectura intensiva.

---

## Consecuencias

### Positivas

- Disminuye la latencia en consultas frecuentes.
- Reduce la carga sobre PostgreSQL.
- Mejora la experiencia de navegación en el feed.
- Ayuda a sostener el rendimiento esperado del sistema.

### Negativas

- Se necesita estrategia de expiración e invalidación.
- La información en caché puede quedar desactualizada si no se sincroniza bien.
- Agrega una capa tecnológica adicional.
