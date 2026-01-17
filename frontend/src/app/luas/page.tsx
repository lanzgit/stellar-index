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
      setError('Erro ao carregar dados. Tente novamente.');
      console.error('Erro ao carregar dados:', err);
    } finally {
      setLoading(false);
    }
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
      carregarDados();
    } catch (err) {
      alert('Erro ao excluir lua. Tente novamente.');
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
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setLuaEditando(null);
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

    if (!formData.planetaId || formData.planetaId === 0) {
      alert('Por favor, selecione um planeta.');
      return;
    }

    try {
      if (luaEditando) {
        const updateData: LuaDTO = {
          nome: formData.nome,
          temperaturaMedia: formData.temperaturaMedia,
          descricao: formData.descricao,
          ehHabitavel: formData.ehHabitavel,
          distanciaOrbitral: formData.distanciaOrbitral,
          planetaId: formData.planetaId,
          planetaNome: formData.planetaNome,
        };
        await luaService.atualizar(luaEditando.id, updateData);
      } else {
        const createData: LuaDTO = {
          nome: formData.nome,
          temperaturaMedia: formData.temperaturaMedia,
          descricao: formData.descricao,
          ehHabitavel: formData.ehHabitavel,
          distanciaOrbitral: formData.distanciaOrbitral,
          planetaId: formData.planetaId,
          planetaNome: formData.planetaNome,
        };
        await luaService.criar(createData);
      }

      handleCloseModal();
      carregarDados();
    } catch (err) {
      alert(
        `Erro ao ${luaEditando ? 'atualizar' : 'criar'} lua. Tente novamente.`
      );
      console.error('Erro ao salvar lua:', err);
    }
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value, type } = e.target;

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

                  {lua.descricao && (
                    <p className="mt-4 text-sm text-gray-600 italic">
                      {lua.descricao}
                    </p>
                  )}
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
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Planeta *
            </label>
            <select
              name="planetaId"
              value={formData.planetaId}
              onChange={handleInputChange}
              required
              disabled={luaEditando !== null}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent disabled:bg-gray-100 disabled:cursor-not-allowed"
            >
              <option value={0}>Selecione um planeta</option>
              {planetas.map((planeta) => (
                <option key={planeta.id} value={planeta.id}>
                  {planeta.nome}
                </option>
              ))}
            </select>
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
              required
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
              required
              step="0.01"
              min="0"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Descrição
            </label>
            <textarea
              name="descricao"
              value={formData.descricao}
              onChange={handleInputChange}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
            />
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