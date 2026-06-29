import React, { useState } from 'react';
import { Building2, Star, Heart, ShoppingCart, Calendar, Smartphone, Check, Loader2 } from 'lucide-react';
import carritoService from '../services/carritoService';

const etiquetaConfig = {
  lluvia: { emoji: String.fromCodePoint(0x1F327, 0xFE0F), color: 'bg-blue-100 text-blue-800' },
  frio: { emoji: String.fromCodePoint(0x2744, 0xFE0F), color: 'bg-cyan-100 text-cyan-800' },
  calor: { emoji: String.fromCodePoint(0x2600, 0xFE0F), color: 'bg-orange-100 text-orange-800' },
  proteccion: { emoji: String.fromCodePoint(0x1F6E1, 0xFE0F), color: 'bg-green-100 text-green-800' },
  abrigo: { emoji: String.fromCodePoint(0x1F9E5), color: 'bg-purple-100 text-purple-800' },
  calzado: { emoji: String.fromCodePoint(0x1F45F), color: 'bg-indigo-100 text-indigo-800' },
  oficina: { emoji: String.fromCodePoint(0x1F4BC), color: 'bg-gray-100 text-gray-800' },
  casa: { emoji: String.fromCodePoint(0x1F3E0), color: 'bg-yellow-100 text-yellow-800' },
  deporte: { emoji: String.fromCodePoint(0x26BD), color: 'bg-red-100 text-red-800' }
};

const etiquetaKeys = {
  'lluvia': 'Lluvia', 'frio': 'Frio', 'calor': 'Calor',
  'proteccion': 'Proteccion', 'abrigo': 'Abrigo', 'calzado': 'Calzado',
  'oficina': 'Oficina', 'casa': 'Casa', 'deporte': 'Deporte'
};

