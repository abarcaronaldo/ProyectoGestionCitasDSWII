import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { CitasService } from '../../core/citas/citas.service';
import { DoctorService } from '../../core/doctor/doctor.service';
import { EspecialidadDTO, MedicoDTO } from '../../core/doctor/doctor.models';
import { PacienteService } from '../../core/paciente/paciente.service';

@Component({
  selector: 'app-reservar-cita',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './reservar-cita.component.html',
  styles: ``
})
export class ReservarCitaComponent implements OnInit {
  medicos: MedicoDTO[] = [];
  especialidades: EspecialidadDTO[] = [];

  medicoId: number | null = null;
  especialidadId: number | null = null;
  fecha = '';
  motivo = '';

  cargando = true;
  error = '';
  exito = false;

  private pacienteId: number | null = null;

  // Solo los médicos de la especialidad elegida (vacío mientras no se elija una).
  get medicosFiltrados(): MedicoDTO[] {
    if (this.especialidadId === null) {
      return [];
    }
    return this.medicos.filter((m) => m.especialidadId === this.especialidadId);
  }

  // Al cambiar de especialidad se limpia el médico para no dejar uno de otra especialidad.
  onEspecialidadCambio(): void {
    this.medicoId = null;
  }

  constructor(
    private auth: AuthService,
    private pacienteService: PacienteService,
    private doctorService: DoctorService,
    private citasService: CitasService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const usuarioId = this.auth.getUsuarioId();
    if (usuarioId === null) {
      this.error = 'No se pudo identificar al usuario. Vuelve a iniciar sesión.';
      this.cargando = false;
      return;
    }

    this.pacienteService.obtenerPorUsuarioId(usuarioId).subscribe({
      next: (paciente) => (this.pacienteId = paciente.id),
      error: () => (this.error = 'No se encontró un paciente asociado a tu cuenta.'),
    });

    this.doctorService.listarMedicos().subscribe({
      next: (medicos) => (this.medicos = medicos),
      error: () => (this.error = 'No se pudo cargar la lista de médicos.'),
    });

    this.doctorService.listarEspecialidades().subscribe({
      next: (especialidades) => {
        this.especialidades = especialidades;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo cargar la lista de especialidades.';
        this.cargando = false;
      },
    });
  }

  enviar(): void {
    this.error = '';
    if (this.pacienteId === null || this.medicoId === null || this.especialidadId === null || !this.fecha) {
      this.error = 'Completa todos los campos.';
      return;
    }

    const fechaHora = this.fecha.length === 16 ? `${this.fecha}:00` : this.fecha;

    this.citasService
      .reservar({
        pacienteId: this.pacienteId,
        medicoId: this.medicoId,
        especialidadId: this.especialidadId,
        fechaHora,
        motivo: this.motivo,
      })
      .subscribe({
        next: () => {
          this.exito = true;
          setTimeout(() => this.router.navigateByUrl('/paciente/mis-citas'), 1200);
        },
        error: () => (this.error = 'No se pudo reservar la cita. Verifica los datos (médico/horario disponible).'),
      });
  }
}
