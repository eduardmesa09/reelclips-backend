package org.arquitectura.reelclipsv2.usuarios.internal.service;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.shared.enums.EstadoCuenta;
import org.arquitectura.reelclipsv2.shared.exception.AccesoDenegadoException;
import org.arquitectura.reelclipsv2.shared.exception.RecursoNoEncontradoException;
import org.arquitectura.reelclipsv2.shared.exception.ReglaNegocioException;
import org.arquitectura.reelclipsv2.shared.storage.SupabaseStorageService;
import org.arquitectura.reelclipsv2.usuarios.api.dto.PerfilInfo;
import org.arquitectura.reelclipsv2.usuarios.api.dto.UsuarioInfo;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Canal;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Usuario;
import org.arquitectura.reelclipsv2.usuarios.internal.repository.ICanalRepository;
import org.arquitectura.reelclipsv2.usuarios.internal.repository.IUsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioService.class);

    private final IUsuarioRepository usuarioRepo;
    private final ICanalRepository canalRepo;
    private final SupabaseStorageService storageService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioInfo registrar(String username, String email, String password) {
        if (usuarioRepo.existsByEmail(email)) {
            throw new ReglaNegocioException("El email ya esta registrado: " + email);
        }
        if (usuarioRepo.existsByUsername(username)) {
            throw new ReglaNegocioException("El username ya esta en uso: " + username);
        }

        Usuario usuario = Usuario.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .nombreVisualizacion(username)
                .estadoCuenta(EstadoCuenta.ACTIVA)
                .fechaRegistro(LocalDateTime.now())
                .build();
        usuarioRepo.save(usuario);

        Canal canal = Canal.builder()
                .usuario(usuario)
                .fechaCreacion(LocalDateTime.now())
                .build();
        canalRepo.save(canal);

        return toInfo(usuario);
    }

    public UsuarioInfo iniciarSesion(String email, String password) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        if (!coincidePassword(usuario, password)) {
            throw new AccesoDenegadoException("Credenciales incorrectas");
        }
        if (usuario.getEstadoCuenta() == EstadoCuenta.DESACTIVADA) {
            throw new AccesoDenegadoException("La cuenta esta desactivada");
        }
        return toInfo(usuario);
    }

    public UsuarioInfo editarPerfil(Long id, String nombre, String foto, String descripcion) {
        Usuario usuario = buscar(id);
        usuario.setNombreVisualizacion(nombre);
        usuario.setFotoPerfil(foto);
        usuario.setDescripcion(descripcion);
        return toInfo(usuarioRepo.save(usuario));
    }

    public UsuarioInfo subirFotoPerfil(Long id, MultipartFile archivo) {
        Usuario usuario = buscar(id);
        String fotoAnterior = usuario.getFotoPerfil();
        String nuevaFotoUrl = storageService.subirImagenPerfil(archivo);

        try {
            usuario.setFotoPerfil(nuevaFotoUrl);
            Usuario usuarioActualizado = usuarioRepo.save(usuario);

            eliminarFotoAnteriorSilenciosamente(fotoAnterior, nuevaFotoUrl, id);
            return toInfo(usuarioActualizado);
        } catch (RuntimeException ex) {
            eliminarNuevaFotoSilenciosamente(nuevaFotoUrl, id);
            throw ex;
        }
    }

    public UsuarioInfo cambiarUsername(Long id, String nuevoUsername) {
        Usuario usuario = buscar(id);
        if (usuario.getUltimoCambioUsername() != null &&
                usuario.getUltimoCambioUsername().plusDays(30).isAfter(LocalDate.now())) {
            throw new ReglaNegocioException("Solo puedes cambiar el username una vez cada 30 dias");
        }
        if (usuarioRepo.existsByUsername(nuevoUsername)) {
            throw new ReglaNegocioException("El username ya esta en uso: " + nuevoUsername);
        }
        usuario.setUsername(nuevoUsername);
        usuario.setUltimoCambioUsername(LocalDate.now());
        return toInfo(usuarioRepo.save(usuario));
    }

    public PerfilInfo verPerfil(Long id) {
        Usuario usuario = buscar(id);
        return toPerfilInfo(usuario);
    }

    public List<PerfilInfo> listarPerfilesPublicos(Long usuarioId) {
        if (!estaActivo(usuarioId)) {
            throw new AccesoDenegadoException("Debes tener una cuenta activa para consultar perfiles publicos");
        }

        return usuarioRepo.findByEstadoCuentaAndIdNot(EstadoCuenta.ACTIVA, usuarioId)
                .stream()
                .map(this::toPerfilInfo)
                .toList();
    }

    public void desactivarCuenta(Long id) {
        Usuario usuario = buscar(id);
        usuario.setEstadoCuenta(EstadoCuenta.DESACTIVADA);
        usuario.setFechaDesactivacion(LocalDateTime.now());
        usuarioRepo.save(usuario);
    }

    public UsuarioInfo buscarPorId(Long id) {
        return toInfo(buscar(id));
    }

    public boolean existePorId(Long id) {
        return usuarioRepo.existsById(id);
    }

    public boolean estaActivo(Long id) {
        return usuarioRepo.findById(id)
                .map(usuario -> usuario.getEstadoCuenta() == EstadoCuenta.ACTIVA)
                .orElse(false);
    }

    public Long obtenerCanalId(Long usuarioId) {
        return canalRepo.findByUsuarioId(usuarioId)
                .map(Canal::getId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Canal no encontrado para el usuario: " + usuarioId));
    }

    private Usuario buscar(Long id) {
        return usuarioRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
    }

    private boolean coincidePassword(Usuario usuario, String password) {
        String passwordGuardado = usuario.getPasswordHash();

        if (passwordEncoder.matches(password, passwordGuardado)) {
            return true;
        }

        if (passwordGuardado.equals(password)) {
            usuario.setPasswordHash(passwordEncoder.encode(password));
            usuarioRepo.save(usuario);
            return true;
        }

        return false;
    }

    private void eliminarFotoAnteriorSilenciosamente(String fotoAnterior, String nuevaFotoUrl, Long usuarioId) {
        if (fotoAnterior == null || fotoAnterior.isBlank() || fotoAnterior.equals(nuevaFotoUrl)) {
            return;
        }

        try {
            storageService.eliminarImagenPerfil(fotoAnterior);
        } catch (RuntimeException ex) {
            LOGGER.warn("No se pudo eliminar la foto anterior del usuario {}", usuarioId, ex);
        }
    }

    private void eliminarNuevaFotoSilenciosamente(String nuevaFotoUrl, Long usuarioId) {
        try {
            storageService.eliminarImagenPerfil(nuevaFotoUrl);
        } catch (RuntimeException cleanupEx) {
            LOGGER.warn("No se pudo limpiar la nueva foto del usuario {} despues de un fallo", usuarioId, cleanupEx);
        }
    }

    private PerfilInfo toPerfilInfo(Usuario usuario) {
        return new PerfilInfo(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombreVisualizacion(),
                usuario.getFotoPerfil(),
                usuario.getDescripcion()
        );
    }

    private UsuarioInfo toInfo(Usuario usuario) {
        return new UsuarioInfo(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNombreVisualizacion(),
                usuario.getFotoPerfil(),
                usuario.getDescripcion(),
                usuario.getEstadoCuenta(),
                usuario.getFechaRegistro()
        );
    }
}
