import React, { useState } from 'react';
import { Mail, Lock, Eye, EyeOff, AlertCircle, CheckCircle, ArrowRight, User, Store, ShoppingBag } from 'lucide-react';
import authService from '../services/authService';

/**
 * Componente de Login y Registro de Usuario
 * Diseño premium con glassmorphism, gradientes modernos y transiciones fluidas.
 */
const Login = ({ onLoginSuccess, onCancel }) => {
  const [esLogin, setEsLogin] = useState(true); // true = Login, false = Registro
  const [email, setEmail] = useState('');
  const [contrasena, setContrasena] = useState('');
  const [confirmarContrasena, setConfirmarContrasena] = useState('');
  const [rolSeleccionado, setRolSeleccionado] = useState('CLIENTE');
  
  // Estados de UI
  const [mostrarContrasena, setMostrarContrasena] = useState(false);
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState('');
  const [exito, setExito] = useState('');

  const validarCampos = () => {
    if (!email || !email.includes('@')) {
      setError('Por favor, ingresa un correo electrónico válido.');
      return false;
    }
    if (!contrasena || contrasena.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres.');
      return false;
    }
    if (!esLogin && contrasena !== confirmarContrasena) {
      setError('Las contraseñas no coinciden.');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setExito('');

    if (!validarCampos()) return;

    setCargando(true);
    try {
      if (esLogin) {
        const response = await authService.login(email, contrasena);
        if (response.exito) {
          setExito('Inicio de sesion exitoso. Redirigiendo...');
          const usuario = authService.obtenerUsuarioActual() || {
            id: response.datos?.usuarioId,
            nombre: response.datos?.nombre,
            email: response.datos?.email,
            rol: response.datos?.rol
          };
          setTimeout(() => onLoginSuccess(usuario), 1000);
        } else {
          setError(response.mensaje || 'Credenciales invalidas.');
        }
      } else {
        const response = await authService.registro(email, contrasena, rolSeleccionado);
        if (response.exito) {
          setExito('Registro completado. Redirigiendo...');
          const usuario = authService.obtenerUsuarioActual() || {
            id: response.datos?.usuarioId,
            nombre: response.datos?.nombre,
            email: response.datos?.email,
            rol: response.datos?.rol
          };
          setTimeout(() => onLoginSuccess(usuario), 1500);
        } else {
          setError(response.mensaje || 'Error al registrar el usuario.');
        }
      }
    } catch (err) {
      console.error(err);
      setError(err.mensaje || 'Ocurrió un error de conexión con el servidor. Inténtalo de nuevo.');
    } finally {
      setCargando(false);
    }
  };

  const cambiarPestana = (modoLogin) => {
    setEsLogin(modoLogin);
    setError('');
    setExito('');
    setContrasena('');
    setConfirmarContrasena('');
  };

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-4 py-12 relative overflow-hidden bg-slate-50">
      {/* Fondo decorativo con gradientes y blobs */}
      <div className="absolute top-1/4 left-1/4 -translate-x-1/2 -translate-y-1/2 w-72 h-72 bg-primary-300 rounded-full mix-blend-multiply filter blur-3xl opacity-30 animate-pulse"></div>
      <div className="absolute bottom-1/4 right-1/4 translate-x-1/2 translate-y-1/2 w-80 h-80 bg-secondary-200 rounded-full mix-blend-multiply filter blur-3xl opacity-20"></div>
      
      {/* Tarjeta de login con Glassmorphism */}
      <div className="w-full max-w-md z-10">
        <div className="backdrop-blur-md bg-white/80 border border-white/40 shadow-2xl rounded-2xl overflow-hidden transition-all duration-300 transform hover:scale-[1.01]">
          
          {/* Cabecera / Pestañas */}
          <div className="flex border-b border-gray-200 bg-white/40">
            <button
              onClick={() => cambiarPestana(true)}
              className={`w-1/2 py-4 text-center font-semibold text-sm transition-all duration-300 border-b-2 ${
                esLogin 
                  ? 'border-primary-500 text-primary-600 bg-white/60' 
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:bg-white/20'
              }`}
            >
              Iniciar Sesión
            </button>
            <button
              onClick={() => cambiarPestana(false)}
              className={`w-1/2 py-4 text-center font-semibold text-sm transition-all duration-300 border-b-2 ${
                !esLogin 
                  ? 'border-primary-500 text-primary-600 bg-white/60' 
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:bg-white/20'
              }`}
            >
              Crear Cuenta
            </button>
          </div>

          <div className="p-8">
            {/* Título e introducción */}
            <div className="text-center mb-8">
              <h2 className="text-3xl font-extrabold text-slate-800 tracking-tight">
                {esLogin ? '¡Hola de nuevo!' : 'Únete al Mercado'}
              </h2>
              <p className="mt-2 text-sm text-gray-600">
                {esLogin 
                  ? 'Accede para ver tus recomendaciones personalizadas' 
                  : 'Regístrate para comprar y vender productos en Punata'}
              </p>
            </div>

            {/* Alertas */}
            {error && (
              <div className="mb-6 flex items-center gap-3 p-4 rounded-xl bg-red-50 border border-red-100 text-red-700 text-sm animate-shake">
                <AlertCircle className="h-5 w-5 shrink-0" />
                <span>{error}</span>
              </div>
            )}

            {exito && (
              <div className="mb-6 flex items-center gap-3 p-4 rounded-xl bg-green-50 border border-green-100 text-green-700 text-sm animate-fade-in">
                <CheckCircle className="h-5 w-5 shrink-0" />
                <span>{exito}</span>
              </div>
            )}

            {/* Formulario */}
            <form onSubmit={handleSubmit} className="space-y-5">
              
              {/* Campo Email */}
              <div>
                <label className="block text-xs font-semibold uppercase tracking-wider text-gray-600 mb-2">
                  Correo Electrónico
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-gray-400">
                    <Mail className="h-5 w-5" />
                  </div>
                  <input
                    type="email"
                    required
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="tuemail@ejemplo.com"
                    className="block w-full pl-11 pr-4 py-3 border border-gray-200 rounded-xl bg-white/50 focus:bg-white focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition-all placeholder:text-gray-400 text-sm text-slate-800 font-medium"
                  />
                </div>
              </div>

              {/* Campo Contraseña */}
              <div>
                <label className="block text-xs font-semibold uppercase tracking-wider text-gray-600 mb-2">
                  Contraseña
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-gray-400">
                    <Lock className="h-5 w-5" />
                  </div>
                  <input
                    type={mostrarContrasena ? 'text' : 'password'}
                    required
                    value={contrasena}
                    onChange={(e) => setContrasena(e.target.value)}
                    placeholder="••••••••"
                    className="block w-full pl-11 pr-11 py-3 border border-gray-200 rounded-xl bg-white/50 focus:bg-white focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition-all placeholder:text-gray-400 text-sm text-slate-800 font-medium"
                  />
                  <button
                    type="button"
                    onClick={() => setMostrarContrasena(!mostrarContrasena)}
                    className="absolute inset-y-0 right-0 pr-3.5 flex items-center text-gray-400 hover:text-gray-600 transition-colors"
                  >
                    {mostrarContrasena ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                  </button>
                </div>
              </div>

              {/* Confirmación de contraseña (solo en Registro) */}
              {!esLogin && (
                <div className="animate-slide-down">
                  <label className="block text-xs font-semibold uppercase tracking-wider text-gray-600 mb-2">
                    Confirmar Contraseña
                  </label>
                  <div className="relative">
                    <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-gray-400">
                      <Lock className="h-5 w-5" />
                    </div>
                    <input
                      type={mostrarContrasena ? 'text' : 'password'}
                      required
                      value={confirmarContrasena}
                      onChange={(e) => setConfirmarContrasena(e.target.value)}
                      placeholder="••••••••"
                      className="block w-full pl-11 pr-4 py-3 border border-gray-200 rounded-xl bg-white/50 focus:bg-white focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition-all placeholder:text-gray-400 text-sm text-slate-800 font-medium"
                    />
                  </div>
                </div>
              )}

              {/* Selector de Rol (solo en Registro) */}
              {!esLogin && (
                <div className="animate-slide-down">
                  <label className="block text-xs font-semibold uppercase tracking-wider text-gray-600 mb-2">
                    Tipo de Cuenta
                  </label>
                  <div className="grid grid-cols-2 gap-3">
                    <button
                      type="button"
                      onClick={() => setRolSeleccionado('CLIENTE')}
                      className={`flex items-center gap-3 p-3 rounded-xl border-2 transition-all ${
                        rolSeleccionado === 'CLIENTE'
                          ? 'border-primary-500 bg-primary-50 text-primary-700'
                          : 'border-gray-200 bg-white/50 text-gray-600 hover:border-gray-300'
                      }`}
                    >
                      <ShoppingBag className={`h-5 w-5 ${rolSeleccionado === 'CLIENTE' ? 'text-primary-500' : 'text-gray-400'}`} />
                      <div className="text-left">
                        <p className="text-sm font-bold">Comprador</p>
                        <p className="text-xs opacity-75">Para comprar productos</p>
                      </div>
                    </button>
                    <button
                      type="button"
                      onClick={() => setRolSeleccionado('COMERCIANTE')}
                      className={`flex items-center gap-3 p-3 rounded-xl border-2 transition-all ${
                        rolSeleccionado === 'COMERCIANTE'
                          ? 'border-primary-500 bg-primary-50 text-primary-700'
                          : 'border-gray-200 bg-white/50 text-gray-600 hover:border-gray-300'
                      }`}
                    >
                      <Store className={`h-5 w-5 ${rolSeleccionado === 'COMERCIANTE' ? 'text-primary-500' : 'text-gray-400'}`} />
                      <div className="text-left">
                        <p className="text-sm font-bold">Vendedor</p>
                        <p className="text-xs opacity-75">Para vender productos</p>
                      </div>
                    </button>
                  </div>
                </div>
              )}

              {/* Botones de acción */}
              <div className="pt-2 space-y-3">
                <button
                  type="submit"
                  disabled={cargando}
                  className="w-full flex items-center justify-center gap-2 py-3.5 px-4 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-xl font-bold text-sm shadow-md hover:from-primary-600 hover:to-primary-700 active:scale-[0.98] transition-all disabled:opacity-50 disabled:pointer-events-none"
                >
                  {cargando ? (
                    <div className="h-5 w-5 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                  ) : (
                    <>
                      {esLogin ? 'Iniciar Sesión' : 'Registrarme'}
                      <ArrowRight className="h-4 w-4" />
                    </>
                  )}
                </button>

                <button
                  type="button"
                  onClick={onCancel}
                  className="w-full py-3 px-4 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-xl font-semibold text-sm transition-colors text-center"
                >
                  Volver al Inicio
                </button>
              </div>

            </form>
          </div>

        </div>
      </div>
    </div>
  );
};

export default Login;
