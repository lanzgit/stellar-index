'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { authService } from '@/src/services/authService';
import { CredenciaisDTO } from '@/src/types/auth';
import { ROUTES } from '@/src/utils/constants';

export default function LoginPage() {
  const router = useRouter();
  const [formData, setFormData] = useState<CredenciaisDTO>({
    username: '',
    senha: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [errosValidacao, setErrosValidacao] = useState<Record<string, string>>({});

  const validarFormulario = (): boolean => {
    const erros: Record<string, string> = {};

    if (!formData.username || formData.username.trim().length < 3) {
      erros.username = 'Usuário deve ter no mínimo 3 caracteres';
    } else if (formData.username.trim().length > 50) {
      erros.username = 'Usuário deve ter no máximo 50 caracteres';
    }

    if (!formData.senha || formData.senha.length < 6) {
      erros.senha = 'Senha deve ter no mínimo 6 caracteres';
    }

    setErrosValidacao(erros);
    return Object.keys(erros).length === 0;
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    if (errosValidacao[name]) {
      setErrosValidacao((prev) => {
        const novosErros = { ...prev };
        delete novosErros[name];
        return novosErros;
      });
    }

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validarFormulario()) {
      return;
    }

    setLoading(true);
    setError(null);

    try {
      await authService.login(formData);
      router.push(ROUTES.HOME);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro ao fazer login');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4">
      <div className="bg-white rounded-lg shadow-2xl p-8 w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-sky-900 mb-2">
            StellarIndex
          </h1>
          <p className="text-gray-600">Faça login para continuar</p>
        </div>

        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label
              htmlFor="username"
              className="block text-sm font-semibold text-gray-700 mb-2"
            >
              Usuário *
            </label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleInputChange}
              className={`w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent ${
                errosValidacao.username ? 'border-red-500' : 'border-gray-300'
              }`}
              placeholder="Digite seu usuário"
            />
            {errosValidacao.username && (
              <p className="mt-1 text-sm text-red-600">{errosValidacao.username}</p>
            )}
          </div>

          <div>
            <label
              htmlFor="senha"
              className="block text-sm font-semibold text-gray-700 mb-2"
            >
              Senha *
            </label>
            <input
              type="password"
              id="senha"
              name="senha"
              value={formData.senha}
              onChange={handleInputChange}
              className={`w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent ${
                errosValidacao.senha ? 'border-red-500' : 'border-gray-300'
              }`}
              placeholder="Digite sua senha"
            />
            {errosValidacao.senha && (
              <p className="mt-1 text-sm text-red-600">{errosValidacao.senha}</p>
            )}
          </div>

          <button
            type="submit"
            disabled={loading}
            className={`w-full py-3 rounded-lg font-semibold text-white transition-colors ${
              loading
                ? 'bg-gray-400 cursor-not-allowed'
                : 'bg-sky-600 hover:bg-sky-700'
            }`}
          >
            {loading ? 'Entrando...' : 'Entrar'}
          </button>
        </form>

        <div className="mt-6 text-center">
          <p className="text-gray-600">
            Não tem uma conta?{' '}
            <Link
              href={ROUTES.REGISTER}
              className="text-sky-600 hover:text-sky-700 font-semibold"
            >
              Registre-se
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}