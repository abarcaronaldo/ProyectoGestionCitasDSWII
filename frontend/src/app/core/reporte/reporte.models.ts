export interface ResumenGeneralDTO {
  totalReservadas: number;
  totalAtendidas: number;
  totalCanceladas: number;
  totalNoAsistio: number;
  total: number;
}

export interface ResumenMedicoDTO extends ResumenGeneralDTO {
  medicoId: number;
  medicoNombre: string;
}

export interface ResumenEspecialidadDTO extends ResumenGeneralDTO {
  especialidadId: number;
  especialidadNombre: string;
}
