package org.arquitectura.reelclipsv2.chat.internal.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.chat.api.dto.ConversacionInfo;
import org.arquitectura.reelclipsv2.chat.api.dto.MensajeInfo;
import org.arquitectura.reelclipsv2.chat.internal.model.Conversacion;
import org.arquitectura.reelclipsv2.chat.internal.model.Mensaje;
import org.arquitectura.reelclipsv2.chat.internal.repository.IConversacionRepository;
import org.arquitectura.reelclipsv2.chat.internal.repository.IMensajeRepository;
import org.arquitectura.reelclipsv2.shared.enums.TipoMensaje;
import org.arquitectura.reelclipsv2.shared.exception.AccesoDenegadoException;
import org.arquitectura.reelclipsv2.shared.exception.RecursoNoEncontradoException;
import org.arquitectura.reelclipsv2.shared.exception.ReglaNegocioException;
import org.arquitectura.reelclipsv2.usuarios.api.IUsuarioModuloApi;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final IConversacionRepository conversacionRepo;
    private final IMensajeRepository mensajeRepo;
    private final IUsuarioModuloApi usuarioModuloApi;
    private final EntityManager entityManager;

    public Long[] obtenerParticipantes(Long conversacionId) {
        Conversacion conversacion = conversacionRepo.findById(conversacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Conversacion no encontrada"));
        return new Long[]{
                conversacion.getUsuario1().getId(),
                conversacion.getUsuario2().getId()
        };
    }

    public ConversacionInfo iniciarConversacion(Long usuarioId, Long destinatarioId) {
        if (!usuarioModuloApi.estaActivo(usuarioId)) {
            throw new AccesoDenegadoException("Debes tener una cuenta activa para iniciar un chat");
        }
        if (!usuarioModuloApi.existePorId(destinatarioId)) {
            throw new RecursoNoEncontradoException("El destinatario no existe");
        }
        if (usuarioId.equals(destinatarioId)) {
            throw new ReglaNegocioException("No puedes iniciar una conversacion contigo mismo");
        }

        return conversacionRepo.findEntreUsuarios(usuarioId, destinatarioId)
                .map(this::toInfo)
                .orElseGet(() -> {
                    Usuario u1 = entityManager.getReference(Usuario.class, usuarioId);
                    Usuario u2 = entityManager.getReference(Usuario.class, destinatarioId);

                    Conversacion conversacion = Conversacion.builder()
                            .usuario1(u1)
                            .usuario2(u2)
                            .fechaInicio(LocalDateTime.now())
                            .build();
                    return toInfo(conversacionRepo.save(conversacion));
                });
    }

    public List<ConversacionInfo> obtenerConversaciones(Long usuarioId) {
        if (!usuarioModuloApi.estaActivo(usuarioId)) {
            throw new AccesoDenegadoException("Debes tener una cuenta activa para consultar tus conversaciones");
        }

        return conversacionRepo.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toInfo)
                .toList();
    }

    public MensajeInfo enviarMensaje(Long conversacionId, Long remitenteId,
                                     String contenido, TipoMensaje tipo, Long reelReferidoId) {
        TipoMensaje tipoMensaje = tipo != null ? tipo : TipoMensaje.TEXTO;

        if (!usuarioModuloApi.estaActivo(remitenteId)) {
            throw new AccesoDenegadoException("Debes tener una cuenta activa para enviar mensajes");
        }

        Conversacion conversacion = conversacionRepo.findById(conversacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Conversacion no encontrada"));

        boolean esParticipante =
                conversacion.getUsuario1().getId().equals(remitenteId) ||
                        conversacion.getUsuario2().getId().equals(remitenteId);
        if (!esParticipante) {
            throw new AccesoDenegadoException("No eres participante de esta conversacion");
        }

        if (contenido == null || contenido.isBlank()) {
            throw new ReglaNegocioException("El mensaje no puede estar vacio");
        }

        if (tipoMensaje == TipoMensaje.ENLACE_REEL && reelReferidoId == null) {
            throw new ReglaNegocioException("Debes indicar el reel al que hace referencia el mensaje");
        }

        Usuario remitente = entityManager.getReference(Usuario.class, remitenteId);

        Mensaje mensaje = Mensaje.builder()
                .conversacion(conversacion)
                .remitente(remitente)
                .contenido(contenido)
                .tipoContenido(tipoMensaje)
                .reelReferidoId(reelReferidoId)
                .fechaEnvio(LocalDateTime.now())
                .build();

        return toMensajeInfo(mensajeRepo.save(mensaje));
    }

    public List<MensajeInfo> obtenerMensajes(Long conversacionId, Long usuarioId) {
        Conversacion conversacion = conversacionRepo.findById(conversacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Conversacion no encontrada"));

        boolean esParticipante =
                conversacion.getUsuario1().getId().equals(usuarioId) ||
                        conversacion.getUsuario2().getId().equals(usuarioId);
        if (!esParticipante) {
            throw new AccesoDenegadoException("No eres participante de esta conversacion");
        }

        return mensajeRepo.findByConversacionIdOrderByFechaEnvioAsc(conversacionId)
                .stream()
                .map(this::toMensajeInfo)
                .toList();
    }

    private ConversacionInfo toInfo(Conversacion conversacion) {
        return new ConversacionInfo(
                conversacion.getId(),
                conversacion.getUsuario1().getId(),
                conversacion.getUsuario2().getId(),
                conversacion.getFechaInicio()
        );
    }

    private MensajeInfo toMensajeInfo(Mensaje mensaje) {
        return new MensajeInfo(
                mensaje.getId(),
                mensaje.getConversacion().getId(),
                mensaje.getRemitente().getId(),
                mensaje.getContenido(),
                mensaje.getTipoContenido(),
                mensaje.getReelReferidoId(),
                mensaje.getFechaEnvio()
        );
    }
}
