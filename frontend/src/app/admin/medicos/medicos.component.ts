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
          this.nombres = '';
          this.apellidos = '';
          this.cmp = '';
          this.telefono = '';
          this.email = '';
          this.especialidadId = null;
          this.cargar();
        },
        error: () => (this.error = 'No se pudo crear el médico (¿CMP repetido?).'),
      });
  }

  eliminar(medico: MedicoDTO): void {
    this.doctorService.eliminarMedico(medico.id).subscribe({
      next: () => this.cargar(),
      error: () => (this.error = 'No se pudo eliminar el médico.'),
    });
  }
}
