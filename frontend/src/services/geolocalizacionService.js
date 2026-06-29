import api from './api';

/**
 * Servicio para manejar coordenadas y geolocalización
 * Conecta con el GeolocalizacionService del backend Spring Boot
 */
const geolocalizacionService = {
  /**
   * Obtiene la ubicación actual del usuario usando la API de geolocalización del navegador
   */
  obtenerUbicacionActual: () => {
    return new Promise((resolve, reject) => {
      if (!navigator.geolocation) {
        reject(new Error('Geolocalización no soportada por el navegador'));
        return;
      }

      navigator.geolocation.getCurrentPosition(
        (position) => {
          resolve({
            latitud: position.coords.latitude,
            longitud: position.coords.longitude
          });
        },
        (error) => {
          console.error('Error al obtener ubicación:', error);
          console.warn('Usando ubicación por defecto: Punata, Bolivia');
          resolve({
            latitud: -17.5528,
            longitud: -65.8756
          });
        },
        {
          enableHighAccuracy: true,
          timeout: 10000,
          maximumAge: 0
        }
      );
    });
  },

  /**
   * Obtener comercios cercanos a una ubicación
   */
  obtenerComerciosCercanos: async (latitud, longitud, radioKm = 5) => {
    try {
      const response = await api.get('/comercios/cercanos', {
        params: { latitud, longitud, radio: radioKm }
      });
      return response.datos || [];
    } catch (error) {
      console.error('Error al obtener comercios cercanos:', error);
      throw error;
    }
  },

  /**
   * Obtener productos cercanos (por ubicación de comercio)
   */
  obtenerProductosCercanos: async (latitud, longitud, radioKm = 5) => {
    try {
      const response = await api.get('/recomendaciones/ubicacion', {
        params: { latitud, longitud, radio: radioKm }
      });
      return response.datos || [];
    } catch (error) {
      console.error('Error al obtener productos cercanos:', error);
      throw error;
    }
  },
};

export default geolocalizacionService;
