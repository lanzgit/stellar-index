'use client';

import { useEffect, useState } from 'react';
import { asteroideService } from '@/src/services/asteroideService';
import { Asteroide } from '@/src/types/asteroide';
import Modal from '@/src/components/common/Modal';

export default function AsteroidesPage() {
  const [asteroides, setAsteroides] = useState<Asteroide[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [asteroideEditando, setAsteroideEditando] = useState<Asteroide | null>(null);
  const [formData, setFormData] = useState<Omit<Asteroide, 'id'>>({
    nome: '',
    temperaturaMedia: 0,
    descricao: '',
    ehHabitavel: false,
    designacao: '',
    nomeCompleto: '',
    classificacaoOrbital: '',
    periodoOrbital: 0,
    tipo: '',
    neo: false,
    pha: false,
  });

  useEffect(() => {
    carregarAsteroides();
  }, []);

  const carregarAsteroides = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await asteroideService.listarTodos();
      setAsteroides(data);
    } catch (err) {
      setError('Erro ao carregar asteroides. Tente novamente.');
      console.error('Erro ao carregar asteroides:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleExcluir = async (id: number, nome: string) => {
    const confirmacao = window.confirm(
      `Tem certeza que deseja excluir o asteroide "${nome}"?`
    );

    if (!confirmacao) {
      return;
    }

    try {
      await asteroideService.excluir(id);
      carregarAsteroides();
    } catch (err) {
      alert('Erro ao excluir asteroide. Tente novamente.');
      console.error('Erro ao excluir asteroide:', err);
    }
  };

  const handleAtualizar = (asteroide: Asteroide) => {
    setAsteroideEditando(asteroide);
    setFormData({
      nome: asteroide.nome,
      temperaturaMedia: asteroide.temperaturaMedia,
      descricao: asteroide.descricao,
      ehHabitavel: asteroide.ehHabitavel,
      designacao: asteroide.designacao,
      nomeCompleto: asteroide.nomeCompleto || '',
      classificacaoOrbital: asteroide.classificacaoOrbital || '',
      periodoOrbital: asteroide.periodoOrbital || 0,
      tipo: asteroide.tipo || '',
      neo: asteroide.neo,
      pha: asteroide.pha,
    });
    setIsModalOpen(true);
  };

  const handleNovoCadastro = () => {
    setAsteroideEditando(null);
    setFormData({
      nome: '',
      temperaturaMedia: 0,
      descricao: '',
      ehHabitavel: false,
      designacao: '',
      nomeCompleto: '',
      classificacaoOrbital: '',
      periodoOrbital: 0,
      tipo: '',
      neo: false,
      pha: false,
    });
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setAsteroideEditando(null);
    setFormData({
      nome: '',
      temperaturaMedia: 0,
      descricao: '',
      ehHabitavel: false,
      designacao: '',
      nomeCompleto: '',
      classificacaoOrbital: '',
      periodoOrbital: 0,
      tipo: '',
      neo: false,
      pha: false,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      if (asteroideEditando) {
        await asteroideService.atualizar(asteroideEditando.id, formData);
      } else {
        await asteroideService.criar(formData);
      }
      
      handleCloseModal();
      carregarAsteroides();
    } catch (err) {
      alert(
        `Erro ao ${asteroideEditando ? 'atualizar' : 'criar'} asteroide. Tente novamente.`
      );
      console.error('Erro ao salvar asteroide:', err);
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
        <div className="text-purple-800 text-2xl">Carregando asteroides...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-red-600 text-2xl">{error}</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen p-8">
      <div className="max-w-7xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-4xl text-purple-800 font-bold">Asteroides</h1>
          <button
            onClick={handleNovoCadastro}
            className="px-6 py-3 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors flex items-center gap-2"
          >
            <span className="material-symbols-outlined">add</span>
            Novo Asteroide
          </button>
        </div>

        <div className="mb-6 text-white">
          <p>Total: {asteroides.length} asteroide{asteroides.length !== 1 ? 's' : ''}</p>
        </div>

        {asteroides.length === 0 ? (
          <div className="text-white text-center text-xl">
            Nenhum asteroide encontrado.
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {asteroides.map((asteroide) => (
              <div
                key={asteroide.id}
                className="bg-white rounded-lg shadow-lg p-6 hover:shadow-xl transition-shadow"
              >
                <div className="mb-4">
                  <h2 className="text-2xl font-bold text-purple-800 mb-2">
                    {asteroide.nome}
                  </h2>
                  <p className="text-sm text-gray-600 mb-1">
                    <span className="font-semibold">Designação:</span>{' '}
                    {asteroide.designacao}
                  </p>
                  {asteroide.nomeCompleto && (
                    <p className="text-sm text-gray-600 mb-1">
                      <span className="font-semibold">Nome Completo:</span>{' '}
                      {asteroide.nomeCompleto}
                    </p>
                  )}
                </div>

                <div className="space-y-2 text-gray-700">
                  <p>
                    <span className="font-semibold">Temperatura Média:</span>{' '}
                    {asteroide.temperaturaMedia}K
                  </p>

                  {asteroide.tipo && (
                    <p>
                      <span className="font-semibold">Tipo:</span> {asteroide.tipo}
                    </p>
                  )}

                  {asteroide.classificacaoOrbital && (
                    <p>
                      <span className="font-semibold">Classificação Orbital:</span>{' '}
                      {asteroide.classificacaoOrbital}
                    </p>
                  )}

                  {asteroide.periodoOrbital && Number(asteroide.periodoOrbital) > 0 && (
                    <p>
                      <span className="font-semibold">Período Orbital:</span>{' '}
                      {Number(asteroide.periodoOrbital).toFixed(2)} dias
                    </p>
                  )}

                  <div className="flex gap-4 mt-2">
                    <p>
                      <span className="font-semibold">NEO:</span>{' '}
                      <span
                        className={asteroide.neo ? 'text-orange-600' : 'text-gray-600'}
                      >
                        {asteroide.neo ? 'Sim' : 'Não'}
                      </span>
                    </p>
                    <p>
                      <span className="font-semibold">PHA:</span>{' '}
                      <span
                        className={asteroide.pha ? 'text-red-600' : 'text-gray-600'}
                      >
                        {asteroide.pha ? 'Sim' : 'Não'}
                      </span>
                    </p>
                  </div>

                  <p>
                    <span className="font-semibold">Habitável:</span>{' '}
                    <span
                      className={
                        asteroide.ehHabitavel ? 'text-green-600' : 'text-red-600'
                      }
                    >
                      {asteroide.ehHabitavel ? 'Sim' : 'Não'}
                    </span>
                  </p>

                  {asteroide.descricao && (
                    <p className="mt-4 text-sm text-gray-600 italic">
                      {asteroide.descricao}
                    </p>
                  )}
                </div>

                <div className="flex gap-2 mt-6 justify-end">
                  <button
                    onClick={() => handleAtualizar(asteroide)}
                    className="px-3 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center justify-center"
                    title="Atualizar asteroide"
                  >
                    <span className="material-symbols-outlined text-xl">edit</span>
                  </button>
                  <button
                    onClick={() => handleExcluir(asteroide.id, asteroide.nome)}
                    className="px-3 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors flex items-center justify-center"
                    title="Excluir asteroide"
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
        title={asteroideEditando ? 'Editar Asteroide' : 'Novo Asteroide'}
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
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Designação *
            </label>
            <input
              type="text"
              name="designacao"
              value={formData.designacao}
              onChange={handleInputChange}
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Temperatura Média (K) *
            </label>
            <input
              type="number"
              name="temperaturaMedia"
              value={formData.temperaturaMedia}
              onChange={handleInputChange}
              required
              step="0.01"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Nome Completo
            </label>
            <input
              type="text"
              name="nomeCompleto"
              value={formData.nomeCompleto}
              onChange={handleInputChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Tipo
            </label>
            <input
              type="text"
              name="tipo"
              value={formData.tipo}
              onChange={handleInputChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Classificação Orbital
            </label>
            <input
              type="text"
              name="classificacaoOrbital"
              value={formData.classificacaoOrbital}
              onChange={handleInputChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Período Orbital (dias)
            </label>
            <input
              type="number"
              name="periodoOrbital"
              value={formData.periodoOrbital}
              onChange={handleInputChange}
              step="0.01"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
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
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
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
                name="neo"
                id="neo"
                checked={formData.neo}
                onChange={handleCheckboxChange}
                className="mr-2 w-4 h-4"
              />
              <label htmlFor="neo" className="text-sm font-medium text-gray-700">
                NEO (Near-Earth Object)
              </label>
            </div>

            <div className="flex items-center">
              <input
                type="checkbox"
                name="pha"
                id="pha"
                checked={formData.pha}
                onChange={handleCheckboxChange}
                className="mr-2 w-4 h-4"
              />
              <label htmlFor="pha" className="text-sm font-medium text-gray-700">
                PHA (Potentially Hazardous)
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
              className="px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors"
            >
              {asteroideEditando ? 'Atualizar' : 'Criar'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}