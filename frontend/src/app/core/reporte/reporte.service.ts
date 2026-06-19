import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ResumenEspecialidadDTO, ResumenGeneralDTO, ResumenMedicoDTO } from './reporte.models';

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private http = inject(HttpClient);

  resumenGeneral(): Observable<ResumenGeneralDTO> {
    return this.http.get<ResumenGeneralDTO>(`${environment.apiUrl}/api/reportes/resumen`);
  }

  resumenPorMedico(medicoId: number): Observable<ResumenMedicoDTO> {
    return this.http.get<ResumenMedicoDTO>(`${environment.apiUrl}/api/reportes/medico/${medicoId}`);
  }

  resumenPorEspecialidad(especialidadId: number): Observable<ResumenEspecialidadDTO> {
    return this.http.get<ResumenEspecialidadDTO>(
      `${environment.apiUrl}/api/reportes/especialidad/${especialidadId}`
    );
  }
}
