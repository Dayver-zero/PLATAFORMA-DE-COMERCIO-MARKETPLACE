import React, { useState, useEffect, useCallback, useMemo } from 'react';
import { Sparkles, MapPin, Filter, RefreshCw, Cloud, Sun, Snowflake, Umbrella } from 'lucide-react';
import ProductCard from './ProductCard';
import climaService from '../services/climaService';
import recomendacionesService from '../services/recomendacionesService';

const PEXEL = (id) => `https://images.pexels.com/photos/${id}/pexels-photo-${id}.jpeg?auto=compress&cs=tinysrgb&w=300&h=200&dpr=1`;

const PRODUCTOS_MOCK = [
  {
    id: 1, nombre: 'Paraguas Plegable', descripcion: 'Paraguas resistente al viento, ideal para días lluviosos',
    precio: 45.00, precioAnterior: 60.00,
    urlImagen: PEXEL(1451040),
    nombreComercio: 'Tienda El Sol', distancia: 0.5,
    etiquetasInteligentes: ['lluvia', 'protección'], calificacionPromedio: 4.5, stock: 15, comercioId: 1
  },
  {
    id: 2, nombre: 'Chaqueta Térmica', descripcion: 'Chaqueta abrigada para días fríos, forro interior polar',
    precio: 120.00, precioAnterior: 150.00,
    urlImagen: PEXEL(9286989),
    nombreComercio: 'Moda Punata', distancia: 0.8,
    etiquetasInteligentes: ['frío', 'abrigo'], calificacionPromedio: 4.8, stock: 8, comercioId: 2
  },
  {
    id: 3, nombre: 'Botas de Agua', descripcion: 'Botas impermeables para caminar bajo la lluvia',
    precio: 85.00,
    urlImagen: PEXEL(14479209),
    nombreComercio: 'Calzado Local', distancia: 1.2,
    etiquetasInteligentes: ['lluvia', 'calzado'], calificacionPromedio: 4.3, stock: 12, comercioId: 3
  },
  {
    id: 4, nombre: 'Gorro de Lana', descripcion: 'Gorro tejido a mano, perfecto para el frío punateño',
    precio: 25.00, precioAnterior: 35.00,
    urlImagen: PEXEL(1124465),
    nombreComercio: 'Artesanías Punata', distancia: 0.3,
    etiquetasInteligentes: ['frío', 'abrigo'], calificacionPromedio: 4.6, stock: 20, comercioId: 4
  },
  {
    id: 5, nombre: 'Guantes Térmicos', descripcion: 'Guantes con forro polar, mantienen tus manos calientes',
    precio: 35.00,
    urlImagen: PEXEL(2884869),
    nombreComercio: 'Moda Punata', distancia: 0.8,
    etiquetasInteligentes: ['frío', 'abrigo'], calificacionPromedio: 4.4, stock: 10, comercioId: 2
  },
  {
    id: 6, nombre: 'Lentes de Sol', descripcion: 'Lentes polarizados con protección UV, ideales para días soleados',
    precio: 65.00, precioAnterior: 90.00,
    urlImagen: PEXEL(978808),
    nombreComercio: 'Óptica Punata', distancia: 0.6,
    etiquetasInteligentes: ['calor', 'protección'], calificacionPromedio: 4.7, stock: 6, comercioId: 5
  },
  {
    id: 7, nombre: 'Sombrero de Ala Ancha', descripcion: 'Sombrero artesanal para protegerte del sol',
    precio: 40.00,
    urlImagen: PEXEL(1154390),
    nombreComercio: 'Artesanías Punata', distancia: 0.3,
    etiquetasInteligentes: ['calor', 'protección'], calificacionPromedio: 4.2, stock: 14, comercioId: 4
  },
  {
    id: 8, nombre: 'Bloqueador Solar', descripcion: 'Protector solar FPS 50, resistente al agua',
    precio: 55.00,
    urlImagen: PEXEL(6476079),
    nombreComercio: 'Farmacia Punata', distancia: 0.4,
    etiquetasInteligentes: ['calor', 'protección'], calificacionPromedio: 4.9, stock: 25, comercioId: 6
  },
  {
    id: 9, nombre: 'Abrigo de Invierno', descripcion: 'Abrigo largo acolchado, resistente al frío extremo',
    precio: 200.00, precioAnterior: 250.00,
    urlImagen: PEXEL(5825655),
    nombreComercio: 'Tienda El Sol', distancia: 0.5,
    etiquetasInteligentes: ['frío', 'abrigo'], calificacionPromedio: 4.8, stock: 5, comercioId: 1
  },
  {
    id: 10, nombre: 'Bebida Hidratante', descripcion: 'Bebida isotónica natural, refrescante para el calor',
    precio: 8.00,
    urlImagen: PEXEL(30847728),
    nombreComercio: 'Tienda El Sol', distancia: 0.5,
    etiquetasInteligentes: ['calor'], calificacionPromedio: 4.0, stock: 50, comercioId: 1
  },
  {
    id: 11, nombre: 'Chocolatería Artesanal', descripcion: 'Chocolate caliente en polvo, ideal para días fríos',
    precio: 18.00, precioAnterior: 22.00,
    urlImagen: PEXEL(926361),
    nombreComercio: 'Artesanías Punata', distancia: 0.3,
    etiquetasInteligentes: ['frío', 'casa'], calificacionPromedio: 4.7, stock: 30, comercioId: 4
  },
  {
    id: 12, nombre: 'Impermeable para Moto', descripcion: 'Cubierta impermeable para motocicleta, días lluviosos',
    precio: 95.00, precioAnterior: 130.00,
    urlImagen: PEXEL(2246792),
    nombreComercio: 'Repuestos Punata', distancia: 1.5,
    etiquetasInteligentes: ['lluvia', 'protección'], calificacionPromedio: 4.1, stock: 7, comercioId: 7
  },
  {
    id: 13, nombre: 'Helado Artesanal', descripcion: 'Helado cremoso de frutas, perfecto para el calor',
    precio: 12.00,
    urlImagen: PEXEL(29241942),
    nombreComercio: 'Heladería Punata', distancia: 0.2,
    etiquetasInteligentes: ['calor'], calificacionPromedio: 4.9, stock: 40, comercioId: 8
  },
  {
    id: 14, nombre: 'Chaleco Reflectivo', descripcion: 'Chaleco de seguridad reflectivo, visible en días nublados/lluvia',
    precio: 30.00,
    urlImagen: PEXEL(1208468),
    nombreComercio: 'Repuestos Punata', distancia: 1.5,
    etiquetasInteligentes: ['lluvia', 'protección'], calificacionPromedio: 4.3, stock: 18, comercioId: 7
  },
  {
    id: 15, nombre: 'Ventilador Portátil', descripcion: 'Ventilador USB recargable, alivio instantáneo del calor',
    precio: 45.00, precioAnterior: 55.00,
    urlImagen: PEXEL(28792263),
    nombreComercio: 'Electro Punata', distancia: 0.7,
    etiquetasInteligentes: ['calor', 'casa'], calificacionPromedio: 4.5, stock: 22, comercioId: 9
  }
];

