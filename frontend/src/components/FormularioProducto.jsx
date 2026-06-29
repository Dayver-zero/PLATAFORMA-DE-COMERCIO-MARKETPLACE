import React, { useState, useRef, useCallback } from 'react';
import { Save, X, Upload, Image as ImageIcon, Link } from 'lucide-react';
import productosService from '../services/productosService';

const CATEGORIAS = [
  'ROPA', 'ACCESORIOS', 'COMIDA', 'BEBIDA', 'ELECTRONICA',
  'HOGAR', 'DEPORTES', 'BELLEZA', 'SALUD', 'LIBROS', 'JUGUETES', 'OTROS'
];

const ESTADOS = ['DISPONIBLE', 'AGOTADO', 'DESCONTINUADO'];

const API_HOST = (import.meta.env.VITE_API_URL || 'http://localhost:8080').replace(/\/api\/?$/, '');

function FormularioProducto({ producto, comercioId, onGuardado, onCancelar }) {
  const esEdicion = !!producto;
  const fileInputRef = useRef(null);
  const dropRef = useRef(null);

  const [formData, setFormData] = useState({
    nombre: producto?.nombre || '',
    descripcion: producto?.descripcion || '',
    precio: producto?.precio || '',
    stock: producto?.stock ?? '',
    urlImagen: producto?.urlImagen || '',
    categoria: producto?.categoria || 'OTROS',
    estado: producto?.estado || 'DISPONIBLE',
    etiquetasInteligentes: (producto?.etiquetasInteligentes || []).join(', '),
  });

  const [imagenSubida, setImagenSubida] = useState(!!producto?.urlImagen);
  const [modoImagen, setModoImagen] = useState(producto?.urlImagen ? 'url' : 'url');
  const [subiendo, setSubiendo] = useState(false);
  const [guardando, setGuardando] = useState(false);
  const [dragOver, setDragOver] = useState(false);
  const [previewUrl, setPreviewUrl] = useState(producto?.urlImagen || '');
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleImageUrlChange = (e) => {
    const url = e.target.value;
    setFormData(prev => ({ ...prev, urlImagen: url }));
    setPreviewUrl(url);
  };

  const subirArchivo = useCallback(async (archivo) => {
    if (!archivo) return;
    const tiposValidos = ['image/jpeg', 'image/png', 'image/webp', 'image/gif'];
    if (!tiposValidos.includes(archivo.type)) {
      setError('Solo se permiten JPG, PNG, WEBP y GIF');
      return;
    }
    if (archivo.size > 5 * 1024 * 1024) {
      setError('La imagen no debe superar los 5MB');
      return;
    }
    setSubiendo(true);
    setError('');
    try {
      const response = await productosService.subirImagen(archivo);
      if (response.exito && response.datos?.url) {
        setFormData(prev => ({ ...prev, urlImagen: response.datos.url }));
        setPreviewUrl(response.datos.url);
        setImagenSubida(true);
      } else {
        setError(response.mensaje || 'Error al subir imagen');
      }
    } catch (err) {
      setError(err?.mensaje || 'Error al subir imagen');
    } finally {
      setSubiendo(false);
    }
  }, []);

  const handleFileSelect = (e) => {
    const archivo = e.target.files?.[0];
    if (archivo) subirArchivo(archivo);
  };

  const handleDrop = useCallback((e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragOver(false);
    const archivo = e.dataTransfer?.files?.[0];
    if (archivo) subirArchivo(archivo);
  }, [subirArchivo]);

  const handleDragOver = useCallback((e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragOver(true);
  }, []);

  const handleDragLeave = useCallback((e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragOver(false);
  }, []);

  const quitarImagen = () => {
    setFormData(prev => ({ ...prev, urlImagen: '' }));
    setPreviewUrl('');
    setImagenSubida(false);
    if (fileInputRef.current) fileInputRef.current.value = '';
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setGuardando(true);

    const etiquetasArray = formData.etiquetasInteligentes
      .split(',')
      .map(t => t.trim().toLowerCase())
      .filter(t => t.length > 0);

    const payload = {
      nombre: formData.nombre,
      descripcion: formData.descripcion,
      precio: parseFloat(formData.precio),
      stock: parseInt(formData.stock),
      urlImagen: formData.urlImagen || null,
      categoria: formData.categoria,
      estado: formData.estado,
      etiquetasInteligentes: etiquetasArray,
    };

    try {
      let response;
      if (esEdicion) {
        response = await productosService.actualizar(producto.id, payload);
      } else {
        response = await productosService.crear(payload);
      }
      if (response.exito) {
        onGuardado && onGuardado(response.datos);
      } else {
        setError(response.mensaje || 'Error al guardar producto');
      }
    } catch (err) {
      setError(err?.mensaje || 'Error de conexión');
    } finally {
      setGuardando(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-5">

      {error && (
        <div className="p-3 rounded-xl bg-red-50 border border-red-100 text-red-700 text-sm">
          {error}
        </div>
      )}

      {/* Imagen del producto */}
      <div>
        <label className="block text-sm font-semibold text-gray-700 mb-2">
          Imagen del producto
        </label>

        {/* Preview */}
        {previewUrl && (
          <div className="relative mb-3 inline-block">
            <img
              src={previewUrl.startsWith('http') ? previewUrl : `${API_HOST}${previewUrl}`}
              alt="Preview"
              className="h-32 w-32 object-cover rounded-xl border border-gray-200 shadow-sm"
              onError={(e) => { e.target.style.display = 'none'; }}
            />
            <button
              type="button"
              onClick={quitarImagen}
              className="absolute -top-2 -right-2 p-1 bg-red-500 text-white rounded-full shadow hover:bg-red-600 transition-colors"
            >
              <X className="h-3 w-3" />
            </button>
          </div>
        )}

        {/* Toggle URL / Archivo */}
        <div className="flex gap-2 mb-3">
          <button
            type="button"
            onClick={() => setModoImagen('url')}
            className={`px-3 py-1.5 text-xs font-medium rounded-lg transition-colors ${
              modoImagen === 'url'
                ? 'bg-primary-100 text-primary-700 border border-primary-200'
                : 'bg-gray-100 text-gray-600 border border-gray-200 hover:bg-gray-200'
            }`}
          >
            <Link className="h-3 w-3 inline mr-1" />
            Usar URL
          </button>
          <button
            type="button"
            onClick={() => { setModoImagen('archivo'); fileInputRef.current?.click(); }}
            className={`px-3 py-1.5 text-xs font-medium rounded-lg transition-colors ${
              modoImagen === 'archivo'
                ? 'bg-primary-100 text-primary-700 border border-primary-200'
                : 'bg-gray-100 text-gray-600 border border-gray-200 hover:bg-gray-200'
            }`}
          >
            <Upload className="h-3 w-3 inline mr-1" />
            Subir archivo
          </button>
        </div>

        {/* URL input */}
        {modoImagen === 'url' && (
          <input
            type="url"
            value={formData.urlImagen}
            onChange={handleImageUrlChange}
            placeholder="https://ejemplo.com/imagen.jpg"
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent text-sm"
          />
        )}

        {/* Drag & Drop zone */}
        <div
          ref={dropRef}
          onDrop={handleDrop}
          onDragOver={handleDragOver}
          onDragLeave={handleDragLeave}
          onClick={() => fileInputRef.current?.click()}
          className={`mt-2 border-2 border-dashed rounded-xl p-6 text-center cursor-pointer transition-colors ${
            dragOver
              ? 'border-primary-400 bg-primary-50'
              : 'border-gray-300 hover:border-gray-400 bg-gray-50 hover:bg-gray-100'
          }`}
        >
          <input
            ref={fileInputRef}
            type="file"
            accept="image/jpeg,image/png,image/webp,image/gif"
            onChange={handleFileSelect}
            className="hidden"
          />
          {subiendo ? (
            <div className="flex items-center justify-center gap-2">
              <div className="h-5 w-5 border-2 border-primary-500 border-t-transparent rounded-full animate-spin" />
              <span className="text-sm text-gray-600">Subiendo imagen...</span>
            </div>
          ) : (
            <div>
              <ImageIcon className="h-8 w-8 text-gray-400 mx-auto mb-2" />
              <p className="text-sm text-gray-600 font-medium">
                Arrastra una imagen aquí o haz clic para seleccionar
              </p>
              <p className="text-xs text-gray-400 mt-1">JPG, PNG, WEBP, GIF — Max 5MB</p>
            </div>
          )}
        </div>
      </div>

      {/* Nombre */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Nombre del producto *
        </label>
        <input
          type="text"
          name="nombre"
          value={formData.nombre}
          onChange={handleChange}
          required
          className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          placeholder="Ej: Paraguas Plegable"
        />
      </div>

      {/* Descripción */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Descripción *
        </label>
        <textarea
          name="descripcion"
          value={formData.descripcion}
          onChange={handleChange}
          required
          rows="3"
          className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          placeholder="Describe el producto..."
        />
      </div>

      {/* Precio y Stock */}
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Precio (Bs.) *
          </label>
          <input
            type="number"
            name="precio"
            value={formData.precio}
            onChange={handleChange}
            required
            step="0.01"
            min="0"
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
            placeholder="0.00"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Stock *
          </label>
          <input
            type="number"
            name="stock"
            value={formData.stock}
            onChange={handleChange}
            required
            min="0"
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
            placeholder="0"
          />
        </div>
      </div>

      {/* Categoría y Estado */}
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Categoría *
          </label>
          <select
            name="categoria"
            value={formData.categoria}
            onChange={handleChange}
            required
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          >
            {CATEGORIAS.map(cat => (
              <option key={cat} value={cat}>{cat}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Estado *
          </label>
          <select
            name="estado"
            value={formData.estado}
            onChange={handleChange}
            required
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          >
            {ESTADOS.map(est => (
              <option key={est} value={est}>{est}</option>
            ))}
          </select>
        </div>
      </div>

      {/* Etiquetas inteligentes */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Etiquetas inteligentes
        </label>
        <input
          type="text"
          name="etiquetasInteligentes"
          value={formData.etiquetasInteligentes}
          onChange={handleChange}
          className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
          placeholder="Ej: lluvia, frio, proteccion (separadas por comas)"
        />
        <p className="text-xs text-gray-500 mt-1">
          Ayudan a recomendar el producto segun el clima
        </p>
      </div>

      {/* Botones */}
      <div className="flex gap-2 pt-2">
        <button
          type="submit"
          disabled={guardando}
          className="flex-1 bg-primary-600 text-white py-2.5 px-4 rounded-lg font-semibold hover:bg-primary-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
        >
          {guardando ? (
            <>
              <div className="h-5 w-5 border-2 border-white border-t-transparent rounded-full animate-spin mr-2" />
              Guardando...
            </>
          ) : (
            <>
              <Save className="h-5 w-5 mr-2" />
              {esEdicion ? 'Actualizar producto' : 'Agregar producto'}
            </>
          )}
        </button>
        {onCancelar && (
          <button
            type="button"
            onClick={onCancelar}
            className="px-4 py-2.5 border border-gray-300 rounded-lg font-medium text-gray-700 hover:bg-gray-100 transition-colors"
          >
            <X className="h-5 w-5" />
          </button>
        )}
      </div>
    </form>
  );
}

export default FormularioProducto;
