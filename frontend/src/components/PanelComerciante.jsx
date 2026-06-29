import React, { useState, useEffect } from 'react';
import { Package, AlertCircle, Plus, LayoutDashboard, Archive, ClipboardList, CalendarCheck } from 'lucide-react';
import authService from '../services/authService';
import productosService from '../services/productosService';
import comerciosService from '../services/comerciosService';
import FormularioProducto from './FormularioProducto';
import TablaInventario from './TablaInventario';

const TABS = [
  { id: 'inventario', label: 'Inventario', icon: Archive },
  { id: 'productos', label: 'Productos', icon: Package },
  { id: 'pedidos', label: 'Pedidos Recibidos', icon: ClipboardList },
  { id: 'reservas', label: 'Reservas Recibidas', icon: CalendarCheck },
];

function PanelComerciante({ onNavigate }) {
  const [tabActivo, setTabActivo] = useState('inventario');
  const [productos, setProductos] = useState([]);
  const [comercioId, setComercioId] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [productoEditando, setProductoEditando] = useState(null);

  useEffect(() => {
    const cargarDatos = async () => {
      setCargando(true);
      setError('');
      try {
        const usuario = authService.obtenerUsuarioActual();
        if (!usuario) {
          setError('Debes iniciar sesion para gestionar tus productos.');
          setCargando(false);
          return;
        }

        const resComercios = await comerciosService.obtenerMisComercios();
        if (resComercios.exito && resComercios.datos && resComercios.datos.length > 0) {
          const miComercio = resComercios.datos[0];
          setComercioId(miComercio.id);

          const resProductos = await productosService.obtenerPorComercio(miComercio.id);
          if (resProductos.exito) {
            setProductos(resProductos.datos || []);
          }
        } else {
          setError('No tienes un comercio registrado. Al registrarse como vendedor se crea uno automaticamente.');
        }
      } catch (err) {
        console.error('Error al cargar datos:', err);
        setError('Error al cargar los datos. Intenta recargar la pagina.');
      } finally {
        setCargando(false);
      }
    };

    cargarDatos();
  }, []);

  const handleEditar = (producto) => {
    setProductoEditando(producto);
    setTabActivo('productos');
  };

  const handleNuevoProducto = () => {
    setProductoEditando(null);
    setTabActivo('productos');
  };

  const handleGuardado = (productoGuardado) => {
    if (productoEditando) {
      setProductos(productos.map(p => p.id === productoEditando.id ? productoGuardado : p));
    } else {
      setProductos([...productos, productoGuardado]);
    }
    setProductoEditando(null);
    setTabActivo('inventario');
  };

  const handleEliminado = (id) => {
    setProductos(productos.filter(p => p.id !== id));
  };

  const handleCancelarFormulario = () => {
    setProductoEditando(null);
    setTabActivo('inventario');
  };

  if (cargando) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex items-center justify-center py-20">
          <div className="animate-spin h-10 w-10 border-4 border-primary-500 border-t-transparent rounded-full"></div>
          <span className="ml-4 text-gray-600">Cargando tu comercio...</span>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">

      {/* Encabezado */}
      <div className="mb-6">
        <h2 className="text-2xl font-bold text-gray-900 flex items-center gap-2">
          <LayoutDashboard className="h-6 w-6 text-primary-500" />
          Panel Comerciante
        </h2>
        <p className="text-gray-600 mt-1">Administra los productos de tu comercio local</p>
      </div>

      {/* Error global */}
      {error && (
        <div className="mb-6 flex items-center gap-3 p-4 rounded-xl bg-red-50 border border-red-100 text-red-700 text-sm">
          <AlertCircle className="h-5 w-5 shrink-0" />
          <span>{error}</span>
        </div>
      )}

      {/* Tabs */}
      <div className="flex items-center gap-1 mb-6 bg-gray-100 p-1 rounded-xl w-fit">
        {TABS.map(tab => (
          <button
            key={tab.id}
            onClick={() => {
              if (tab.id === 'pedidos') {
                onNavigate && onNavigate('gestion-pedidos');
              } else if (tab.id === 'reservas') {
                onNavigate && onNavigate('gestion-reservas');
              } else {
                setTabActivo(tab.id);
              }
            }}
            className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-semibold transition-all ${
              tabActivo === tab.id
                ? 'bg-white text-primary-700 shadow-sm'
                : 'text-gray-600 hover:text-gray-800'
            }`}
          >
            <tab.icon className="h-4 w-4" />
            {tab.label}
          </button>
        ))}
      </div>

      {/* Contenido según tab */}
      {tabActivo === 'inventario' && (
        <div className="space-y-4">
          <div className="flex justify-end">
            <button
              onClick={handleNuevoProducto}
              className="flex items-center gap-2 px-4 py-2 bg-primary-600 text-white rounded-lg font-semibold hover:bg-primary-700 transition-colors text-sm"
            >
              <Plus className="h-4 w-4" />
              Nuevo producto
            </button>
          </div>
          <TablaInventario
            productos={productos}
            onEditar={handleEditar}
            onEliminado={handleEliminado}
          />
        </div>
      )}

      {tabActivo === 'productos' && (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
          <h3 className="text-lg font-bold text-gray-900 mb-5">
            {productoEditando ? 'Editar producto' : 'Nuevo producto'}
          </h3>
          <FormularioProducto
            producto={productoEditando}
            comercioId={comercioId}
            onGuardado={handleGuardado}
            onCancelar={handleCancelarFormulario}
          />
        </div>
      )}
    </div>
  );
}

export default PanelComerciante;
