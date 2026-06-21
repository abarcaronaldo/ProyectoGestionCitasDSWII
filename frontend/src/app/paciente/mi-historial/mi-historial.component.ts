import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AtencionDTO } from '../../core/historial/historial.models';
import { HistorialService } from '../../core/historial/historial.service';

@Component({
  selector: 'app-mi-historial',
  standalone: true,
  imports: [RouterLink, DatePipe],
  templateUrl: './mi-historial.component.html',
  styles: ``
})
export class MiHistorialComponent implements OnInit {
  atenciones: AtencionDTO[] = [];
  cargando = true;
  error = '';

  constructor(private historialService: HistorialService) {}

  ngOnInit(): void {
    this.historialService.misAtenciones().subscribe({
      next: (atenciones) => {
        this.atenciones = atenciones;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo cargar tu historial.';
        this.cargando = false;
      },
    });
  }
}
