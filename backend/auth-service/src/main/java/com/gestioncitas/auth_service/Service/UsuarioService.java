package com.gestioncitas.auth_service.Service;

import com.gestioncitas.auth_service.Entity.Usuario;
import com.gestioncitas.auth_service.Exception.ApiExceptions;
import com.gestioncitas.auth_service.Repository.UserRepository;
import com.gestioncitas.auth_service.dto.UsuarioActualizarDTO;
import com.gestioncitas.auth_service.dto.UsuarioDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UserRepository usuarioRepository;

    public UsuarioService(UserRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findByActivoTrue().stream().map(UsuarioDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtener(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioDTO::from)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Usuario no encontrado: " + id));
    }

    @Transactional
    public UsuarioDTO actualizar(Long id, UsuarioActualizarDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Usuario no encontrado: " + id));
        usuario.setNombre(dto.nombres());
        usuario.setApellido(dto.apellidos());
        usuario.setTelefono(dto.telefono());
        return UsuarioDTO.from(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions.RecursoNoEncontrado("Usuario no encontrado: " + id));
        // baja lógica: se desactiva en lugar de borrar para preservar trazabilidad
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
}
