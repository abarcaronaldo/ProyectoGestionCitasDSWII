import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { environment } from '../../../environments/environment';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styles: ``
})
export class HomeComponent {
  apiUrl = environment.apiUrl;

  constructor(public auth: AuthService) {}

  cerrarSesion(): void {
    this.auth.logout();
  }
}
