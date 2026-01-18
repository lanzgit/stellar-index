'use client';

import { useEffect, useState } from 'react';
import { estrelaService } from '@/src/services/estrelaService';
import { Estrela, EstrelaPageDTO, EstrelaDoc } from '@/src/types/estrela';
import { ConstelacaoEnum, ConstelacaoLabels } from '@/src/types/enums/ConstelacaoEnum';
import { LuminosidadeEnum, LuminosidadeLabels } from '@/src/types/enums/LuminosidadeEnum';
import Modal from '@/src/components/common/Modal';

export default function EstrelasPage() {
  const [estrelas, setEstrelas] = useState<Estrela[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const [isSearching, setIsSearching] = useState(false);
  const [activeSearchTerm, setActiveSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [estrelaEditando, setEstrelaEditando] = useState<Estrela | null>(null);
  const [errosValidacao, setErrosValidacao] = useState<Record<string, string>>({});
  const [formData, setFormData] = useState<Omit<Estrela, 'id'>>({
    nome: '',
    temperaturaMedia: 0,
    constelacao: ConstelacaoEnum.ARIES,
    luminosidade: LuminosidadeEnum.SEQUENCIA_PRINCIPAL,
    ehHabitavel: false,
    descricao: '',
  });
  const pageSize = 9;

  useEffect(() => {
    if (isSearching && activeSearchTerm) {
      buscarEstrelas();
    } else if (!isSearching) {
      carregarEstrelas();
    }
  }, [page, isSearching, activeSearchTerm]);

  const carregarEstrelas = async () => {
    try {
      setLoading(true);
      setError(null);
      const data: EstrelaPageDTO = await estrelaService.listarPaginado(
        page,
        pageSize
      );
      setEstrelas(data.estrelas);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      setError('Erro ao carregar estrelas. Tente novamente.');
      console.error('Erro ao carregar estrelas:', err);
    } finally {
      setLoading(false);
    }
  };

  const buscarEstrelas = async () => {
    try {
      setLoading(true);
      setError(null);
      const resultados: EstrelaDoc[] = await estrelaService.buscarPorTexto(
        activeSearchTerm,
        page,
        pageSize
      );
      
      const estrelasConvertidas: Estrela[] = resultados.map(doc => ({
        id: doc.id,
        nome: doc.nome,
        temperaturaMedia: doc.temperaturaMedia,
        constelacao: doc.constelacao,
        luminosidade: doc.luminosidade,
        ehHabitavel: doc.ehHabitavel,
        descricao: doc.descricao,
      }));
      
      setEstrelas(estrelasConvertidas);
      setTotalElements(estrelasConvertidas.length);
      setTotalPages(Math.ceil(estrelasConvertidas.length / pageSize));
    } catch (err) {
      setError('Erro ao buscar estrelas. Tente novamente.');
      console.error('Erro ao buscar estrelas:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchTerm.trim()) {
      setPage(0);
      setActiveSearchTerm(searchTerm);
      setIsSearching(true);
    }
  };

  const handleClearSearch = () => {
    setSearchTerm('');
    setActiveSearchTerm('');
    setIsSearching(false);
    setPage(0);
  };

  const handlePreviousPage = () => {
    if (page > 0) {
      setPage(page - 1);
    }
  };

  const handleNextPage = () => {
    if (page < totalPages - 1) {
      setPage(page + 1);
    }
  };

  const handleExcluir = async (id: number, nome: string) => {
    if (!confirm(`Deseja realmente excluir a estrela "${nome}"?`)) {
      return;
    }

    try {
      await estrelaService.excluir(id);
      if (isSearching) {
        buscarEstrelas();
      } else {
        carregarEstrelas();
      }
    } catch (err) {
      alert('Erro ao excluir estrela. Tente novamente.');
      console.error('Erro ao excluir estrela:', err);
    }
  };

  const handleAtualizar = (estrela: Estrela) => {
    setEstrelaEditando(estrela);
    setFormData({
      nome: estrela.nome,
      temperaturaMedia: estrela.temperaturaMedia,
      constelacao: estrela.constelacao,
      luminosidade: estrela.luminosidade,
      ehHabitavel: estrela.ehHabitavel,
      descricao: estrela.descricao,
    });
    setIsModalOpen(true);
  };

  const handleNovoCadastro = () => {
    setEstrelaEditando(null);
    setFormData({
      nome: '',
      temperaturaMedia: 0,
      constelacao: ConstelacaoEnum.ARIES,
      luminosidade: LuminosidadeEnum.SEQUENCIA_PRINCIPAL,
      ehHabitavel: false,
      descricao: '',
    });
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEstrelaEditando(null);
    setErrosValidacao({});
    setFormData({
      nome: '',
      temperaturaMedia: 0,
      constelacao: ConstelacaoEnum.ARIES,
      luminosidade: LuminosidadeEnum.SEQUENCIA_PRINCIPAL,
      ehHabitavel: false,
      descricao: '',
    });
  };

  const validarFormulario = (): boolean => {
    const erros: Record<string, string> = {};

    if (!formData.nome || formData.nome.trim().length < 2) {
      erros.nome = 'Nome deve ter no mínimo 2 caracteres';
    } else if (formData.nome.trim().length > 19) {
      erros.nome = 'Nome deve ter no máximo 19 caracteres';
    }

    if (!formData.descricao || formData.descricao.trim().length < 3) {
      erros.descricao = 'Descrição deve ter no mínimo 3 caracteres';
    } else if (formData.descricao.trim().length > 100) {
      erros.descricao = 'Descrição deve ter no máximo 100 caracteres';
    }

    if (!formData.constelacao) {
      erros.constelacao = 'Selecione uma constelação';
    }

    if (!formData.luminosidade) {
      erros.luminosidade = 'Selecione uma luminosidade';
    }

    setErrosValidacao(erros);
    return Object.keys(erros).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validarFormulario()) {
      return;
    }

    try {
      if (estrelaEditando) {
        await estrelaService.atualizar(estrelaEditando.id, formData);
      } else {
        await estrelaService.criar(formData);
      }
      
      handleCloseModal();
      
      if (isSearching) {
        buscarEstrelas();
      } else {
        carregarEstrelas();
      }
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : `Erro ao ${estrelaEditando ? 'atualizar' : 'criar'} estrela`;
      alert(errorMessage);
      console.error('Erro ao salvar estrela:', err);
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
      <div className="flex justify-center items-center min-h-[400px]">
        <div className="text-xl text-gray-600">Carregando estrelas...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center min-h-[400px]">
        <div className="text-xl text-red-600">{error}</div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-4xl font-bold text-sky-900">
          Estrelas
        </h1>
        <button
          onClick={handleNovoCadastro}
          className="px-6 py-3 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600 transition-colors font-semibold flex items-center gap-2"
        >
          <span className="material-symbols-outlined">add</span>
          Nova Estrela
        </button>
      </div>

      <form onSubmit={handleSearch} className="mb-8">
        <div className="flex gap-2">
          <input
            type="text"
            placeholder="Buscar estrelas por descrição..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent"
          />
          <button
            type="submit"
            className="px-6 py-2 bg-sky-600 text-white rounded-lg hover:bg-sky-700 transition-colors font-semibold"
          >
            ElasticBusca
          </button>
          {isSearching && (
            <button
              type="button"
              onClick={handleClearSearch}
              className="px-6 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-colors font-semibold"
            >
              Limpar
            </button>
          )}
        </div>
      </form>

      {loading && (
        <div className="text-center py-12">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-sky-600"></div>
          <p className="mt-4 text-gray-600">Carregando estrelas...</p>
        </div>
      )}

      {!loading && error && (
        <div className="text-center text-red-600 py-12">{error}</div>
      )}

      {!loading && !error && estrelas.length === 0 && (
        <div className="text-center text-gray-600 py-12">
          Nenhuma estrela encontrada.
        </div>
      )}

      {!loading && !error && estrelas.length > 0 && (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {estrelas.map((estrela) => (
              <div
                key={estrela.id}
                className="bg-white rounded-lg shadow-md p-6 hover:shadow-xl transition-shadow"
              >
                <h2 className="text-2xl font-bold mb-4 text-yellow-600">
                  {estrela.nome}
                </h2>

                <div className="space-y-2 text-gray-700">
                  <p>
                    <span className="font-semibold">Temperatura Média:</span>{' '}
                    {estrela.temperaturaMedia.toLocaleString()}°C
                  </p>

                  <p>
                    <span className="font-semibold">Constelação:</span>{' '}
                    {estrela.constelacao}
                  </p>

                  <p>
                    <span className="font-semibold">Luminosidade:</span>{' '}
                    {estrela.luminosidade}
                  </p>

                  <p>
                    <span className="font-semibold">Habitável:</span>{' '}
                    <span
                      className={
                        estrela.ehHabitavel ? 'text-green-600' : 'text-red-600'
                      }
                    >
                      {estrela.ehHabitavel ? 'Sim' : 'Não'}
                    </span>
                  </p>

                  <p className="mt-4 text-sm text-gray-600 italic">
                    {estrela.descricao}
                  </p>
                </div>

                <div className="flex gap-2 mt-6 justify-end">
                  <button
                    onClick={() => handleAtualizar(estrela)}
                    className="px-3 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors flex items-center justify-center"
                    title="Atualizar estrela"
                  >
                    <span className="material-symbols-outlined text-xl">edit</span>
                  </button>
                  <button
                    onClick={() => handleExcluir(estrela.id, estrela.nome)}
                    className="px-3 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors flex items-center justify-center"
                    title="Excluir estrela"
                  >
                    <span className="material-symbols-outlined text-xl">delete</span>
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div className="flex justify-center items-center gap-4 mt-8">
            <button
              onClick={handlePreviousPage}
              disabled={page === 0}
              className={`px-4 py-2 rounded-lg font-semibold transition-colors ${
                page === 0
                  ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                  : 'bg-sky-600 text-white hover:bg-sky-700'
              }`}
            >
              ← Anterior
            </button>

            <span className="text-gray-700 font-medium">
              Página {page + 1} de {totalPages} ({totalElements} estrelas)
            </span>

            <button
              onClick={handleNextPage}
              disabled={page >= totalPages - 1}
              className={`px-4 py-2 rounded-lg font-semibold transition-colors ${
                page >= totalPages - 1
                  ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                  : 'bg-sky-600 text-white hover:bg-sky-700'
              }`}
            >
              Próxima →
            </button>
          </div>
        </>
      )}

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={estrelaEditando ? `Editar Estrela: ${estrelaEditando.nome}` : 'Nova Estrela'}
        maxWidth="2xl"
      >
        <form onSubmit={handleSubmit}>
          <div className="space-y-4">
            <div>
              <label htmlFor="nome" className="block text-sm font-semibold text-gray-700 mb-2">
                Nome da Estrela *
              </label>
              <input
                type="text"
                id="nome"
                name="nome"
                value={formData.nome}
                onChange={handleInputChange}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent ${
                  errosValidacao.nome ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="Ex: Sirius"
              />
              {errosValidacao.nome && (
                <p className="mt-1 text-sm text-red-600">{errosValidacao.nome}</p>
              )}
              <p className="mt-1 text-xs text-gray-500">
                {formData.nome.length}/19 caracteres
              </p>
            </div>

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
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent"
                placeholder="Ex: 9940"
              />
            </div>

            <div>
              <label htmlFor="constelacao" className="block text-sm font-semibold text-gray-700 mb-2">
                Constelação *
              </label>
              <select
                id="constelacao"
                name="constelacao"
                value={formData.constelacao}
                onChange={handleInputChange}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent ${
                  errosValidacao.constelacao ? 'border-red-500' : 'border-gray-300'
                }`}
              >
                {Object.values(ConstelacaoEnum).map((constelacao) => (
                  <option key={constelacao} value={constelacao}>
                    {ConstelacaoLabels[constelacao]}
                  </option>
                ))}
              </select>
              {errosValidacao.constelacao && (
                <p className="mt-1 text-sm text-red-600">{errosValidacao.constelacao}</p>
              )}
            </div>

            <div>
              <label htmlFor="luminosidade" className="block text-sm font-semibold text-gray-700 mb-2">
                Luminosidade *
              </label>
              <select
                id="luminosidade"
                name="luminosidade"
                value={formData.luminosidade}
                onChange={handleInputChange}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent ${
                  errosValidacao.luminosidade ? 'border-red-500' : 'border-gray-300'
                }`}
              >
                {Object.values(LuminosidadeEnum).map((luminosidade) => (
                  <option key={luminosidade} value={luminosidade}>
                    {LuminosidadeLabels[luminosidade].classe} - {LuminosidadeLabels[luminosidade].nome}
                  </option>
                ))}
              </select>
              {errosValidacao.luminosidade && (
                <p className="mt-1 text-sm text-red-600">{errosValidacao.luminosidade}</p>
              )}
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
                rows={4}
                className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-sky-500 focus:border-transparent resize-none ${
                  errosValidacao.descricao ? 'border-red-500' : 'border-gray-300'
                }`}
                placeholder="Descreva a estrela..."
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
                id="ehHabitavel"
                name="ehHabitavel"
                checked={formData.ehHabitavel}
                onChange={handleCheckboxChange}
                className="w-5 h-5 text-sky-600 border-gray-300 rounded focus:ring-sky-500"
              />
              <label htmlFor="ehHabitavel" className="ml-2 text-sm font-semibold text-gray-700">
                Zona Habitável
              </label>
            </div>
          </div>

          <div className="flex gap-3 mt-6 pt-6 border-t border-gray-200">
            <button
              type="button"
              onClick={handleCloseModal}
              className="flex-1 px-4 py-3 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-colors font-semibold"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="flex-1 px-4 py-3 bg-sky-600 text-white rounded-lg hover:bg-sky-700 transition-colors font-semibold"
            >
              {estrelaEditando ? 'Salvar Alterações' : 'Criar Estrela'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}