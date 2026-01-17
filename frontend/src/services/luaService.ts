import api from './api';
import { API_ENDPOINTS } from '../utils/constants';
import { Lua, LuaDTO } from '../types/lua';

class LuaService {
  async listarTodos(): Promise<Lua[]> {
    const response = await api.get<Lua[]>(API_ENDPOINTS.LUAS);
    return response.data;
  }

  async obterPorId(id: number): Promise<Lua> {
    const response = await api.get<Lua>(`${API_ENDPOINTS.LUA}/${id}`);
    return response.data;
  }

  async criar(lua: LuaDTO): Promise<Lua> {
    const response = await api.post<Lua>(
      `${API_ENDPOINTS.LUA}`,
      lua
    );
    return response.data;
  }

  async atualizar(id: number, lua: LuaDTO): Promise<Lua> {
    const response = await api.put<Lua>(
      `${API_ENDPOINTS.LUA}/${id}`,
      lua
    );
    return response.data;
  }

  async excluir(id: number): Promise<void> {
    await api.delete(`${API_ENDPOINTS.LUA}/${id}`);
  }
}

export const luaService = new LuaService();