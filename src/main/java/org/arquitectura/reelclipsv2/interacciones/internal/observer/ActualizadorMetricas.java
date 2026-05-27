package org.arquitectura.reelclipsv2.interacciones.internal.observer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arquitectura.reelclipsv2.interacciones.internal.model.EventoInteraccion;
import org.arquitectura.reelclipsv2.reels.api.IReelModuloApi;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActualizadorMetricas implements IObservador {

    private final IReelModuloApi reelModuloApi;

    @Override
    public void actualizar(EventoInteraccion evento) {
        reelModuloApi.actualizarMetricas(evento.getReelId(), evento.getTipoEvento());
        log.info("[ActualizadorMetricas] Metricas actualizadas para reel {}", evento.getReelId());
    }
}
