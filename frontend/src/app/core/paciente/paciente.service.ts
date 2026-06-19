import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PacienteDTO } from './paciente.models';

@Injectable({ providedIn: 'root' })
export class PacienteService {
  private http = inject(HttpClient);

  obtenerPorUsuarioId(usuarioId: number): Observable<PacienteDTO> {
    return this.http.get<PacienteDTO>(`${environment.apiUrl}/api/pacientes/usuario/${usuarioId}`);
  }
}
