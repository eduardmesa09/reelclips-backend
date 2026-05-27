package org.arquitectura.reelclipsv2.usuarios.internal.repository;

import org.arquitectura.reelclipsv2.shared.enums.EstadoCuenta;
import org.arquitectura.reelclipsv2.usuarios.internal.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    @Query("SELECT u FROM Usuario u WHERE u.estadoCuenta = :estadoCuenta AND u.id <> :id")
    List<Usuario> findByEstadoCuentaAndIdNot(@Param("estadoCuenta") EstadoCuenta estadoCuenta, @Param("id") Long id);
}
