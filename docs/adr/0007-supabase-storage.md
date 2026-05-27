# ADR-0007: Usar Supabase Storage para almacenar videos e imágenes

- **Status:** Accepted
- **Fecha:** 2026-05-26
- **Autores:** Equipo ReelClips

---

## Contexto

ReelClips maneja archivos multimedia pesados, especialmente videos cortos y fotos de perfil. Guardar estos archivos dentro de la base de datos relacional aumentaría el tamaño de la base, complicaría las copias de seguridad y afectaría el rendimiento. Además, la plataforma necesita una solución externa que permita almacenar y servir archivos de manera eficiente.

---

## Decisión

Se utilizará **Supabase Storage** para almacenar los videos de los reels y las imágenes de perfil. La base de datos principal conservará solo los metadatos del contenido, mientras que los archivos multimedia se mantendrán fuera de PostgreSQL.

---

## Opciones evaluadas

| Opción                    | Ventajas                           | Desventajas                          | Decisión   |
|---------------------------|------------------------------------|--------------------------------------|------------|
| Almacenar en PostgreSQL   | Todo centralizado                  | Degrada rendimiento y backups        | Rechazada  |
| **Supabase Storage**      | Desacoplado y optimizado para archivos | Dependencia de servicio externo   | **Aceptada** |
| AWS S3                    | Muy robusto y escalable            | Mayor complejidad y costo            | Rechazada  |

---
## Consecuencias

### Positivas

- Se evita sobrecargar la base de datos con archivos pesados.
- El almacenamiento multimedia queda separado de la persistencia transaccional.
- Mejora la organización y escalabilidad del sistema.
- Facilita la recuperación y entrega de archivos.

### Negativas

- Depende de un servicio externo.
- Se requiere gestionar credenciales y acceso seguro.
- La disponibilidad del contenido multimedia queda ligada al proveedor.
- Cuota baja de almacenamiento para el plan gratuito.
