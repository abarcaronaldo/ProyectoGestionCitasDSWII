export interface EspecialidadDTO {
  id: number;
  nombre: string;
  descripcion: string | null;
  activo: boolean;
  creadoEn: string;
}

export interface MedicoDTO {
  id: number;
  usuarioId: number | null;
  nombres: string;
  apellidos: string;
  cmp: string;
  telefono: string | null;
  email: string | null;
  especialidadId: number;
  especialidadNombre: string;
  activo: boolean;
  creadoEn: string;
}

export interface EspecialidadCrearRequest {
  nombre: string;
  descripcion?: string;
}

export interface MedicoCrearRequest {
  usuarioId?: number;
  nombres: string;
  apellidos: string;
  cmp: string;
  telefono?: string;
  email?: string;
  especialidadId: number;
}
