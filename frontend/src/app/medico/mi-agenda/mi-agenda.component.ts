import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { CitaDTO } from '../../core/citas/citas.models';
import { CitasService } from '../../core/citas/citas.service';
import { DoctorService } from '../../core/doctor/doctor.service';
import { HistorialService } from '../../core/historial/historial.service';

@Component({
  selector: 'app-mi-agenda',
  standalone: true,
  imports: [RouterLink, DatePipe],
  templateUrl: './mi-agenda.component.html',
  styles: ``
})
export class MiAgendaComponent implements OnInit {
  citas: CitaDTO[] = [];
  citasConAtencion = new Set<number>();
  cargando = true;
  error = '';

  constructor(
    private auth: AuthService,
    private doctorService: DoctorService,
    private citasService: CitasService,
    private historialService: HistorialService
  ) {}

  ngOnInit(): void {
    const usuarioId = this.auth.getUsuarioId();
    if (usuarioId === null) {
      this.error = 'No se pudo identificar al usuario.';
      this.cargando = false;
      return;
    }

    this.doctorService.obtenerPorUsuarioId(usuarioId).subscribe({
      next: (medico) => {
        this.citasService.listarPorMedico(medico.id).subscribe({
          next: (citas) => {
            this.citas = citas;
            this.cargando = false;
          },
          error: () => {
            this.error = 'No se pudieron cargar tus citas.';
            this.cargando = false;
          },
        });
      },
      error: () => {
        this.error = 'No se encontró un médico asociado a tu cuenta.';
        this.cargando = false;
      },
    });

    this.historialService.misAtenciones().subscribe({
      next: (atenciones) => {
        this.citasConAtencion = new Set(atenciones.map((a) => a.citaId));
      },
      error: () => {
        // no bloquea la agenda si esto falla; solo no se podra distinguir cual cita ya tiene atencion
      },
    });
  }

  tieneAtencionRegistrada(cita: CitaDTO): boolean {
    return this.citasConAtencion.has(cita.id);
  }

  marcarAsistencia(cita: CitaDTO, asistio: boolean): void {
    if (!asistio && !confirm('¿Confirmas que el paciente NO asistió a la cita?')) {
      return;
    }
    this.citasService.marcarAsistencia(cita.id, asistio).subscribe({
      next: (actualizada) => (cita.estado = actualizada.estado),
      error: () => (this.error = 'No se pudo actualizar la asistencia.'),
    });
  }
}
