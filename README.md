# ReelClips - Plataforma de videos cortos e interacciones

Proyecto desarrollado para la asignatura **Diseño y Arquitectura de Software** de la **Universidad de La Sabana - Facultad de Ingeniería**.

ReelClips es una plataforma enfocada en el consumo, creación y compartición de videos cortos. El backend implementa gestión de usuarios, publicación de reels, categorías, feed paginado, interacciones sociales, chat en tiempo real y almacenamiento de archivos en Supabase Storage.

---

## Equipo de trabajo

| Integrante | Rol |
|---|---|
| Nicolas Joel Cáceres Parra | Desarrollo Frontend |
| Jorge Luis Alarcón Isturiz | Desarrollo Backend |
| Daniel Felipe Esquinas Suarez | Desarrollo Backend |
| Eduard Meza Salazar | Desarrollo Backend |
| Juan José Campos Covaleda | Desarrollo Backend |

**Docente:** Wilmer Fabian Triana Pulgarín  
**Curso:** Diseño y Arquitectura de Software  
**Semestre:** 2026-1

---

## Objetivo del proyecto

Desarrollar un sistema de información que permita a los usuarios consumir, crear y compartir videos cortos, con el fin de ofrecer una plataforma enfocada en la libre expresión digital, la interacción social y el descubrimiento de contenido dinámico, accesible y centrado en la experiencia del usuario.

---

## Alcance implementado en este repositorio

Este repositorio contiene el **backend** de ReelClips. Incluye:

- API REST para usuarios, reels, categorías, interacciones, feed y chat.
- WebSocket/STOMP para mensajería en tiempo real.
- Persistencia con PostgreSQL y Spring Data JPA.
- Integración con Supabase Storage para videos e imágenes de perfil.
- Documentación OpenAPI/Swagger.
- Docker Compose para entorno local.
- Validación de convenciones arquitectónicas con ArchUnit.

No incluye el frontend.

---

## Requerimientos funcionales

### Gestión de usuarios

| ID | Requerimiento |
|---|---|
| RF-01 | El sistema debe permitir a un usuario registrarse proporcionando nombre de usuario, correo y contraseña. |
| RF-02 | El sistema debe permitir iniciar sesión mediante credenciales. |
| RF-03 | El usuario debe poder cerrar sesión en cualquier momento. |
| RF-04 | El usuario debe poder editar su perfil, foto y descripción. |
| RF-05 | El sistema debe permitir visualizar perfiles de usuarios. |
| RF-06 | El usuario debe poder desactivar su cuenta. |

### Gestión de reels

| ID | Requerimiento |
|---|---|
| RF-07 | El sistema debe permitir publicar reels con descripción y categoría. |
| RF-08 | El usuario debe poder editar descripción y categorías de sus reels. |
| RF-09 | El usuario debe poder eliminar sus reels. |
| RF-10 | El sistema debe permitir visualizar reels publicados. |

### Interacciones sociales

| ID | Requerimiento |
|---|---|
| RF-12 | El sistema debe permitir reaccionar con likes a un reel. |
| RF-13 | El usuario debe poder eliminar una reacción previamente realizada. |
| RF-14 | El sistema debe permitir comentar reels. |
| RF-15 | El usuario debe poder eliminar sus comentarios. |

### Sistema de chat

| ID | Requerimiento |
|---|---|
| RF-16 | El sistema debe permitir iniciar conversaciones entre usuarios. |
| RF-17 | El sistema debe permitir enviar mensajes de texto entre usuarios. |

### Gestión de categorías

| ID | Requerimiento |
|---|---|
| RF-18 | El sistema debe obligar a asignar al menos una categoría al publicar un reel. |
| RF-19 | El sistema debe permitir administrar las categorías disponibles. |

### Feed de contenido

| ID | Requerimiento |
|---|---|
| RF-20 | El sistema debe mostrar un feed de reels públicos. |
| RF-21 | El usuario debe poder filtrar contenido por categorías. |
| RF-22 | El sistema debe cargar contenido continuamente mediante scroll infinito. |

---

## Reglas de negocio

### Cuentas de usuario

