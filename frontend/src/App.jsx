import React, { useState, useEffect, useCallback } from 'react';
import Header from './components/Header';
import FeedRecomendaciones from './components/FeedRecomendaciones';
import PanelComerciante from './components/PanelComerciante';
import Perfil from './components/Perfil';
import Login from './components/Login';
import Carrito from './components/Carrito';
import Checkout from './components/Checkout';
import HistorialPedidos from './components/HistorialPedidos';
import MisReservas from './components/MisReservas';
import ModalReserva from './components/ModalReserva';
import GestionPedidos from './components/GestionPedidos';
import GestionReservas from './components/GestionReservas';
import authService from './services/authService';

function App() {
  const [vistaActual, setVistaActual] = useState('inicio');
  const [usuario, setUsuario] = useState(null);
  const [modalReservaProducto, setModalReservaProducto] = useState(null);
  const [mensajeGlobal, setMensajeGlobal] = useState(null);
  const [checkoutMetodoPago, setCheckoutMetodoPago] = useState(null);

  useEffect(() => {
    const usuarioGuardado = authService.obtenerUsuarioActual();
    if (usuarioGuardado) {
      setUsuario(usuarioGuardado);
    }
  }, []);

  const handleLoginSuccess = (usuarioLogueado) => {
    setUsuario(usuarioLogueado);
    setVistaActual('inicio');
  };

  const handleLogout = () => {
    authService.logout();
    setUsuario(null);
    setVistaActual('inicio');
  };

  const handleNavigate = useCallback((vista, parametro) => {
    if (vista === 'checkout' && parametro === 'YAPE') {
      setCheckoutMetodoPago('YAPE');
    } else {
      setCheckoutMetodoPago(null);
    }
    setVistaActual(vista);
  }, []);

  const handleReservar = (producto) => {
    setModalReservaProducto(producto);
  };

  const handleMostrarMensaje = (msg) => {
    setMensajeGlobal(msg);
    setTimeout(() => setMensajeGlobal(null), 3000);
  };

  const renderVista = () => {
    switch (vistaActual) {
      case 'inicio':
        return <FeedRecomendaciones onReservar={handleReservar} onNavigate={handleNavigate}
          onMostrarMensaje={handleMostrarMensaje} />;
      case 'comerciante':
        return <PanelComerciante onNavigate={handleNavigate} />;
      case 'perfil':
        return <Perfil usuarioActual={usuario} onLogout={handleLogout} onNavigate={handleNavigate} />;
      case 'login':
        return <Login onLoginSuccess={handleLoginSuccess} onCancel={() => setVistaActual('inicio')} />;
      case 'carrito':
        return <Carrito onNavigate={handleNavigate} />;
      case 'checkout':
        return <Checkout onNavigate={handleNavigate} metodoPagoInicial={checkoutMetodoPago} />;
      case 'pedidos':
        return <HistorialPedidos onNavigate={handleNavigate} />;
      case 'reservas':
        return <MisReservas onNavigate={handleNavigate} />;
      case 'gestion-pedidos':
        return <GestionPedidos onNavigate={handleNavigate} />;
      case 'gestion-reservas':
        return <GestionReservas onNavigate={handleNavigate} />;
      default:
        return <FeedRecomendaciones onReservar={handleReservar} onNavigate={handleNavigate}
          onMostrarMensaje={handleMostrarMensaje} />;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Header
        onNavigate={handleNavigate}
        usuarioActual={usuario}
        onLogout={handleLogout}
      />

      {mensajeGlobal && (
        <div className="fixed top-4 right-4 z-50 bg-green-600 text-white px-4 py-2 rounded-lg shadow-lg animate-slide-down text-sm font-medium">
          {mensajeGlobal}
        </div>
      )}

      <div className="bg-white border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <nav className="flex space-x-8 py-3 overflow-x-auto">
            <button onClick={() => setVistaActual('inicio')}
              className={`px-3 py-2 rounded-md text-sm font-medium whitespace-nowrap transition-colors ${
                vistaActual === 'inicio' ? 'bg-primary-100 text-primary-700' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
              }`}>
              Inicio
            </button>
            <button onClick={() => setVistaActual('carrito')}
              className={`px-3 py-2 rounded-md text-sm font-medium whitespace-nowrap transition-colors ${
                vistaActual === 'carrito' ? 'bg-primary-100 text-primary-700' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
              }`}>
              Carrito
            </button>
            {usuario && (
              <>
                <button onClick={() => setVistaActual('perfil')}
                  className={`px-3 py-2 rounded-md text-sm font-medium whitespace-nowrap transition-colors ${
                    vistaActual === 'perfil' ? 'bg-primary-100 text-primary-700' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
                  }`}>
                  Mi Perfil
                </button>
                <button onClick={() => setVistaActual('pedidos')}
                  className={`px-3 py-2 rounded-md text-sm font-medium whitespace-nowrap transition-colors ${
                    vistaActual === 'pedidos' ? 'bg-primary-100 text-primary-700' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
                  }`}>
                  Mis Pedidos
                </button>
                <button onClick={() => setVistaActual('reservas')}
                  className={`px-3 py-2 rounded-md text-sm font-medium whitespace-nowrap transition-colors ${
                    vistaActual === 'reservas' ? 'bg-primary-100 text-primary-700' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
                  }`}>
                  Mis Reservas
                </button>
                {(usuario.rol === 'COMERCIANTE' || usuario.rol === 'ADMIN') && (
                  <>
                    <button onClick={() => setVistaActual('comerciante')}
                      className={`px-3 py-2 rounded-md text-sm font-medium whitespace-nowrap transition-colors ${
                        vistaActual === 'comerciante' ? 'bg-primary-100 text-primary-700' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
                      }`}>
                      Panel Comerciante
                    </button>
                    <button onClick={() => setVistaActual('gestion-pedidos')}
                      className={`px-3 py-2 rounded-md text-sm font-medium whitespace-nowrap transition-colors ${
                        vistaActual === 'gestion-pedidos' ? 'bg-primary-100 text-primary-700' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
                      }`}>
                      Pedidos Recibidos
                    </button>
                    <button onClick={() => setVistaActual('gestion-reservas')}
                      className={`px-3 py-2 rounded-md text-sm font-medium whitespace-nowrap transition-colors ${
                        vistaActual === 'gestion-reservas' ? 'bg-primary-100 text-primary-700' : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
                      }`}>
                      Reservas Recibidas
                    </button>
                  </>
                )}
              </>
            )}
          </nav>
        </div>
      </div>

      <main>{renderVista()}</main>

      {modalReservaProducto && (
        <ModalReserva
          producto={modalReservaProducto}
          onClose={() => setModalReservaProducto(null)}
          onNavigate={handleNavigate}
        />
      )}

      <footer className="bg-white border-t border-gray-200 mt-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="text-center text-gray-600">
            <p className="text-sm">© 2024 Mercado Local Punata - Plataforma de comercio local inteligente</p>
            <p className="text-xs mt-2">Fortaleciendo la economía local de Punata, Bolivia</p>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default App;
