export const ROUTES = {
  HOME: '/',
  LOGIN: '/auth/login',
  REGISTER: '/auth/register',
  ESTRELAS: '/estrelas',
  PLANETAS: '/planetas',
  LUAS: '/luas',
  ASTEROIDES: '/asteroides',
} as const;

export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth/registrar',
  },
  ESTRELAS: '/estrelas',
  PLANETAS: '/planetas',
  LUAS: '/luas',
  ASTEROIDES: '/asteroides',
  NASA: '/nasa/corpos-celeste',
} as const;