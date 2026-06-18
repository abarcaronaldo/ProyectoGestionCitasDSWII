package com.gestioncitas.doctor_service.Controller;

import com.gestioncitas.doctor_service.Service.EspecialidadService;
import com.gestioncitas.doctor_service.dto.EspecialidadActualizarDTO;
import com.gestioncitas.doctor_service.dto.EspecialidadCrearDTO;
import com.gestioncitas.doctor_service.dto.EspecialidadDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @PostMapping
    public ResponseEntity<EspecialidadDTO> crear(@Valid @RequestBody EspecialidadCrearDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadService.crear(dto));
    }

    @GetMapping
    public List<EspecialidadDTO> listar() {
        return especialidadService.listar();
    }

    @GetMapping("/{id}")
    public EspecialidadDTO obtener(@PathVariable Long id) {
        return especialidadService.obtener(id);
    }

    @PutMapping("/{id}")
    public EspecialidadDTO actualizar(@PathVariable Long id, @Valid @RequestBody EspecialidadActualizarDTO dto) {
        return especialidadService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
