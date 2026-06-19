import { Routes } from '@angular/router';
import { HomeComponent } from './core/home/home.component';
import { LoginComponent } from './core/auth/login/login.component';
import { RegistroComponent } from './core/auth/registro/registro.component';
import { ReservarCitaComponent } from './paciente/reservar-cita/reservar-cita.component';
import { MisCitasComponent } from './paciente/mis-citas/mis-citas.component';
import { MiHistorialComponent } from './paciente/mi-historial/mi-historial.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'paciente/mis-citas', component: MisCitasComponent },
  { path: 'paciente/reservar-cita', component: ReservarCitaComponent },
  { path: 'paciente/mi-historial', component: MiHistorialComponent },
];
