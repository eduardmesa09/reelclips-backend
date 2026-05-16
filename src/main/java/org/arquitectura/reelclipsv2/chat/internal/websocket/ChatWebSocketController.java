package org.arquitectura.reelclipsv2.chat.internal.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arquitectura.reelclipsv2.chat.ChatFacade;
import org.arquitectura.reelclipsv2.chat.api.dto.MensajeInfo;
import org.arquitectura.reelclipsv2.shared.exception.AccesoDenegadoException;
import org.arquitectura.reelclipsv2.shared.exception.RecursoNoEncontradoException;
import org.arquitectura.reelclipsv2.shared.exception.ReglaNegocioException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatFacade chatFacade;
    private final SimpMessagingTemplate messagingTemplate;

    // El cliente envía a: /app/chat.enviar
    @MessageMapping("/chat.enviar")
    public void enviarMensaje(@Payload MensajeEntranteWS entrada) {
        try {
            MensajeInfo guardado = chatFacade.enviarMensaje(
                    entrada.conversacionId(),
                    entrada.remitenteId(),
                    entrada.contenido(),
                    entrada.tipoContenido(),
                    entrada.reelReferidoId()
            );

            MensajeSalienteWS salida = new MensajeSalienteWS(
                    guardado.id(),
                    guardado.conversacionId(),
                    guardado.remitenteId(),
                    guardado.contenido(),
                    guardado.tipoContenido(),
                    guardado.reelReferidoId(),
                    guardado.fechaEnvio()
            );

            // Enviar a ambos participantes por su canal privado
            // El cliente se suscribe a: /user/queue/mensajes
            Long[] participantes = obtenerParticipantes(entrada.conversacionId());
            for (Long participanteId : participantes) {
                messagingTemplate.convertAndSendToUser(
                        participanteId.toString(),
                        "/queue/mensajes",
                        salida
                );
            }

        } catch (AccesoDenegadoException | RecursoNoEncontradoException | ReglaNegocioException e) {
            // Notificar error solo al remitente
            messagingTemplate.convertAndSendToUser(
                    entrada.remitenteId().toString(),
                    "/queue/errores",
                    e.getMessage()
            );
        }
    }

    // El cliente envía a: /app/chat.escribiendo
    // Notifica al otro participante que alguien está escribiendo
    @MessageMapping("/chat.escribiendo")
    public void notificarEscribiendo(@Payload EscribiendoWS escribiendo) {
        messagingTemplate.convertAndSendToUser(
                escribiendo.destinatarioId().toString(),
                "/queue/escribiendo",
                escribiendo
        );
    }

    private Long[] obtenerParticipantes(Long conversacionId) {
        return chatFacade.obtenerParticipantes(conversacionId);
    }
}