| ID | Regla |
|---|---|
| RN-01 | Cada usuario debe tener un identificador único dentro de la plataforma. |
| RN-02 | Solo usuarios autenticados pueden publicar, reaccionar, comentar o iniciar chats. |
| RN-03 | Cada cuenta crea automáticamente un canal personal único. |
| RN-04 | El nombre de usuario solo puede modificarse una vez cada 30 días. |
| RN-05 | Una cuenta desactivada conserva reels y mensajes durante 30 días antes de eliminarse permanentemente. |

### Publicación de reels

| ID | Regla |
|---|---|
| RN-06 | Los reels deben tener máximo 90 segundos y 500 MB de tamaño. |
| RN-07 | Todo reel debe tener al menos una categoría asignada. |
| RN-08 | Solo el propietario del reel puede editarlo o eliminarlo. |
| RN-09 | Todo reel publicado es público por defecto. |

### Feed de contenido

| ID | Regla |
|---|---|
| RN-10 | El feed principal muestra contenido de toda la comunidad. |
| RN-11 | El sistema organiza y filtra contenido según categorías e historial de interacción. |
| RN-12 | Los reels propios no aparecen en el feed de descubrimiento del usuario. |
| RN-13 | Cada usuario puede dejar únicamente una reacción por reel. |
| RN-14 | El feed debe precargar contenido para soportar scroll infinito. |

### Chat directo

| ID | Regla |
|---|---|
| RN-15 | Solo usuarios registrados pueden participar en chats. |
| RN-16 | Las conversaciones son exclusivamente uno a uno. |
| RN-17 | Cualquier usuario autenticado puede iniciar conversaciones. |
| RN-18 | Los mensajes permanecen almacenados persistentemente. |
| RN-19 | Los mensajes pueden contener texto y referencias a reels, pero no archivos multimedia directos. |

---

## Arquitectura

El sistema adopta una arquitectura de **Monolito Modular**. Cada módulo expone contratos públicos en paquetes `api/` y encapsula la implementación en paquetes `internal/`.

### Módulos principales

| Módulo | Responsabilidad |
|---|---|
| `usuarios` | Registro, autenticación, perfiles, canal personal y foto de perfil |
| `reels` | Publicación, edición, eliminación, consulta y streaming de reels |
| `categorias` | Administración y filtrado de categorías |
| `feed` | Orquestación del feed, visibilidad y paginación |
| `interacciones` | Likes, comentarios y publicación de eventos de interacción |
| `chat` | Conversaciones, mensajes persistentes y WebSocket/STOMP |
| `shared` | Configuración, enums, excepciones y servicios comunes |

### Convenciones internas

- `api/`: facades, DTOs e interfaces compartidas entre módulos.
- `internal/controller/`: endpoints HTTP.
- `internal/service/`: lógica de aplicación.
- `internal/repository/`: acceso a datos con Spring Data JPA.
- `internal/model/`: entidades JPA.
- `internal/proxy/`, `internal/observer/`, `internal/websocket/`: implementaciones de patrones específicos.

---

## Patrones de diseño aplicados

| Patrón | Categoría | Ubicación | Propósito |
|---|---|---|---|
| Facade | Estructural | `categorias/api/`, `usuarios/api/`, `reels/api/`, `feed/api/`, `chat/api/`, `interacciones/api/` | Expone una API estable por módulo y desacopla controllers de servicios internos |
| Proxy | Estructural | `reels/internal/proxy/ProxyReel.java` | Controla autorización, caché y acceso al stream de video |
| Observer | Comportamiento | `interacciones/internal/observer/` | Distribuye eventos de interacción a observadores desacoplados |

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Spring Boot 4.0.6 + Java 17 |
| API REST | Spring Web MVC |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | PostgreSQL |
| Cliente HTTP interno | Spring WebFlux `WebClient` |
| Validación | Spring Validation |
| Seguridad de contraseñas | `spring-security-crypto` |
| WebSocket | STOMP sobre SockJS y WebSocket nativo |
| Documentación API | Springdoc OpenAPI + Swagger UI |
| Almacenamiento | Supabase Storage |
| Configuración local | `spring-dotenv` + `.env` |
| Pruebas | JUnit 5, Spring Boot Test, ArchUnit, H2 |
| Build | Maven Wrapper |
| Contenedores locales | Docker Compose |

---

## Funcionalidades implementadas

### Usuarios

