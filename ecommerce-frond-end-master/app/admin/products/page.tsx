'use client';

import { useEffect, useState } from 'react';
import { useAuth } from '@/context/auth';
import {
  getProductsPaginated,
  getAllCategoriesActive,
  updateProductStatus,
  createProduct,
  updateProduct,
  Product,
  Category,
  PageResponse,
  setAuthToken
} from '@/lib/graphql/admin';
import { currencyFormatter as formatCurrency } from '@/lib/currencyFormatter';
import { getProductStatusLabel } from '@/lib/status';
import { PlusIcon, EditIcon, PackageIcon } from '@/components/icons';
import { ConfirmModal } from '@/components/ConfirmModal';

interface ProductInput {
  name: string;
  price: number;
  description?: string;
  categoryId: number;
  discount?: number;
  imagesUrl?: string[];
  colors?: string[];
  sizes?: string[];
}

export default function AdminProductsPage() {
  const { token } = useAuth();
  const [products, setProducts] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterCategory, setFilterCategory] = useState('');
  const [filterStatus, setFilterStatus] = useState('');
  const [showToggleModal, setShowToggleModal] = useState(false);
  const [togglingProduct, setTogglingProduct] = useState<Product | null>(null);
  const [toggleLoading, setToggleLoading] = useState(false);

  const [formData, setFormData] = useState<ProductInput>({
    name: '',
    price: 0,
    description: '',
    categoryId: 0,
    discount: 0,
    imagesUrl: [],
    colors: [],
    sizes: []
  });

  useEffect(() => {
    if (token) {
      setAuthToken(token);
      fetchData();
    }
  }, [token]);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [productsData, categoriesData] = await Promise.all([
        getProductsPaginated(0, 100),
        getAllCategoriesActive()
      ]);
      setProducts(productsData.content);
      setCategories(categoriesData);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleToggleStatus = (product: Product) => {
    setTogglingProduct(product);
    setShowToggleModal(true);
  };

  const confirmToggleStatus = async () => {
    if (!togglingProduct) return;

    setToggleLoading(true);
    try {
      const newStatus = togglingProduct.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
      const success = await updateProductStatus(togglingProduct.id, newStatus);
      if (success) {
        setProducts(products.map(p =>
          p.id === togglingProduct.id ? { ...p, status: newStatus } : p
        ));
      }
    } finally {
      setToggleLoading(false);
      setShowToggleModal(false);
      setTogglingProduct(null);
    }
  };

  const handleEdit = (product: Product) => {
    setEditingProduct(product);
    setFormData({
      name: product.name,
      price: product.price,
      description: product.description || '',
      categoryId: product.category?.id ? parseInt(product.category.id) : 0,
      discount: product.discount || 0,
      imagesUrl: product.imagesUrl || [],
      colors: product.colors || [],
      sizes: product.sizes || []
    });
    setShowModal(true);
  };

  const handleCreate = () => {
    setEditingProduct(null);
    setFormData({
      name: '',
      price: 0,
      description: '',
      categoryId: categories[0] ? parseInt(categories[0].id) : 0,
      discount: 0,
      imagesUrl: [],
      colors: [],
      sizes: []
    });
    setShowModal(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    let success;
    if (editingProduct) {
      success = await updateProduct(editingProduct.id, formData);
    } else {
      success = await createProduct(formData);
    }

    if (success) {
      setShowModal(false);
      fetchData();
    }
  };

  const getStatusBadge = (status: string) => {
    switch (status?.toUpperCase()) {
      case 'ACTIVE':
        return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
      case 'INACTIVE':
        return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
      default:
        return 'bg-neutral-100 text-neutral-800 dark:bg-neutral-900 dark:text-neutral-200';
    }
  };

  const filteredProducts = products.filter(product => {
    const matchesSearch = product.name.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory = !filterCategory || product.category?.id === filterCategory;
    const matchesStatus = !filterStatus || product.status === filterStatus;
    return matchesSearch && matchesCategory && matchesStatus;
  });

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-black dark:text-white">Productos</h1>
          <p className="text-neutral-500 dark:text-neutral-400 mt-1">
            {products.length} productos en el catálogo
          </p>
        </div>
        <button
          onClick={handleCreate}
          className="px-6 py-3 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-lg hover:from-blue-700 hover:to-blue-800 transition shadow-blue-500/30 flex items-center gap-2"
        >
          <PlusIcon className="w-5 h-5" />
          Nuevo Producto
        </button>
      </div>

      {/* Filters */}
      <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-6">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
              Buscar
            </label>
            <input
              type="text"
              placeholder="Nombre del producto..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
              Categoría
            </label>
            <select
              value={filterCategory}
              onChange={(e) => setFilterCategory(e.target.value)}
              className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="">Todas</option>
              {categories.map(cat => (
                <option key={cat.id} value={cat.id}>{cat.name}</option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
              Estado
            </label>
            <select
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="">Todos</option>
              <option value="ACTIVE">Activo</option>
              <option value="INACTIVE">Inactivo</option>
            </select>
          </div>
          <div className="flex items-end">
            <button
              onClick={() => { setSearchTerm(''); setFilterCategory(''); setFilterStatus(''); }}
              className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg text-neutral-700 dark:text-neutral-300 hover:bg-neutral-100 dark:hover:bg-neutral-700 transition"
            >
              Limpiar Filtros
            </button>
          </div>
        </div>
      </div>

      {/* Products Table */}
      <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-neutral-50 dark:bg-neutral-900">
              <tr>
                <th className="px-6 py-4 text-left text-xs font-semibold text-neutral-500 dark:text-neutral-400 uppercase tracking-wider">
                  Producto
                </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-neutral-500 dark:text-neutral-400 uppercase tracking-wider">
                  Categoría
                </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-neutral-500 dark:text-neutral-400 uppercase tracking-wider">
                  Precio
                </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-neutral-500 dark:text-neutral-400 uppercase tracking-wider">
                  Estado
                </th>
                <th className="px-6 py-4 text-right text-xs font-semibold text-neutral-500 dark:text-neutral-400 uppercase tracking-wider">
                  Acciones
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-neutral-200 dark:divide-neutral-700">
              {filteredProducts.map((product) => (
                <tr key={product.id} className="hover:bg-neutral-50 dark:hover:bg-neutral-700/50 transition">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-4">
                      <div className="w-12 h-12 bg-neutral-100 dark:bg-neutral-900 rounded-lg overflow-hidden flex-shrink-0">
                        {product.imagesUrl?.[0] ? (
                          <img
                            src={product.imagesUrl[0]}
                            alt={product.name}
                            className="w-full h-full object-cover"
                          />
                        ) : (
                          <div className="w-full h-full flex items-center justify-center text-neutral-400">
                            <PackageIcon className="w-6 h-6" />
                          </div>
                        )}
                      </div>
                      <div>
                        <p className="font-medium text-black dark:text-white">
                          {product.name}
                        </p>
                        <p className="text-sm text-neutral-500 dark:text-neutral-400">
                          ID: {product.id}
                        </p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <span className="px-3 py-1 bg-neutral-100 dark:bg-neutral-900 rounded-full text-sm text-neutral-700 dark:text-neutral-300">
                      {product.category?.name || 'Sin categoría'}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <div>
                      <p className="font-medium text-black dark:text-white">
                        {formatCurrency(product.price)}
                      </p>
                      {product.discount && product.discount > 0 && (
                        <p className="text-sm text-green-600">
                          -{product.discount}% descuento
                        </p>
                      )}
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusBadge(product.status)}`}>
                      {getProductStatusLabel(product.status)}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-right">
                    <div className="flex justify-end gap-2">
                      <button
                        onClick={() => handleEdit(product)}
                        className="p-2 text-blue-600 hover:bg-blue-100 dark:hover:bg-blue-900/30 rounded-lg transition"
                        title="Editar"
                      >
                        <EditIcon className="w-5 h-5" />
                      </button>
                      <button
                        onClick={() => handleToggleStatus(product)}
                        className={`px-3 py-1 rounded-lg text-sm font-medium transition ${
                          product.status === 'ACTIVE' 
                            ? 'text-orange-600 hover:bg-orange-100 dark:hover:bg-orange-900/30' 
                            : 'text-green-600 hover:bg-green-100 dark:hover:bg-green-900/30'
                        }`}
                        title={product.status === 'ACTIVE' ? 'Desactivar' : 'Activar'}
                      >
                        {product.status === 'ACTIVE' ? 'Desactivar' : 'Activar'}
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {filteredProducts.length === 0 && (
            <div className="text-center py-12">
              <div className="flex justify-center mb-4">
                <PackageIcon className="w-12 h-12 text-neutral-400" />
              </div>
              <p className="text-neutral-500 dark:text-neutral-400">No se encontraron productos</p>
            </div>
          )}
        </div>
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-2xl shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
            <div className="p-6 border-b dark:border-neutral-700">
              <h2 className="text-2xl font-bold text-black dark:text-white">
                {editingProduct ? 'Editar Producto' : 'Nuevo Producto'}
              </h2>
            </div>
            <form onSubmit={handleSubmit} className="p-6 space-y-6">
              <div className="grid grid-cols-2 gap-4">
                <div className="col-span-2">
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Nombre *
                  </label>
                  <input
                    type="text"
                    required
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    className="w-full px-4 py-3 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Precio *
                  </label>
                  <input
                    type="number"
                    required
                    min="0"
                    step="0.01"
                    value={formData.price}
                    onChange={(e) => setFormData({ ...formData, price: parseFloat(e.target.value) })}
                    className="w-full px-4 py-3 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Descuento (%)
                  </label>
                  <input
                    type="number"
                    min="0"
                    max="100"
                    value={formData.discount || 0}
                    onChange={(e) => setFormData({ ...formData, discount: parseInt(e.target.value) })}
                    className="w-full px-4 py-3 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>
                <div className="col-span-2">
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Categoría *
                  </label>
                  <select
                    required
                    value={formData.categoryId}
                    onChange={(e) => setFormData({ ...formData, categoryId: parseInt(e.target.value) })}
                    className="w-full px-4 py-3 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  >
                    <option value="">Seleccionar categoría</option>
                    {categories.map(cat => (
                      <option key={cat.id} value={cat.id}>{cat.name}</option>
                    ))}
                  </select>
                </div>
                <div className="col-span-2">
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Descripción
                  </label>
                  <textarea
                    rows={3}
                    value={formData.description || ''}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    className="w-full px-4 py-3 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>
                <div className="col-span-2">
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    URLs de Imágenes (una por línea)
                  </label>
                  <textarea
                    rows={3}
                    value={formData.imagesUrl?.join('\n') || ''}
                    onChange={(e) => setFormData({ ...formData, imagesUrl: e.target.value.split('\n').filter(Boolean) })}
                    placeholder="https://ejemplo.com/imagen1.jpg&#10;https://ejemplo.com/imagen2.jpg"
                    className="w-full px-4 py-3 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Colores (separados por coma)
                  </label>
                  <input
                    type="text"
                    value={formData.colors?.join(', ') || ''}
                    onChange={(e) => setFormData({ ...formData, colors: e.target.value.split(',').map(s => s.trim()).filter(Boolean) })}
                    placeholder="Rojo, Azul, Verde"
                    className="w-full px-4 py-3 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Tallas (separadas por coma)
                  </label>
                  <input
                    type="text"
                    value={formData.sizes?.join(', ') || ''}
                    onChange={(e) => setFormData({ ...formData, sizes: e.target.value.split(',').map(s => s.trim()).filter(Boolean) })}
                    placeholder="S, M, L, XL"
                    className="w-full px-4 py-3 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>
              </div>
              <div className="flex justify-end gap-4 pt-4 border-t dark:border-neutral-700">
                <button
                  type="button"
                  onClick={() => setShowModal(false)}
                  className="px-6 py-3 border border-neutral-300 dark:border-neutral-600 rounded-lg text-neutral-700 dark:text-neutral-300 hover:bg-neutral-100 dark:hover:bg-neutral-700 transition"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className="px-6 py-3 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-lg hover:from-blue-700 hover:to-blue-800 transition "
                >
                  {editingProduct ? 'Actualizar' : 'Crear'} Producto
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal de confirmación de cambio de estado */}
      <ConfirmModal
        isOpen={showToggleModal}
        onClose={() => {
          setShowToggleModal(false);
          setTogglingProduct(null);
        }}
        onConfirm={confirmToggleStatus}
        title={togglingProduct?.status === 'ACTIVE' ? 'Desactivar producto' : 'Activar producto'}
        message={togglingProduct?.status === 'ACTIVE'
          ? `¿Estás seguro de que deseas desactivar el producto "${togglingProduct?.name}"?`
          : `¿Estás seguro de que deseas activar el producto "${togglingProduct?.name}"?`
        }
        confirmText={togglingProduct?.status === 'ACTIVE' ? 'Desactivar' : 'Activar'}
        cancelText="Cancelar"
        variant={togglingProduct?.status === 'ACTIVE' ? 'warning' : 'info'}
        loading={toggleLoading}
      />
    </div>
  );
}

