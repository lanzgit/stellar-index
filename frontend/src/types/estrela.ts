import { ConstelacaoEnum } from "./enums/ConstelacaoEnum";
import { LuminosidadeEnum } from "./enums/LuminosidadeEnum";

export interface Estrela {
  id: number;
  nome: string;
  temperaturaMedia: number;
  descricao: string;
  ehHabitavel: boolean;
  constelacao: ConstelacaoEnum;
  luminosidade: LuminosidadeEnum;
}

export interface EstrelaPageDTO {
  estrelas: Estrela[];
  totalElements: number;
  totalPages: number;
}

export interface EstrelaDoc extends Estrela {
  score?: number;
}