- Registro con creación automática de canal.
- Inicio de sesión por email y contraseña.
- Consulta de perfil público.
- Edición de perfil.
- Cambio de username con restricción de 30 días.
- Subida y reemplazo de foto de perfil en Supabase Storage.
- Desactivación de cuenta.
- Listado de perfiles públicos excluyendo al usuario solicitante.

### Reels

- Publicación de reels con validación de duración, tamaño y categorías.
- Edición de descripción y categorías.
- Eliminación lógica cambiando estado a `ELIMINADO`.
- Consulta por id.
- Listado público.
- Listado por canal.
- Streaming mediante `ProxyReel`.

### Categorías

- CRUD de categorías.
- Búsqueda por id.
- Filtrado por nombres.

### Feed

- Obtención de feed paginado.
- Exclusión de reels del propio usuario.
- Filtrado opcional por categorías.
- Orden estable por semilla o por ventana horaria.

### Interacciones

- Dar like.
- Quitar like.
- Comentar reels.
- Eliminar comentarios propios.
- Listar comentarios por reel.
- Publicación de eventos para actualizar métricas y registrar actividad.

### Chat

- Iniciar conversación uno a uno.
- Listar conversaciones de un usuario.
- Obtener historial de mensajes.
- Enviar mensajes por HTTP a través de facade/servicio.
- Enviar mensajes en tiempo real por WebSocket/STOMP.
- Notificación de “escribiendo”.
- Soporte de mensajes con referencia a reel.

---

## Estructura del proyecto

```text
src/main/java/org/arquitectura/reelclipsv2/
├── ReelclipsV2Application.java
├── categorias/
│   ├── api/
│   │   ├── CategoriasFacade.java
│   │   ├── ICategoriaModuloApi.java
│   │   └── dto/
│   └── internal/
│       ├── controller/
│       ├── model/
│       ├── repository/
│       └── service/
├── chat/
│   ├── api/
│   │   ├── ChatFacade.java
│   │   └── dto/
│   └── internal/
│       ├── config/
│       ├── controller/
│       ├── model/
│       ├── repository/
│       ├── service/
│       └── websocket/
├── feed/
│   ├── api/
│   │   ├── FeedFacade.java
│   │   └── dto/
│   └── internal/
│       ├── controller/
│       └── service/
├── interacciones/
│   ├── api/
│   │   ├── InteraccionesFacade.java
│   │   └── dto/
│   └── internal/
│       ├── controller/
│       ├── model/
│       ├── observer/
│       ├── repository/
│       └── service/
├── reels/
│   ├── api/
│   │   ├── IReelModuloApi.java
│   │   ├── ReelsFacade.java
│   │   └── dto/
│   └── internal/
│       ├── controller/
│       ├── model/
│       ├── proxy/
│       ├── repository/
│       └── service/
├── shared/
│   ├── config/
│   ├── enums/
│   ├── exception/
│   └── storage/
└── usuarios/
    ├── api/
    │   ├── IUsuarioModuloApi.java
    │   ├── UsuariosFacade.java
    │   └── dto/
    └── internal/
        ├── controller/
        ├── model/
        ├── repository/
        └── service/
```

---

## Endpoints REST

| Módulo | Método | Endpoint | RF |
|---|---|---|---|
| Usuarios | POST | `/api/usuarios/registro` | RF-01 |
| Usuarios | POST | `/api/usuarios/login` | RF-02 |
| Usuarios | GET | `/api/usuarios/{id}/perfil` | RF-05 |
| Usuarios | GET | `/api/usuarios/perfiles-publicos` | RF-05 |
| Usuarios | PUT | `/api/usuarios/{id}/perfil` | RF-04 |
| Usuarios | POST | `/api/usuarios/{id}/foto` | RF-04 |
| Usuarios | PATCH | `/api/usuarios/{id}/username` | RF-04 |
| Usuarios | DELETE | `/api/usuarios/{id}` | RF-06 |
| Reels | POST | `/api/reels` | RF-07 |
| Reels | GET | `/api/reels` | RF-10 |
| Reels | GET | `/api/reels/{id}` | RF-10 |
| Reels | PUT | `/api/reels/{reelId}` | RF-08 |
| Reels | DELETE | `/api/reels/{reelId}` | RF-09 |
| Reels | GET | `/api/reels/{reelId}/stream` | RF-10 |
| Reels | GET | `/api/reels/canal/{canalId}` | RF-10 |
| Categorías | GET | `/api/categorias` | RF-19 |
| Categorías | GET | `/api/categorias/{id}` | RF-19 |
| Categorías | POST | `/api/categorias` | RF-19 |
| Categorías | PUT | `/api/categorias/{id}` | RF-19 |
| Categorías | DELETE | `/api/categorias/{id}` | RF-19 |
| Categorías | GET | `/api/categorias/filtrar` | RF-21 |
| Interacciones | POST | `/api/interacciones/like` | RF-12 |
| Interacciones | DELETE | `/api/interacciones/like` | RF-13 |
| Interacciones | POST | `/api/interacciones/comentario` | RF-14 |
| Interacciones | DELETE | `/api/interacciones/comentario/{comentarioId}` | RF-15 |
| Interacciones | GET | `/api/interacciones/comentarios/{reelId}` | RF-14 |
| Feed | GET | `/api/feed` | RF-20, RF-21, RF-22 |
| Chat | GET | `/api/chat/conversaciones` | RF-16 |
| Chat | POST | `/api/chat/conversacion` | RF-16 |
| Chat | GET | `/api/chat/conversacion/{conversacionId}/mensajes` | RF-17 |

