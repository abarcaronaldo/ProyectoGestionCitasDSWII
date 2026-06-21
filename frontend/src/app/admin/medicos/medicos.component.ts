import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { EspecialidadDTO, MedicoDTO } from '../../core/doctor/doctor.models';
import { DoctorService } from '../../core/doctor/doctor.service';

@Component({
  selector: 'app-medicos',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './medicos.component.html',
  styles: ``
})
export class MedicosComponent implements OnInit {
  medicos: MedicoDTO[] = [];
  especialidades: EspecialidadDTO[] = [];
  cargando = true;
  error = '';

  nombres = '';
  apellidos = '';
  cmp = '';
  telefono = '';
  email = '';
  especialidadId: number | null = null;

  editandoId: number | null = null;

  constructor(private doctorService: DoctorService) {}

  ngOnInit(): void {
    this.cargar();
    this.doctorService.listarEspecialidades().subscribe({
      next: (especialidades) => (this.especialidades = especialidades),
      error: () => (this.error = 'No se pudo cargar la lista de especialidades.'),
    });
  }

  private cargar(): void {
    this.cargando = true;
    this.doctorService.listarMedicos().subscribe({
      next: (medicos) => {
        this.medicos = medicos;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo cargar la lista de médicos.';
        this.cargando = false;
      },
    });
  }

  // El formulario sirve para crear o, si hay un médico en edición, para actualizar.
  guardar(): void {
    if (this.editandoId === null) {
      this.crear();
    } else {
      this.actualizar();
    }
  }

  crear(): void {
    this.error = '';
    if (!this.nombres || !this.apellidos || !this.cmp || this.especialidadId === null) {
      this.error = 'Completa los campos obligatorios (nombres, apellidos, CMP, especialidad).';
      return;
    }
    this.doctorService
      .crearMedico({
        nombres: this.nombres,
        apellidos: this.apellidos,
        cmp: this.cmp,
        telefono: this.telefono,
        email: this.email,
        especialidadId: this.especialidadId,
      })
      .subscribe({
        next: () => {
          this.limpiarFormulario();
          this.cargar();
        },
        error: () => (this.error = 'No se pudo crear el médico (¿CMP repetido?).'),
      });
  }

  actualizar(): void {
    this.error = '';
    if (this.editandoId === null) {
      return;
    }
    if (!this.nombres || !this.apellidos || this.especialidadId === null) {
      this.error = 'Completa los campos obligatorios (nombres, apellidos, especialidad).';
      return;
    }
    this.doctorService
      .actualizarMedico(this.editandoId, {
        nombres: this.nombres,
        apellidos: this.apellidos,
        telefono: this.telefono,
        email: this.email,
        especialidadId: this.especialidadId,
      })
      .subscribe({
        next: () => {
          this.cancelarEdicion();
          this.cargar();
        },
        error: () => (this.error = 'No se pudo actualizar el médico.'),
      });
  }

  // Carga los datos del médico en el formulario para editarlo (el CMP no es editable).
  editar(medico: MedicoDTO): void {
    this.editandoId = medico.id;
    this.nombres = medico.nombres;
    this.apellidos = medico.apellidos;
    this.cmp = medico.cmp;
    this.telefono = medico.telefono ?? '';
    this.email = medico.email ?? '';
    this.especialidadId = medico.especialidadId;
    this.error = '';
  }

  cancelarEdicion(): void {
    this.editandoId = null;
    this.limpiarFormulario();
    this.error = '';
  }

  private limpiarFormulario(): void {
    this.nombres = '';
    this.apellidos = '';
    this.cmp = '';
    this.telefono = '';
    this.email = '';
    this.especialidadId = null;
  }

  eliminar(medico: MedicoDTO): void {
    if (!confirm(`¿Eliminar al médico ${medico.nombres} ${medico.apellidos}?`)) {
      return;
    }
    this.doctorService.eliminarMedico(medico.id).subscribe({
      next: () => this.cargar(),
      error: () => (this.error = 'No se pudo eliminar el médico.'),
    });
  }
}
