# ADR-0006: Implementar chat en tiempo real con WebSocket

- **Status:** Accepted
- **Fecha:** 2026-05-24
- **Autores:** Equipo ReelClips

---

## Contexto

El sistema requiere conversaciones directas entre usuarios con baja latencia y actualización inmediata de mensajes. Una solución basada únicamente en polling REST produciría retrasos y mayor consumo de recursos.

---

## Decisión

Se implementa el chat utilizando **WebSocket** para comunicación bidireccional en tiempo real y eventos internos de Spring para desacoplar procesos secundarios relacionados con notificaciones y métricas.

---

## Consecuencias

### Positivas

- Comunicación casi inmediata entre usuarios.
- Mejor experiencia de usuario.
- Menor latencia en mensajería.
- Arquitectura más orientada a eventos.

### Negativas

- Mayor complejidad técnica.
- Manejo más delicado de conexiones persistentes.
- Incremento en dificultad de monitoreo y depuración.
