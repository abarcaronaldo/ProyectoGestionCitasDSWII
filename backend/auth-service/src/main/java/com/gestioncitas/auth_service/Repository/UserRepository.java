package com.gestioncitas.auth_service.Repository;


import com.gestioncitas.auth_service.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByRolNombre(String nombre);

    List<Usuario> findByActivoTrue();
}
