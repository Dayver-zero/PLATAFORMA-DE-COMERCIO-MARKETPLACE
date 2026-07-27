import React, { useState, useEffect, useCallback, useMemo } from 'react';
import { Sparkles, MapPin, Filter, RefreshCw, Cloud, Sun, Snowflake, Umbrella } from 'lucide-react';
import ProductCard from './ProductCard';
import climaService from '../services/climaService';
import recomendacionesService from '../services/recomendacionesService';
import productosService from '../services/productosService';

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

  const fetchClima = useCallback(async (lat, lng) => {
    try {
      const clima = await climaService.obtenerClimaPorCoordenadas(lat, lng);
      setClimaActual(clima);
    } catch {
      setClimaActual(null);
    }
  }, []);

  const cargarProductosPublicos = useCallback(async () => {
    const res = await productosService.obtenerTodos();
    if (res?.exito && res.datos?.length > 0) {
      setRecomendaciones(res.datos);
    } else {
      setRecomendaciones([]);
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

      const usuario = JSON.parse(localStorage.getItem('usuario'));
      const usuarioId = usuario?.id;

      if (usuarioId) {
        try {
          const data = await recomendacionesService.obtenerRecomendaciones(usuarioId, ubicacion.latitud, ubicacion.longitud);
          if (data && data.length > 0) {
            setRecomendaciones(data);
            await fetchClima(ubicacion.latitud, ubicacion.longitud);
            return;
          }
        } catch {
          // fallback a productos públicos
        }
      }

      await cargarProductosPublicos();
      await fetchClima(ubicacion.latitud, ubicacion.longitud);
    } catch (err) {
      setError('Error al cargar recomendaciones');
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  }, [fetchClima, cargarProductosPublicos]);

  useEffect(() => { fetchRecomendaciones(); }, [fetchRecomendaciones]);

  const handleRefresh = () => { fetchRecomendaciones(); };

  const productosFiltrados = useMemo(() => {
    let items = [...recomendaciones];

    switch (filtroActivo) {
      case 'clima': {
        const condicion = getCondicionClima(climaActual);
        if (condicion && items.length > 0) {
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
          <p className="text-gray-600">No hay productos disponibles</p>
        </div>
      )}
    </div>
  );
};

export default FeedRecomendaciones;
