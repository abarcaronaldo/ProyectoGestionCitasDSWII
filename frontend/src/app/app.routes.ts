import { Routes } from '@angular/router';
import { HomeComponent } from './core/home/home.component';
import { LoginComponent } from './core/auth/login/login.component';
import { RegistroComponent } from './core/auth/registro/registro.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
];
