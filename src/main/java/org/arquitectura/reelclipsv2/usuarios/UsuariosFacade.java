package org.arquitectura.reelclipsv2.usuarios;

import org.arquitectura.reelclipsv2.usuarios.api.IUsuarioModuloApi;
import org.arquitectura.reelclipsv2.usuarios.api.dto.UsuarioInfo;
import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.usuarios.internal.service.UsuarioService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuariosFacade implements IUsuarioModuloApi {

    private final UsuarioService service;

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
}
