package com.gestioncitas.citas_service.Controller;

import com.gestioncitas.citas_service.Service.CitaService;
import com.gestioncitas.citas_service.dto.AsistenciaDTO;
import com.gestioncitas.citas_service.dto.CitaDTO;
import com.gestioncitas.citas_service.dto.ReprogramarCitaDTO;
import com.gestioncitas.citas_service.dto.ReservaCitaDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public ResponseEntity<CitaDTO> reservar(@Valid @RequestBody ReservaCitaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.reservar(dto));
    }

    @GetMapping("/{id}")
    public CitaDTO obtener(@PathVariable Long id) {
        return citaService.obtener(id);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<CitaDTO> listarPorPaciente(@PathVariable Long pacienteId, Authentication authentication) {
        return citaService.listarPorPacienteVerificandoPropiedad(pacienteId, usuarioId(authentication), rol(authentication));
    }

    @GetMapping("/medico/{medicoId}")
    public List<CitaDTO> listarPorMedico(@PathVariable Long medicoId, Authentication authentication) {
        return citaService.listarPorMedicoVerificandoPropiedad(medicoId, usuarioId(authentication), rol(authentication));
    }

    @PatchMapping("/{id}/cancelar")
    public CitaDTO cancelar(@PathVariable Long id, Authentication authentication) {
        return citaService.cancelarVerificandoPropiedad(id, usuarioId(authentication), rol(authentication));
    }

    @PatchMapping("/{id}/reprogramar")
    public CitaDTO reprogramar(@PathVariable Long id, @Valid @RequestBody ReprogramarCitaDTO dto) {
        return citaService.reprogramar(id, dto);
    }

    @PatchMapping("/{id}/asistencia")
    public CitaDTO marcarAsistencia(@PathVariable Long id, @Valid @RequestBody AsistenciaDTO dto) {
        return citaService.marcarAsistencia(id, dto.asistio());
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
