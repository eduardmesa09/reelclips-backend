package org.arquitectura.reelclipsv2.usuarios.api;

import org.arquitectura.reelclipsv2.usuarios.api.dto.UsuarioInfo;

public interface IUsuarioModuloApi {
    UsuarioInfo buscarPorId(Long id);
    boolean existePorId(Long id);
    boolean estaActivo(Long id);
    Long obtenerCanalId(Long usuarioId);
}
