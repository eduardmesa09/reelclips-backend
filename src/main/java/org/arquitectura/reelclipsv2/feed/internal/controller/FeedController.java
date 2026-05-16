package org.arquitectura.reelclipsv2.feed.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.arquitectura.reelclipsv2.feed.FeedFacade;
import org.arquitectura.reelclipsv2.feed.api.dto.FeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
@Tag(name = "Feed")
public class FeedController {

    private final FeedFacade feedFacade;

    @Operation(
            summary = "Obtener feed",
            description = "RF-20 / RF-21 / RF-22 — Retorna reels públicos paginados. " +
                    "Excluye reels propios (RN-12). Filtra por categorías si se indican (RF-21). " +
                    "Soporta scroll infinito mediante paginación (RN-14)."
    )
    @GetMapping
    public ResponseEntity<FeedResponse> obtenerFeed(
            @Parameter(description = "ID del usuario autenticado") @RequestParam Long usuarioId,
            @Parameter(description = "Lista de nombres de categorías para filtrar (opcional)") @RequestParam(required = false) List<String> categorias,
            @Parameter(description = "Número de página (empieza en 0)") @RequestParam(defaultValue = "0") int pagina) {
        return ResponseEntity.ok(feedFacade.obtenerFeed(usuarioId, categorias, pagina));
    }
}
