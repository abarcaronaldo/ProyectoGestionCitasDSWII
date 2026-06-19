export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegistroRequest {
  nombres: string;
  apellidos: string;
  email: string;
  password: string;
  telefono?: string;
  rol: string;
}

export interface TokenResponse {
  token: string;
  tipo: string;
  usuarioId: number;
  nombres: string;
  email: string;
  rol: string;
}

export interface UsuarioResponse {
  id: number;
  nombres: string;
  apellidos: string;
  email: string;
  telefono: string;
  rol: string;
  activo: boolean;
}
