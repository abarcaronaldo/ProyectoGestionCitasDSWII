package com.gestioncitas.reporte_service.Controller;

import com.gestioncitas.reporte_service.Service.ReporteService;
import com.gestioncitas.reporte_service.dto.ResumenEspecialidadDTO;
import com.gestioncitas.reporte_service.dto.ResumenGeneralDTO;
import com.gestioncitas.reporte_service.dto.ResumenMedicoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/resumen")
    public ResumenGeneralDTO resumenGeneral() {
        return reporteService.resumenGeneral();
    }

    @GetMapping("/medico/{medicoId}")
    public ResumenMedicoDTO resumenPorMedico(@PathVariable Long medicoId) {
        return reporteService.resumenPorMedico(medicoId);
    }

    @GetMapping("/especialidad/{especialidadId}")
    public ResumenEspecialidadDTO resumenPorEspecialidad(@PathVariable Long especialidadId) {
        return reporteService.resumenPorEspecialidad(especialidadId);
    }
}
