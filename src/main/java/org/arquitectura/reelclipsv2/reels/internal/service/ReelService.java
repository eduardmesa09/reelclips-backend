package org.arquitectura.reelclipsv2.reels.internal.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.arquitectura.reelclipsv2.categorias.api.ICategoriaModuloApi;
import org.arquitectura.reelclipsv2.categorias.internal.model.Categoria;
import org.arquitectura.reelclipsv2.reels.api.dto.ReelInfo;
import org.arquitectura.reelclipsv2.reels.internal.model.Reel;
import org.arquitectura.reelclipsv2.reels.internal.proxy.CacheVideo;
import org.arquitectura.reelclipsv2.reels.internal.repository.IReelRepository;
import org.arquitectura.reelclipsv2.shared.enums.EstadoReel;
import org.arquitectura.reelclipsv2.shared.exception.AccesoDenegadoException;
import org.arquitectura.reelclipsv2.shared.exception.RecursoNoEncontradoException;
import org.arquitectura.reelclipsv2.shared.exception.ReglaNegocioException;
import org.arquitectura.reelclipsv2.usuarios.api.IUsuarioModuloApi;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Canal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReelService {

    private final IReelRepository reelRepo;
    private final ICategoriaModuloApi categoriaModuloApi;
    private final IUsuarioModuloApi usuarioModuloApi;
    private final CacheVideo cache;
    private final EntityManager entityManager;

    public ReelInfo publicar(Long usuarioId, String urlVideo, String descripcion,
                             int duracionSegundos, double tamanoMB, List<Long> categoriaIds) {
        if (duracionSegundos > 90) {
            throw new ReglaNegocioException("El reel no puede superar los 90 segundos");
        }
        if (tamanoMB > 500) {
            throw new ReglaNegocioException("El archivo no puede superar los 500 MB");
        }
        if (categoriaIds == null || categoriaIds.isEmpty()) {
            throw new ReglaNegocioException("Debes asignar al menos una categoria al reel");
        }

        Long canalId = usuarioModuloApi.obtenerCanalId(usuarioId);
        Canal canal = entityManager.getReference(Canal.class, canalId);
        List<Categoria> categorias = obtenerCategoriasValidas(categoriaIds);

        Reel reel = Reel.builder()
                .urlVideo(urlVideo)
                .descripcion(descripcion)
                .duracionSegundos(duracionSegundos)
                .tamanoArchivoMB(tamanoMB)
                .estado(EstadoReel.ACTIVO)
                .fechaPublicacion(LocalDateTime.now())
                .canal(canal)
                .categorias(categorias)
                .build();

        return toInfo(reelRepo.save(reel));
    }

    public ReelInfo editar(Long reelId, Long usuarioId, String descripcion, List<Long> categoriaIds) {
        Reel reel = buscar(reelId);
        validarPropietario(reel, usuarioId);

        if (categoriaIds == null || categoriaIds.isEmpty()) {
            throw new ReglaNegocioException("Debes asignar al menos una categoria");
        }

        reel.setDescripcion(descripcion);
        reel.setCategorias(obtenerCategoriasValidas(categoriaIds));
        return toInfo(reelRepo.save(reel));
    }

    public void eliminar(Long reelId, Long usuarioId) {
        Reel reel = buscar(reelId);
        validarPropietario(reel, usuarioId);
        reel.setEstado(EstadoReel.ELIMINADO);
        reelRepo.save(reel);
        cache.invalidar(reelId);
    }

    public ReelInfo buscarPorId(Long id) {
        return toInfo(buscar(id));
    }

    public boolean existePorId(Long id) {
        return reelRepo.existsById(id);
    }

    public List<ReelInfo> listarPublicos() {
        return reelRepo.findByEstado(EstadoReel.ACTIVO)
                .stream().map(this::toInfo).toList();
    }

    public List<ReelInfo> listarPorCanal(Long canalId) {
        return reelRepo.findByCanalId(canalId)
                .stream().map(this::toInfo).toList();
    }

    public void actualizarMetricas(Long reelId, String tipoEvento) {
        Reel reel = buscar(reelId);

        switch (tipoEvento) {
            case "LIKE" -> reel.setContadorLikes(reel.getContadorLikes() + 1);
            case "UNLIKE" -> reel.setContadorLikes(Math.max(0, reel.getContadorLikes() - 1));
            case "COMENTARIO" -> reel.setContadorComentarios(reel.getContadorComentarios() + 1);
            case "DEL_COMENTARIO" -> reel.setContadorComentarios(Math.max(0, reel.getContadorComentarios() - 1));
            default -> throw new ReglaNegocioException("Tipo de evento no soportado para metricas: " + tipoEvento);
        }

        reelRepo.save(reel);
    }

    private void validarPropietario(Reel reel, Long usuarioId) {
        if (!reel.getCanal().getUsuario().getId().equals(usuarioId)) {
            throw new AccesoDenegadoException("No tienes permiso para modificar este reel");
        }
    }

    private Reel buscar(Long id) {
        return reelRepo.findDetalleById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reel no encontrado: " + id));
    }

    private List<Categoria> obtenerCategoriasValidas(List<Long> categoriaIds) {
        List<Long> idsUnicos = categoriaIds.stream().distinct().toList();

        if (idsUnicos.stream().anyMatch(id -> !categoriaModuloApi.existePorId(id))) {
            throw new ReglaNegocioException("Una o mas categorias indicadas no existen");
        }

        return idsUnicos.stream()
                .map(id -> entityManager.getReference(Categoria.class, id))
                .toList();
    }

    public ReelInfo toInfo(Reel r) {
        return new ReelInfo(
                r.getId(), r.getUrlVideo(), r.getUrlMiniatura(),
                r.getDescripcion(), r.getDuracionSegundos(), r.getTamanoArchivoMB(),
                r.getEstado(), r.getFechaPublicacion(),
                r.getContadorLikes(), r.getContadorComentarios(),
                r.getCanal().getId(),
                r.getCategorias().stream().map(Categoria::getNombre).toList()
        );
    }
}
