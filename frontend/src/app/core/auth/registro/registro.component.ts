import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './registro.component.html',
  styles: ``
})
export class RegistroComponent {
  nombres = '';
  apellidos = '';
  email = '';
  password = '';
  telefono = '';
  error = '';
  exito = false;

  constructor(private auth: AuthService, private router: Router) {}

  enviar(): void {
    this.error = '';
    this.auth
      .registro({
        nombres: this.nombres,
        apellidos: this.apellidos,
        email: this.email,
        password: this.password,
        telefono: this.telefono,
        rol: 'PACIENTE',
      })
      .subscribe({
        next: () => {
          this.exito = true;
          setTimeout(() => this.router.navigateByUrl('/login'), 1500);
        },
        error: () => (this.error = 'No se pudo registrar. Revisa los datos.'),
      });
  }
}
