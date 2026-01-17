export interface Astro {
  id: number;
  nome: string;
  temperaturaMedia: number;
  descricao: string;
  ehHabitavel: boolean;
}

export interface Estrela extends Astro {
  constelacao: string;
  luminosidade: string;
}

export interface Planeta extends Astro {
  gravidade: number;
  temSateliteNatural: boolean;
  luas?: Lua[];
}

export interface Lua extends Astro {
  distanciaOrbitral: number;
  planetaId: number;
  planetaNome?: string;
}

export interface Asteroide extends Astro {
  designacao: string;
  nomeCompleto: string;
  classificacaoOrbital: string;
  periodoOrbital: string;
  tipo: string;
  neo: boolean;
  pha: boolean;
}

export interface Usuario {
  username: string;
  senha: string;
  papel?: string;
}

export interface LoginResponse {
  token: string;
}