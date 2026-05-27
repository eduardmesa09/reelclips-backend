package org.arquitectura.reelclipsv2.feed.api;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.feed.api.dto.FeedResponse;
import org.arquitectura.reelclipsv2.feed.internal.service.ServicioFiltroVisibilidad;
import org.arquitectura.reelclipsv2.feed.internal.service.ServicioPaginacion;
import org.arquitectura.reelclipsv2.reels.api.IReelModuloApi;
import org.arquitectura.reelclipsv2.reels.api.dto.ReelInfo;
import org.arquitectura.reelclipsv2.usuarios.api.IUsuarioModuloApi;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class FeedFacade {

    private final IReelModuloApi reelModuloApi;
    private final IUsuarioModuloApi usuarioModuloApi;
    private final ServicioFiltroVisibilidad filtroVisibilidad;
    private final ServicioPaginacion paginacion;

    public FeedResponse obtenerFeed(Long usuarioId, List<String> categorias, int pagina, Long seed) {
        List<ReelInfo> reels = reelModuloApi.listarPublicos();
        Long canalIdPropio = usuarioModuloApi.obtenerCanalId(usuarioId);

        reels = filtroVisibilidad.filtrar(reels, canalIdPropio);

        if (categorias != null && !categorias.isEmpty()) {
            reels = reels.stream()
                    .filter(reel -> reel.categorias().stream().anyMatch(categorias::contains))
                    .toList();
        }

        reels = new ArrayList<>(reels);
        Collections.shuffle(reels, new Random(calcularSemilla(usuarioId, categorias, seed)));

        return paginacion.paginar(reels, pagina);
    }

    private long calcularSemilla(Long usuarioId, List<String> categorias, Long seed) {
        if (seed != null) {
            return Objects.hash(usuarioId, categoriasNormalizadas(categorias), seed);
        }

        long ventanaHoraUtc = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() / 3600;
        return Objects.hash(usuarioId, categoriasNormalizadas(categorias), ventanaHoraUtc);
    }

    private List<String> categoriasNormalizadas(List<String> categorias) {
        return categorias == null
                ? List.of()
                : categorias.stream().sorted().toList();
    }
}
