import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

/**
 * Agrega "Authorization: Bearer <token>" a cada peticion saliente, si hay sesion.
 * Sin esto, ninguna pantalla protegida podria llamar al backend a traves del gateway.
 *
 * Ademas, si el backend responde 401 (token invalido o expirado) y habia una sesion
 * activa, cierra la sesion y redirige al login avisando que expiro. El guard
 * "auth.estaAutenticado()" evita que el 401 de un login con credenciales incorrectas
 * (donde aun no hay token) dispare la redireccion.
 */
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const token = auth.getToken();

  const request = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && auth.estaAutenticado()) {
        auth.logout();
        router.navigate(['/login'], { queryParams: { expirado: '1' } });
      }
      return throwError(() => error);
    })
  );
};
