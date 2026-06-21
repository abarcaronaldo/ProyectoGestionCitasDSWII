import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export function roleGuard(rolesPermitidos: string[]): CanActivateFn {
  return (route, state) => {
    const auth = inject(AuthService);
    const router = inject(Router);

    if (!auth.estaAutenticado()) {
      // Recuerda a dónde quería ir para volver ahí tras iniciar sesión.
      return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
    }

    if (!rolesPermitidos.includes(auth.getRol() ?? '')) {
      return router.parseUrl('/');
    }

    return true;
  };
}
