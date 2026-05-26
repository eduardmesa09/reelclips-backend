package org.arquitectura.reelclipsv2.feed.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.feed.api.FeedFacade;
import org.arquitectura.reelclipsv2.feed.api.dto.FeedResponse;
import org.arquitectura.reelclipsv2.shared.exception.ReglaNegocioException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
@Tag(name = "Feed")
public class FeedController {

    private final FeedFacade feedFacade;

    @Operation(
            summary = "Obtener feed",
            description = "RF-20 / RF-21 / RF-22 - Retorna reels publicos paginados. " +
                    "Excluye reels propios (RN-12). Filtra por categorias si se indican (RF-21). " +
                    "Soporta scroll infinito mediante paginacion (RN-14)."
    )
    @GetMapping
    public ResponseEntity<FeedResponse> obtenerFeed(
            @Parameter(description = "ID del usuario autenticado") @RequestParam Long usuarioId,
            @Parameter(description = "Lista de nombres de categorias para filtrar (opcional)")
            @RequestParam(required = false) List<String> categorias,
            @Parameter(description = "Numero de pagina (empieza en 0)")
            @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Semilla de sesion para mantener estable el orden del feed durante una apertura de la app")
            @RequestParam(required = false) Long seed) {
        if (pagina < 0) {
            throw new ReglaNegocioException("El numero de pagina no puede ser negativo");
        }
        return ResponseEntity.ok(feedFacade.obtenerFeed(usuarioId, categorias, pagina, seed));
    }
}
