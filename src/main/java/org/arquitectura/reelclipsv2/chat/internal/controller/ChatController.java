package org.arquitectura.reelclipsv2.chat.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.chat.ChatFacade;
import org.arquitectura.reelclipsv2.chat.api.dto.ConversacionInfo;
import org.arquitectura.reelclipsv2.chat.api.dto.MensajeInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Chat")
public class ChatController {

    private final ChatFacade chatFacade;

    @Operation(summary = "Listar conversaciones", description = "Retorna todas las conversaciones en las que participa un usuario")
    @GetMapping("/conversaciones")
    public ResponseEntity<List<ConversacionInfo>> listarConversaciones(@RequestParam Long usuarioId) {
        return ResponseEntity.ok(chatFacade.obtenerConversaciones(usuarioId));
    }

    @Operation(summary = "Iniciar conversación", description = "RF-16 / RN-17 - Crea o reutiliza una conversación entre dos usuarios")
    @PostMapping("/conversacion")
    public ResponseEntity<ConversacionInfo> iniciar(
            @RequestParam Long usuarioId,
            @RequestParam Long destinatarioId) {
        return ResponseEntity.ok(chatFacade.iniciarConversacion(usuarioId, destinatarioId));
    }

    @Operation(summary = "Historial de mensajes", description = "RF-17 / RN-18 - Retorna todos los mensajes de una conversación en orden cronológico")
    @GetMapping("/conversacion/{conversacionId}/mensajes")
    public ResponseEntity<List<MensajeInfo>> historial(
            @PathVariable Long conversacionId,
            @RequestParam Long usuarioId) {
        return ResponseEntity.ok(chatFacade.obtenerMensajes(conversacionId, usuarioId));
    }
}
