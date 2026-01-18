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
  const [errosValidacao, setErrosValidacao] = useState<Record<string, string>>({});
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

    if (!formData.designacao || formData.designacao.trim().length < 1) {
      erros.designacao = 'Designação deve ter no mínimo 1 caractere';
    } else if (formData.designacao.trim().length > 20) {
      erros.designacao = 'Designação deve ter no máximo 20 caracteres';
    }

    if (!formData.nomeCompleto || formData.nomeCompleto.trim().length < 3) {
      erros.nomeCompleto = 'Nome completo deve ter no mínimo 3 caracteres';
    } else if (formData.nomeCompleto.trim().length > 100) {
      erros.nomeCompleto = 'Nome completo deve ter no máximo 100 caracteres';
    }

    if (!formData.classificacaoOrbital || formData.classificacaoOrbital.trim().length < 1) {
      erros.classificacaoOrbital = 'Classificação orbital deve ter no mínimo 1 caractere';
    } else if (formData.classificacaoOrbital.trim().length > 50) {
      erros.classificacaoOrbital = 'Classificação orbital deve ter no máximo 50 caracteres';
    }

    if (!formData.tipo || formData.tipo.trim().length < 1) {
      erros.tipo = 'Tipo deve ter no mínimo 1 caractere';
    } else if (formData.tipo.trim().length > 30) {
      erros.tipo = 'Tipo deve ter no máximo 30 caracteres';
    }

    setErrosValidacao(erros);
    return Object.keys(erros).length === 0;
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
      nomeCompleto: asteroide.nomeCompleto,
      classificacaoOrbital: asteroide.classificacaoOrbital,
      periodoOrbital: asteroide.periodoOrbital,
      tipo: asteroide.tipo,
      neo: asteroide.neo,
      pha: asteroide.pha,
    });
    setErrosValidacao({});
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
    setErrosValidacao({});
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setAsteroideEditando(null);
    setErrosValidacao({});
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

    if (!validarFormulario()) {
      return;
    }

    try {
      if (asteroideEditando) {
        await asteroideService.atualizar(asteroideEditando.id, formData);
      } else {
        await asteroideService.criar(formData);
      }

      handleCloseModal();
      carregarAsteroides();
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : `Erro ao ${asteroideEditando ? 'atualizar' : 'criar'} asteroide`;
      alert(errorMessage);
      console.error('Erro ao salvar asteroide:', err);
    }
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
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
        title={asteroideEditando ? `Editar Asteroide: ${asteroideEditando.nome}` : 'Novo Asteroide'}
        maxWidth="2xl"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="nome" className="block text-sm font-semibold text-gray-700 mb-2">
              Nome *
            </label>
            <input
              type="text"
              id="nome"
              name="nome"
              value={formData.nome}
              onChange={handleInputChange}
              className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent ${
                errosValidacao.nome ? 'border-red-500' : 'border-gray-300'
              }`}
              placeholder="Ex: Eros"
            />
            {errosValidacao.nome && (
              <p className="mt-1 text-sm text-red-600">{errosValidacao.nome}</p>
            )}
            <p className="mt-1 text-xs text-gray-500">
              {formData.nome.length}/15 caracteres
            </p>
          </div>

          <div>
            <label htmlFor="designacao" className="block text-sm font-semibold text-gray-700 mb-2">
              Designação *
            </label>
            <input
              type="text"
              id="designacao"
              name="designacao"
              value={formData.designacao}
              onChange={handleInputChange}
              className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent ${
                errosValidacao.designacao ? 'border-red-500' : 'border-gray-300'
              }`}
              placeholder="Ex: 433"
            />
            {errosValidacao.designacao && (
              <p className="mt-1 text-sm text-red-600">{errosValidacao.designacao}</p>
            )}
            <p className="mt-1 text-xs text-gray-500">
              {formData.designacao.length}/20 caracteres
            </p>
          </div>

          <div>
            <label htmlFor="nomeCompleto" className="block text-sm font-semibold text-gray-700 mb-2">
              Nome Completo *
            </label>
            <input
              type="text"
              id="nomeCompleto"
              name="nomeCompleto"
              value={formData.nomeCompleto}
              onChange={handleInputChange}
              className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent ${
                errosValidacao.nomeCompleto ? 'border-red-500' : 'border-gray-300'
              }`}
              placeholder="Ex: 433 Eros (A898 PA)"
            />
            {errosValidacao.nomeCompleto && (
              <p className="mt-1 text-sm text-red-600">{errosValidacao.nomeCompleto}</p>
            )}
            <p className="mt-1 text-xs text-gray-500">
              {formData.nomeCompleto?.length}/100 caracteres
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="classificacaoOrbital" className="block text-sm font-semibold text-gray-700 mb-2">
                Classificação Orbital *
              </label>
              <input
                type="text"
                id="classificacaoOrbital"
                name="classificacaoOrbital"
                value={formData.classificacaoOrbital}
                onChange={handleInputChange}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent ${
                  errosValidacao.classificacaoOrbital ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="Ex: AMO"
              />
              {errosValidacao.classificacaoOrbital && (
                <p className="mt-1 text-sm text-red-600">{errosValidacao.classificacaoOrbital}</p>
              )}
              <p className="mt-1 text-xs text-gray-500">
                {formData.classificacaoOrbital?.length}/50 caracteres
              </p>
            </div>

            <div>
              <label htmlFor="tipo" className="block text-sm font-semibold text-gray-700 mb-2">
                Tipo *
              </label>
              <input
                type="text"
                id="tipo"
                name="tipo"
                value={formData.tipo}
                onChange={handleInputChange}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent ${
                  errosValidacao.tipo ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="Ex: S"
              />
              {errosValidacao.tipo && (
                <p className="mt-1 text-sm text-red-600">{errosValidacao.tipo}</p>
              )}
              <p className="mt-1 text-xs text-gray-500">
                {formData.tipo?.length}/30 caracteres
              </p>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="temperaturaMedia" className="block text-sm font-semibold text-gray-700 mb-2">
                Temperatura Média (°C) *
              </label>
              <input
                type="number"
                id="temperaturaMedia"
                name="temperaturaMedia"
                value={formData.temperaturaMedia}
                onChange={handleInputChange}
                required
                step="0.01"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                placeholder="Ex: -73"
              />
            </div>

            <div>
              <label htmlFor="periodoOrbital" className="block text-sm font-semibold text-gray-700 mb-2">
                Período Orbital (dias) *
              </label>
              <input
                type="number"
                id="periodoOrbital"
                name="periodoOrbital"
                value={formData.periodoOrbital}
                onChange={handleInputChange}
                required
                step="0.01"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                placeholder="Ex: 642.9"
              />
            </div>
          </div>

          <div>
            <label htmlFor="descricao" className="block text-sm font-semibold text-gray-700 mb-2">
              Descrição *
            </label>
            <textarea
              id="descricao"
              name="descricao"
              value={formData.descricao}
              onChange={handleInputChange}
              rows={3}
              className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent resize-none ${
                errosValidacao.descricao ? 'border-red-500' : 'border-gray-300'
              }`}
              placeholder="Descreva o asteroide..."
            />
            {errosValidacao.descricao && (
              <p className="mt-1 text-sm text-red-600">{errosValidacao.descricao}</p>
            )}
            <p className="mt-1 text-xs text-gray-500">
              {formData.descricao?.length}/100 caracteres
            </p>
          </div>

          <div className="grid grid-cols-3 gap-4">
            <div className="flex items-center">
              <input
                type="checkbox"
                id="ehHabitavel"
                name="ehHabitavel"
                checked={formData.ehHabitavel}
                onChange={handleCheckboxChange}
                className="w-5 h-5 text-purple-600 border-gray-300 rounded focus:ring-purple-500"
              />
              <label htmlFor="ehHabitavel" className="ml-2 text-sm font-semibold text-gray-700">
                Habitável
              </label>
            </div>

            <div className="flex items-center">
              <input
                type="checkbox"
                id="neo"
                name="neo"
                checked={formData.neo}
                onChange={handleCheckboxChange}
                className="w-5 h-5 text-purple-600 border-gray-300 rounded focus:ring-purple-500"
              />
              <label htmlFor="neo" className="ml-2 text-sm font-semibold text-gray-700">
                NEO
              </label>
            </div>

            <div className="flex items-center">
              <input
                type="checkbox"
                id="pha"
                name="pha"
                checked={formData.pha}
                onChange={handleCheckboxChange}
                className="w-5 h-5 text-purple-600 border-gray-300 rounded focus:ring-purple-500"
              />
              <label htmlFor="pha" className="ml-2 text-sm font-semibold text-gray-700">
                PHA
              </label>
            </div>
          </div>

          <div className="flex gap-3 pt-6 border-t border-gray-200">
            <button
              type="button"
              onClick={handleCloseModal}
              className="flex-1 px-4 py-3 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-colors font-semibold"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="flex-1 px-4 py-3 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors font-semibold"
            >
              {asteroideEditando ? 'Salvar Alterações' : 'Criar Asteroide'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}