const CONDICION_CLIMA = {
  LLOVIENDOSO: ['lluvia', 'lluvioso', 'lloviendo', 'rain', 'drizzle', 'thunderstorm'],
  FRIO: ['frío', 'frio', 'cold', 'snow', 'nieve'],
  CALOR: ['calor', 'caliente', 'hot', 'clear', 'soleado'],
  NUBLADO: ['nublado', 'clouds', 'cloudy']
};

const getCondicionClima = (clima) => {
  if (!clima) return null;
  const temp = clima.temperatura;
  const desc = (clima.condicion || '').toLowerCase();

  if (temp < 15) return 'frío';
  if (temp > 25) return 'calor';
  if (desc.includes('lluvia') || desc.includes('rain') || desc.includes('drizzle') || desc.includes('thunder')) return 'lluvia';
  if (desc.includes('nube') || desc.includes('cloud')) return 'nublado';
  return null;
};

const WEATHER_CONFIG = {
  lluvia: { color: 'bg-blue-500', label: 'Lluvioso', icon: Umbrella },
  frío: { color: 'bg-cyan-500', label: 'Frío', icon: Snowflake },
  calor: { color: 'bg-orange-500', label: 'Calor', icon: Sun },
  nublado: { color: 'bg-gray-500', label: 'Nublado', icon: Cloud }
};