Documentación interactiva:

```text
http://localhost:8082/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8082/v3/api-docs
```

---

## WebSocket

| Canal | Dirección | Propósito |
|---|---|---|
| `/ws-chat` | Conexión | Endpoint STOMP usando SockJS |
| `/ws-chat-native` | Conexión | Endpoint STOMP sin SockJS |
| `/app/chat.enviar` | Cliente -> Servidor | Enviar un mensaje |
| `/app/chat.escribiendo` | Cliente -> Servidor | Notificar que un usuario escribe |
| `/user/queue/mensajes` | Servidor -> Cliente | Recibir mensajes nuevos |
| `/user/queue/escribiendo` | Servidor -> Cliente | Recibir estado de escritura |
| `/user/queue/errores` | Servidor -> Cliente | Recibir errores del chat |

El usuario se identifica durante el handshake con el parámetro `usuarioId`:

```text
/ws-chat?usuarioId=1
/ws-chat-native?usuarioId=1
```

---

## Configuración

La aplicación puede leer configuración desde variables de entorno y desde un archivo `.env` local gracias a:

- `spring.config.import=optional:file:.env[.properties]`
- la dependencia `spring-dotenv`

Archivo de referencia:

```text
.env.example
```

En PowerShell:

```powershell
Copy-Item .env.example .env
```

Variables disponibles:

| Variable | Uso |
|---|---|
| `DB_URL` | URL JDBC de PostgreSQL |
| `DB_USERNAME` | Usuario de PostgreSQL |
| `DB_PASSWORD` | Contraseña de PostgreSQL |
| `SUPABASE_URL` | URL del proyecto de Supabase |
| `SUPABASE_KEY` | Llave de acceso a Supabase Storage |
| `SUPABASE_BUCKET_REELS` | Bucket para videos |
| `SUPABASE_BUCKET_IMAGENES` | Bucket para imágenes de perfil |
| `APP_PORT` | Puerto público para Docker Compose y fallback local |
| `PORT` | Puerto inyectado por plataformas como Render |

Ejemplo:

```properties
DB_URL=jdbc:postgresql://postgres:5432/reelclips_db
DB_USERNAME=postgres
DB_PASSWORD=reelclips123

SUPABASE_URL=https://tu-proyecto.supabase.co
SUPABASE_KEY=tu_supabase_key
SUPABASE_BUCKET_REELS=reels
SUPABASE_BUCKET_IMAGENES=imagenes-perfil

APP_PORT=8082
```

Fragmento relevante de `application.properties`:

```properties
spring.config.import=optional:file:.env[.properties]
server.port=${PORT:${APP_PORT:8082}}

spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

supabase.url=${SUPABASE_URL}
supabase.key=${SUPABASE_KEY}
supabase.bucket.reels=${SUPABASE_BUCKET_REELS}
supabase.bucket.imagenes=${SUPABASE_BUCKET_IMAGENES}
```

Notas:

- `.env` no debe subirse al repositorio.
- En Docker Compose, `DB_URL` debe apuntar a `postgres`.
- Si ejecutas Maven fuera de Docker, `DB_URL` debe apuntar a `localhost`.

