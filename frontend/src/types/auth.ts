export interface CredenciaisDTO {
  username: string;
  senha: string;
}

export interface UsuarioDTO {
  username: string;
  senha: string;
  papel: 'USER' | 'ADMIN';
}

export interface TokenResponseDTO {
  token: string;
  tipo: string;
  username: string;
  papel: string;
}

export interface AuthState {
  token: string | null;
  username: string | null;
  papel: string | null;
  isAuthenticated: boolean;
}