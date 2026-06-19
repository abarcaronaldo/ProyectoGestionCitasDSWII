import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { EspecialidadDTO, MedicoDTO } from '../../core/doctor/doctor.models';
import { DoctorService } from '../../core/doctor/doctor.service';
import { ResumenEspecialidadDTO, ResumenGeneralDTO, ResumenMedicoDTO } from '../../core/reporte/reporte.models';
import { ReporteService } from '../../core/reporte/reporte.service';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './reportes.component.html',
  styles: ``,
})
export class ReportesComponent implements OnInit {
  resumenGeneral: ResumenGeneralDTO | null = null;
  error = '';

  medicos: MedicoDTO[] = [];
  especialidades: EspecialidadDTO[] = [];

  medicoId: number | null = null;
  especialidadId: number | null = null;

  resumenMedico: ResumenMedicoDTO | null = null;
  resumenEspecialidad: ResumenEspecialidadDTO | null = null;

  constructor(
    private reporteService: ReporteService,
    private doctorService: DoctorService
  ) {}

  ngOnInit(): void {
    this.reporteService.resumenGeneral().subscribe({
      next: (resumen) => (this.resumenGeneral = resumen),
      error: () => (this.error = 'No se pudo cargar el resumen general.'),
    });

    this.doctorService.listarMedicos().subscribe({
      next: (medicos) => (this.medicos = medicos),
      error: () => (this.error = 'No se pudo cargar la lista de médicos.'),
    });

    this.doctorService.listarEspecialidades().subscribe({
      next: (especialidades) => (this.especialidades = especialidades),
      error: () => (this.error = 'No se pudo cargar la lista de especialidades.'),
    });
  }

  consultarPorMedico(): void {
    if (this.medicoId === null) {
      return;
    }
    this.reporteService.resumenPorMedico(this.medicoId).subscribe({
      next: (resumen) => (this.resumenMedico = resumen),
      error: () => (this.error = 'No se pudo cargar el reporte del médico.'),
    });
  }

  consultarPorEspecialidad(): void {
    if (this.especialidadId === null) {
      return;
    }
    this.reporteService.resumenPorEspecialidad(this.especialidadId).subscribe({
      next: (resumen) => (this.resumenEspecialidad = resumen),
      error: () => (this.error = 'No se pudo cargar el reporte de la especialidad.'),
    });
  }
}
