import { Routes } from '@angular/router';
import { HomeComponent } from './core/home/home.component';
import { LoginComponent } from './core/auth/login/login.component';
import { RegistroComponent } from './core/auth/registro/registro.component';
import { ReservarCitaComponent } from './paciente/reservar-cita/reservar-cita.component';
import { MisCitasComponent } from './paciente/mis-citas/mis-citas.component';
import { MiHistorialComponent } from './paciente/mi-historial/mi-historial.component';
import { MiAgendaComponent } from './medico/mi-agenda/mi-agenda.component';
import { RegistrarAtencionComponent } from './medico/registrar-atencion/registrar-atencion.component';
import { EspecialidadesComponent } from './admin/especialidades/especialidades.component';
import { MedicosComponent } from './admin/medicos/medicos.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'paciente/mis-citas', component: MisCitasComponent },
  { path: 'paciente/reservar-cita', component: ReservarCitaComponent },
  { path: 'paciente/mi-historial', component: MiHistorialComponent },
  { path: 'medico/mi-agenda', component: MiAgendaComponent },
  { path: 'medico/registrar-atencion/:citaId', component: RegistrarAtencionComponent },
  { path: 'admin/medicos', component: MedicosComponent },
  { path: 'admin/especialidades', component: EspecialidadesComponent },
];
