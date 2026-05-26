# ADR-0001: Adoptar arquitectura de monolito modular

- **Status:** Accepted
- **Fecha:** 2026-05-24
- **Autores:** Equipo ReelClips

---

## Contexto

ReelClips posee varios dominios funcionales claramente definidos, como usuarios, reels, feed, categorías, interacciones y chat. Debido al alcance académico del proyecto y al tamaño reducido del equipo, una arquitectura distribuida basada en microservicios introduciría complejidad innecesaria en despliegue, comunicación y mantenimiento. Además, la construcción del feed requiere interacción constante entre varios dominios internos.

---

## Decisión

Se adopta una arquitectura de **monolito modular**. La aplicación se desplegará como una única unidad ejecutable, pero organizada internamente en módulos con responsabilidades separadas y fronteras claras.

---

## Consecuencias

### Positivas

- Menor complejidad de despliegue y configuración.
- Mayor velocidad de desarrollo.
- Separación lógica de responsabilidades internas.
- Menor costo operativo comparado con microservicios.

### Negativas

- No existe escalabilidad independiente por módulo.
- Un despliegue afecta toda la aplicación.
- El crecimiento excesivo de un módulo podría requerir refactorización futura.
