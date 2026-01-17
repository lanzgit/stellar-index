import api from './api';
import { API_ENDPOINTS } from '../utils/constants';
import { Planeta } from '../types/planeta';

class PlanetaService {
  async listarTodos(): Promise<Planeta[]> {
    const response = await api.get<Planeta[]>(API_ENDPOINTS.PLANETAS);
    return response.data;
  }

  async obterPorId(id: number): Promise<Planeta> {
    const response = await api.get<Planeta>(`${API_ENDPOINTS.PLANETAS}/${id}`);
    return response.data;
  }

  async criar(planeta: Omit<Planeta, 'id' | 'luas'>): Promise<Planeta> {
    const response = await api.post<Planeta>(
      `${API_ENDPOINTS.PLANETA}`,
      planeta
    );
    return response.data;
  }

  async atualizar(id: number, planeta: Omit<Planeta, 'id' | 'luas'>): Promise<Planeta> {
    const response = await api.put<Planeta>(
      `${API_ENDPOINTS.PLANETA}/${id}`,
      planeta
    );
    return response.data;
  }

  async excluir(id: number): Promise<void> {
    await api.delete(`${API_ENDPOINTS.PLANETA}/${id}`);
  }

  async destruir(id: number): Promise<Planeta> {
    const response = await api.patch<Planeta>(`${API_ENDPOINTS.PLANETA}/${id}/nuke`);
    return response.data;
  }
}

export const planetaService = new PlanetaService();