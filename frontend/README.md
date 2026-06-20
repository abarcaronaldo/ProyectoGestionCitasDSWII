# Frontend — Gestión de Citas Médicas

Aplicación web en **Angular 18** para el sistema de gestión de citas médicas. Es la interfaz que
usan pacientes, médicos y personal administrativo; se comunica **únicamente con el API Gateway**
(puerto 8080), que a su vez enruta a los microservicios.

## Requisitos

- Node.js 18+ y npm
- El **backend corriendo** (Gateway en `http://localhost:8080`) para que el login y los datos funcionen

## Cómo levantarlo

```bash
npm install      # solo la primera vez
npm start        # equivale a "ng serve"
```

Luego abrir **http://localhost:4200/**. El servidor recarga solo al guardar cambios.

> En Windows con PowerShell, si `npm start` da un error de "scripts deshabilitados", usar
> `npm.cmd start`.

## Configuración

La URL del backend se define en `src/environments/`:

- `environment.development.ts` → desarrollo (`apiUrl: 'http://localhost:8080'`)
- `environment.ts` → producción: cambiar `apiUrl` a la URL pública del Gateway antes de desplegar.

El token JWT se guarda en `localStorage` y un *interceptor* lo agrega a cada petición.

## Usuarios de prueba

Contraseña de todos: `Gestion2026`

| Rol | Email |
|---|---|
| Administrador | `admin@gestion.pe` |
| Médico | `medico.Flores@gestion.pe` |
| Paciente | `paciente.Quispe@gestion.pe` |

## Pantallas por rol

- **Paciente:** reservar cita, mis citas (cancelar), mi historial.
- **Médico:** mi agenda (marcar asistencia), registrar atención.
- **Admin/Recepción:** médicos, especialidades, reportes.

Las rutas están protegidas por *guards* según el rol.

## Compilar para producción

```bash
npm run build    # genera la versión optimizada en dist/
```

El contenido de `dist/frontend/browser/` son archivos estáticos listos para servir.
