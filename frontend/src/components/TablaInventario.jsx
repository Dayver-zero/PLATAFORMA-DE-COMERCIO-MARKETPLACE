import React, { useState, useMemo } from 'react';
import { Edit, Trash2, Package, Search, AlertCircle } from 'lucide-react';
import productosService from '../services/productosService';

const API_HOST = (import.meta.env.VITE_API_URL || 'http://localhost:8080').replace(/\/api\/?$/, '');

function TablaInventario({ productos, onEditar, onEliminado }) {
  const [busqueda, setBusqueda] = useState('');
  const [error, setError] = useState('');

  const productosFiltrados = useMemo(() => {
    if (!busqueda.trim()) return productos;
    const termino = busqueda.toLowerCase();
    return productos.filter(p =>
      p.nombre?.toLowerCase().includes(termino) ||
      p.categoria?.toLowerCase().includes(termino)
    );
  }, [productos, busqueda]);

  const handleEliminar = async (id) => {
    if (!window.confirm('¿Estas seguro de eliminar este producto?')) return;
    setError('');
    try {
      const res = await productosService.eliminar(id);
      if (res.exito) {
        onEliminado && onEliminado(id);
      } else {
        setError(res.mensaje || 'Error al eliminar producto');
      }
    } catch (err) {
      setError(err?.mensaje || 'Error de conexion');
    }
  };

  const stockBajo = (stock) => stock != null && stock <= 5;
  const stockAgotado = (stock) => stock == null || stock === 0;

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">

      {/* Encabezado */}
      <div className="p-5 border-b border-gray-100">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
          <div>
            <h3 className="text-lg font-bold text-gray-900">Inventario</h3>
            <p className="text-sm text-gray-500">{productos.length} producto(s) registrado(s)</p>
          </div>

          {/* Buscador */}
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
            <input
              type="text"
              value={busqueda}
              onChange={(e) => setBusqueda(e.target.value)}
              placeholder="Buscar producto..."
              className="w-full sm:w-56 pl-9 pr-3 py-2 border border-gray-200 rounded-lg text-sm focus:ring-2 focus:ring-primary-500 focus:border-transparent"
            />
          </div>
        </div>
      </div>

      {/* Error */}
      {error && (
        <div className="mx-5 mt-4 flex items-center gap-2 p-3 rounded-xl bg-red-50 border border-red-100 text-red-700 text-sm">
          <AlertCircle className="h-4 w-4 shrink-0" />
          <span>{error}</span>
        </div>
      )}

      {/* Tabla */}
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Producto</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Precio</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Stock</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Estado</th>
              <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase">Acciones</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {productosFiltrados.map((producto) => (
              <tr key={producto.id} className="hover:bg-gray-50 transition-colors">
                <td className="px-4 py-3">
                  <div className="flex items-center gap-3">
                    {/* Imagen miniatura */}
                    {producto.urlImagen ? (
                      <img
                        src={producto.urlImagen.startsWith('http') ? producto.urlImagen : `${API_HOST}${producto.urlImagen}`}
                        alt={producto.nombre}
                        className="h-10 w-10 rounded-lg object-cover border border-gray-200"
                        onError={(e) => { e.target.style.display = 'none'; }}
                      />
                    ) : (
                      <div className="h-10 w-10 rounded-lg bg-gray-100 flex items-center justify-center">
                        <Package className="h-5 w-5 text-gray-400" />
                      </div>
                    )}
                    <div>
                      <p className="font-medium text-gray-900 text-sm">{producto.nombre}</p>
                      <p className="text-xs text-gray-500">{producto.categoria}</p>
                    </div>
                  </div>
                </td>
                <td className="px-4 py-3 text-sm text-gray-700">
                  Bs. {producto.precio?.toFixed(2)}
                </td>
                <td className="px-4 py-3">
                  <span className={`inline-flex px-2 py-1 rounded-full text-xs font-medium ${
                    stockAgotado(producto.stock)
                      ? 'bg-red-100 text-red-800'
                      : stockBajo(producto.stock)
                      ? 'bg-orange-100 text-orange-800'
                      : 'bg-green-100 text-green-800'
                  }`}>
                    {producto.stock ?? 0}
                  </span>
                </td>
                <td className="px-4 py-3">
                  <span className={`inline-flex px-2 py-1 rounded-full text-xs font-medium ${
                    producto.estado === 'DISPONIBLE'
                      ? 'bg-green-100 text-green-800'
                      : producto.estado === 'AGOTADO'
                      ? 'bg-red-100 text-red-800'
                      : 'bg-gray-100 text-gray-800'
                  }`}>
                    {producto.estado}
                  </span>
                </td>
                <td className="px-4 py-3 text-right">
                  <div className="flex items-center justify-end gap-1">
                    <button
                      onClick={() => onEditar && onEditar(producto)}
                      className="p-2 text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                      title="Editar producto"
                    >
                      <Edit className="h-4 w-4" />
                    </button>
                    <button
                      onClick={() => handleEliminar(producto.id)}
                      className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                      title="Eliminar producto"
                    >
                      <Trash2 className="h-4 w-4" />
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Vacío */}
      {productosFiltrados.length === 0 && (
        <div className="p-8 text-center text-gray-500">
          <Package className="h-12 w-12 mx-auto mb-3 text-gray-300" />
          <p className="font-medium">
            {busqueda ? 'No se encontraron productos' : 'No hay productos registrados'}
          </p>
          {!busqueda && (
            <p className="text-sm mt-1">Agrega tu primer producto en la seccion "Productos"</p>
          )}
        </div>
      )}
    </div>
  );
}

export default TablaInventario;
