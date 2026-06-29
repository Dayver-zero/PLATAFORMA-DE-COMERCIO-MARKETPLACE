import React, { useState, useEffect } from 'react';
import { User, Mail, MapPin, Calendar, Shield, ArrowLeft, PenLine, Save, Trash2, RefreshCw, AlertTriangle } from 'lucide-react';
import authService from '../services/authService';

function Perfil({ usuarioActual, onLogout, onNavigate }) {
  const [perfil, setPerfil] = useState(null);
  const [editandoNombre, setEditandoNombre] = useState(false);
  const [nombreInput, setNombreInput] = useState('');
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);
  const [mensaje, setMensaje] = useState(null);

  useEffect(() => {
    if (usuarioActual?.id) {
      cargarPerfil(usuarioActual.id);
    } else {
      setCargando(false);
    }
  }, [usuarioActual?.id]);

  const cargarPerfil = async (id) => {
    setCargando(true);
    setError(null);
    try {
      const response = await authService.obtenerPerfil(id);
      if (response.exito && response.datos) {
        setPerfil(response.datos);
        setNombreInput(response.datos.nombre || '');
      } else {
        setError(response.mensaje || 'Error al cargar perfil');
      }
    } catch (err) {
      setError(err?.mensaje || 'Error al cargar perfil');
    } finally {
      setCargando(false);
    }
  };

  const guardarNombre = async () => {
    if (!nombreInput.trim() || !perfil) return;
    try {
      const response = await authService.actualizarPerfil(perfil.id, { nombre: nombreInput.trim() });
      if (response.exito && response.datos) {
        setPerfil(response.datos);
        setEditandoNombre(false);
        setMensaje('Nombre actualizado exitosamente');
        setTimeout(() => setMensaje(null), 3000);
      } else {
        setError(response.mensaje || 'Error al actualizar nombre');
      }
    } catch (err) {
      setError(err?.mensaje || 'Error al actualizar nombre');
    }
  };

  const cancelarEdicionNombre = () => {
    setNombreInput(perfil?.nombre || '');
    setEditandoNombre(false);
  };

  const handleCambiarRol = async () => {
    if (!perfil) return;
    const nuevoRol = perfil.rol === 'CLIENTE' ? 'COMERCIANTE' : 'CLIENTE';
    const esComerciante = nuevoRol === 'COMERCIANTE';

    const mensajeConfirmacion = esComerciante
      ? 'Al convertirte en COMERCIANTE se creara una tienda por defecto en Punata. ¿Deseas continuar?'
      : 'Al convertirte en CLIENTE tus tiendas y productos se desactivaran (se pueden reactivar despues). ¿Deseas continuar?';

    if (!window.confirm(mensajeConfirmacion)) return;

    try {
      const response = await authService.cambiarRol(perfil.id, nuevoRol);
      if (response.exito && response.datos) {
        window.location.reload();
      } else {
        setError(response.mensaje || 'Error al cambiar rol');
      }
    } catch (err) {
      setError(err?.mensaje || 'Error al cambiar rol');
    }
  };

  const handleEliminarCuenta = () => {
    if (!window.confirm('¿Estas seguro de eliminar tu cuenta? Esta accion no se puede deshacer y todos tus datos seran desactivados permanentemente.')) return;

    authService.eliminarCuenta().then(() => {
      onLogout && onLogout();
    }).catch((err) => {
      setError(err?.mensaje || 'Error al eliminar la cuenta');
    });
  };

  if (cargando) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-8">
        <div className="flex items-center justify-center py-20">
          <RefreshCw className="h-8 w-8 text-primary-500 animate-spin" />
          <span className="ml-3 text-gray-600">Cargando perfil...</span>
        </div>
      </div>
    );
  }

  if (!perfil) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-8">
        <div className="bg-yellow-50 border border-yellow-200 rounded-xl p-6 text-center">
          <AlertTriangle className="h-12 w-12 text-yellow-500 mx-auto mb-3" />
          <p className="text-yellow-800 font-medium">No se pudo cargar el perfil</p>
          <p className="text-yellow-600 text-sm mt-1">{error || 'Inicia sesion para ver tu perfil'}</p>
          <button onClick={() => onNavigate('inicio')} className="mt-4 px-4 py-2 bg-yellow-600 text-white rounded-lg hover:bg-yellow-700 text-sm font-medium">
            Volver al inicio
          </button>
        </div>
      </div>
    );
  }

  const rolLabel = {
    CLIENTE: 'Cliente',
    COMERCIANTE: 'Comerciante',
    ADMIN: 'Administrador',
  };

  const rolColor = {
    CLIENTE: 'bg-blue-100 text-blue-700',
    COMERCIANTE: 'bg-green-100 text-green-700',
    ADMIN: 'bg-purple-100 text-purple-700',
  };

  return (
    <div className="max-w-2xl mx-auto px-4 sm:px-6 py-6 animate-fade-in">

      {/* Encabezado */}
      <div className="flex items-center justify-between mb-6">
        <button
          onClick={() => onNavigate('inicio')}
          className="flex items-center gap-1.5 text-gray-600 hover:text-primary-600 transition-colors"
        >
          <ArrowLeft className="h-5 w-5" />
          <span className="text-sm font-medium">Volver</span>
        </button>
      </div>

      {/* Mensajes */}
      {mensaje && (
        <div className="mb-4 px-4 py-3 bg-green-50 border border-green-200 rounded-xl text-green-700 text-sm font-medium animate-slide-down">
          {mensaje}
        </div>
      )}
      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-red-700 text-sm font-medium animate-slide-down">
          {error}
        </div>
      )}

      {/* Tarjeta de perfil */}
      <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">

        {/* Avatar y nombre */}
        <div className="bg-gradient-to-r from-primary-500 to-primary-600 px-6 py-8 text-center">
          <div className="h-20 w-20 rounded-full bg-white/20 backdrop-blur-sm mx-auto flex items-center justify-center shadow-lg mb-3">
            <span className="text-3xl font-bold text-white">
              {perfil.nombre ? perfil.nombre.substring(0, 1).toUpperCase() : perfil.email?.substring(0, 1).toUpperCase() || 'U'}
            </span>
          </div>

          {editandoNombre ? (
            <div className="flex items-center justify-center gap-2">
              <input
                type="text"
                value={nombreInput}
                onChange={(e) => setNombreInput(e.target.value)}
                className="px-3 py-1.5 rounded-lg border border-white/50 bg-white/90 text-gray-800 text-center text-lg font-semibold focus:outline-none focus:ring-2 focus:ring-white/60"
                autoFocus
                onKeyDown={(e) => { if (e.key === 'Enter') guardarNombre(); if (e.key === 'Escape') cancelarEdicionNombre(); }}
              />
              <button onClick={guardarNombre} className="p-1.5 rounded-lg bg-white/20 hover:bg-white/30 text-white transition-colors" title="Guardar">
                <Save className="h-4 w-4" />
              </button>
            </div>
          ) : (
            <div className="flex items-center justify-center gap-2">
              <h1 className="text-xl font-bold text-white">{perfil.nombre || perfil.email?.split('@')[0]}</h1>
              <button onClick={() => setEditandoNombre(true)} className="p-1 rounded-lg bg-white/20 hover:bg-white/30 text-white transition-colors" title="Editar nombre">
                <PenLine className="h-3.5 w-3.5" />
              </button>
            </div>
          )}

          <span className={`inline-block mt-2 px-3 py-0.5 rounded-full text-xs font-bold ${rolColor[perfil.rol] || 'bg-gray-100 text-gray-700'}`}>
            {rolLabel[perfil.rol] || perfil.rol}
          </span>
        </div>

        {/* Datos del usuario */}
        <div className="px-6 py-5 space-y-4">
          <div className="flex items-start gap-3">
            <Mail className="h-5 w-5 text-gray-400 mt-0.5" />
            <div>
              <p className="text-xs text-gray-500 font-medium uppercase tracking-wide">Email</p>
              <p className="text-sm text-gray-800">{perfil.email}</p>
            </div>
          </div>

          <div className="flex items-start gap-3">
            <Shield className="h-5 w-5 text-gray-400 mt-0.5" />
            <div>
              <p className="text-xs text-gray-500 font-medium uppercase tracking-wide">Rol</p>
              <p className="text-sm text-gray-800">{rolLabel[perfil.rol] || perfil.rol}</p>
            </div>
          </div>

          <div className="flex items-start gap-3">
            <MapPin className="h-5 w-5 text-gray-400 mt-0.5" />
            <div>
              <p className="text-xs text-gray-500 font-medium uppercase tracking-wide">Ubicacion</p>
              <p className="text-sm text-gray-800">
                {perfil.latitud && perfil.longitud
                  ? `${perfil.latitud.toFixed(4)}, ${perfil.longitud.toFixed(4)}`
                  : 'No configurada'}
              </p>
              {perfil.radioBusquedaKm && (
                <p className="text-xs text-gray-500">Radio de busqueda: {perfil.radioBusquedaKm} km</p>
              )}
            </div>
          </div>

          <div className="flex items-start gap-3">
            <Calendar className="h-5 w-5 text-gray-400 mt-0.5" />
            <div>
              <p className="text-xs text-gray-500 font-medium uppercase tracking-wide">Miembro desde</p>
              <p className="text-sm text-gray-800">
                {perfil.fechaCreacion
                  ? new Date(perfil.fechaCreacion + (perfil.fechaCreacion.includes('T') ? '' : 'T00:00:00')).toLocaleDateString('es-BO', { year: 'numeric', month: 'long', day: 'numeric' })
                  : 'Desconocida'}
              </p>
            </div>
          </div>
        </div>

        {/* Divisor */}
        <div className="border-t border-gray-100" />

        {/* Acciones */}
        <div className="px-6 py-5 space-y-3">
          <h2 className="text-sm font-bold text-gray-700 uppercase tracking-wide mb-3">Configuracion de cuenta</h2>

          {/* Acceso rapido */}
          <div className="grid grid-cols-2 gap-3">
            <button onClick={() => onNavigate('pedidos')}
              className="bg-blue-50 border border-blue-100 rounded-xl p-4 text-left hover:bg-blue-100 transition-colors">
              <p className="text-sm font-semibold text-blue-800">Mis Pedidos</p>
              <p className="text-xs text-blue-600 mt-0.5">Ver historial de compras</p>
            </button>
            <button onClick={() => onNavigate('reservas')}
              className="bg-purple-50 border border-purple-100 rounded-xl p-4 text-left hover:bg-purple-100 transition-colors">
              <p className="text-sm font-semibold text-purple-800">Mis Reservas</p>
              <p className="text-xs text-purple-600 mt-0.5">Gestiona tus reservas</p>
            </button>
          </div>
          <div className="border-t border-gray-100 my-4" />

          {/* Cambiar rol */}
          <div className="bg-gray-50 rounded-xl p-4">
            <div className="flex items-start justify-between gap-4">
              <div>
                <p className="text-sm font-semibold text-gray-800">Tipo de cuenta</p>
                <p className="text-xs text-gray-500 mt-0.5">
                  {perfil.rol === 'CLIENTE'
                    ? 'Actualmente eres Cliente. Puedes cambiarte a Comerciante para vender productos.'
                    : 'Actualmente eres Comerciante. Puedes cambiarte a Cliente para solo comprar.'}
                </p>
              </div>
              <button
                onClick={handleCambiarRol}
                className="shrink-0 px-3 py-2 text-xs font-bold rounded-lg transition-colors
                  ${perfil.rol === 'CLIENTE'
                    ? 'bg-green-600 hover:bg-green-700 text-white'
                    : 'bg-blue-600 hover:bg-blue-700 text-white'}"
              >
                {perfil.rol === 'CLIENTE' ? 'Volverse Comerciante' : 'Volverse Cliente'}
              </button>
            </div>
          </div>

          {/* Eliminar cuenta */}
          <div className="bg-red-50 border border-red-100 rounded-xl p-4">
            <div className="flex items-start justify-between gap-4">
              <div className="flex items-start gap-3">
                <AlertTriangle className="h-5 w-5 text-red-500 mt-0.5 shrink-0" />
                <div>
                  <p className="text-sm font-semibold text-red-800">Eliminar cuenta</p>
                  <p className="text-xs text-red-600 mt-0.5">
                    Esta accion desactivara tu cuenta permanentemente. Si eres comerciante, tus tiendas y productos tambien se desactivaran.
                  </p>
                </div>
              </div>
              <button
                onClick={handleEliminarCuenta}
                className="shrink-0 px-3 py-2 text-xs font-bold text-white bg-red-600 hover:bg-red-700 rounded-lg transition-colors"
              >
                <Trash2 className="h-3.5 w-3.5 inline mr-1" />
                Eliminar
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Perfil;
