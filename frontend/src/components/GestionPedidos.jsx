import React, { useState, useEffect } from 'react';
import { Package, Clock, CheckCircle, XCircle, Truck, CheckSquare, Loader2 } from 'lucide-react';
import pedidosService from '../services/pedidosService';
import authService from '../services/authService';

const ESTADOS = ['PENDIENTE', 'CONFIRMADO', 'ENVIADO', 'ENTREGADO', 'CANCELADO'];
const SIGUIENTE_ESTADO = {
  PENDIENTE: 'CONFIRMADO',
  CONFIRMADO: 'ENVIADO',
  ENVIADO: 'ENTREGADO',
};

const ESTADO_CONFIG = {
  PENDIENTE: { color: 'bg-yellow-100 text-yellow-800', icon: Clock },
  CONFIRMADO: { color: 'bg-blue-100 text-blue-800', icon: CheckCircle },
  ENVIADO: { color: 'bg-indigo-100 text-indigo-800', icon: Truck },
  ENTREGADO: { color: 'bg-green-100 text-green-800', icon: CheckSquare },
  CANCELADO: { color: 'bg-red-100 text-red-800', icon: XCircle },
};

const GestionPedidos = ({ onNavigate }) => {
  const [pedidos, setPedidos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [procesando, setProcesando] = useState(null);
  const [usuario, setUsuario] = useState(null);

  useEffect(() => {
    const user = authService.obtenerUsuarioActual();
    setUsuario(user);
    cargar();
  }, []);

  const cargar = async () => {
    setLoading(true);
    const response = await pedidosService.listarComercio();
    if (response.exito) setPedidos(response.datos || []);
    setLoading(false);
  };

  const handleCambiarEstado = async (id, nuevoEstado) => {
    setProcesando(id);
    await pedidosService.cambiarEstado(id, nuevoEstado);
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
        <Package className="h-7 w-7 text-primary-600" />
        <h2 className="text-2xl font-bold text-gray-900">Gestión de Pedidos</h2>
      </div>

      {pedidos.length === 0 ? (
        <div className="text-center py-16 bg-white rounded-2xl shadow-sm border border-gray-100">
          <Package className="h-16 w-16 text-gray-300 mx-auto mb-4" />
          <p className="text-gray-600 text-lg">No tienes pedidos recibidos</p>
        </div>
      ) : (
        <div className="space-y-4">
          {pedidos.map((pedido) => {
            const EstIcon = ESTADO_CONFIG[pedido.estado]?.icon || Package;
            const estColor = ESTADO_CONFIG[pedido.estado]?.color || 'bg-gray-100 text-gray-800';
            const proxEstado = SIGUIENTE_ESTADO[pedido.estado];
            const esYape = pedido.metodoPago === 'YAPE';

            return (
              <div key={pedido.id} className="bg-white rounded-xl shadow-sm border border-gray-100 p-4 sm:p-6">
                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2 mb-1">
                      <span className="text-sm text-gray-400">Pedido #{pedido.id}</span>
                      <span className={`inline-flex items-center gap-1 px-3 py-0.5 rounded-full text-xs font-medium ${estColor}`}>
                        <EstIcon className="h-3 w-3" />
                        {pedido.estado}
                      </span>
                    </div>
                    <p className="text-sm text-gray-600">
                      Cliente: <span className="font-medium">{pedido.nombreUsuario}</span>
                    </p>
                    <p className="text-sm text-gray-500">
                      {new Date(pedido.fechaCreacion).toLocaleDateString('es-BO', {
                        day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit'
                      })}
                    </p>

                    <div className="flex flex-wrap gap-4 mt-2 text-sm">
                      {pedido.items?.map((item) => (
                        <span key={item.id} className="text-gray-600">
                          {item.nombreProducto} x{item.cantidad}
                        </span>
                      ))}
                    </div>
                  </div>

                  <div className="text-right shrink-0">
                    <p className="text-xl font-bold text-gray-900">Bs. {pedido.total.toFixed(2)}</p>
                    <p className="text-xs text-gray-500">{pedido.metodoPago}</p>

                    {esYape && (
                      <div className="mt-2">
                        {pedido.referenciaPago ? (
                          <div className="bg-green-50 border border-green-200 rounded-lg px-3 py-2 text-xs">
                            <p className="text-green-700 font-medium">Pago Yape recibido</p>
                            <p className="text-green-600">Ref: {pedido.referenciaPago}</p>
                          </div>
                        ) : pedido.codigoPago ? (
                          <div className="bg-yellow-50 border border-yellow-200 rounded-lg px-3 py-2 text-xs">
                            <p className="text-yellow-700">Código Yape: {pedido.codigoPago}</p>
                            <p className="text-yellow-600">Esperando pago...</p>
                          </div>
                        ) : null}
                      </div>
                    )}
                  </div>
                </div>

                {proxEstado && (
                  <div className="mt-4 border-t border-gray-100 pt-4 flex gap-2">
                    <button
                      onClick={() => handleCambiarEstado(pedido.id, proxEstado)}
                      disabled={procesando === pedido.id}
                      className={`flex items-center gap-1.5 px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                        proxEstado === 'CONFIRMADO'
                          ? 'bg-blue-600 text-white hover:bg-blue-700'
                          : proxEstado === 'ENVIADO'
                          ? 'bg-indigo-600 text-white hover:bg-indigo-700'
                          : 'bg-green-600 text-white hover:bg-green-700'
                      } disabled:bg-gray-300 disabled:cursor-not-allowed`}
                    >
                      {procesando === pedido.id ? (
                        <Loader2 className="h-4 w-4 animate-spin" />
                      ) : null}
                      Marcar como {proxEstado}
                    </button>
                    {pedido.estado !== 'CANCELADO' && pedido.estado !== 'ENTREGADO' && (
                      <button
                        onClick={() => handleCambiarEstado(pedido.id, 'CANCELADO')}
                        disabled={procesando === pedido.id}
                        className="px-4 py-2 bg-red-100 text-red-700 rounded-lg text-sm font-medium hover:bg-red-200 transition-colors disabled:bg-gray-100 disabled:text-gray-400"
                      >
                        Cancelar
                      </button>
                    )}
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default GestionPedidos;
