import axios, { AxiosError } from 'axios';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 20000
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('papel');
      window.location.href = '/auth/login';
      return Promise.reject(new Error('Sessão expirada. Faça login novamente.'));
    }

    if (error.response?.status === 403) {
      return Promise.reject(new Error('Você não tem permissão para realizar esta ação.'));
    }

    if (error.response?.status === 400) {
      const message = error.response.data || 'Dados inválidos. Verifique os campos.';
      return Promise.reject(new Error(typeof message === 'string' ? message : JSON.stringify(message)));
    }

    if (error.response?.status === 404) {
      return Promise.reject(new Error('Recurso não encontrado.'));
    }

    if (error.response?.status && error.response.status >= 500) {
      return Promise.reject(new Error('Erro no servidor. Tente novamente mais tarde.'));
    }

    if (error.code === 'ECONNABORTED') {
      return Promise.reject(new Error('Tempo de requisição excedido. Verifique sua conexão.'));
    }

    if (!error.response) {
      return Promise.reject(new Error('Erro de conexão. Verifique sua internet.'));
    }

    return Promise.reject(error);
  }
);

export default api;