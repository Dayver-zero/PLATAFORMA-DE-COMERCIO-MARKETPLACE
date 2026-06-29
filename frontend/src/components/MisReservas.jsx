import React, { useState, useEffect } from 'react';
import { Calendar, XCircle, CheckCircle, Clock, Loader2 } from 'lucide-react';
import reservasService from '../services/reservasService';

const ESTADO_CONFIG = {
  PENDIENTE: { color: 'bg-yellow-100 text-yellow-800', icon: Clock },
  CONFIRMADA: { color: 'bg-blue-100 text-blue-800', icon: CheckCircle },
  COMPLETADA: { color: 'bg-green-100 text-green-800', icon: CheckCircle },
  CANCELADA: { color: 'bg-gray-100 text-gray-500', icon: XCircle },
};

const MisReservas = ({ onNavigate }) => {
  const [reservas, setReservas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [procesando, setProcesando] = useState(null);

  const cargar = async () => {
    setLoading(true);
    const response = await reservasService.listar();
    if (response.exito) setReservas(response.datos || []);
    setLoading(false);
  };

  useEffect(() => { cargar(); }, []);

  const handleCancelar = async (id) => {
    if (!window.confirm('¿Cancelar esta reserva?')) return;
    setProcesando(id);
    await reservasService.cancelar(id);
    setProcesando(null);
    cargar();
  };

  if (loading) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-8">
        <div className="flex items-center justify-center py-20">
          <Loader2 className="h-8 w-8 text-primary-500 animate-spin" />
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 py-6 animate-fade-in">
      <div className="flex items-center gap-3 mb-6">
        <Calendar className="h-7 w-7 text-primary-600" />
        <h2 className="text-2xl font-bold text-gray-900">Mis Reservas</h2>
      </div>

      {reservas.length === 0 ? (
        <div className="text-center py-16 bg-white rounded-2xl shadow-sm border border-gray-100">
          <Calendar className="h-16 w-16 text-gray-300 mx-auto mb-4" />
          <p className="text-gray-600 text-lg mb-2">No tienes reservas activas</p>
          <button onClick={() => onNavigate('inicio')}
            className="mt-4 px-6 py-2.5 bg-primary-600 text-white rounded-xl font-medium hover:bg-primary-700">
            Ver productos
          </button>
        </div>
      ) : (
        <div className="space-y-4">
          {reservas.map((reserva) => {
            const EstIcon = ESTADO_CONFIG[reserva.estado]?.icon || Clock;
            const estColor = ESTADO_CONFIG[reserva.estado]?.color || 'bg-gray-100 text-gray-800';
            const expirado = reserva.estado === 'PENDIENTE' && new Date(reserva.fechaExpiracion) < new Date();

            return (
              <div key={reserva.id} className="bg-white rounded-xl shadow-sm border border-gray-100 p-4 sm:p-6">
                <div className="flex gap-4">
                  <img src={reserva.urlImagen || 'https://via.placeholder.com/80'} alt={reserva.nombreProducto}
                    className="w-20 h-20 object-cover rounded-lg" />
                  <div className="flex-1 min-w-0">
                    <div className="flex items-start justify-between">
                      <div>
                        <h3 className="font-semibold text-gray-900">{reserva.nombreProducto}</h3>
                        <p className="text-sm text-gray-500">{reserva.nombreComercio}</p>
                      </div>
                      <span className={`inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium ${estColor}`}>
                        <EstIcon className="h-3.5 w-3.5" />
                        {reserva.estado}
                      </span>
                    </div>

                    <div className="mt-2 flex items-center gap-4 text-sm text-gray-600">
                      <span>Cantidad: {reserva.cantidad}</span>
                      {reserva.fechaExpiracion && (
                        <span className={`flex items-center gap-1 ${expirado ? 'text-red-600' : ''}`}>
                          <Clock className="h-3.5 w-3.5" />
                          Expira: {new Date(reserva.fechaExpiracion).toLocaleDateString('es-BO', {
                            day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit'
                          })}
                          {expirado && ' (Vencida)'}
                        </span>
                      )}
                    </div>

                    {reserva.notas && (
                      <p className="text-xs text-gray-400 mt-1">Notas: {reserva.notas}</p>
                    )}

                    {reserva.estado === 'PENDIENTE' && !expirado && (
                      <button
                        onClick={() => handleCancelar(reserva.id)}
                        disabled={procesando === reserva.id}
                        className="mt-3 flex items-center gap-1.5 text-sm text-red-600 hover:text-red-700 font-medium"
                      >
                        {procesando === reserva.id ? (
                          <Loader2 className="h-4 w-4 animate-spin" />
                        ) : (
                          <XCircle className="h-4 w-4" />
                        )}
                        Cancelar reserva
                      </button>
                    )}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default MisReservas;
