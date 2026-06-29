import React, { useState, useEffect } from 'react';
import { CreditCard, Wallet, Smartphone, ArrowLeft, CheckCircle, Loader2 } from 'lucide-react';
import carritoService from '../services/carritoService';
import pedidosService from '../services/pedidosService';

const METODOS_PAGO = [
  { id: 'EFECTIVO', label: 'Efectivo', icon: Wallet, desc: 'Paga cuando retires el producto' },
  { id: 'YAPE', label: 'Yape', icon: Smartphone, desc: 'Paga por adelantado con Yape' },
];

const Checkout = ({ onNavigate, metodoPagoInicial }) => {
  const [carrito, setCarrito] = useState(null);
  const [metodoPago, setMetodoPago] = useState(metodoPagoInicial || 'EFECTIVO');
  const [loading, setLoading] = useState(true);
  const [procesando, setProcesando] = useState(false);
  const [error, setError] = useState(null);
  const [pedidoCreado, setPedidoCreado] = useState(null);

  useEffect(() => {
    const cargarCarrito = async () => {
      const response = await carritoService.obtener();
      if (response.exito && response.datos) {
        setCarrito(response.datos);
      } else {
        setError('Error al cargar carrito');
      }
      setLoading(false);
    };
    cargarCarrito();
  }, []);

  const handleConfirmar = async () => {
    setProcesando(true);
    setError(null);
    try {
      const response = await pedidosService.crear(metodoPago);
      if (response.exito && response.datos) {
        setPedidoCreado(response.datos);
      } else {
        setError(response.mensaje || 'Error al crear pedido');
      }
    } catch (err) {
      setError('Error al procesar el pedido');
    } finally {
      setProcesando(false);
    }
  };

  if (loading) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-8">
        <div className="flex items-center justify-center py-20">
          <Loader2 className="h-8 w-8 text-primary-500 animate-spin" />
          <span className="ml-3 text-gray-600">Preparando checkout...</span>
        </div>
      </div>
    );
  }

  if (pedidoCreado) {
    const esYape = pedidoCreado.metodoPago === 'YAPE';

    return (
      <div className="max-w-2xl mx-auto px-4 py-8">
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 text-center">
          <CheckCircle className="h-16 w-16 text-green-500 mx-auto mb-4" />
          <h2 className="text-2xl font-bold text-gray-900 mb-2">Pedido Creado</h2>
          <p className="text-gray-600 mb-2">Pedido #{pedidoCreado.id}</p>
          <p className="text-lg font-semibold text-primary-600 mb-6">
            Total: Bs. {pedidoCreado.total.toFixed(2)}
          </p>

          {esYape && (
            <div className="bg-yellow-50 border border-yellow-200 rounded-xl p-6 mb-6 text-left">
              <h3 className="font-bold text-yellow-800 mb-2">Pago con Yape</h3>
              <div className="space-y-2 text-sm text-yellow-700">
                <p>1. Abre Yape en tu celular</p>
                <p>2. Busca el número: <strong>+591 7XXX-XXXX</strong></p>
                <p>3. Ingresa el monto: <strong>Bs. {pedidoCreado.total.toFixed(2)}</strong></p>
                <p>4. Usa este código de referencia: <strong className="text-lg font-mono bg-yellow-100 px-3 py-1 rounded">{pedidoCreado.codigoPago}</strong></p>
                <p>5. Vuelve a "Mis Pedidos" para confirmar el pago con tu número de operación</p>
              </div>
            </div>
          )}

          <div className="text-sm text-gray-500 mb-6">
            {esYape
              ? 'Tu pedido está pendiente hasta que confirmes el pago.'
              : 'Tu pedido está confirmado. Presenta este número al recoger.'}
          </div>

          <div className="flex gap-3 justify-center">
            <button
              onClick={() => onNavigate('inicio')}
              className="px-6 py-2.5 bg-gray-100 text-gray-700 rounded-xl font-medium hover:bg-gray-200"
            >
              Seguir comprando
            </button>
            <button
              onClick={() => onNavigate('pedidos')}
              className="px-6 py-2.5 bg-primary-600 text-white rounded-xl font-medium hover:bg-primary-700"
            >
              Mis Pedidos
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto px-4 sm:px-6 py-6 animate-fade-in">
      <button
        onClick={() => onNavigate('carrito')}
        className="flex items-center gap-1.5 text-sm text-gray-600 hover:text-primary-600 mb-6"
      >
        <ArrowLeft className="h-4 w-4" />
        Volver al carrito
      </button>

      <h2 className="text-2xl font-bold text-gray-900 mb-6">Finalizar Compra</h2>

      {error && (
        <div className="bg-red-50 border border-red-200 rounded-xl p-4 mb-4 text-red-700 text-sm">
          {error}
        </div>
      )}

      <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 mb-6">
        <h3 className="font-bold text-gray-900 mb-4">Resumen del pedido</h3>
        {carrito?.items?.map((item) => (
          <div key={item.id} className="flex justify-between py-2 text-sm">
            <span className="text-gray-600">
              {item.nombreProducto} x {item.cantidad}
            </span>
            <span className="font-medium">Bs. {item.subtotal.toFixed(2)}</span>
          </div>
        ))}
        <div className="border-t border-gray-100 mt-3 pt-3 flex justify-between font-bold text-lg">
          <span>Total</span>
          <span>Bs. {carrito?.subtotal?.toFixed(2) || '0.00'}</span>
        </div>
      </div>

      <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 mb-6">
        <h3 className="font-bold text-gray-900 mb-4">Método de pago</h3>
        <div className="space-y-3">
          {METODOS_PAGO.map((mp) => {
            const Icon = mp.icon;
            return (
              <label
                key={mp.id}
                className={`flex items-start gap-4 p-4 rounded-xl border-2 cursor-pointer transition-all ${
                  metodoPago === mp.id
                    ? 'border-primary-500 bg-primary-50'
                    : 'border-gray-100 hover:border-gray-200'
                }`}
              >
                <input
                  type="radio"
                  name="metodoPago"
                  value={mp.id}
                  checked={metodoPago === mp.id}
                  onChange={() => setMetodoPago(mp.id)}
                  className="mt-1"
                />
                <Icon className={`h-6 w-6 ${metodoPago === mp.id ? 'text-primary-600' : 'text-gray-400'}`} />
                <div>
                  <p className="font-semibold text-gray-900">{mp.label}</p>
                  <p className="text-sm text-gray-500">{mp.desc}</p>
                  {mp.id === 'YAPE' && (
                    <p className="text-xs text-yellow-600 mt-1">
                      Recibirás un código para realizar el pago desde tu app Yape
                    </p>
                  )}
                </div>
              </label>
            );
          })}
        </div>
      </div>

      <button
        onClick={handleConfirmar}
        disabled={procesando || !carrito?.items?.length}
        className="w-full flex items-center justify-center gap-2 px-6 py-3 bg-primary-600 text-white rounded-xl font-semibold hover:bg-primary-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
      >
        {procesando ? (
          <>
            <Loader2 className="h-5 w-5 animate-spin" />
            Procesando...
          </>
        ) : (
          <>
            <CreditCard className="h-5 w-5" />
            Confirmar pedido
          </>
        )}
      </button>
    </div>
  );
};

export default Checkout;
