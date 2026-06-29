import api from './api';

const pedidosService = {
  crear: async (metodoPago = 'EFECTIVO') => {
    try {
      return await api.post('/pedidos', { metodoPago });
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al crear pedido' };
    }
  },

  listar: async () => {
    try {
      return await api.get('/pedidos');
    } catch (error) {
      return { exito: false, mensaje: 'Error al obtener pedidos', datos: [] };
    }
  },

  obtener: async (id) => {
    try {
      return await api.get(`/pedidos/${id}`);
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al obtener pedido' };
    }
  },

  cambiarEstado: async (id, estado) => {
    try {
      return await api.put(`/pedidos/${id}/estado`, { estado });
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al cambiar estado' };
    }
  },

  generarPagoYape: async (id) => {
    try {
      return await api.post(`/pedidos/${id}/pago/yape/generar`);
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al generar código Yape' };
    }
  },

  confirmarPagoYape: async (id, referenciaPago, comprobanteUrl) => {
    try {
      return await api.post(`/pedidos/${id}/pago/yape/confirmar`, { referenciaPago, comprobanteUrl });
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al confirmar pago Yape' };
    }
  },

  listarComercio: async () => {
    try {
      return await api.get('/pedidos/comercio');
    } catch (error) {
      return { exito: false, mensaje: 'Error al obtener pedidos del comercio', datos: [] };
    }
  },
};

export default pedidosService;
