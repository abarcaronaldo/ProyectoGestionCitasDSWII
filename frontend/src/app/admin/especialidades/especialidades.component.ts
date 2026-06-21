import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { EspecialidadDTO } from '../../core/doctor/doctor.models';
import { DoctorService } from '../../core/doctor/doctor.service';

@Component({
  selector: 'app-especialidades',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './especialidades.component.html',
  styles: ``
})
export class EspecialidadesComponent implements OnInit {
  especialidades: EspecialidadDTO[] = [];
  cargando = true;
  error = '';

  nombre = '';
  descripcion = '';

  constructor(private doctorService: DoctorService) {}

  ngOnInit(): void {
    this.cargar();
  }

  private cargar(): void {
    this.cargando = true;
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

  crear(): void {
    this.error = '';
    if (!this.nombre) {
      this.error = 'El nombre es obligatorio.';
      return;
    }
    this.doctorService.crearEspecialidad({ nombre: this.nombre, descripcion: this.descripcion }).subscribe({
      next: () => {
        this.nombre = '';
        this.descripcion = '';
        this.cargar();
      },
      error: () => (this.error = 'No se pudo crear la especialidad (¿nombre repetido?).'),
    });
  }

  eliminar(especialidad: EspecialidadDTO): void {
    if (!confirm(`¿Eliminar la especialidad "${especialidad.nombre}"?`)) {
      return;
    }
    this.doctorService.eliminarEspecialidad(especialidad.id).subscribe({
      next: () => this.cargar(),
      error: () => (this.error = 'No se pudo eliminar la especialidad.'),
    });
  }
}
