import api from './api';

/**
 * Servicio para obtener datos del clima del backend
 * Conecta con el ClimaService del backend Spring Boot
 */
const climaService = {
  /**
   * Obtiene el clima actual basado en coordenadas GPS
   */
  obtenerClimaPorCoordenadas: async (latitud, longitud) => {
    try {
      const response = await api.get('/clima', {
        params: { latitud, longitud }
      });
      return response.datos;
    } catch (error) {
      console.error('Error al obtener clima por coordenadas:', error);
      throw error;
    }
  },

  /**
   * Obtiene el clima actual basado en el nombre de una ciudad
   */
  obtenerClimaPorCiudad: async (ciudad) => {
    try {
      const response = await api.get('/clima/ciudad', {
        params: { ciudad }
      });
      return response.datos;
    } catch (error) {
      console.error('Error al obtener clima por ciudad:', error);
      throw error;
    }
  },

  /**
   * Obtiene el pronóstico para los próximos días
   */
  obtenerPronostico: async (latitud, longitud, dias = 5) => {
    try {
      const response = await api.get('/clima/pronostico', {
        params: { latitud, longitud, dias }
      });
      return response.datos || [];
    } catch (error) {
      console.error('Error al obtener pronóstico:', error);
      throw error;
    }
  },
};

export default climaService;
