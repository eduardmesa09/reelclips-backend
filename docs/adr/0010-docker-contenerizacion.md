# ADR-0010: Contenerizar la aplicación con Docker

- **Status:** Accepted
- **Fecha:** 2026-05-24
- **Autores:** Equipo ReelClips

---

## Contexto

El proyecto será desarrollado por varios integrantes y debe ejecutarse de forma consistente en distintos entornos. Sin una forma estándar de empaquetar la aplicación, pueden aparecer diferencias entre el ambiente local y el de despliegue.

---

## Decisión

Se utilizará **Docker** para contenerizar el backend, el frontend y los servicios de soporte necesarios. Esto permitirá levantar el sistema de manera reproducible en cualquier entorno compatible.

---

## Consecuencias

### Positivas

- Consistencia entre ambientes.
- Menos problemas de configuración local.
- Despliegue y pruebas más ordenadas.
- Facilita la integración con otros servicios.

### Negativas

- Requiere definir y mantener archivos de configuración.
- Agrega complejidad inicial al proyecto.
- Puede requerir ajustes para desarrollo local.
