export type EstadoCita = 'RESERVADA' | 'ATENDIDA' | 'CANCELADA' | 'NO_ASISTIO';

export interface ReservaCitaRequest {
  pacienteId: number;
  medicoId: number;
  especialidadId: number;
  fechaHora: string;
  motivo?: string;
}

export interface ReprogramarCitaRequest {
  fechaHora: string;
}

export interface CitaDTO {
  id: number;
  pacienteId: number;
  medicoId: number;
  especialidadId: number;
  fechaHora: string;
  estado: EstadoCita;
  motivo: string | null;
  creadaEn: string;
}
