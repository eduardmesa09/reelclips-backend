package org.arquitectura.reelclipsv2.feed.internal.service;

import org.arquitectura.reelclipsv2.reels.api.dto.ReelInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioFiltroVisibilidad {

    public List<ReelInfo> filtrar(List<ReelInfo> reels, Long canalIdPropio) {
        return reels.stream()
                .filter(reel -> !reel.canalId().equals(canalIdPropio))
                .toList();
    }
}
