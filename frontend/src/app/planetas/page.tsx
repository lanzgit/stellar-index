'use client';

import { useEffect, useState } from 'react';
import { planetaService } from '@/src/services/planetaService';
import { Planeta } from '@/src/types/planeta';
import Modal from '@/src/components/common/Modal';

export default function PlanetasPage() {
  const [planetas, setPlanetas] = useState<Planeta[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [planetaEditando, setPlanetaEditando] = useState<Planeta | null>(null);
  const [luasVisiveis, setLuasVisiveis] = useState<Set<number>>(new Set());
  const [formData, setFormData] = useState<Omit<Planeta, 'id' | 'luas'>>({
    nome: '',
    temperaturaMedia: 0,
    descricao: '',
    ehHabitavel: false,
    gravidade: 0,
    temSateliteNatural: false,
  });

  useEffect(() => {
    carregarPlanetas();
  }, []);

  const carregarPlanetas = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await planetaService.listarTodos();
      setPlanetas(data);
    } catch (err) {
      setError('Erro ao carregar planetas. Tente novamente.');
      console.error('Erro ao carregar planetas:', err);
    } finally {
      setLoading(false);
    }
  };

  const toggleLuas = (planetaId: number) => {
    setLuasVisiveis((prev) => {
      const newSet = new Set(prev);
      if (newSet.has(planetaId)) {
        newSet.delete(planetaId);
      } else {
        newSet.add(planetaId);
      }
      return newSet;
    });
  };

  const handleExcluir = async (id: number, nome: string) => {
    const confirmacao = window.confirm(
      `Tem certeza que deseja excluir o planeta "${nome}"?`
    );

    if (!confirmacao) {
      return;
    }

    try {
      await planetaService.excluir(id);
      carregarPlanetas();
    } catch (err) {
      alert('Erro ao excluir planeta. Tente novamente.');
      console.error('Erro ao excluir planeta:', err);
    }
  };

  const handleDestruir = async (id: number, nome: string) => {
    const confirmacao = window.confirm(
      `Tem certeza que deseja DESTRUIR o planeta "${nome}"? Ele se tornará inabitável!`
    );

    if (!confirmacao) {
      return;
    }

    try {
      await planetaService.destruir(id);
      carregarPlanetas();
    } catch (err) {
      alert('Erro ao destruir planeta. Tente novamente.');
      console.error('Erro ao destruir planeta:', err);
    }
  };

  const handleAtualizar = (planeta: Planeta) => {
    setPlanetaEditando(planeta);
    setFormData({
      nome: planeta.nome,
      temperaturaMedia: planeta.temperaturaMedia,
      descricao: planeta.descricao,
      ehHabitavel: planeta.ehHabitavel,
      gravidade: planeta.gravidade,
      temSateliteNatural: planeta.temSateliteNatural,
    });
    setIsModalOpen(true);
  };

  const handleNovoCadastro = () => {
    setPlanetaEditando(null);
    setFormData({
      nome: '',
      temperaturaMedia: 0,
      descricao: '',
      ehHabitavel: false,
      gravidade: 0,
      temSateliteNatural: false,
    });
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setPlanetaEditando(null);
    setFormData({
      nome: '',
      temperaturaMedia: 0,
      descricao: '',
      ehHabitavel: false,
      gravidade: 0,
      temSateliteNatural: false,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      if (planetaEditando) {
        await planetaService.atualizar(planetaEditando.id, formData);
      } else {
        await planetaService.criar(formData);
      }

      handleCloseModal();
      carregarPlanetas();
    } catch (err) {
      alert(
        `Erro ao ${planetaEditando ? 'atualizar' : 'criar'} planeta. Tente novamente.`
      );
      console.error('Erro ao salvar planeta:', err);
    }
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value, type } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: type === 'number' ? Number.parseFloat(value) : value,
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
        <div className="text-white text-2xl">Carregando planetas...</div>
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
          <h1 className="text-4xl font-bold text-blue-700">Planetas</h1>
          <button
            onClick={handleNovoCadastro}
            className="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center gap-2"
          >
            <span className="material-symbols-outlined">add</span>
            Novo Planeta
          </button>
        </div>

        {planetas.length === 0 ? (
          <div className="text-blue-700 text-center text-xl">
            Nenhum planeta encontrado.
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {planetas.map((planeta) => (
              <div
                key={planeta.id}
                className="bg-white rounded-lg shadow-lg p-6 hover:shadow-xl transition-shadow"
              >
                <div className="mb-4">
                  <h2 className="text-2xl font-bold text-blue-800 mb-2">
                    {planeta.nome}
                  </h2>
                </div>

                <div className="space-y-2 text-gray-700">
                  <p>
                    <span className="font-semibold">Temperatura Média:</span>{' '}
                    {planeta.temperaturaMedia}°C
                  </p>

                  <p>
                    <span className="font-semibold">Gravidade:</span>{' '}
                    {planeta.gravidade} m/s²
                  </p>

                  <p>
                    <span className="font-semibold">Habitável:</span>{' '}
                    <span
                      className={
                        planeta.ehHabitavel ? 'text-green-600' : 'text-red-600'
                      }
                    >
                      {planeta.ehHabitavel ? 'Sim' : 'Não'}
                    </span>
                  </p>

                  <p>
                    <span className="font-semibold">Tem Satélite Natural:</span>{' '}
                    <span
                      className={
                        planeta.temSateliteNatural
                          ? 'text-green-600'
                          : 'text-gray-600'
                      }
                    >
                      {planeta.temSateliteNatural ? 'Sim' : 'Não'}
                    </span>
                  </p>

                  {planeta.descricao && (
                    <p className="mt-4 text-sm text-gray-600 italic">
                      {planeta.descricao}
                    </p>
                  )}

                  {planeta.luas && planeta.luas.length > 0 && (
                    <div className="mt-4 pt-4 border-t border-gray-200">
                      <button
                        onClick={() => toggleLuas(planeta.id)}
                        className="flex items-center justify-between w-full text-left font-semibold text-blue-700 hover:text-blue-900 transition-colors"
                      >
                        <span>
                          Luas ({planeta.luas.length})
                        </span>
                        <span className="material-symbols-outlined text-xl">
                          {luasVisiveis.has(planeta.id)
                            ? 'expand_less'
                            : 'expand_more'}
                        </span>
                      </button>

                      {luasVisiveis.has(planeta.id) && (
                        <div className="mt-3 space-y-2 pl-2">
                          {planeta.luas.map((lua) => (
                            <div
                              key={lua.id}
                              className="bg-blue-50 p-3 rounded-lg border border-blue-200"
                            >
                              <p className="font-medium text-blue-900">
                                {lua.nome}
                              </p>
                              <p className="text-sm text-gray-600">
                                Distância Orbital:{' '}
                                {lua.distanciaOrbitral.toLocaleString()} km
                              </p>
                            </div>
                          ))}
                        </div>
                      )}
                    </div>
                  )}
                </div>

                <div className="flex gap-2 mt-6 justify-end">
                  <button
                    onClick={() => handleAtualizar(planeta)}
                    className="px-3 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center justify-center"
                    title="Atualizar planeta"
                  >
                    <span className="material-symbols-outlined text-xl">edit</span>
                  </button>
                  
                  {planeta.ehHabitavel && (
                    <button
                      onClick={() => handleDestruir(planeta.id, planeta.nome)}
                      className="px-3 py-2 bg-orange-600 text-white rounded-lg hover:bg-orange-700 transition-colors flex items-center justify-center"
                      title="Destruir planeta (torná-lo inabitável)"
                    >
                      <span className="material-symbols-outlined text-xl">
                        bomb
                      </span>
                    </button>
                  )}

                  <button
                    onClick={() => handleExcluir(planeta.id, planeta.nome)}
                    className="px-3 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors flex items-center justify-center"
                    title="Excluir planeta"
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
        title={planetaEditando ? 'Editar Planeta' : 'Novo Planeta'}
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
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
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
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Gravidade (m/s²) *
            </label>
            <input
              type="number"
              name="gravidade"
              value={formData.gravidade}
              onChange={handleInputChange}
              required
              step="0.01"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
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
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
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

            <div className="flex items-center">
              <input
                type="checkbox"
                name="temSateliteNatural"
                id="temSateliteNatural"
                checked={formData.temSateliteNatural}
                onChange={handleCheckboxChange}
                className="mr-2 w-4 h-4"
              />
              <label
                htmlFor="temSateliteNatural"
                className="text-sm font-medium text-gray-700"
              >
                Tem Satélite Natural
              </label>
            </div>
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
              className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
            >
              {planetaEditando ? 'Atualizar' : 'Criar'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}