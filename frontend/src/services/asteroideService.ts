import api from './api';
import { API_ENDPOINTS } from '../utils/constants';
import { Asteroide, AsteroidePageDTO } from '../types/asteroide';

class AsteroideService {
  async listarPaginado(page: number, size: number): Promise<AsteroidePageDTO> {
    const response = await api.get<AsteroidePageDTO>(
      `${API_ENDPOINTS.ASTEROIDES}/paginado`,
      {
        params: { page, size },
      }
    );
    return response.data;
  }

  async listarTodos(): Promise<Asteroide[]> {
    const response = await api.get<Asteroide[]>(API_ENDPOINTS.ASTEROIDES);
    return response.data;
  }

  async obterPorId(id: number): Promise<Asteroide> {
    const response = await api.get<Asteroide>(`${API_ENDPOINTS.ASTEROIDE}/${id}`);
    return response.data;
  }

  async criar(asteroide: Omit<Asteroide, 'id'>): Promise<Asteroide> {
    const response = await api.post<Asteroide>(
      API_ENDPOINTS.ASTEROIDE,
      asteroide
    );
    return response.data;
  }

  async atualizar(id: number, asteroide: Omit<Asteroide, 'id'>): Promise<Asteroide> {
    const response = await api.put<Asteroide>(
      `${API_ENDPOINTS.ASTEROIDE}/${id}`,
      asteroide
    );
    return response.data;
  }

  async excluir(id: number): Promise<void> {
    await api.delete(`${API_ENDPOINTS.ASTEROIDE}/${id}`);
  }
}

export const asteroideService = new AsteroideService();