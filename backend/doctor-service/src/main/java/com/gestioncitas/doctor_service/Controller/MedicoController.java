package com.gestioncitas.doctor_service.Controller;

import com.gestioncitas.doctor_service.Service.MedicoService;
import com.gestioncitas.doctor_service.dto.MedicoActualizarDTO;
import com.gestioncitas.doctor_service.dto.MedicoCrearDTO;
import com.gestioncitas.doctor_service.dto.MedicoDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public ResponseEntity<MedicoDTO> crear(@Valid @RequestBody MedicoCrearDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.crear(dto));
    }

    @GetMapping
    public List<MedicoDTO> listar() {
        return medicoService.listar();
    }

    @GetMapping("/{id}")
    public MedicoDTO obtener(@PathVariable Long id) {
        return medicoService.obtener(id);
    }

    @GetMapping("/especialidad/{especialidadId}")
    public List<MedicoDTO> listarPorEspecialidad(@PathVariable Long especialidadId) {
        return medicoService.listarPorEspecialidad(especialidadId);
    }

    @GetMapping("/usuario/{usuarioId}")
    public MedicoDTO obtenerPorUsuarioId(@PathVariable Long usuarioId) {
        return medicoService.obtenerPorUsuarioId(usuarioId);
    }

    @PutMapping("/{id}")
    public MedicoDTO actualizar(@PathVariable Long id, @Valid @RequestBody MedicoActualizarDTO dto) {
        return medicoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
