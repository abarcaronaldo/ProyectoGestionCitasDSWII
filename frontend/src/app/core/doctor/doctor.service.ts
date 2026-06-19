import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EspecialidadCrearRequest, EspecialidadDTO, MedicoCrearRequest, MedicoDTO } from './doctor.models';

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

  crearEspecialidad(datos: EspecialidadCrearRequest): Observable<EspecialidadDTO> {
    return this.http.post<EspecialidadDTO>(`${environment.apiUrl}/api/especialidades`, datos);
  }

  eliminarEspecialidad(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/api/especialidades/${id}`);
  }

  crearMedico(datos: MedicoCrearRequest): Observable<MedicoDTO> {
    return this.http.post<MedicoDTO>(`${environment.apiUrl}/api/medicos`, datos);
  }

  eliminarMedico(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/api/medicos/${id}`);
  }
}
