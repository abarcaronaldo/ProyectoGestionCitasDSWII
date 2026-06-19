import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CitaDTO, ReprogramarCitaRequest, ReservaCitaRequest } from './citas.models';

@Injectable({ providedIn: 'root' })
export class CitasService {
  private http = inject(HttpClient);

  reservar(datos: ReservaCitaRequest): Observable<CitaDTO> {
    return this.http.post<CitaDTO>(`${environment.apiUrl}/api/citas`, datos);
  }

  listarPorPaciente(pacienteId: number): Observable<CitaDTO[]> {
    return this.http.get<CitaDTO[]>(`${environment.apiUrl}/api/citas/paciente/${pacienteId}`);
  }

  cancelar(citaId: number): Observable<CitaDTO> {
    return this.http.patch<CitaDTO>(`${environment.apiUrl}/api/citas/${citaId}/cancelar`, {});
  }

  reprogramar(citaId: number, datos: ReprogramarCitaRequest): Observable<CitaDTO> {
    return this.http.patch<CitaDTO>(`${environment.apiUrl}/api/citas/${citaId}/reprogramar`, datos);
  }
}
