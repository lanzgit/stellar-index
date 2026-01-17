import api from './api';
import { API_ENDPOINTS } from '../utils/constants';
import { Estrela, EstrelaPageDTO, EstrelaDoc } from '../types/estrela';

class EstrelaService {
  async listarPaginado(page: number, size: number): Promise<EstrelaPageDTO> {
    const response = await api.get<EstrelaPageDTO>(
      `${API_ENDPOINTS.ESTRELAS}/paginado`,
      {
        params: { page, size },
      }
    );
    return response.data;
  }

  async buscarPorTexto(
    texto: string,
    page: number = 0,
    size: number = 10
  ): Promise<EstrelaDoc[]> {
    const response = await api.get<EstrelaDoc[]>(
      `${API_ENDPOINTS.ESTRELAS}/search`,
      {
        params: { texto, page, size },
      }
    );
    return response.data;
  }

  async criar(estrela: Omit<Estrela, 'id'>): Promise<Estrela> {
    const response = await api.post<Estrela>(`${API_ENDPOINTS.ESTRELA}`, estrela);
    return response.data;
  }

  async atualizar(id: number, estrela: Omit<Estrela, 'id'>): Promise<Estrela> {
    const response = await api.put<Estrela>(
      `${API_ENDPOINTS.ESTRELA}/${id}`,
      estrela
    );
    return response.data;
  }

  async excluir(id: number): Promise<void> {
    await api.delete(`${API_ENDPOINTS.ESTRELA}/${id}`);
  }
}

export const estrelaService = new EstrelaService();