'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { authService } from '@/src/services/authService';
import { UsuarioDTO } from '@/src/types/auth';
import { ROUTES } from '@/src/utils/constants';

export default function RegisterPage() {
  const router = useRouter();
  const [formData, setFormData] = useState<UsuarioDTO>({
    username: '',
    senha: '',
    papel: 'USER',
  });
  const [confirmSenha, setConfirmSenha] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    if (formData.senha !== confirmSenha) {
      setError('As senhas não coincidem');
      setLoading(false);
      return;
    }

    if (formData.senha.length < 6) {
      setError('A senha deve ter pelo menos 6 caracteres');
      setLoading(false);
      return;
    }

    try {
      await authService.registrar(formData);
      setSuccess(true);
      setTimeout(() => {
        router.push(ROUTES.LOGIN);
      }, 2000);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro ao registrar usuário');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4">
      <div className="bg-white rounded-lg shadow-2xl p-8 w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-sky-900 mb-2">
            Criar Conta
          </h1>
          <p className="text-gray-600">Registre-se no StellarIndex</p>
        </div>

        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
            {error}
          </div>
        )}

        {success && (
          <div className="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg mb-6">
            Usuário registrado com sucesso! Redirecionando para login...
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label
              htmlFor="username"
              className="block text-sm font-semibold text-gray-700 mb-2"
            >
              Usuário
            </label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleInputChange}
              required
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent"
              placeholder="Escolha um nome de usuário"
            />
          </div>

          <div>
            <label
              htmlFor="senha"
              className="block text-sm font-semibold text-gray-700 mb-2"
            >
              Senha
            </label>
            <input
              type="password"
              id="senha"
              name="senha"
              value={formData.senha}
              onChange={handleInputChange}
              required
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent"
              placeholder="Mínimo 6 caracteres"
            />
          </div>

          <div>
            <label
              htmlFor="confirmSenha"
              className="block text-sm font-semibold text-gray-700 mb-2"
            >
              Confirmar Senha
            </label>
            <input
              type="password"
              id="confirmSenha"
              name="confirmSenha"
              value={confirmSenha}
              onChange={(e) => setConfirmSenha(e.target.value)}
              required
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent"
              placeholder="Digite a senha novamente"
            />
          </div>

          <div>
            <label
              htmlFor="papel"
              className="block text-sm font-semibold text-gray-700 mb-2"
            >
              Tipo de Conta
            </label>
            <select
              id="papel"
              name="papel"
              value={formData.papel}
              onChange={handleInputChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent"
            >
              <option value="USER">Usuário</option>
              <option value="ADMIN">Administrador</option>
            </select>
          </div>

          <button
            type="submit"
            disabled={loading || success}
            className={`w-full py-3 rounded-lg font-semibold text-white transition-colors ${
              loading || success
                ? 'bg-gray-400 cursor-not-allowed'
                : 'bg-sky-600 hover:bg-sky-700'
            }`}
          >
            {loading ? 'Registrando...' : 'Registrar'}
          </button>
        </form>

        <div className="mt-6 text-center">
          <p className="text-gray-600">
            Já tem uma conta?{' '}
            <Link
              href={ROUTES.LOGIN}
              className="text-sky-600 hover:text-sky-700 font-semibold"
            >
              Faça login
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}