const FeedRecomendaciones = ({ onReservar, onNavigate, onMostrarMensaje }) => {
  const [recomendaciones, setRecomendaciones] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filtroActivo, setFiltroActivo] = useState('todos');
  const [ubicacionUsuario, setUbicacionUsuario] = useState(null);
  const [climaActual, setClimaActual] = useState(null);
  const [usandoBackend, setUsandoBackend] = useState(false);

  const fetchClima = useCallback(async (lat, lng) => {
    try {
      const clima = await climaService.obtenerClimaPorCoordenadas(lat, lng);
      setClimaActual(clima);
    } catch {
      setClimaActual(null);
    }
  }, []);

  const fetchRecomendaciones = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      const ubicacion = {
        latitud: -17.5528,
        longitud: -65.8756,
        ciudad: 'Punata'
      };
      setUbicacionUsuario(ubicacion);

      let data = null;
      try {
        const usuario = JSON.parse(localStorage.getItem('usuario'));
        const usuarioId = usuario?.id;
        if (usuarioId) {
          data = await recomendacionesService.obtenerRecomendaciones(usuarioId, ubicacion.latitud, ubicacion.longitud);
        }
      } catch {
        // fallback a mock
      }

      if (data && data.length > 0) {
        setRecomendaciones(data);
        setUsandoBackend(true);
      } else {
        setRecomendaciones(PRODUCTOS_MOCK);
        setUsandoBackend(false);
      }

      await fetchClima(ubicacion.latitud, ubicacion.longitud);
    } catch (err) {
      setError('Error al cargar recomendaciones');
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  }, [fetchClima]);

  useEffect(() => { fetchRecomendaciones(); }, [fetchRecomendaciones]);

  const handleRefresh = () => { fetchRecomendaciones(); };

  const productosFiltrados = useMemo(() => {
    let items = [...recomendaciones];

    switch (filtroActivo) {
      case 'clima': {
        const condicion = getCondicionClima(climaActual);
        if (condicion && PRODUCTOS_MOCK.length > 0) {
          items = items.filter(p => p.etiquetasInteligentes?.includes(condicion));
          if (items.length === 0) items = [...recomendaciones];
        }
        break;
      }
      case 'cercanos':
        items.sort((a, b) => (a.distancia || 0) - (b.distancia || 0));
        break;
      case 'ofertas':
        items = items.filter(p => {
          const ant = p.precioAnterior || p.precioOriginal;
          return ant && ant > p.precio;
        });
        break;
    }
    return items;
  }, [recomendaciones, filtroActivo, climaActual]);

  const condicionClima = climaActual ? getCondicionClima(climaActual) : null;
  const weatherCfg = condicionClima ? WEATHER_CONFIG[condicionClima] : null;
  const WeatherIcon = weatherCfg?.icon || Cloud;

  const filtros = [
    { id: 'todos', label: 'Todos' },
    { id: 'clima', label: weatherCfg ? `Según Clima (${weatherCfg.label})` : 'Según Clima' },
    { id: 'cercanos', label: 'Más Cercanos' },
    { id: 'ofertas', label: 'Ofertas' }
  ];

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="animate-pulse space-y-4">
          <div className="h-8 bg-gray-200 rounded w-1/3"></div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {[1, 2, 3, 4, 5, 6].map(i => (
              <div key={i} className="h-80 bg-gray-200 rounded-lg"></div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="bg-red-50 border border-red-200 rounded-lg p-4">
          <p className="text-red-800">{error}</p>
          <button onClick={handleRefresh} className="mt-2 text-red-600 hover:text-red-800 flex items-center">
            <RefreshCw className="h-4 w-4 mr-1" />
            Reintentar
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-6">
        <div className="flex items-center justify-between mb-4">
          <div>
            <h2 className="text-2xl font-bold text-gray-900 flex items-center">
              <Sparkles className="h-6 w-6 mr-2 text-primary-500" />
              Recomendaciones para ti
            </h2>
            {ubicacionUsuario && (
              <p className="text-sm text-gray-600 mt-1 flex items-center">
                <MapPin className="h-4 w-4 mr-1" />
                Basado en tu ubicación: {ubicacionUsuario.ciudad}
              </p>
            )}
          </div>
          <button
            onClick={handleRefresh}
            className="p-2 rounded-full hover:bg-gray-100 transition-colors"
            title="Actualizar recomendaciones"
          >
            <RefreshCw className="h-5 w-5 text-gray-600" />
          </button>
        </div>

        {climaActual && weatherCfg && (
          <div className={`mb-4 inline-flex items-center gap-2 px-3 py-1.5 rounded-full text-sm font-medium text-white ${weatherCfg.color}`}>
            <WeatherIcon className="h-4 w-4" />
            {climaActual.temperatura.toFixed(0)}°C - {climaActual.condicion}
          </div>
        )}

        <div className="flex flex-wrap gap-2">
          {filtros.map(filtro => (
            <button
              key={filtro.id}
              onClick={() => setFiltroActivo(filtro.id)}
              className={`px-4 py-2 rounded-full text-sm font-medium transition-colors ${
                filtroActivo === filtro.id
                  ? 'bg-primary-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              {filtro.label}
            </button>
          ))}
        </div>

        {filtroActivo === 'clima' && condicionClima && (
          <p className="mt-2 text-sm text-gray-500">
            Mostrando productos recomendados para clima {WEATHER_CONFIG[condicionClima]?.label?.toLowerCase() || condicionClima}
          </p>
        )}
      </div>

      {productosFiltrados.length > 0 ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {productosFiltrados.map(producto => (
            <ProductCard key={producto.id} producto={producto}
              onNavigate={onNavigate} onReservar={onReservar} onMostrarMensaje={onMostrarMensaje} />
          ))}
        </div>
      ) : (
        <div className="text-center py-12">
          <Sparkles className="h-12 w-12 text-gray-400 mx-auto mb-4" />
          <p className="text-gray-600">No hay productos para este filtro</p>
        </div>
      )}
    </div>
  );
};

export default FeedRecomendaciones;
