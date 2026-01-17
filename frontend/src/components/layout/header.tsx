'use client';

import { ROUTES } from '@/src/utils/constants';
import Link from 'next/link';
import { usePathname, useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import { authService } from '@/src/services/authService';

export default function Header() {
  const pathname = usePathname();
  const router = useRouter();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [username, setUsername] = useState<string | null>(null);

  useEffect(() => {
    const checkAuth = () => {
      setIsAuthenticated(authService.isAuthenticated());
      setUsername(authService.getUsername());
    };

    checkAuth();
    const interval = setInterval(checkAuth, 1000);
    
    return () => clearInterval(interval);
  }, [pathname]);

  const handleLogout = () => {
    authService.logout();
    setIsAuthenticated(false);
    setUsername(null);
    router.push(ROUTES.LOGIN);
  };

  return (
    <header className="bg-gradient-to-r from-sky-900 via-sky-700 to-indigo-900 text-white shadow-lg">
      <nav className="container mx-auto px-4 py-4">
        <div className="flex justify-between items-center">
          <Link href={ROUTES.HOME} className="text-2xl font-bold hover:text-yellow-300 transition-colors">
            StellarIndex
          </Link>
          
          <div className="flex items-center gap-6">
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
              <div className="flex items-center gap-4">
                <span className="text-sm">
                  Olá, <span className="font-semibold">{username}</span>
                </span>
                <button
                  onClick={handleLogout}
                  className="bg-red-500 hover:bg-red-600 px-4 py-2 rounded transition-colors"
                >
                  Sair
                </button>
              </div>
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