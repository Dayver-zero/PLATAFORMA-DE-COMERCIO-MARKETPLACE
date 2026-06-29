import api from './api';

const reservasService = {
  crear: async (productoId, cantidad = 1, notas = '') => {
    try {
      return await api.post('/reservas', { productoId, cantidad, notas });
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al crear reserva' };
    }
  },

  listar: async () => {
    try {
      return await api.get('/reservas');
    } catch (error) {
      return { exito: false, mensaje: 'Error al obtener reservas', datos: [] };
    }
  },

  obtener: async (id) => {
    try {
      return await api.get(`/reservas/${id}`);
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al obtener reserva' };
    }
  },

  cancelar: async (id) => {
    try {
      return await api.put(`/reservas/${id}/cancelar`);
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al cancelar reserva' };
    }
  },

  completar: async (id) => {
    try {
      return await api.put(`/reservas/${id}/completar`);
    } catch (error) {
      return error?.exito !== undefined ? error : { exito: false, mensaje: 'Error al completar reserva' };
    }
  },

  listarComercio: async () => {
    try {
      return await api.get('/reservas/comercio');
    } catch (error) {
      return { exito: false, mensaje: 'Error al obtener reservas del comercio', datos: [] };
    }
  },
};

export default reservasService;
