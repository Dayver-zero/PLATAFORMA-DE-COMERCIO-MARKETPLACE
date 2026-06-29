import React, { useState, useEffect } from 'react';
import { Calendar, CheckCircle, XCircle, Clock, Loader2 } from 'lucide-react';
import reservasService from '../services/reservasService';
import authService from '../services/authService';

const ESTADO_CONFIG = {
  PENDIENTE: { color: 'bg-yellow-100 text-yellow-800', icon: Clock },
  CONFIRMADA: { color: 'bg-blue-100 text-blue-800', icon: CheckCircle },
  COMPLETADA: { color: 'bg-green-100 text-green-800', icon: CheckCircle },
  CANCELADA: { color: 'bg-gray-100 text-gray-500', icon: XCircle },
};

const GestionReservas = ({ onNavigate }) => {
  const [reservas, setReservas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [procesando, setProcesando] = useState(null);

  const cargar = async () => {
    setLoading(true);
    const response = await reservasService.listarComercio();
    if (response.exito) setReservas(response.datos || []);
    setLoading(false);
  };

  useEffect(() => { cargar(); }, []);

  const handleCompletar = async (id) => {
    if (!window.confirm('¿Marcar esta reserva como retirada/entregada?')) return;
    setProcesando(id);
    await reservasService.completar(id);
    setProcesando(null);
    cargar();
  };

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
    <div className="max-w-5xl mx-auto px-4 sm:px-6 py-6 animate-fade-in">
      <div className="flex items-center gap-3 mb-6">
        <Calendar className="h-7 w-7 text-primary-600" />
        <h2 className="text-2xl font-bold text-gray-900">Gestión de Reservas</h2>
      </div>

      {reservas.length === 0 ? (
        <div className="text-center py-16 bg-white rounded-2xl shadow-sm border border-gray-100">
          <Calendar className="h-16 w-16 text-gray-300 mx-auto mb-4" />
          <p className="text-gray-600 text-lg">No tienes reservas recibidas</p>
        </div>
      ) : (
        <div className="space-y-4">
          {reservas.map((reserva) => {
            const EstIcon = ESTADO_CONFIG[reserva.estado]?.icon || Clock;
            const estColor = ESTADO_CONFIG[reserva.estado]?.color || 'bg-gray-100 text-gray-800';

            return (
              <div key={reserva.id} className="bg-white rounded-xl shadow-sm border border-gray-100 p-4 sm:p-6">
                <div className="flex gap-4">
                  <img src={reserva.urlImagen || '/uploads/productos/sin-imagen.svg'} alt={reserva.nombreProducto}
                    className="w-16 h-16 object-cover rounded-lg" />
                  <div className="flex-1 min-w-0">
                    <div className="flex items-start justify-between gap-4">
                      <div>
                        <div className="flex items-center gap-2 mb-1">
                          <h3 className="font-semibold text-gray-900">{reserva.nombreProducto}</h3>
                          <span className={`inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium ${estColor}`}>
                            <EstIcon className="h-3 w-3" />
                            {reserva.estado}
                          </span>
                        </div>
                        <p className="text-sm text-gray-500">
                          Cliente: <span className="font-medium">{reserva.nombreUsuario}</span>
                        </p>
                        <div className="flex gap-4 mt-1 text-sm text-gray-600">
                          <span>Cantidad: {reserva.cantidad}</span>
                          <span>
                            {new Date(reserva.fechaReserva).toLocaleDateString('es-BO', {
                              day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit'
                            })}
                          </span>
                        </div>
                        {reserva.notas && <p className="text-xs text-gray-400 mt-1">Notas: {reserva.notas}</p>}
                      </div>

                      {reserva.estado === 'PENDIENTE' && (
                        <div className="flex gap-2 shrink-0">
                          <button
                            onClick={() => handleCompletar(reserva.id)}
                            disabled={procesando === reserva.id}
                            className="flex items-center gap-1 px-3 py-1.5 bg-green-600 text-white rounded-lg text-xs font-medium hover:bg-green-700 disabled:bg-gray-300"
                          >
                            {procesando === reserva.id ? <Loader2 className="h-3 w-3 animate-spin" /> : <CheckCircle className="h-3 w-3" />}
                            Retirado
                          </button>
                          <button
                            onClick={() => handleCancelar(reserva.id)}
                            disabled={procesando === reserva.id}
                            className="flex items-center gap-1 px-3 py-1.5 bg-red-100 text-red-700 rounded-lg text-xs font-medium hover:bg-red-200 disabled:bg-gray-100 disabled:text-gray-400"
                          >
                            <XCircle className="h-3 w-3" />
                            Cancelar
                          </button>
                        </div>
                      )}
                    </div>
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

export default GestionReservas;
