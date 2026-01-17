import api from './api';
import { API_ENDPOINTS } from '../utils/constants';
import { Planeta } from '../types/planeta';

export const planetaService = {
  listarTodos: async (): Promise<Planeta[]> => {
    const response = await api.get<Planeta[]>(API_ENDPOINTS.PLANETAS);
    return response.data;
  },

  buscarPorId: async (id: number): Promise<Planeta> => {
    const response = await api.get<Planeta>(`${API_ENDPOINTS.PLANETAS}/${id}`);
    return response.data;
  },
};