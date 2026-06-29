import api from './api';

const comerciosService = {
  obtenerMisComercios: async () => {
    try {
      return await api.get('/comercios/mis-comercios');
    } catch (error) {
      console.error('Error al obtener mis comercios:', error);
      return { exito: false, datos: [] };
    }
  },
};

export default comerciosService;
