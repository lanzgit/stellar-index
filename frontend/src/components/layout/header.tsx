'use client';

import { ROUTES } from '@/src/utils/constants';
import Link from 'next/link';
import { usePathname, useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';

export default function Header() {
  const pathname = usePathname();
  const router = useRouter();
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');
    setIsAuthenticated(!!token);
  }, [pathname]);

  const handleLogout = () => {
    localStorage.removeItem('token');
    setIsAuthenticated(false);
    router.push(ROUTES.LOGIN);
  };

  return (
    <header className="bg-gray-800 text-white shadow-lg">
      <nav className="container mx-auto px-4 py-4">
        <div className="flex justify-between items-center">
          <Link href={ROUTES.HOME} className="text-2xl font-bold">
            StellarIndex
          </Link>
          
          <div className="flex gap-6 items-center">
            <Link 
              href={ROUTES.ESTRELAS}
              className={`hover:text-yellow-300 transition-colors ${
                pathname === ROUTES.ESTRELAS ? 'text-yellow-300' : ''
              }`}
            >
              Estrelas
            </Link>
            <Link 
              href={ROUTES.PLANETAS}
              className={`hover:text-yellow-300 transition-colors ${
                pathname === ROUTES.PLANETAS ? 'text-yellow-300' : ''
              }`}
            >
              Planetas
            </Link>
            <Link 
              href={ROUTES.LUAS}
              className={`hover:text-yellow-300 transition-colors ${
                pathname === ROUTES.LUAS ? 'text-yellow-300' : ''
              }`}
            >
              Luas
            </Link>
            <Link 
              href={ROUTES.ASTEROIDES}
              className={`hover:text-yellow-300 transition-colors ${
                pathname === ROUTES.ASTEROIDES ? 'text-yellow-300' : ''
              }`}
            >
              Asteroides
            </Link>
            
            {isAuthenticated ? (
              <button
                onClick={handleLogout}
                className="bg-red-500 hover:bg-red-600 px-4 py-2 rounded transition-colors"
              >
                Sair
              </button>
            ) : (
              <Link 
                href={ROUTES.LOGIN}
                className="bg-green-500 hover:bg-green-600 px-4 py-2 rounded transition-colors"
              >
                Login
              </Link>
            )}
          </div>
        </div>
      </nav>
    </header>
  );
}