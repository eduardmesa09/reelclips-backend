package org.arquitectura.reelclipsv2.reels.api;

import org.arquitectura.reelclipsv2.reels.api.dto.ReelInfo;

import java.util.List;

public interface IReelModuloApi {
    ReelInfo buscarPorId(Long id);
    boolean existePorId(Long id);
    List<ReelInfo> listarPublicos();
    List<ReelInfo> listarPorCanal(Long canalId);
    void actualizarMetricas(Long reelId, String tipoEvento);
}
