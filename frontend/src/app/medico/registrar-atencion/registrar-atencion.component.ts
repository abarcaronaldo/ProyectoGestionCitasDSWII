import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HistorialService } from '../../core/historial/historial.service';

@Component({
  selector: 'app-registrar-atencion',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './registrar-atencion.component.html',
  styles: ``
})
export class RegistrarAtencionComponent implements OnInit {
  citaId!: number;
  motivoConsulta = '';
  diagnostico = '';
  tratamiento = '';
  observaciones = '';

  error = '';
  exito = false;
  enviando = false;

  constructor(
    private route: ActivatedRoute,
    private historialService: HistorialService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.citaId = Number(this.route.snapshot.paramMap.get('citaId'));
  }

  enviar(): void {
    this.error = '';
    this.enviando = true;
    this.historialService
      .crear({
        citaId: this.citaId,
        motivoConsulta: this.motivoConsulta,
        diagnostico: this.diagnostico,
        tratamiento: this.tratamiento,
        observaciones: this.observaciones,
      })
      .subscribe({
        next: () => {
          this.exito = true;
          setTimeout(() => this.router.navigateByUrl('/medico/mi-agenda'), 1200);
        },
        error: () => {
          this.error = 'No se pudo registrar la atención.';
          this.enviando = false;
        },
      });
  }
}