const ProductCard = ({ producto, onNavigate, onReservar, onMostrarMensaje }) => {
  const [agregando, setAgregando] = useState(false);
  const [agregadoMsg, setAgregadoMsg] = useState(false);
  const precioAnterior = producto.precioAnterior || producto.precioOriginal;

  const handleAgregarAlCarrito = async () => {
    setAgregando(true);
    try {
      const response = await carritoService.agregarItem(producto.id, 1);
      if (response.exito) {
        setAgregadoMsg(true);
        setTimeout(() => setAgregadoMsg(false), 2000);
        if (onMostrarMensaje) onMostrarMensaje('Producto agregado al carrito');
      }
    } catch (e) {
      // fallback
    } finally {
      setAgregando(false);
    }
  };

  const handleComprarYape = async () => {
    setAgregando(true);
    try {
      await carritoService.agregarItem(producto.id, 1);
      if (onNavigate) onNavigate('checkout', 'YAPE');
    } catch (e) {
      // fallback
    } finally {
      setAgregando(false);
    }
  };

  const renderEtiquetas = () => {
    if (!producto.etiquetasInteligentes || producto.etiquetasInteligentes.length === 0) return null;
    return (
      <div className="flex flex-wrap gap-1 mt-2">
        {producto.etiquetasInteligentes.map((etiqueta, index) => {
          const key = etiqueta.normalize('NFD').replace(/[\u0300-\u036f]/g, '');
          const config = etiquetaConfig[key] || { emoji: String.fromCodePoint(0x1F3F7, 0xFE0F), color: 'bg-gray-100 text-gray-800' };
          const label = etiquetaKeys[key] || (etiqueta.charAt(0).toUpperCase() + etiqueta.slice(1));
          return (
            <span key={index} className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-medium ${config.color}`}>
              <span className="mr-1">{config.emoji}</span>
              {label}
            </span>
          );
        })}
      </div>
    );
  };

  const renderCalificacion = () => {
    const cal = producto.calificacionPromedio || producto.calificacion;
    if (!cal) return null;
    return (
      <div className="flex items-center">
        <Star className="h-4 w-4 text-yellow-400 fill-current" />
        <span className="ml-1 text-sm font-medium text-gray-700">{cal.toFixed(1)}</span>
      </div>
    );
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
      <div className="relative">
        <img src={producto.urlImagen || producto.imagenUrl} alt={producto.nombre}
          className="w-full h-48 object-cover" loading="lazy" />
        <button onClick={() => console.log('Favorito:', producto.nombre)}
          className="absolute top-2 right-2 p-2 bg-white rounded-full shadow-md hover:bg-gray-100 transition-colors" title="Agregar a favoritos">
          <Heart className="h-5 w-5 text-gray-400 hover:text-red-500" />
        </button>
        {producto.stock <= 5 && producto.stock > 0 && (
          <div className="absolute top-2 left-2 bg-orange-500 text-white text-xs px-2 py-1 rounded-full">
            Ultimos {producto.stock}!
          </div>
        )}
        {producto.stock === 0 && (
          <div className="absolute top-2 left-2 bg-red-500 text-white text-xs px-2 py-1 rounded-full">Agotado</div>
        )}
        {producto.permiteReserva && (
          <div className="absolute top-2 right-12 bg-purple-500 text-white text-xs px-2 py-1 rounded-full">
            Reserva
          </div>
        )}
        {producto.permitePagoAdelantado && (
          <div className="absolute top-2 right-24 bg-yellow-500 text-white text-xs px-2 py-1 rounded-full">
            Yape
          </div>
        )}
      </div>

      {precioAnterior && precioAnterior > producto.precio && !(producto.stock <= 5) && (
        <div className="absolute top-2 left-2 bg-red-500 text-white text-xs px-2 py-1 rounded-full font-bold">
          -{Math.round((1 - producto.precio / precioAnterior) * 100)}%
        </div>
      )}

      <div className="p-4">
        <h3 className="text-lg font-semibold text-gray-900 mb-1 line-clamp-2">{producto.nombre}</h3>
        <p className="text-sm text-gray-600 mb-2 line-clamp-2">{producto.descripcion}</p>
        {renderEtiquetas()}

        <div className="mt-3 pt-3 border-t border-gray-200">
          <div className="flex items-center justify-between">
            <div className="flex items-center text-sm text-gray-600">
              <Building2 className="h-4 w-4 mr-1 text-primary-500" />
              <span className="font-medium">{producto.nombreComercio}</span>
            </div>
            {renderCalificacion()}
          </div>
          {producto.distancia && (
            <p className="text-xs text-gray-500 mt-1">{producto.distancia} km de distancia</p>
          )}
        </div>

        <div className="mt-4">
          <div className="flex items-center justify-between mb-3">
            <div>
              {precioAnterior && precioAnterior > producto.precio && (
                <p className="text-xs text-gray-400 line-through">Bs. {precioAnterior.toFixed(2)}</p>
              )}
              <p className="text-2xl font-bold text-primary-600">Bs. {producto.precio.toFixed(2)}</p>
            </div>
          </div>

          <div className="flex flex-col gap-2">
            <button
              onClick={handleAgregarAlCarrito}
              disabled={producto.stock === 0 || agregando}
              className={`w-full flex items-center justify-center px-4 py-2 rounded-lg font-medium transition-colors ${
                producto.stock === 0
                  ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                  : agregadoMsg
                  ? 'bg-green-600 text-white'
                  : 'bg-primary-600 text-white hover:bg-primary-700'
              }`}
            >
              {agregando ? (
                <Loader2 className="h-5 w-5 mr-2 animate-spin" />
              ) : agregadoMsg ? (
                <Check className="h-5 w-5 mr-2" />
              ) : (
                <ShoppingCart className="h-5 w-5 mr-2" />
              )}
              {agregadoMsg ? 'Agregado' : producto.stock === 0 ? 'Agotado' : 'Agregar al carrito'}
            </button>

            {producto.stock > 0 && (
              <div className="flex gap-2">
                {producto.permiteReserva && (
                  <button
                    onClick={() => onReservar && onReservar(producto)}
                    className="flex-1 flex items-center justify-center gap-1.5 px-3 py-2 bg-purple-100 text-purple-700 rounded-lg text-sm font-medium hover:bg-purple-200 transition-colors"
                  >
                    <Calendar className="h-4 w-4" />
                    Reservar
                  </button>
                )}
                {producto.permitePagoAdelantado && (
                  <button
                    onClick={handleComprarYape}
                    disabled={agregando}
                    className="flex-1 flex items-center justify-center gap-1.5 px-3 py-2 bg-yellow-500 text-white rounded-lg text-sm font-medium hover:bg-yellow-600 transition-colors"
                  >
                    <Smartphone className="h-4 w-4" />
                    Yape
                  </button>
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductCard;
