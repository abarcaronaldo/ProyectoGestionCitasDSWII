import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EspecialidadDTO, MedicoDTO } from './doctor.models';

@Injectable({ providedIn: 'root' })
export class DoctorService {
  private http = inject(HttpClient);

  listarMedicos(): Observable<MedicoDTO[]> {
    return this.http.get<MedicoDTO[]>(`${environment.apiUrl}/api/medicos`);
  }

  listarEspecialidades(): Observable<EspecialidadDTO[]> {
    return this.http.get<EspecialidadDTO[]>(`${environment.apiUrl}/api/especialidades`);
  }

  obtenerPorUsuarioId(usuarioId: number): Observable<MedicoDTO> {
    return this.http.get<MedicoDTO>(`${environment.apiUrl}/api/medicos/usuario/${usuarioId}`);
  }
}
