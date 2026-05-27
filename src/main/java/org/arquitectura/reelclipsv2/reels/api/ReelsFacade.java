package org.arquitectura.reelclipsv2.reels.api;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.reels.api.dto.ReelInfo;
import org.arquitectura.reelclipsv2.reels.internal.proxy.ProxyReel;
import org.arquitectura.reelclipsv2.reels.internal.proxy.ServicioAlmacenamientoVideo;
import org.arquitectura.reelclipsv2.reels.internal.proxy.VideoStream;
import org.arquitectura.reelclipsv2.reels.internal.service.ReelService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReelsFacade implements IReelModuloApi {

    private final ReelService service;
    private final ProxyReel proxyReel;
    private final ServicioAlmacenamientoVideo servicioAlmacenamientoVideo;

    public ReelInfo publicar(Long usuarioId, MultipartFile video, String descripcion,
                             int duracionSegundos, double tamanoMB, List<Long> categoriaIds) {
        String urlVideo = servicioAlmacenamientoVideo.guardar(video);
        return service.publicar(usuarioId, urlVideo, descripcion, duracionSegundos, tamanoMB, categoriaIds);
    }

    public ReelInfo editar(Long reelId, Long usuarioId, String descripcion, List<Long> categoriaIds) {
        return service.editar(reelId, usuarioId, descripcion, categoriaIds);
    }

    public void eliminar(Long reelId, Long usuarioId) {
        service.eliminar(reelId, usuarioId);
    }

    public VideoStream obtenerStream(Long reelId, Long usuarioId) {
        return proxyReel.obtenerStream(reelId, usuarioId);
    }

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

    @Override
    public void actualizarMetricas(Long reelId, String tipoEvento) {
        service.actualizarMetricas(reelId, tipoEvento);
    }
}
