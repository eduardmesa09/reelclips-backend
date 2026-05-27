package org.arquitectura.reelclipsv2.chat.internal.repository;

import org.arquitectura.reelclipsv2.chat.internal.model.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IConversacionRepository extends JpaRepository<Conversacion, Long> {

    @Query("SELECT c FROM Conversacion c WHERE " +
            "(c.usuario1.id = :u1 AND c.usuario2.id = :u2) OR " +
            "(c.usuario1.id = :u2 AND c.usuario2.id = :u1)")
    Optional<Conversacion> findEntreUsuarios(@Param("u1") Long u1, @Param("u2") Long u2);

    @Query("SELECT c FROM Conversacion c WHERE " +
            "c.usuario1.id = :usuarioId OR c.usuario2.id = :usuarioId " +
            "ORDER BY c.fechaInicio DESC")
    List<Conversacion> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
