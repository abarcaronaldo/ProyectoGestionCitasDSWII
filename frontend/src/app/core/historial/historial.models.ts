export interface AtencionDTO {
  id: number;
  citaId: number;
  pacienteId: number;
  medicoId: number;
  fechaAtencion: string;
  motivoConsulta: string;
  diagnostico: string;
  tratamiento: string | null;
  observaciones: string | null;
  activo: boolean;
  creadoEn: string;
}

export interface AtencionCrearRequest {
  citaId: number;
  motivoConsulta: string;
  diagnostico: string;
  tratamiento?: string;
  observaciones?: string;
}
