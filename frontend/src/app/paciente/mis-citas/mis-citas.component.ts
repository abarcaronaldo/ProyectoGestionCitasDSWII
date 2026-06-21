import { DatePipe, NgClass } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { CitaDTO } from '../../core/citas/citas.models';
import { CitasService } from '../../core/citas/citas.service';
import { PacienteService } from '../../core/paciente/paciente.service';

@Component({
  selector: 'app-mis-citas',
  standalone: true,
  imports: [RouterLink, DatePipe, NgClass],
  templateUrl: './mis-citas.component.html',
  styles: ``
})
export class MisCitasComponent implements OnInit {
  citas: CitaDTO[] = [];
  cargando = true;
  error = '';

  constructor(
    private auth: AuthService,
    private pacienteService: PacienteService,
    private citasService: CitasService
  ) {}

  ngOnInit(): void {
    const usuarioId = this.auth.getUsuarioId();
    if (usuarioId === null) {
      this.error = 'No se pudo identificar al usuario.';
      this.cargando = false;
      return;
    }

    this.pacienteService.obtenerPorUsuarioId(usuarioId).subscribe({
      next: (paciente) => {
        this.citasService.listarPorPaciente(paciente.id).subscribe({
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
        this.error = 'No se encontró un paciente asociado a tu cuenta.';
        this.cargando = false;
      },
    });
  }

  cancelar(cita: CitaDTO): void {
    if (!confirm('¿Seguro que deseas cancelar esta cita?')) {
      return;
    }
    this.citasService.cancelar(cita.id).subscribe({
      next: (actualizada) => {
        cita.estado = actualizada.estado;
      },
      error: () => (this.error = 'No se pudo cancelar la cita.'),
    });
  }
}
