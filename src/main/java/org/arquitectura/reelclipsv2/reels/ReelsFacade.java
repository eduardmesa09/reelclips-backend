package org.arquitectura.reelclipsv2.reels;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.reels.api.IReelModuloApi;
import org.arquitectura.reelclipsv2.reels.api.dto.ReelInfo;
import org.arquitectura.reelclipsv2.reels.internal.service.ReelService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReelsFacade implements IReelModuloApi {

    private final ReelService service;

    @Override
    public ReelInfo buscarPorId(Long id) {
        return service.buscarPorId(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return service.existePorId(id);
    }

    @Override
    public List<ReelInfo> listarPublicos() {
        return service.listarPublicos();
    }

    @Override
    public List<ReelInfo> listarPorCanal(Long canalId) {
        return service.listarPorCanal(canalId);
    }
}
