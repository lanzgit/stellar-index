import { API_ENDPOINTS } from '../utils/constants';
import { CredenciaisDTO, TokenResponseDTO, UsuarioDTO } from '../types/auth';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

class AuthService {
    
  async login(credenciais: CredenciaisDTO): Promise<TokenResponseDTO> {
    const response = await fetch(`${API_URL}${API_ENDPOINTS.AUTH.LOGIN}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credenciais),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || 'Erro ao fazer login');
    }

    const data: TokenResponseDTO = await response.json();
    this.saveAuthData(data);
    return data;
  }

  async registrar(usuario: UsuarioDTO): Promise<string> {
    const response = await fetch(`${API_URL}${API_ENDPOINTS.AUTH.REGISTER}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(usuario),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || 'Erro ao registrar usuário');
    }

    return await response.text();
  }

  saveAuthData(data: TokenResponseDTO): void {
    if (typeof window !== 'undefined') {
      localStorage.setItem('token', data.token);
      localStorage.setItem('username', data.username);
      localStorage.setItem('papel', data.papel);
    }
  }

  getToken(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('token');
    }
    return null;
  }

  getUsername(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('username');
    }
    return null;
  }

  getPapel(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('papel');
    }
    return null;
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

  logout(): void {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('papel');
    }
  }
}

export const authService = new AuthService();