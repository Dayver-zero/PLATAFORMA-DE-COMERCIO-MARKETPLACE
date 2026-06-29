import api from './api';

const productosService = {
  obtenerTodos: async () => {
    try {
      return await api.get('/productos');
    } catch (error) {
      console.error('Error al obtener productos:', error);
      return { exito: false, datos: [] };
    }
  },

  obtenerPorComercio: async (comercioId) => {
    try {
      return await api.get(`/productos/comercio/${comercioId}`);
    } catch (error) {
      console.error('Error al obtener productos del comercio:', error);
      return { exito: false, datos: [] };
    }
  },

  crear: async (producto) => {
    try {
      return await api.post('/productos', producto);
    } catch (error) {
      console.error('Error al crear producto:', error);
      return error.exito !== undefined ? error : { exito: false, mensaje: 'Error al crear producto' };
    }
  },

  actualizar: async (id, producto) => {
    try {
      return await api.put(`/productos/${id}`, producto);
    } catch (error) {
      console.error('Error al actualizar producto:', error);
      return error.exito !== undefined ? error : { exito: false, mensaje: 'Error al actualizar producto' };
    }
  },

  eliminar: async (id) => {
    try {
      return await api.delete(`/productos/${id}`);
    } catch (error) {
      console.error('Error al eliminar producto:', error);
      return error.exito !== undefined ? error : { exito: false, mensaje: 'Error al eliminar producto' };
    }
  },

  subirImagen: async (archivo) => {
    try {
      const formData = new FormData();
      formData.append('archivo', archivo);
      return await api.post('/upload', formData, {
        headers: { 'Content-Type': undefined },
        timeout: 30000,
      });
    } catch (error) {
      console.error('Error al subir imagen:', error);
      return error.exito !== undefined ? error : { exito: false, mensaje: 'Error al subir imagen' };
    }
  },
};

export default productosService;
