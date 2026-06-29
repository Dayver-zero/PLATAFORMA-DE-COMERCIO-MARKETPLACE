import React, { useState, useEffect, useCallback } from 'react';
import { Search, MapPin, Menu, X, User, ShoppingCart } from 'lucide-react';
import climaService from '../services/climaService';
import geolocalizacionService from '../services/geolocalizacionService';
import authService from '../services/authService';
import carritoService from '../services/carritoService';

const obtenerIconoClima = (icono, condicion) => {
  if (icono) {
    if (icono.startsWith('01')) return '☀️';
    if (icono.startsWith('02')) return '⛅';
    if (icono.startsWith('03') || icono.startsWith('04')) return '☁️';
    if (icono.startsWith('09') || icono.startsWith('10')) return '🌧️';
    if (icono.startsWith('11')) return '⛈️';
    if (icono.startsWith('13')) return '❄️';
    if (icono.startsWith('50')) return '🌫️';
  }

  const texto = (condicion || '').toLowerCase();
  if (texto.includes('lluvia')) return '🌧️';
  if (texto.includes('nublado') || texto.includes('nube')) return '☁️';
  if (texto.includes('despejado') || texto.includes('soleado')) return '☀️';
  return '⛅';
};

/**
 * Header component - Barra de navegación principal
 * Incluye logo, buscador, widget climático y menú móvil
 * Diseño Mobile-First con Tailwind CSS
 */
