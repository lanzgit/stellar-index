export interface Lua {
  id: number;
  nome: string;
  temperaturaMedia: number;
  descricao: string;
  ehHabitavel: boolean;
  distanciaOrbitral: number;
  planetaId: number;
  planetaNome: string;
}

export interface LuaDTO {
  id?: number;  
  nome: string;
  temperaturaMedia: number;
  descricao: string;
  ehHabitavel: boolean;
  distanciaOrbitral: number;
  planetaId: number;
  planetaNome: string;
}