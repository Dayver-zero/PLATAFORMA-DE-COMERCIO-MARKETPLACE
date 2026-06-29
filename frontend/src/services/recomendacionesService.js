import api from './api';

/**
 * Servicio para obtener recomendaciones personalizadas
 * Conecta con MotorRecomendacionService del backend
 */
const recomendacionesService = {
  /**
   * Genera recomendaciones completas (clima + ubicación + historial)
   */
  obtenerRecomendaciones: async (usuarioId, latitud, longitud) => {
    try {
      const response = await api.get('/recomendaciones', {
        params: { usuarioId, latitud, longitud }
      });
      return response.datos || [];
    } catch (error) {
      console.error('Error obteniendo recomendaciones:', error);
      throw error;
    }
  },

  /**
   * Recomendaciones solo por clima
   */
  obtenerPorClima: async (latitud, longitud) => {
    try {
      const response = await api.get('/recomendaciones/clima', {
        params: { latitud, longitud }
      });
      return response.datos || [];
    } catch (error) {
      console.error('Error obteniendo recomendaciones por clima:', error);
      throw error;
    }
  },

  /**
   * Recomendaciones solo por ubicación
   */
  obtenerPorUbicacion: async (latitud, longitud, radio = 5) => {
    try {
      const response = await api.get('/recomendaciones/ubicacion', {
        params: { latitud, longitud, radio }
      });
      return response.datos || [];
    } catch (error) {
      console.error('Error obteniendo recomendaciones por ubicación:', error);
      throw error;
    }
  },

  /**
   * Recomendaciones por historial del usuario
   */
  obtenerPorHistorial: async (usuarioId) => {
    try {
      const response = await api.get('/recomendaciones/historial', {
        params: { usuarioId }
      });
      return response.datos || [];
    } catch (error) {
      console.error('Error obteniendo recomendaciones por historial:', error);
      throw error;
    }
  },
};

export default recomendacionesService;
