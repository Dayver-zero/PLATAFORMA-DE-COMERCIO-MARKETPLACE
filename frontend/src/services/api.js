  import axios from 'axios';

  /**
   * Configuración base de Axios para conectar con el backend
   * Base URL: http://localhost:8080/api
   */
  const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
    timeout: 10000,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  /**
   * Interceptor para agregar token JWT a las peticiones
   */
  api.interceptors.request.use(
    (config) => {
      // Obtener token JWT del localStorage
      const token = localStorage.getItem('jwtToken');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  /**
   * Interceptor para manejar respuestas y errores
   */
  api.interceptors.response.use(
    (response) => {
      // Retornar solo los datos de la respuesta
      return response.data;
    },
    (error) => {
      // Manejar errores 401 (token expirado o no válido)
      if (error.response?.status === 401) {
        console.error('Token expirado - Redirigiendo al login');
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('usuario');
        window.location.href = '/login';
      }
      
      // Retornar objeto de error con estructura consistente
      const errorData = error.response?.data || {
        exito: false,
        mensaje: error.message || 'Error en la solicitud',
        errores: [error.message]
      };
      
      return Promise.reject(errorData);
    }
  );

  export default api;
