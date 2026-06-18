package com.gestioncitas.paciente_service.Controller;

import com.gestioncitas.paciente_service.Service.PacienteService;
import com.gestioncitas.paciente_service.dto.PacienteActualizarDTO;
import com.gestioncitas.paciente_service.dto.PacienteCrearDTO;
import com.gestioncitas.paciente_service.dto.PacienteDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> crear(@Valid @RequestBody PacienteCrearDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.crear(dto));
    }

    @GetMapping
    public List<PacienteDTO> listar() {
        return pacienteService.listar();
    }

    @GetMapping("/{id}")
    public PacienteDTO obtener(@PathVariable Long id) {
        return pacienteService.obtener(id);
    }

    @PutMapping("/{id}")
    public PacienteDTO actualizar(@PathVariable Long id, @Valid @RequestBody PacienteActualizarDTO dto) {
        return pacienteService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
