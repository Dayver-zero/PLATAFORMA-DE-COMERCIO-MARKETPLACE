import React, { useState } from 'react';
import { X, Calendar, Loader2, CheckCircle } from 'lucide-react';
import reservasService from '../services/reservasService';

const ModalReserva = ({ producto, onClose, onNavigate }) => {
  const [cantidad, setCantidad] = useState(1);
  const [notas, setNotas] = useState('');
  const [procesando, setProcesando] = useState(false);
  const [resultado, setResultado] = useState(null);
  const [error, setError] = useState(null);

  const handleReservar = async () => {
    setProcesando(true);
    setError(null);
    try {
      const response = await reservasService.crear(producto.id, cantidad, notas);
      if (response.exito && response.datos) {
        setResultado(response.datos);
      } else {
        setError(response.mensaje || 'Error al crear reserva');
      }
    } catch (err) {
      setError('Error al crear reserva');
    } finally {
      setProcesando(false);
    }
  };

  if (resultado) {
    return (
      <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
        <div className="bg-white rounded-2xl shadow-xl p-6 max-w-md w-full animate-fade-in text-center">
          <CheckCircle className="h-16 w-16 text-green-500 mx-auto mb-4" />
          <h3 className="text-xl font-bold text-gray-900 mb-2">Reserva Confirmada</h3>
          <p className="text-gray-600 mb-1">
            Reservaste {resultado.cantidad}x {resultado.nombreProducto}
          </p>
          <p className="text-sm text-gray-500 mb-4">
            En {resultado.nombreComercio}
          </p>
          <div className="bg-yellow-50 border border-yellow-200 rounded-xl p-4 mb-6">
            <p className="text-sm text-yellow-700">
              <Calendar className="h-4 w-4 inline mr-1" />
              Tu reserva expira el{' '}
              {new Date(resultado.fechaExpiracion).toLocaleDateString('es-BO', {
                year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'
              })}
            </p>
          </div>
          <div className="flex gap-3 justify-center">
            <button onClick={onClose} className="px-4 py-2 bg-gray-100 text-gray-700 rounded-xl font-medium hover:bg-gray-200">
              Cerrar
            </button>
            <button
              onClick={() => { onClose(); onNavigate && onNavigate('reservas'); }}
              className="px-4 py-2 bg-primary-600 text-white rounded-xl font-medium hover:bg-primary-700"
            >
              Mis Reservas
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-2xl shadow-xl p-6 max-w-md w-full animate-fade-in">
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-xl font-bold text-gray-900">Reservar Producto</h3>
          <button onClick={onClose} className="p-2 hover:bg-gray-100 rounded-lg">
            <X className="h-5 w-5 text-gray-500" />
          </button>
        </div>

        <div className="flex gap-4 mb-6">
          <img
            src={producto.urlImagen || 'https://via.placeholder.com/80'}
            alt={producto.nombre}
            className="w-20 h-20 object-cover rounded-lg"
          />
          <div>
            <h4 className="font-semibold text-gray-900">{producto.nombre}</h4>
            <p className="text-sm text-gray-500">{producto.nombreComercio}</p>
            <p className="text-lg font-bold text-primary-600">Bs. {producto.precio.toFixed(2)}</p>
          </div>
        </div>

        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-3 mb-4 text-sm text-red-700">
            {error}
          </div>
        )}

        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-1">Cantidad</label>
          <div className="flex items-center border border-gray-200 rounded-lg w-fit">
            <button
              onClick={() => setCantidad(Math.max(1, cantidad - 1))}
              className="p-2 hover:bg-gray-50 rounded-l-lg"
            >
              -
            </button>
            <span className="px-6 py-2 text-center font-medium min-w-[60px]">{cantidad}</span>
            <button
              onClick={() => setCantidad(Math.min(producto.stock || 99, cantidad + 1))}
              className="p-2 hover:bg-gray-50 rounded-r-lg"
            >
              +
            </button>
          </div>
          {producto.stock <= 5 && (
            <p className="text-xs text-orange-600 mt-1">Solo {producto.stock} disponibles</p>
          )}
        </div>

        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-700 mb-1">Notas (opcional)</label>
          <textarea
            value={notas}
            onChange={(e) => setNotas(e.target.value)}
            placeholder="Ej: Prefiero retirar por la tarde..."
            className="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:ring-2 focus:ring-primary-500 focus:border-transparent resize-none"
            rows={3}
          />
        </div>

        <div className="bg-blue-50 border border-blue-100 rounded-xl p-3 mb-6">
          <p className="text-xs text-blue-700">
            <Calendar className="h-3.5 w-3.5 inline mr-1" />
            La reserva tiene una vigencia de 24 horas. Después de ese tiempo, se libera automáticamente.
          </p>
        </div>

        <button
          onClick={handleReservar}
          disabled={procesando}
          className="w-full flex items-center justify-center gap-2 px-6 py-3 bg-primary-600 text-white rounded-xl font-semibold hover:bg-primary-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
        >
          {procesando ? (
            <>
              <Loader2 className="h-5 w-5 animate-spin" />
              Reservando...
            </>
          ) : (
            <>
              <Calendar className="h-5 w-5" />
              Confirmar Reserva
            </>
          )}
        </button>
      </div>
    </div>
  );
};

export default ModalReserva;
