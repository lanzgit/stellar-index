export interface Asteroide {
  id: number;
  nome: string;
  temperaturaMedia: number;
  descricao: string;
  ehHabitavel: boolean;
  designacao: string;
  nomeCompleto?: string;
  classificacaoOrbital?: string;
  periodoOrbital?: number;
  tipo?: string;
  neo: boolean;
  pha: boolean;
}

export interface AsteroidePageDTO {
  asteroides: Asteroide[];
  totalElements: number;
  totalPages: number;
}