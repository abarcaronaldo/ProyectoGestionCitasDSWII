import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styles: ``
})
export class LoginComponent implements OnInit {
  email = '';
  password = '';
  error = '';
  sesionExpirada = false;

  constructor(
    private auth: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.sesionExpirada = this.route.snapshot.queryParamMap.get('expirado') === '1';
  }

  enviar(): void {
    this.error = '';
    this.sesionExpirada = false;
    this.auth.login({ email: this.email, password: this.password }).subscribe({
      next: () => this.router.navigateByUrl('/'),
      error: () => (this.error = 'Email o contraseña incorrectos.'),
    });
  }
}
