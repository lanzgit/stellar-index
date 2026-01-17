import Link from "next/link";
import { ROUTES } from "../utils/constants";

export default function Home() {
  return (
    <div className="text-center py-12">
      <h1 className="text-5xl font-bold mb-6 text-sky-900">
        StellarIndex
      </h1>
      <p className="text-xl text-gray-600 mb-8">
        Explore o universo de objetos celestes
      </p>
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mt-12">
        <Link 
          href={ROUTES.ESTRELAS}
          className="bg-yellow-100 hover:bg-yellow-200 p-8 rounded-lg transition-colors"
        >
          <h2 className="text-2xl font-semibold">Estrelas</h2>
        </Link>
        
        <Link 
          href={ROUTES.PLANETAS}
          className="bg-blue-100 hover:bg-blue-200 p-8 rounded-lg transition-colors"
        >
          <h2 className="text-2xl font-semibold">Planetas</h2>
        </Link>
        
        <Link 
          href={ROUTES.LUAS}
          className="bg-gray-100 hover:bg-gray-200 p-8 rounded-lg transition-colors"
        >
          <h2 className="text-2xl font-semibold">Luas</h2>
        </Link>
        
        <Link 
          href={ROUTES.ASTEROIDES}
          className="bg-red-100 hover:bg-red-200 p-8 rounded-lg transition-colors"
        >
          <h2 className="text-2xl font-semibold">Asteroides</h2>
        </Link>
      </div>
    </div>
  );
}
