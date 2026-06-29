import React, { useState, useEffect } from 'react';
import { ShoppingCart, Trash2, Minus, Plus, ArrowLeft, CreditCard, Loader2 } from 'lucide-react';
import carritoService from '../services/carritoService';

const Carrito = ({ onNavigate }) => {
  const [carrito, setCarrito] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const cargarCarrito = async () => {
    setLoading(true);
    try {
      const response = await carritoService.obtener();
      if (response.exito && response.datos) {
        setCarrito(response.datos);
      } else {
        setError(response.mensaje || 'Error al cargar carrito');
      }
    } catch (err) {
      setError('Error al cargar carrito');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { cargarCarrito(); }, []);

  const handleActualizarCantidad = async (itemId, nuevaCantidad) => {
    if (nuevaCantidad < 1) {
      await handleEliminar(itemId);
      return;
    }
    const response = await carritoService.actualizarItem(itemId, nuevaCantidad);
    if (response.exito) cargarCarrito();
  };

  const handleEliminar = async (itemId) => {
    const response = await carritoService.eliminarItem(itemId);
    if (response.exito) cargarCarrito();
  };

  const handleLimpiar = async () => {
    if (!window.confirm('¿Vaciar carrito completamente?')) return;
    await carritoService.limpiar();
    cargarCarrito();
  };

  const irAPagar = () => {
    onNavigate('checkout');
  };

  if (loading) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-8">
        <div className="flex items-center justify-center py-20">
          <Loader2 className="h-8 w-8 text-primary-500 animate-spin" />
          <span className="ml-3 text-gray-600">Cargando carrito...</span>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 py-6 animate-fade-in">
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center gap-3">
          <ShoppingCart className="h-7 w-7 text-primary-600" />
          <h2 className="text-2xl font-bold text-gray-900">Mi Carrito</h2>
        </div>
        <button
          onClick={() => onNavigate('inicio')}
          className="flex items-center gap-1.5 text-sm text-gray-600 hover:text-primary-600"
        >
          <ArrowLeft className="h-4 w-4" />
          Seguir comprando
        </button>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 rounded-xl p-4 mb-4 text-red-700 text-sm">
          {error}
        </div>
      )}

      {!carrito || !carrito.items || carrito.items.length === 0 ? (
        <div className="text-center py-16 bg-white rounded-2xl shadow-sm border border-gray-100">
          <ShoppingCart className="h-16 w-16 text-gray-300 mx-auto mb-4" />
          <p className="text-gray-600 text-lg mb-2">Tu carrito está vacío</p>
          <p className="text-gray-400 text-sm mb-6">Agrega productos para empezar a comprar</p>
          <button
            onClick={() => onNavigate('inicio')}
            className="px-6 py-2.5 bg-primary-600 text-white rounded-xl font-medium hover:bg-primary-700 transition-colors"
          >
            Ver productos
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-4">
            {carrito.items.map((item) => (
              <div key={item.id} className="bg-white rounded-xl shadow-sm border border-gray-100 p-4 flex gap-4">
                <img
                  src={item.urlImagen || '/uploads/productos/sin-imagen.svg'}
                  alt={item.nombreProducto}
                  className="w-24 h-24 object-cover rounded-lg"
                  loading="lazy"
                />
                <div className="flex-1 min-w-0">
                  <h3 className="font-semibold text-gray-900 truncate">{item.nombreProducto}</h3>
                  <p className="text-sm text-gray-500">{item.nombreComercio}</p>
                  <p className="text-lg font-bold text-primary-600 mt-1">
                    Bs. {item.precioUnitario.toFixed(2)}
                  </p>

                  <div className="flex items-center justify-between mt-3">
                    <div className="flex items-center border border-gray-200 rounded-lg">
                      <button
                        onClick={() => handleActualizarCantidad(item.id, item.cantidad - 1)}
                        className="p-2 hover:bg-gray-50 rounded-l-lg"
                      >
                        <Minus className="h-4 w-4 text-gray-600" />
                      </button>
                      <span className="px-4 py-2 text-sm font-medium text-gray-900 border-x border-gray-200">
                        {item.cantidad}
                      </span>
                      <button
                        onClick={() => handleActualizarCantidad(item.id, item.cantidad + 1)}
                        className="p-2 hover:bg-gray-50 rounded-r-lg"
                        disabled={item.cantidad >= item.stockDisponible}
                      >
                        <Plus className="h-4 w-4 text-gray-600" />
                      </button>
                    </div>

                    <div className="flex items-center gap-3">
                      <p className="font-bold text-gray-900">
                        Bs. {item.subtotal.toFixed(2)}
                      </p>
                      <button
                        onClick={() => handleEliminar(item.id)}
                        className="p-2 text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-lg transition-colors"
                        title="Eliminar"
                      >
                        <Trash2 className="h-4 w-4" />
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ))}

            <div className="flex justify-end">
              <button
                onClick={handleLimpiar}
                className="text-sm text-gray-500 hover:text-red-600 transition-colors"
              >
                Vaciar carrito
              </button>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 h-fit sticky top-20">
            <h3 className="text-lg font-bold text-gray-900 mb-4">Resumen</h3>

            <div className="space-y-3 text-sm">
              <div className="flex justify-between text-gray-600">
                <span>Productos ({carrito.totalItems})</span>
                <span>Bs. {carrito.subtotal.toFixed(2)}</span>
              </div>
              <div className="border-t border-gray-100 pt-3 flex justify-between font-bold text-lg text-gray-900">
                <span>Total</span>
                <span>Bs. {carrito.subtotal.toFixed(2)}</span>
              </div>
            </div>

            <button
              onClick={irAPagar}
              className="w-full mt-6 flex items-center justify-center gap-2 px-6 py-3 bg-primary-600 text-white rounded-xl font-semibold hover:bg-primary-700 transition-colors"
            >
              <CreditCard className="h-5 w-5" />
              Proceder al pago
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Carrito;
