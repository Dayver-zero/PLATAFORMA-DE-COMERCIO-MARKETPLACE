import api from './api';

const carritoService = {
  obtener: async () => {
    try {
      return await api.get('/carrito');
    } catch (error) {
      return { exito: false, mensaje: 'Error al obtener carrito', datos: { items: [], subtotal: 0, totalItems: 0 } };
    }
  },

  contarItems: async () => {
    try {
      const response = await api.get('/carrito/conteo');
      return response.datos || 0;
    } catch {
      return 0;
    }
  },

  agregarItem: async (productoId, cantidad = 1) => {
    try {
      return await api.post('/carrito/items', { productoId, cantidad });
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al agregar al carrito' };
    }
  },

  actualizarItem: async (itemId, cantidad) => {
    try {
      return await api.put(`/carrito/items/${itemId}`, { cantidad });
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al actualizar cantidad' };
    }
  },

  eliminarItem: async (itemId) => {
    try {
      return await api.delete(`/carrito/items/${itemId}`);
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al eliminar item' };
    }
  },

  limpiar: async () => {
    try {
      return await api.delete('/carrito');
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al limpiar carrito' };
    }
  },
};

export default carritoService;
