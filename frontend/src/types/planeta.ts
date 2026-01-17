import { Lua } from "./lua";

export interface Planeta {
  id: number;
  nome: string;
  temperaturaMedia: number;
  descricao: string;
  ehHabitavel: boolean;
  gravidade: number;
  temSateliteNatural: boolean;
  luas?: Array<Lua>;
}