import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Header from "../components/layout/header";
import Footer from "../components/layout/footer";


const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "StellarIndex - Catálogo de Astros",
  description: "Sistema de gerenciamento de objetos celestes",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="pt-BR">
      <body className={inter.className}>
        <div className="flex flex-col min-h-screen">
          <Header />
          <main className="flex-grow container mx-auto px-4 py-8">
            {children}
          </main>
          <Footer />
        </div>
      </body>
    </html>
  );
}
