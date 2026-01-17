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
    LOGIN: '/api/auth/login',
    REGISTER: '/api/auth/registrar',
  },
  ESTRELAS: '/api/estrelas',
  ESTRELA: '/api/estrela',
  PLANETAS: '/api/planetas',
  PLANETA: '/api/planeta',
  LUAS: '/api/luas',
  LUA: '/api/lua',
  ASTEROIDES: '/api/asteroides',
  ASTEROIDE: '/api/asteroide',
  NASA: '/api/nasa/corpos-celeste',
} as const;