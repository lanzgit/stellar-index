'use client';

import { useEffect, useState } from 'react';
import { luaService } from '@/src/services/luaService';
import { planetaService } from '@/src/services/planetaService';
import { Lua, LuaDTO } from '@/src/types/lua';
import { Planeta } from '@/src/types/planeta';
import Modal from '@/src/components/common/Modal';

interface FormData {
  nome: string;
  temperaturaMedia: number;
  descricao: string;
  ehHabitavel: boolean;
  distanciaOrbitral: number;
  planetaId: number;
  planetaNome: string;
}

export default function LuasPage() {
  const [luas, setLuas] = useState<Lua[]>([]);
  const [planetas, setPlanetas] = useState<Planeta[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [luaEditando, setLuaEditando] = useState<Lua | null>(null);
  const [errosValidacao, setErrosValidacao] = useState<Record<string, string>>({});
  const [formData, setFormData] = useState<FormData>({
    nome: '',
    temperaturaMedia: 0,
    descricao: '',
    ehHabitavel: false,
    distanciaOrbitral: 0,
    planetaId: 0,
    planetaNome: '',
  });

  useEffect(() => {
    carregarDados();
  }, []);

  const carregarDados = async () => {
    try {
      setLoading(true);
      setError(null);
      const [luasData, planetasData] = await Promise.all([
        luaService.listarTodos(),
        planetaService.listarTodos(),
      ]);
      setLuas(luasData);
      setPlanetas(planetasData);
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Erro ao carregar dados';
      setError(errorMessage);
      console.error('Erro ao carregar dados:', err);
    } finally {
      setLoading(false);
    }
  };

  const validarFormulario = (): boolean => {
    const erros: Record<string, string> = {};

    if (!formData.nome || formData.nome.trim().length < 2) {
      erros.nome = 'Nome deve ter no mínimo 2 caracteres';
    } else if (formData.nome.trim().length > 15) {
      erros.nome = 'Nome deve ter no máximo 15 caracteres';
    }

    if (!formData.descricao || formData.descricao.trim().length < 3) {
      erros.descricao = 'Descrição deve ter no mínimo 3 caracteres';
    } else if (formData.descricao.trim().length > 100) {
      erros.descricao = 'Descrição deve ter no máximo 100 caracteres';
    }

    if (!formData.distanciaOrbitral || formData.distanciaOrbitral <= 0) {
      erros.distanciaOrbitral = 'Distância orbital deve ser maior que zero';
    }

    if (!formData.planetaId || formData.planetaId === 0) {
      erros.planetaId = 'Selecione um planeta';
    }

    setErrosValidacao(erros);
    return Object.keys(erros).length === 0;
  };

  const obterNomePlaneta = (planetaId: number | null): string => {
    if (!planetaId) return 'N/A';
    const planeta = planetas.find((p) => p.id === planetaId);
    return planeta ? planeta.nome : 'Desconhecido';
  };

  const handleExcluir = async (id: number, nome: string) => {
    const confirmacao = window.confirm(
      `Tem certeza que deseja excluir a lua "${nome}"?`
    );

    if (!confirmacao) {
      return;
    }

    try {
      await luaService.excluir(id);
      await carregarDados();
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Erro ao excluir lua';
      alert(errorMessage);
      console.error('Erro ao excluir lua:', err);
    }
  };

  const handleAtualizar = (lua: Lua) => {
    setLuaEditando(lua);
    setFormData({
      nome: lua.nome,
      temperaturaMedia: lua.temperaturaMedia,
      descricao: lua.descricao,
      ehHabitavel: lua.ehHabitavel,
      distanciaOrbitral: lua.distanciaOrbitral,
      planetaId: lua.planetaId || 0,
      planetaNome: lua.planetaNome || '',
    });
    setErrosValidacao({});
    setIsModalOpen(true);
  };

  const handleNovoCadastro = () => {
    setLuaEditando(null);
    setFormData({
      nome: '',
      temperaturaMedia: 0,
      descricao: '',
      ehHabitavel: false,
      distanciaOrbitral: 0,
      planetaId: planetas.length > 0 ? planetas[0].id : 0,
      planetaNome: planetas.length > 0 ? planetas[0].nome : '',
    });
    setErrosValidacao({});
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setLuaEditando(null);
    setErrosValidacao({});
    setFormData({
      nome: '',
      temperaturaMedia: 0,
      descricao: '',
      ehHabitavel: false,
      distanciaOrbitral: 0,
      planetaId: 0,
      planetaNome: '',
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validarFormulario()) {
      return;
    }

    try {
      const luaDTO: LuaDTO = {
        nome: formData.nome.trim(),
        temperaturaMedia: formData.temperaturaMedia,
        descricao: formData.descricao.trim(),
        ehHabitavel: formData.ehHabitavel,
        distanciaOrbitral: formData.distanciaOrbitral,
        planetaId: formData.planetaId,
        planetaNome: formData.planetaNome,
      };

      if (luaEditando) {
        await luaService.atualizar(luaEditando.id, luaDTO);
      } else {
        await luaService.criar(luaDTO);
      }

      handleCloseModal();
      await carregarDados();
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Erro ao salvar lua';
      alert(errorMessage);
      console.error('Erro ao salvar lua:', err);
    }
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value, type } = e.target;

    if (errosValidacao[name]) {
      setErrosValidacao((prev) => {
        const novosErros = { ...prev };
        delete novosErros[name];
        return novosErros;
      });
    }

    setFormData((prev) => ({
      ...prev,
      [name]: type === 'number' ? Number.parseFloat(value) : 
              name === 'planetaId' ? Number.parseInt(value) : value,
    }));
  };

  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: checked,
    }));
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-white text-2xl">Carregando luas...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-red-500 text-2xl">{error}</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen p-8">
      <div className="max-w-7xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-4xl font-bold text-gray-700">Luas</h1>
          <button
            onClick={handleNovoCadastro}
            className="px-6 py-3 bg-gray-600 text-white rounded-lg hover:bg-gray-400 transition-colors flex items-center gap-2"
            disabled={planetas.length === 0}
          >
            <span className="material-symbols-outlined">add</span>
            Nova Lua
          </button>
        </div>

        {planetas.length === 0 && (
          <div className="bg-yellow-100 border-l-4 border-yellow-500 text-yellow-700 p-4 mb-6">
            <p className="font-bold">Atenção!</p>
            <p>É necessário cadastrar pelo menos um planeta antes de criar uma lua.</p>
          </div>
        )}

        {luas.length === 0 ? (
          <div className="text-gray-700 text-center text-xl">
            Nenhuma lua encontrada.
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {luas.map((lua) => (
              <div
                key={lua.id}
                className="bg-white rounded-lg shadow-lg p-6 hover:shadow-xl transition-shadow"
              >
                <div className="mb-4">
                  <h2 className="text-2xl font-bold text-gray-700 mb-2">
                    {lua.nome}
                  </h2>

                  {lua.planetaId && (
                    <div className="flex items-center gap-2 text-sm text-gray-600 bg-indigo-50 px-3 py-2 rounded-lg">
                      <span className="material-symbols-outlined text-indigo-600 text-base">
                        public
                      </span>
                      <span>
                        <span className="font-semibold">Planeta:</span>{' '}
                        {lua.planetaNome || obterNomePlaneta(lua.planetaId)}
                      </span>
                    </div>
                  )}
                </div>

                <div className="space-y-2 text-gray-700">
                  <p>
                    <span className="font-semibold">Temperatura Média:</span>{' '}
                    {lua.temperaturaMedia}°C
                  </p>

                  <p>
                    <span className="font-semibold">Distância Orbital:</span>{' '}
                    {lua.distanciaOrbitral.toLocaleString()} km
                  </p>

                  <p>
                    <span className="font-semibold">Habitável:</span>{' '}
                    <span
                      className={
                        lua.ehHabitavel ? 'text-green-600' : 'text-red-600'
                      }
                    >
                      {lua.ehHabitavel ? 'Sim' : 'Não'}
                    </span>
                  </p>

                  <p className="mt-4 text-sm text-gray-600 italic">
                    {lua.descricao}
                  </p>
                </div>

                <div className="flex gap-2 mt-6 justify-end">
                  <button
                    onClick={() => handleAtualizar(lua)}
                    className="px-3 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center justify-center"
                    title="Atualizar lua"
                  >
                    <span className="material-symbols-outlined text-xl">edit</span>
                  </button>
                  <button
                    onClick={() => handleExcluir(lua.id, lua.nome)}
                    className="px-3 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors flex items-center justify-center"
                    title="Excluir lua"
                  >
                    <span className="material-symbols-outlined text-xl">delete</span>
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={luaEditando ? 'Editar Lua' : 'Nova Lua'}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Nome *
            </label>
            <input
              type="text"
              name="nome"
              value={formData.nome}
              onChange={handleInputChange}
              className={`w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                errosValidacao.nome ? 'border-red-500' : 'border-gray-300'
              }`}
            />
            {errosValidacao.nome && (
              <p className="mt-1 text-sm text-red-600">{errosValidacao.nome}</p>
            )}
            <p className="mt-1 text-xs text-gray-500">
              {formData.nome.length}/15 caracteres
            </p>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Planeta *
            </label>
            <select
              name="planetaId"
              value={formData.planetaId}
              onChange={handleInputChange}
              disabled={!!luaEditando}
              className={`w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                errosValidacao.planetaId ? 'border-red-500' : 'border-gray-300'
              } ${luaEditando ? 'bg-gray-100 cursor-not-allowed' : ''}`}
            >
              <option value={0}>Selecione um planeta</option>
              {planetas.map((planeta) => (
                <option key={planeta.id} value={planeta.id}>
                  {planeta.nome}
                </option>
              ))}
            </select>
            {errosValidacao.planetaId && (
              <p className="mt-1 text-sm text-red-600">{errosValidacao.planetaId}</p>
            )}
            {luaEditando && (
              <p className="text-xs text-gray-500 mt-1">
                O planeta não pode ser alterado após a criação da lua.
              </p>
            )}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Temperatura Média (°C) *
            </label>
            <input
              type="number"
              name="temperaturaMedia"
              value={formData.temperaturaMedia}
              onChange={handleInputChange}
              step="0.01"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Distância Orbital (km) *
            </label>
            <input
              type="number"
              name="distanciaOrbitral"
              value={formData.distanciaOrbitral}
              onChange={handleInputChange}
              step="0.01"
              min="0"
              className={`w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent ${
                errosValidacao.distanciaOrbitral ? 'border-red-500' : 'border-gray-300'
              }`}
            />
            {errosValidacao.distanciaOrbitral && (
              <p className="mt-1 text-sm text-red-600">{errosValidacao.distanciaOrbitral}</p>
            )}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Descrição *
            </label>
            <textarea
              name="descricao"
              value={formData.descricao}
              onChange={handleInputChange}
              rows={3}
              className={`w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent resize-none ${
                errosValidacao.descricao ? 'border-red-500' : 'border-gray-300'
              }`}
            />
            {errosValidacao.descricao && (
              <p className="mt-1 text-sm text-red-600">{errosValidacao.descricao}</p>
            )}
            <p className="mt-1 text-xs text-gray-500">
              {formData.descricao.length}/100 caracteres
            </p>
          </div>

          <div className="flex items-center">
            <input
              type="checkbox"
              name="ehHabitavel"
              id="ehHabitavel"
              checked={formData.ehHabitavel}
              onChange={handleCheckboxChange}
              className="mr-2 w-4 h-4"
            />
            <label htmlFor="ehHabitavel" className="text-sm font-medium text-gray-700">
              Habitável
            </label>
          </div>

          <div className="flex gap-3 justify-end pt-4">
            <button
              type="button"
              onClick={handleCloseModal}
              className="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors"
            >
              {luaEditando ? 'Atualizar' : 'Criar'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}