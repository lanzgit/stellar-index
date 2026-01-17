'use client';

import { useEffect, useState } from 'react';
import { planetaService } from '@/src/services/planetaService';
import { Planeta } from '@/src/types/planeta';

export default function PlanetasPage() {
  const [planetas, setPlanetas] = useState<Planeta[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

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

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-[400px]">
        <div className="text-xl text-gray-600">Carregando planetas...</div>
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
      <h1 className="text-4xl font-bold mb-8 text-sky-900">Planetas</h1>

      {planetas.length === 0 ? (
        <div className="text-center text-gray-600 py-12">
          Nenhum planeta encontrado.
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {planetas.map((planeta) => (
            <div
              key={planeta.id}
              className="bg-white rounded-lg shadow-md p-6 hover:shadow-xl transition-shadow"
            >
              <h2 className="text-2xl font-bold mb-4 text-blue-600">
                {planeta.nome}
              </h2>

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
                  <span className="font-semibold">Satélites Naturais:</span>{' '}
                  {planeta.temSateliteNatural ? 'Sim' : 'Não'}
                </p>

                <p className="mt-4 text-sm text-gray-600 italic">
                  {planeta.descricao}
                </p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}