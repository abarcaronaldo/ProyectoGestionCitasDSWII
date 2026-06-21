import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './core/auth/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';

  constructor(public auth: AuthService, private router: Router) {}

  // Las páginas públicas de acceso (home sin sesión, login, registro) usan
  // su propio layout de pantalla completa, así que ahí se oculta la barra
  // de navegación global.
  get ocultarBarra(): boolean {
    const ruta = this.router.url.split('?')[0];
    return ['/', '/login', '/registro'].includes(ruta) && !this.auth.estaAutenticado();
  }

  cerrarSesion(): void {
    this.auth.logout();
    this.router.navigateByUrl('/');
  }
}
