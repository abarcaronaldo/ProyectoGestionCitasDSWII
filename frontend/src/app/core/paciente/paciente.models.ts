export interface PacienteDTO {
  id: number;
  usuarioId: number | null;
  dni: string;
  nombres: string;
  apellidos: string;
  fechaNacimiento: string | null;
  sexo: string | null;
  telefono: string | null;
  email: string | null;
  direccion: string | null;
  grupoSanguineo: string | null;
  alergias: string | null;
  activo: boolean;
  creadoEn: string;
}
