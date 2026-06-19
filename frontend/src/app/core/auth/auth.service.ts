import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, RegistroRequest, TokenResponse, UsuarioResponse } from './auth.models';

const TOKEN_KEY = 'efsrt_token';
const ROL_KEY = 'efsrt_rol';
const NOMBRES_KEY = 'efsrt_nombres';
const USUARIO_ID_KEY = 'efsrt_usuario_id';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);

  login(datos: LoginRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${environment.apiUrl}/api/auth/login`, datos).pipe(
      tap((res) => {
        localStorage.setItem(TOKEN_KEY, res.token);
        localStorage.setItem(ROL_KEY, res.rol);
        localStorage.setItem(NOMBRES_KEY, res.nombres);
        localStorage.setItem(USUARIO_ID_KEY, String(res.usuarioId));
      })
    );
  }

  registro(datos: RegistroRequest): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(`${environment.apiUrl}/api/auth/registro`, datos);
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROL_KEY);
    localStorage.removeItem(NOMBRES_KEY);
    localStorage.removeItem(USUARIO_ID_KEY);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  getRol(): string | null {
    return localStorage.getItem(ROL_KEY);
  }

  getNombres(): string | null {
    return localStorage.getItem(NOMBRES_KEY);
  }

  getUsuarioId(): number | null {
    const valor = localStorage.getItem(USUARIO_ID_KEY);
    return valor ? Number(valor) : null;
  }

  estaAutenticado(): boolean {
    return this.getToken() !== null;
  }
}
