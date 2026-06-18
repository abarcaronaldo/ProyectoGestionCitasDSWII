package com.gestioncitas.historial_medico_service.Controller;

import com.gestioncitas.historial_medico_service.Service.AtencionService;
import com.gestioncitas.historial_medico_service.dto.AtencionActualizarDTO;
import com.gestioncitas.historial_medico_service.dto.AtencionCrearDTO;
import com.gestioncitas.historial_medico_service.dto.AtencionDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atenciones")
public class AtencionController {

    private final AtencionService atencionService;

    public AtencionController(AtencionService atencionService) {
        this.atencionService = atencionService;
    }

    @PostMapping
    public ResponseEntity<AtencionDTO> crear(@Valid @RequestBody AtencionCrearDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(atencionService.crear(dto));
    }

    @GetMapping
    public List<AtencionDTO> listar() {
        return atencionService.listar();
    }

    @GetMapping("/mis-atenciones")
    public List<AtencionDTO> listarMisAtenciones(Authentication authentication) {
        return atencionService.listarMisAtenciones(usuarioId(authentication), rol(authentication));
    }

    @GetMapping("/{id}")
    public AtencionDTO obtener(@PathVariable Long id, Authentication authentication) {
        return atencionService.obtenerVerificandoPropiedad(id, usuarioId(authentication), rol(authentication));
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<AtencionDTO> listarPorPaciente(@PathVariable Long pacienteId, Authentication authentication) {
        return atencionService.listarPorPacienteVerificandoPropiedad(pacienteId, usuarioId(authentication), rol(authentication));
    }

    @GetMapping("/medico/{medicoId}")
    public List<AtencionDTO> listarPorMedico(@PathVariable Long medicoId, Authentication authentication) {
        return atencionService.listarPorMedicoVerificandoPropiedad(medicoId, usuarioId(authentication), rol(authentication));
    }

    @PutMapping("/{id}")
    public AtencionDTO actualizar(@PathVariable Long id, @Valid @RequestBody AtencionActualizarDTO dto,
                                   Authentication authentication) {
        return atencionService.actualizarVerificandoPropiedad(id, dto, usuarioId(authentication), rol(authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        atencionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Long usuarioId(Authentication authentication) {
        return Long.valueOf(authentication.getName());
    }

    private String rol(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.replace("ROLE_", ""))
                .orElse("");
    }
}
