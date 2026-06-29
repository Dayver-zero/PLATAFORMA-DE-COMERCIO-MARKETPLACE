import React, { useState, useEffect } from 'react';
import { Package, Clock, CheckCircle, XCircle, Truck, Loader2, Smartphone, Upload } from 'lucide-react';
import pedidosService from '../services/pedidosService';

const ESTADO_CONFIG = {
  PENDIENTE: { color: 'bg-yellow-100 text-yellow-800', icon: Clock },
  CONFIRMADO: { color: 'bg-blue-100 text-blue-800', icon: CheckCircle },
  ENVIADO: { color: 'bg-indigo-100 text-indigo-800', icon: Truck },
  ENTREGADO: { color: 'bg-green-100 text-green-800', icon: CheckCircle },
  CANCELADO: { color: 'bg-red-100 text-red-800', icon: XCircle },
};

const HistorialPedidos = ({ onNavigate }) => {
  const [pedidos, setPedidos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pedidoExpandido, setPedidoExpandido] = useState(null);
  const [pagoInfo, setPagoInfo] = useState({});
  const [procesandoPago, setProcesandoPago] = useState(null);

  useEffect(() => {
    const cargar = async () => {
      const response = await pedidosService.listar();
      if (response.exito) setPedidos(response.datos || []);
      setLoading(false);
    };
    cargar();
  }, []);

  const handleGenerarPagoYape = async (pedidoId) => {
    setProcesandoPago(pedidoId);
    const response = await pedidosService.generarPagoYape(pedidoId);
    if (response.exito) {
      setPagoInfo((prev) => ({ ...prev, [pedidoId]: { codigo: response.datos, step: 'generado' } }));
    }
    setProcesandoPago(null);
  };

  const handleConfirmarPagoYape = async (pedidoId) => {
    const info = pagoInfo[pedidoId];
    if (!info?.referencia) return;
    setProcesandoPago(pedidoId);
    const response = await pedidosService.confirmarPagoYape(pedidoId, info.referencia, info.comprobante || '');
    if (response.exito) {
      setPagoInfo((prev) => ({ ...prev, [pedidoId]: { ...prev[pedidoId], step: 'confirmado' } }));
      const res = await pedidosService.listar();
      if (res.exito) setPedidos(res.datos || []);
    }
    setProcesandoPago(null);
  };

  if (loading) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-8">
        <div className="flex items-center justify-center py-20">
          <Loader2 className="h-8 w-8 text-primary-500 animate-spin" />
          <span className="ml-3 text-gray-600">Cargando pedidos...</span>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 py-6 animate-fade-in">
      <div className="flex items-center gap-3 mb-6">
        <Package className="h-7 w-7 text-primary-600" />
        <h2 className="text-2xl font-bold text-gray-900">Mis Pedidos</h2>
      </div>

      {pedidos.length === 0 ? (
        <div className="text-center py-16 bg-white rounded-2xl shadow-sm border border-gray-100">
          <Package className="h-16 w-16 text-gray-300 mx-auto mb-4" />
          <p className="text-gray-600 text-lg mb-2">No tienes pedidos aún</p>
          <button onClick={() => onNavigate('inicio')}
            className="mt-4 px-6 py-2.5 bg-primary-600 text-white rounded-xl font-medium hover:bg-primary-700">
            Ir a la tienda
          </button>
        </div>
      ) : (
        <div className="space-y-4">
          {pedidos.map((pedido) => {
            const EstIcon = ESTADO_CONFIG[pedido.estado]?.icon || Package;
            const estColor = ESTADO_CONFIG[pedido.estado]?.color || 'bg-gray-100 text-gray-800';
            const esYape = pedido.metodoPago === 'YAPE';
            const infoPago = pagoInfo[pedido.id] || {};

            return (
              <div key={pedido.id} className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                <div className="p-4 sm:p-6">
                  <div className="flex items-center justify-between mb-2">
                    <div className="flex items-center gap-3">
                      <span className={`inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium ${estColor}`}>
                        <EstIcon className="h-3.5 w-3.5" />
                        {pedido.estado}
                      </span>
                      <span className="text-sm text-gray-500">
                        {new Date(pedido.fechaCreacion).toLocaleDateString('es-BO', {
                          year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit'
                        })}
                      </span>
                    </div>
                    <span className="text-lg font-bold text-gray-900">
                      Bs. {pedido.total.toFixed(2)}
                    </span>
                  </div>

                  <div className="flex items-center gap-4 text-sm text-gray-500 mb-3">
                    <span>Pedido #{pedido.id}</span>
                    <span>Pago: {pedido.metodoPago}</span>
                    {pedido.codigoPago && (
                      <span className="font-mono text-xs bg-gray-100 px-2 py-0.5 rounded">
                        Código: {pedido.codigoPago}
                      </span>
                    )}
                  </div>

                  <button
                    onClick={() => setPedidoExpandido(pedidoExpandido === pedido.id ? null : pedido.id)}
                    className="text-sm text-primary-600 hover:text-primary-700 font-medium"
                  >
                    {pedidoExpandido === pedido.id ? 'Ocultar detalles' : `Ver ${pedido.items?.length || 0} producto(s)`}
                  </button>

                  {pedidoExpandido === pedido.id && (
                    <div className="mt-4 border-t border-gray-100 pt-4 space-y-2">
                      {pedido.items?.map((item) => (
                        <div key={item.id} className="flex items-center gap-3 text-sm">
                          <img src={item.urlImagen || '/uploads/productos/sin-imagen.svg'} alt={item.nombreProducto}
                            className="w-10 h-10 object-cover rounded" />
                          <div className="flex-1">
                            <p className="font-medium text-gray-900">{item.nombreProducto}</p>
                            <p className="text-gray-500">x{item.cantidad}</p>
                          </div>
                          <span className="font-medium">Bs. {item.subtotal.toFixed(2)}</span>
                        </div>
                      ))}
                    </div>
                  )}

                  {esYape && pedido.estado === 'PENDIENTE' && (
                    <div className="mt-4 border-t border-gray-100 pt-4">
                      {!infoPago.codigo ? (
                        <button
                          onClick={() => handleGenerarPagoYape(pedido.id)}
                          disabled={procesandoPago === pedido.id}
                          className="flex items-center gap-2 px-4 py-2 bg-yellow-500 text-white rounded-lg text-sm font-medium hover:bg-yellow-600 disabled:bg-gray-300 transition-colors"
                        >
                          {procesandoPago === pedido.id ? (
                            <Loader2 className="h-4 w-4 animate-spin" />
                          ) : (
                            <Smartphone className="h-4 w-4" />
                          )}
                          Generar código Yape
                        </button>
                      ) : infoPago.step === 'generado' ? (
                        <div className="bg-yellow-50 border border-yellow-200 rounded-xl p-4">
                          <p className="font-medium text-yellow-800 mb-2">Paga con Yape</p>
                          <p className="text-sm text-yellow-700 mb-1">
                            Código de referencia: <strong className="text-lg font-mono">{infoPago.codigo}</strong>
                          </p>
                          <p className="text-xs text-yellow-600 mb-3">Envía el pago a Yape y completa los datos:</p>
                          <input
                            type="text"
                            placeholder="Número de operación Yape"
                            value={infoPago.referencia || ''}
                            onChange={(e) => setPagoInfo((prev) => ({
                              ...prev,
                              [pedido.id]: { ...prev[pedido.id], referencia: e.target.value }
                            }))}
                            className="w-full px-3 py-2 border border-yellow-300 rounded-lg text-sm mb-2 focus:ring-2 focus:ring-yellow-500"
                          />
                          <input
                            type="text"
                            placeholder="Link del comprobante (opcional)"
                            value={infoPago.comprobante || ''}
                            onChange={(e) => setPagoInfo((prev) => ({
                              ...prev,
                              [pedido.id]: { ...prev[pedido.id], comprobante: e.target.value }
                            }))}
                            className="w-full px-3 py-2 border border-yellow-300 rounded-lg text-sm mb-3 focus:ring-2 focus:ring-yellow-500"
                          />
                          <button
                            onClick={() => handleConfirmarPagoYape(pedido.id)}
                            disabled={!infoPago.referencia || procesandoPago === pedido.id}
                            className="w-full flex items-center justify-center gap-2 px-4 py-2 bg-green-600 text-white rounded-lg text-sm font-medium hover:bg-green-700 disabled:bg-gray-300 transition-colors"
                          >
                            {procesandoPago === pedido.id ? (
                              <Loader2 className="h-4 w-4 animate-spin" />
                            ) : (
                              <Upload className="h-4 w-4" />
                            )}
                            Confirmar pago
                          </button>
                        </div>
                      ) : (
                        <div className="bg-green-50 border border-green-200 rounded-xl p-4 text-center">
                          <p className="text-green-700 font-medium">Pago registrado</p>
                          <p className="text-sm text-green-600">Esperando verificación del comerciante</p>
                        </div>
                      )}
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default HistorialPedidos;
