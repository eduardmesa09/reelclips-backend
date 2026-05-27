package org.arquitectura.reelclipsv2.usuarios.api;

import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.usuarios.api.dto.PerfilInfo;
import org.arquitectura.reelclipsv2.usuarios.api.dto.UsuarioInfo;
import org.arquitectura.reelclipsv2.usuarios.internal.service.UsuarioService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuariosFacade implements IUsuarioModuloApi {

    private final UsuarioService service;

    public UsuarioInfo registrar(String username, String email, String password) {
        return service.registrar(username, email, password);
    }

    public UsuarioInfo iniciarSesion(String email, String password) {
        return service.iniciarSesion(email, password);
    }

    public PerfilInfo verPerfil(Long id) {
        return service.verPerfil(id);
    }

    public List<PerfilInfo> listarPerfilesPublicos(Long usuarioId) {
        return service.listarPerfilesPublicos(usuarioId);
    }

    public UsuarioInfo editarPerfil(Long id, String nombre, String foto, String descripcion) {
        return service.editarPerfil(id, nombre, foto, descripcion);
    }

    public UsuarioInfo subirFotoPerfil(Long id, MultipartFile archivo) {
        return service.subirFotoPerfil(id, archivo);
    }

    public UsuarioInfo cambiarUsername(Long id, String nuevoUsername) {
        return service.cambiarUsername(id, nuevoUsername);
    }

    public void desactivarCuenta(Long id) {
        service.desactivarCuenta(id);
    }

    @Override
    public UsuarioInfo buscarPorId(Long id) {
        return service.buscarPorId(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return service.existePorId(id);
    }

    @Override
    public boolean estaActivo(Long id) {
        return service.estaActivo(id);
    }

    @Override
    public Long obtenerCanalId(Long usuarioId) {
        return service.obtenerCanalId(usuarioId);
    }
}
