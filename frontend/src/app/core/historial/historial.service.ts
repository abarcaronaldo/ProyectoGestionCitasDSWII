import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AtencionCrearRequest, AtencionDTO } from './historial.models';

@Injectable({ providedIn: 'root' })
export class HistorialService {
  private http = inject(HttpClient);

  misAtenciones(): Observable<AtencionDTO[]> {
    return this.http.get<AtencionDTO[]>(`${environment.apiUrl}/api/atenciones/mis-atenciones`);
  }

  crear(datos: AtencionCrearRequest): Observable<AtencionDTO> {
    return this.http.post<AtencionDTO>(`${environment.apiUrl}/api/atenciones`, datos);
  }
}
