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
import { ReportesComponent } from './admin/reportes/reportes.component';
import { roleGuard } from './core/auth/role.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  {
    path: 'paciente/mis-citas',
    component: MisCitasComponent,
    canActivate: [roleGuard(['PACIENTE'])],
  },
  {
    path: 'paciente/reservar-cita',
    component: ReservarCitaComponent,
    canActivate: [roleGuard(['PACIENTE'])],
  },
  {
    path: 'paciente/mi-historial',
    component: MiHistorialComponent,
    canActivate: [roleGuard(['PACIENTE'])],
  },
  {
    path: 'medico/mi-agenda',
    component: MiAgendaComponent,
    canActivate: [roleGuard(['MEDICO'])],
  },
  {
    path: 'medico/registrar-atencion/:citaId',
    component: RegistrarAtencionComponent,
    canActivate: [roleGuard(['MEDICO'])],
  },
  {
    path: 'admin/medicos',
    component: MedicosComponent,
    canActivate: [roleGuard(['ADMIN', 'RECEPCIONISTA'])],
  },
  {
    path: 'admin/especialidades',
    component: EspecialidadesComponent,
    canActivate: [roleGuard(['ADMIN', 'RECEPCIONISTA'])],
  },
  {
    path: 'admin/reportes',
    component: ReportesComponent,
    canActivate: [roleGuard(['ADMIN', 'RECEPCIONISTA'])],
  },
];
