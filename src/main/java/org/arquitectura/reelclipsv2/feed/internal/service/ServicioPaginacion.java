package org.arquitectura.reelclipsv2.feed.internal.service;

import org.arquitectura.reelclipsv2.feed.api.dto.FeedResponse;
import org.arquitectura.reelclipsv2.reels.api.dto.ReelInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioPaginacion {

    private static final int TAMANO_PAGINA = 10;

    public FeedResponse paginar(List<ReelInfo> reels, int pagina) {
        int total = reels.size();
        int totalPaginas = (int) Math.ceil((double) total / TAMANO_PAGINA);
        int desde = pagina * TAMANO_PAGINA;
        int hasta = Math.min(desde + TAMANO_PAGINA, total);

        List<ReelInfo> resultado = (desde >= total)
                ? List.of()
                : reels.subList(desde, hasta);

        return new FeedResponse(
                resultado,
                pagina,
                totalPaginas,
                total,
                pagina < totalPaginas - 1
        );
    }
}