const Header = ({ onNavigate, usuarioActual, onLogout, carritoConteo: carritoConteoExt }) => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [climaData, setClimaData] = useState(null);
  const [ubicacion, setUbicacion] = useState('Punata, Bolivia');
  const [loadingClima, setLoadingClima] = useState(true);
  const [carritoConteo, setCarritoConteo] = useState(0);

  const cargarConteoCarrito = useCallback(async () => {
    if (usuarioActual) {
      const conteo = await carritoService.contarItems();
      setCarritoConteo(conteo);
    } else {
      setCarritoConteo(0);
    }
  }, [usuarioActual]);

  useEffect(() => { cargarConteoCarrito(); }, [cargarConteoCarrito]);

  useEffect(() => {
    const fetchClima = async () => {
      try {
        setLoadingClima(true);
        const { latitud, longitud } = await geolocalizacionService.obtenerUbicacionActual();
        const data = await climaService.obtenerClimaPorCoordenadas(latitud, longitud);

        if (data) {
          setClimaData({
            temperatura: Math.round(data.temperatura),
            condicion: data.condicion,
            icono: obtenerIconoClima(data.icono, data.condicion),
            humedad: data.humedad,
          });

          if (data.ciudad) {
            setUbicacion(data.pais ? `${data.ciudad}, ${data.pais}` : data.ciudad);
          }
        }
      } catch (error) {
        console.error('Error al obtener clima:', error);
      } finally {
        setLoadingClima(false);
      }
    };

    fetchClima();
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    console.log('Buscando:', searchTerm);
  };

  return (
    <header className="bg-white shadow-md sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <div className="flex items-center cursor-pointer" onClick={() => onNavigate && onNavigate('inicio')}>
            <div className="flex-shrink-0">
              <h1 className="text-2xl font-bold text-primary-600">
                🛒 Mercado Punata
              </h1>
            </div>
          </div>

          {/* Buscador - Desktop */}
          <div className="hidden md:block flex-1 max-w-lg mx-8">
            <form onSubmit={handleSearch} className="relative">
              <input
                type="text"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                placeholder="Buscar productos, comercios..."
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              />
              <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
            </form>
          </div>

          {/* Widget Climático */}
          <div className="hidden md:flex items-center space-x-4">
            <div className="flex items-center bg-gradient-to-r from-blue-50 to-cyan-50 px-4 py-2 rounded-lg">
              {loadingClima ? (
                <div className="animate-pulse">
                  <div className="h-6 w-20 bg-gray-200 rounded"></div>
                </div>
              ) : climaData ? (
                <div className="flex items-center space-x-2">
                  <span className="text-2xl">{climaData.icono}</span>
                  <div className="text-sm">
                    <p className="font-semibold text-gray-800">{climaData.temperatura}°C</p>
                    <p className="text-xs text-gray-600">{climaData.condicion}</p>
                  </div>
                </div>
              ) : (
                <span className="text-sm text-gray-500">Clima no disponible</span>
              )}
            </div>
          </div>

          {/* Ubicación */}
          <div className="hidden md:flex items-center text-gray-600">
            <MapPin className="h-5 w-5 mr-1" />
            <span className="text-sm">{ubicacion}</span>
          </div>

          {/* Carrito */}
          <button
            onClick={() => onNavigate && onNavigate('carrito')}
            className="hidden md:flex relative mr-4 p-2 text-gray-600 hover:text-primary-600 hover:bg-gray-100 rounded-lg transition-colors"
            title="Carrito"
          >
            <ShoppingCart className="h-5 w-5" />
            {carritoConteo > 0 && (
              <span className="absolute -top-1 -right-1 bg-primary-600 text-white text-xs font-bold rounded-full h-5 w-5 flex items-center justify-center">
                {carritoConteo > 9 ? '9+' : carritoConteo}
              </span>
            )}
          </button>

          {/* Usuario / Sesión */}
          <div className="hidden md:flex items-center">
            {usuarioActual ? (
              <div className="flex items-center space-x-3">
                <button
                  onClick={() => onNavigate && onNavigate('perfil')}
                  className="flex items-center space-x-2 bg-slate-50 border border-gray-100 rounded-full pl-2 pr-4 py-1.5 shadow-sm hover:shadow-md hover:border-primary-200 transition-all"
                >
                  <div className="h-8 w-8 rounded-full bg-gradient-to-tr from-primary-500 to-primary-600 flex items-center justify-center text-white font-bold shadow-inner">
                    {usuarioActual.nombre ? usuarioActual.nombre.substring(0, 1).toUpperCase() : usuarioActual.email?.substring(0, 1).toUpperCase() || 'U'}
                  </div>
                  <div className="text-left">
                    <p className="text-xs font-semibold text-slate-800 leading-tight">
                      {usuarioActual.nombre || usuarioActual.email?.split('@')[0]}
                    </p>
                    <p className="text-[10px] text-gray-500 leading-none">
                      {usuarioActual.rol || 'CLIENTE'}
                    </p>
                  </div>
                </button>
                <button
                  onClick={onLogout}
                  className="px-3 py-1.5 text-xs font-bold text-slate-600 hover:text-red-600 hover:bg-red-50 rounded-lg border border-gray-200 hover:border-red-100 transition-colors"
                >
                  Salir
                </button>
              </div>
            ) : (
              <button
                onClick={() => onNavigate && onNavigate('login')}
                className="flex items-center gap-1.5 px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white rounded-xl font-bold text-sm shadow-md transition-all active:scale-[0.98]"
              >
                <User className="h-4 w-4" />
                Iniciar Sesión
              </button>
            )}
          </div>

          {/* Botón menú móvil */}
          <div className="md:hidden">
            <button
              onClick={() => setIsMenuOpen(!isMenuOpen)}
              className="inline-flex items-center justify-center p-2 rounded-md text-gray-700 hover:text-primary-600 hover:bg-gray-100 focus:outline-none"
            >
              {isMenuOpen ? (
                <X className="h-6 w-6" />
              ) : (
                <Menu className="h-6 w-6" />
              )}
            </button>
          </div>
        </div>

        {/* Menú móvil */}
        {isMenuOpen && (
          <div className="md:hidden border-t border-gray-200 py-4">
            <form onSubmit={handleSearch} className="relative mb-4">
              <input
                type="text"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                placeholder="Buscar productos..."
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              />
              <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
            </form>

            {/* Widget climático móvil */}
            <div className="flex items-center justify-between bg-gradient-to-r from-blue-50 to-cyan-50 px-4 py-3 rounded-lg mb-4">
              {loadingClima ? (
                <div className="animate-pulse">
                  <div className="h-6 w-20 bg-gray-200 rounded"></div>
                </div>
              ) : climaData ? (
                <div className="flex items-center space-x-2">
                  <span className="text-2xl">{climaData.icono}</span>
                  <div>
                    <p className="font-semibold text-gray-800">{climaData.temperatura}°C</p>
                    <p className="text-xs text-gray-600">{climaData.condicion}</p>
                  </div>
                </div>
              ) : (
                <span className="text-sm text-gray-500">Clima no disponible</span>
              )}
            </div>

            <div className="flex items-center text-gray-600 mb-4">
              <MapPin className="h-5 w-5 mr-2" />
              <span className="text-sm">{ubicacion}</span>
            </div>

            <nav className="flex flex-col space-y-2">
              <button
                onClick={() => {
                  onNavigate && onNavigate('inicio');
                  setIsMenuOpen(false);
                }}
                className="text-left text-gray-700 hover:text-primary-600 py-2 font-medium"
              >
                Inicio
              </button>
              <button
                onClick={() => {
                  onNavigate && onNavigate('carrito');
                  setIsMenuOpen(false);
                }}
                className="text-left text-gray-700 hover:text-primary-600 py-2 font-medium flex items-center gap-2"
              >
                <ShoppingCart className="h-4 w-4" />
                Carrito
                {carritoConteo > 0 && (
                  <span className="bg-primary-600 text-white text-xs font-bold rounded-full px-2 py-0.5">
                    {carritoConteo}
                  </span>
                )}
              </button>
              {usuarioActual ? (
                <>
                  <button
                    onClick={() => {
                      onNavigate && onNavigate('perfil');
                      setIsMenuOpen(false);
                    }}
                    className="w-full py-2 border-t border-b border-gray-100 flex items-center space-x-3 my-2 animate-fade-in hover:bg-gray-50 rounded-lg transition-colors"
                  >
                    <div className="h-9 w-9 rounded-full bg-gradient-to-tr from-primary-500 to-primary-600 flex items-center justify-center text-white font-bold shadow-inner">
                      {usuarioActual.nombre ? usuarioActual.nombre.substring(0, 1).toUpperCase() : usuarioActual.email?.substring(0, 1).toUpperCase()}
                    </div>
                    <div className="text-left">
                      <p className="text-sm font-semibold text-slate-800">
                        {usuarioActual.nombre || usuarioActual.email?.split('@')[0]}
                      </p>
                      <p className="text-xs text-gray-500">
                        {usuarioActual.rol || 'CLIENTE'}
                      </p>
                    </div>
                  </button>
                  <button
                    onClick={() => {
                      onLogout && onLogout();
                      setIsMenuOpen(false);
                    }}
                    className="text-left text-red-600 hover:text-red-700 py-2 font-semibold"
                  >
                    Cerrar Sesión
                  </button>
                </>
              ) : (
                <button
                  onClick={() => {
                    onNavigate && onNavigate('login');
                    setIsMenuOpen(false);
                  }}
                  className="text-left text-primary-600 hover:text-primary-700 py-2 font-semibold"
                >
                  Iniciar Sesión
                </button>
              )}
            </nav>
          </div>
        )}
      </div>
    </header>
  );
};

export default Header;
