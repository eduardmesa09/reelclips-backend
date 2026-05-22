package org.arquitectura.reelclipsv2.chat;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.chat.api.dto.ConversacionInfo;
import org.arquitectura.reelclipsv2.chat.api.dto.MensajeInfo;
import org.arquitectura.reelclipsv2.chat.internal.service.ChatService;
import org.arquitectura.reelclipsv2.shared.enums.TipoMensaje;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatFacade {

    private final ChatService service;

    public ConversacionInfo iniciarConversacion(Long usuarioId, Long destinatarioId) {
        return service.iniciarConversacion(usuarioId, destinatarioId);
    }

    public List<ConversacionInfo> obtenerConversaciones(Long usuarioId) {
        return service.obtenerConversaciones(usuarioId);
    }

    public MensajeInfo enviarMensaje(Long conversacionId, Long remitenteId,
                                     String contenido, TipoMensaje tipo, Long reelReferidoId) {
        return service.enviarMensaje(conversacionId, remitenteId, contenido, tipo, reelReferidoId);
    }

    public List<MensajeInfo> obtenerMensajes(Long conversacionId, Long usuarioId) {
        return service.obtenerMensajes(conversacionId, usuarioId);
    }

    public Long[] obtenerParticipantes(Long conversacionId) {
        return service.obtenerParticipantes(conversacionId);
    }
}
