package org.arquitectura.reelclipsv2.interacciones.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.arquitectura.reelclipsv2.interacciones.InteraccionesFacade;
import org.arquitectura.reelclipsv2.interacciones.api.dto.InteraccionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interacciones")
@RequiredArgsConstructor
@Tag(name = "Interacciones")
public class InteraccionController {

    private final InteraccionesFacade interaccionesFacade;

    @Operation(summary = "Dar like", description = "RF-12 / RN-13 — Un usuario solo puede dar un like por reel")
    @PostMapping("/like")
    public ResponseEntity<InteraccionInfo> darLike(
            @RequestParam Long usuarioId,
            @RequestParam Long reelId) {
        return ResponseEntity.ok(interaccionesFacade.darLike(usuarioId, reelId));
    }

    @Operation(summary = "Quitar like", description = "RF-13 — Elimina la reacción del usuario sobre el reel")
    @DeleteMapping("/like")
    public ResponseEntity<Void> quitarLike(
            @RequestParam Long usuarioId,
            @RequestParam Long reelId) {
        interaccionesFacade.quitarLike(usuarioId, reelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Comentar reel", description = "RF-14 — Agrega un comentario público al reel")
    @PostMapping("/comentario")
    public ResponseEntity<InteraccionInfo> comentar(
            @RequestParam Long usuarioId,
            @RequestParam Long reelId,
            @RequestParam String contenido) {
        return ResponseEntity.ok(interaccionesFacade.comentar(usuarioId, reelId, contenido));
    }

    @Operation(summary = "Eliminar comentario", description = "RF-15 — Solo el autor del comentario puede eliminarlo")
    @DeleteMapping("/comentario/{comentarioId}")
    public ResponseEntity<Void> eliminarComentario(
            @PathVariable Long comentarioId,
            @RequestParam Long usuarioId) {
        interaccionesFacade.eliminarComentario(comentarioId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar comentarios", description = "RF-14 — Retorna todos los comentarios de un reel")
    @GetMapping("/comentarios/{reelId}")
    public ResponseEntity<List<InteraccionInfo>> listarComentarios(@PathVariable Long reelId) {
        return ResponseEntity.ok(interaccionesFacade.listarComentarios(reelId));
    }
}
