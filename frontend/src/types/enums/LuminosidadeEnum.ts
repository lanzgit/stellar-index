export enum LuminosidadeEnum {
  HIPER_GIGANTE = 'HIPER_GIGANTE',
  SUPER_GIGANTE = 'SUPER_GIGANTE',
  GIGANTE = 'GIGANTE',
  GIGANTE_REGULAR = 'GIGANTE_REGULAR',
  GIGANTE_BRILHANTE = 'GIGANTE_BRILHANTE',
  SUBGIGANTE = 'SUBGIGANTE',
  SEQUENCIA_PRINCIPAL = 'SEQUENCIA_PRINCIPAL',
  SUBANA = 'SUBANA',
  ANA_BRANCA = 'ANA_BRANCA',
}

export const LuminosidadeLabels: Record<LuminosidadeEnum, { classe: string; nome: string }> = {
  [LuminosidadeEnum.HIPER_GIGANTE]: { classe: 'Classe 0', nome: 'Hiper Gigante' },
  [LuminosidadeEnum.SUPER_GIGANTE]: { classe: 'Classe I', nome: 'Super Gigante' },
  [LuminosidadeEnum.GIGANTE]: { classe: 'Classe II', nome: 'Gigante' },
  [LuminosidadeEnum.GIGANTE_REGULAR]: { classe: 'Classe III', nome: 'Gigante Regular' },
  [LuminosidadeEnum.GIGANTE_BRILHANTE]: { classe: 'Classe III', nome: 'Gigante Brilhante' },
  [LuminosidadeEnum.SUBGIGANTE]: { classe: 'Classe IV', nome: 'Subgigante' },
  [LuminosidadeEnum.SEQUENCIA_PRINCIPAL]: { classe: 'Classe V', nome: 'Sequência Principal' },
  [LuminosidadeEnum.SUBANA]: { classe: 'Classe VI', nome: 'Subanã' },
  [LuminosidadeEnum.ANA_BRANCA]: { classe: 'Classe VII', nome: 'Anã Branca' },
};