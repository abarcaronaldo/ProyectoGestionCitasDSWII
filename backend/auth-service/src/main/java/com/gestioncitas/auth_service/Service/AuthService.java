package com.gestioncitas.auth_service.Service;


import com.gestioncitas.auth_service.Entity.Rol;
import com.gestioncitas.auth_service.Entity.Usuario;
import com.gestioncitas.auth_service.Exception.ApiExceptions;
import com.gestioncitas.auth_service.Repository.RolRepository;
import com.gestioncitas.auth_service.Repository.UserRepository;
import com.gestioncitas.auth_service.Security.JwtService;
import com.gestioncitas.auth_service.dto.LoginDTO;
import com.gestioncitas.auth_service.dto.RegistroDTO;
import com.gestioncitas.auth_service.dto.TokenDTO;
import com.gestioncitas.auth_service.dto.UsuarioDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository usuarioRepository, RolRepository rolRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public UsuarioDTO registrar(RegistroDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new ApiExceptions.ReglaNegocio("El email ya está registrado");
        }
        Rol rol = rolRepository.findByNombre(dto.rol().toUpperCase())
                .orElseThrow(() -> new ApiExceptions.ReglaNegocio("Rol no válido: " + dto.rol()));

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombres());
        usuario.setApellido(dto.apellidos());
        usuario.setEmail(dto.email());
        usuario.setPasswordHash(passwordEncoder.encode(dto.password()));
        usuario.setTelefono(dto.telefono());
        usuario.setRol(rol);
        usuario.setActivo(true);

        return UsuarioDTO.from(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public TokenDTO autenticar(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(ApiExceptions.CredencialesInvalidas::new);

        if (!usuario.isActivo() || !passwordEncoder.matches(dto.password(), usuario.getPasswordHash())) {
            throw new ApiExceptions.CredencialesInvalidas();
        }

        String token = jwtService.generarToken(usuario);
        return TokenDTO.bearer(token, usuario.getId(), usuario.getNombre(),
                usuario.getEmail(), usuario.getRol().getNombre());
    }
}
