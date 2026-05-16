package org.arquitectura.reelclipsv2.interacciones;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.interacciones.api.dto.InteraccionInfo;
import org.arquitectura.reelclipsv2.interacciones.internal.service.InteraccionService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InteraccionesFacade {

    private final InteraccionService service;

    public InteraccionInfo darLike(Long usuarioId, Long reelId) {
        return service.darLike(usuarioId, reelId);
    }

    public void quitarLike(Long usuarioId, Long reelId) {
        service.quitarLike(usuarioId, reelId);
    }

    public InteraccionInfo comentar(Long usuarioId, Long reelId, String contenido) {
        return service.comentar(usuarioId, reelId, contenido);
    }

    public void eliminarComentario(Long comentarioId, Long usuarioId) {
        service.eliminarComentario(comentarioId, usuarioId);
    }

    public List<InteraccionInfo> listarComentarios(Long reelId) {
        return service.listarComentarios(reelId);
    }
}