---

## Ejecución del proyecto

### Requisitos previos

- Java 17
- Docker Desktop o PostgreSQL 14+
- Cuenta y buckets de Supabase Storage

### Levantar con Docker Compose

1. Crear `.env` a partir de `.env.example`.
2. Completar variables obligatorias.
3. Ejecutar:

```bash
docker compose up -d --build
```

Servicios levantados:

- `reelclips_app`: backend Spring Boot en `http://localhost:8082`
- `reelclips_db`: PostgreSQL en `localhost:5432`

Comandos útiles:

```bash
docker compose logs -f app
docker compose down
```

El volumen `reelclips_data` conserva la información de PostgreSQL entre reinicios.

### Ejecutar sin Docker

Ejemplo en PowerShell:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/reelclips_db"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="reelclips123"
$env:SUPABASE_URL="https://tu-proyecto.supabase.co"
$env:SUPABASE_KEY="tu_supabase_key"
$env:SUPABASE_BUCKET_REELS="reels"
$env:SUPABASE_BUCKET_IMAGENES="imagenes-perfil"
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Aplicación disponible en:

```text
http://localhost:8082
```

---

## Pruebas

Ejecutar:

```bash
./mvnw test
```

Cobertura actual incluida en el repositorio:

- prueba de carga de contexto de Spring Boot
- prueba de convenciones arquitectónicas con ArchUnit para evitar que `controller` y `websocket` dependan directamente de `service`

---

## Base de datos

Si no usas Docker:

```sql
CREATE DATABASE reelclips_db;
```

Hibernate está configurado con:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Las tablas se crean o actualizan automáticamente al iniciar la aplicación.

---

## Despliegue en Render

Render no usa el archivo `.env` del repositorio. Las variables se configuran desde el dashboard del servicio.

### 1. Crear la base de datos

1. Crear un servicio PostgreSQL en Render.
2. Obtener host, puerto, base de datos, usuario y contraseña.

Formato de `DB_URL`:

```properties
DB_URL=jdbc:postgresql://HOST:PORT/DATABASE
```

### 2. Crear el Web Service

1. Crear un Web Service desde el repositorio.
2. Seleccionar runtime **Docker**.
3. Configurar:

```properties
DB_URL=jdbc:postgresql://HOST:PORT/DATABASE
DB_USERNAME=usuario_render
DB_PASSWORD=password_render

SUPABASE_URL=https://tu-proyecto.supabase.co
SUPABASE_KEY=tu_supabase_key
SUPABASE_BUCKET_REELS=reels
SUPABASE_BUCKET_IMAGENES=imagenes-perfil
```

No configures `PORT` manualmente. Render lo inyecta automáticamente y la app lo resuelve con:

```properties
server.port=${PORT:${APP_PORT:8082}}
```

### 3. Verificar despliegue

```text
https://TU-SERVICIO.onrender.com/swagger-ui.html
https://TU-SERVICIO.onrender.com/v3/api-docs
```

### Notas

- No subas `.env`.
- Para producción real conviene reemplazar `ddl-auto=update` por migraciones con Flyway o Liquibase.
- Si el frontend consume este backend desde otro dominio, revisa CORS.

---

## Estado del proyecto

El **backend de ReelClips en este repositorio se encuentra funcionalmente completo para el alcance definido del proyecto académico**.

Estado actual:

- [x] Objetivo del sistema documentado
- [x] Requerimientos funcionales y reglas de negocio documentados
- [x] Arquitectura de monolito modular implementada
- [x] Módulo de usuarios completo
- [x] Módulo de reels completo
- [x] Módulo de categorías completo
- [x] Módulo de feed completo
- [x] Módulo de interacciones completo
- [x] Módulo de chat completo
- [x] Chat en tiempo real con WebSocket/STOMP
- [x] Integración con Supabase Storage para videos e imágenes
- [x] Documentación Swagger/OpenAPI
- [x] Docker Compose para backend + PostgreSQL
- [x] Configuración con `.env` y variables de entorno
- [x] Pruebas base de contexto
- [x] Validación de convenciones arquitectónicas con ArchUnit
- [x] Estructura del proyecto alineada con facades por módulo y paquetes `api/internal`

Fuera del alcance de este repositorio:

- frontend integrado en el mismo repositorio
