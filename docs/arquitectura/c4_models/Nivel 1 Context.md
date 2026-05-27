# C4 Nivel 1 - Context Diagram
 
Este diagrama muestra cómo los distintos tipos de usuarios interactúan con ReelClips y cómo el sistema se comunica con servicios externos como PostgreSQL y Supabase Storage.
 
```mermaid
flowchart LR
    %% =========================
    %% C4 Nivel 1 - CONTEXT
    %% ReelClips
    %% =========================
 
    classDef person fill:#dceeff,stroke:#0f4c81,stroke-width:1px,color:#0f2d4d;
    classDef system fill:#d9ecff,stroke:#1f6fb2,stroke-width:2px,color:#0f2d4d;
    classDef external fill:#f3f3f3,stroke:#7a7a7a,stroke-width:1px,color:#333;
    classDef note fill:#fff8d6,stroke:#c9a227,stroke-width:1px,color:#5a4700;
 
    U1[Usuario no autenticado<br/><i>Persona</i><br/>Visualiza contenido público]:::person
    U2[Usuario autenticado<br/><i>Persona</i><br/>Consume, publica e interactúa]:::person
    ADM[Administrador<br/><i>Persona</i><br/>Gestiona categorías y moderación]:::person
 
    RC[ReelClips<br/><i>Sistema de software</i><br/>Plataforma de videos cortos e interacciones]:::system
 
    SUPA[Almacenamiento contenido - Supabase<br/><i>Sistema externo</i><br/>Guarda videos e imágenes]:::external
    DB[(Base de Datos - PostgreSQL<br/><i>Sistema externo</i><br/>Datos relacionales del dominio)]:::external
    CACHÉ[(Caché<br/><i>Sistema externo</i><br/>Caché de contenido frecuente)]:::external
 
    U1 -->|Visualiza| RC
    U2 -->|Consume| RC
    ADM -->|Gestiona| RC
 
    RC -->|Guarda| SUPA
    RC -->|Almacena| DB
    RC -->|Consulta| CACHÉ
 
    note1["Límite del sistema:<br/>ReelClips incluye frontend + backend.<br/>Usuarios y servicios externos están fuera."]:::note
    RC -.-> note1
```
 
---
 
## Descripción
 
### Personas
 
| Actor | Descripción |
|---|---|
| Usuario no autenticado | Puede visualizar contenido público dentro de la plataforma |
| Usuario autenticado | Puede consumir contenido, publicar reels e interactuar |
| Administrador | Gestiona categorías, moderación y administración del sistema |
 
### Sistema Principal
 
| Sistema | Descripción |
|---|---|
| ReelClips | Plataforma de videos cortos con funcionalidades sociales, feed, chat y gestión de contenido |
 
### Sistemas Externos
 
| Sistema | Responsabilidad |
|---|---|
| PostgreSQL | Persistencia de datos relacionales |
| Caché | Caché para optimizar consultas frecuentes |
| Supabase Storage | Almacenamiento multimedia de videos e imágenes |
