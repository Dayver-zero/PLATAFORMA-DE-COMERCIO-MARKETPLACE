import api from './api';

/**
 * Servicio para autenticación y gestión de usuario
 */
const authService = {
  /**
   * Login de usuario
   * @param {string} email - Email del usuario
   * @param {string} contrasena - Contraseña
   * @returns {Promise<{token, usuario}>} Token JWT y datos del usuario
   */
  login: async (email, contrasena) => {
    try {
      const response = await api.post('/auth/login', { email, contrasena });
      if (response.exito && response.datos?.token) {
        localStorage.setItem('jwtToken', response.datos.token);
        localStorage.setItem('usuario', JSON.stringify({
          id: response.datos.usuarioId,
          nombre: response.datos.nombre,
          email: response.datos.email,
          rol: response.datos.rol
        }));
      }
      return response;
    } catch (error) {
      console.error('Error en login:', error);
      throw error;
    }
  },

  /**
   * Registro de nuevo usuario
   * @param {string} email - Email del nuevo usuario
   * @param {string} contrasena - Contraseña
   * @param {string} rol - Rol del usuario (CLIENTE o COMERCIANTE)
   * @returns {Promise}
   */
  registro: async (email, contrasena, rol = 'CLIENTE') => {
    try {
      const response = await api.post('/auth/registro', { email, contrasena, rol });
      if (response.exito && response.datos?.token) {
        localStorage.setItem('jwtToken', response.datos.token);
        localStorage.setItem('usuario', JSON.stringify({
          id: response.datos.usuarioId,
          nombre: response.datos.nombre,
          email: response.datos.email,
          rol: response.datos.rol
        }));
      }
      return response;
    } catch (error) {
      console.error('Error en registro:', error);
      throw error;
    }
  },

  /**
   * Logout del usuario
   */
  logout: () => {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('usuario');
  },

  /**
   * Verificar si email está disponible
   * @param {string} email - Email a verificar
   * @returns {Promise<boolean>}
   */
  verificarEmail: async (email) => {
    try {
      const response = await api.post(`/auth/verificar-email?email=${email}`);
      return response.datos; // true si está disponible, false si no
    } catch (error) {
      console.error('Error verificando email:', error);
      return false;
    }
  },

  /**
   * Obtener usuario actual del localStorage
   * @returns {object|null}
   */
  obtenerUsuarioActual: () => {
    try {
      const raw = localStorage.getItem('usuario');
      if (!raw || raw === 'undefined' || raw === 'null') return null;
      const parsed = JSON.parse(raw);
      // Migrar desde formato antiguo (LoginResponseDTO con datos anidados)
      if (parsed && parsed.datos && typeof parsed.datos === 'object' && parsed.datos.id) {
        return parsed.datos;
      }
      return parsed;
    } catch {
      localStorage.removeItem('usuario');
      return null;
    }
  },

  /**
   * Eliminar la cuenta del usuario autenticado (soft-delete)
   * @returns {Promise}
   */
  eliminarCuenta: async () => {
    try {
      const response = await api.delete('/usuarios/cuenta');
      return response;
    } catch (error) {
      console.error('Error al eliminar cuenta:', error);
      throw error;
    }
  },

  /**
   * Obtener perfil completo del usuario desde el backend
   * @param {number} id - ID del usuario
   * @returns {Promise}
   */
  obtenerPerfil: async (id) => {
    try {
      const response = await api.get(`/usuarios/${id}`);
      return response;
    } catch (error) {
      console.error('Error al obtener perfil:', error);
      throw error;
    }
  },

  /**
   * Actualizar datos del perfil (nombre, ubicación, preferencias)
   * @param {number} id - ID del usuario
   * @param {object} datos - Campos a actualizar
   * @returns {Promise}
   */
  actualizarPerfil: async (id, datos) => {
    try {
      const response = await api.put(`/usuarios/${id}`, datos);
      if (response.exito && response.datos) {
        localStorage.setItem('usuario', JSON.stringify({
          id: response.datos.id,
          nombre: response.datos.nombre,
          email: response.datos.email,
          rol: response.datos.rol,
        }));
      }
      return response;
    } catch (error) {
      console.error('Error al actualizar perfil:', error);
      throw error;
    }
  },

  /**
   * Cambiar el rol del usuario (CLIENTE <-> COMERCIANTE)
   * @param {number} id - ID del usuario
   * @param {string} nuevoRol - CLIENTE | COMERCIANTE
   * @returns {Promise}
   */
   cambiarRol: async (id, nuevoRol) => {
    try {
      const response = await api.put(`/usuarios/${id}/rol`, { rol: nuevoRol });
      if (response.exito && response.datos) {
        if (response.datos.token) {
          localStorage.setItem('jwtToken', response.datos.token);
        }
        const userData = response.datos.usuario || response.datos;
        localStorage.setItem('usuario', JSON.stringify({
          id: userData.id,
          nombre: userData.nombre,
          email: userData.email,
          rol: userData.rol,
        }));
      }
      return response;
    } catch (error) {
      console.error('Error al cambiar rol:', error);
      throw error;
    }
  },

  /**
   * Verificar si el usuario está autenticado
   * @returns {boolean}
   */
  estaAutenticado: () => {
    return !!localStorage.getItem('jwtToken');
  },
};

export default authService;
