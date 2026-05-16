package org.arquitectura.reelclipsv2.feed;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.categorias.internal.repository.ICategoriaRepository;
import org.arquitectura.reelclipsv2.feed.api.dto.FeedResponse;
import org.arquitectura.reelclipsv2.feed.internal.service.ServicioFiltroVisibilidad;
import org.arquitectura.reelclipsv2.feed.internal.service.ServicioPaginacion;
import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.reels.internal.repository.IReelRepository;
import org.arquitectura.reelclipsv2.reels.internal.service.ReelService;
import org.arquitectura.reelclipsv2.shared.enums.EstadoReel;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedFacade {

    private final IReelRepository reelRepo;
    private final ICategoriaRepository categoriaRepo;
    private final ServicioFiltroVisibilidad filtroVisibilidad;
    private final ServicioPaginacion paginacion;
    private final ReelService reelService;

    // RF-20, RF-21, RF-22
    public FeedResponse obtenerFeed(Long usuarioId, List<String> categorias, int pagina) {
        // 1. Obtener todos los reels activos
        List<Reel> reels = reelRepo.findByEstado(EstadoReel.ACTIVO);

        // 2. Filtrar visibilidad y excluir propios (RN-10, RN-12)
        reels = filtroVisibilidad.filtrar(reels, usuarioId);

        // 3. Filtrar por categorías si se indicaron (RF-21)
        if (categorias != null && !categorias.isEmpty()) {
            reels = reels.stream()
                    .filter(r -> r.getCategorias().stream()
                            .anyMatch(c -> categorias.contains(c.getNombre())))
                    .toList();
        }

        // 4. Paginar (RF-22, RN-14)
        return paginacion.paginar(reels, pagina, reelService);
    }
}
