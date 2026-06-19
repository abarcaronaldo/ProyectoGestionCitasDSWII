import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export function roleGuard(rolesPermitidos: string[]): CanActivateFn {
  return () => {
    const auth = inject(AuthService);
    const router = inject(Router);

    if (!auth.estaAutenticado()) {
      return router.parseUrl('/login');
    }

    if (!rolesPermitidos.includes(auth.getRol() ?? '')) {
      return router.parseUrl('/');
    }

    return true;
  };
}
