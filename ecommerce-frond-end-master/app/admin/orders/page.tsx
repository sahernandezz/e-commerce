'use client';

import { useEffect, useState } from 'react';
import { useAuth } from '@/context/auth';
import {
  getAllOrders,
  updateOrderStatus,
  Order,
  setAuthToken
} from '@/lib/graphql/admin';
import { currencyFormatter as formatCurrency } from '@/lib/currencyFormatter';

const ORDER_STATUSES = ['PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'];

export default function AdminOrdersPage() {
  const { token } = useAuth();
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedOrder, setSelectedOrder] = useState<Order | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState('');

  useEffect(() => {
    if (token) {
      setAuthToken(token);
      fetchOrders();
    }
  }, [token]);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const data = await getAllOrders();
      setOrders(data);
    } catch (error) {
      console.error('Error fetching orders:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleStatusChange = async (orderId: string, newStatus: string) => {
    const success = await updateOrderStatus(orderId, newStatus);
    if (success) {
      setOrders(orders.map(order =>
        order.id === orderId ? { ...order, status: newStatus } : order
      ));
      if (selectedOrder?.id === orderId) {
        setSelectedOrder({ ...selectedOrder, status: newStatus });
      }
    }
  };

  const getStatusColor = (status: string) => {
    switch (status?.toUpperCase()) {
      case 'DELIVERED':
        return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
      case 'PROCESSING':
        return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200';
      case 'SHIPPED':
        return 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
      default:
        return 'bg-neutral-100 text-neutral-800 dark:bg-neutral-900 dark:text-neutral-200';
    }
  };

  const filteredOrders = orders.filter(order => {
    const matchesSearch = order.orderCode.toLowerCase().includes(searchTerm.toLowerCase()) ||
              order.emailCustomer.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = !filterStatus || order.status === filterStatus;
    return matchesSearch && matchesStatus;
  });

  const stats = {
    total: orders.length,
    pending: orders.filter(o => o.status === 'PENDING').length,
    processing: orders.filter(o => o.status === 'PROCESSING').length,
    delivered: orders.filter(o => o.status === 'DELIVERED').length,
  };

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
          <h1 className="text-3xl font-bold text-black dark:text-white">Órdenes</h1>
          <p className="text-neutral-500 dark:text-neutral-400 mt-1">
            Gestiona todos los pedidos de la tienda
          </p>
        </div>
        <button
          onClick={fetchOrders}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition flex items-center gap-2"
        >
          <span>🔄</span>
          Actualizar
        </button>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg shadow p-6">
          <p className="text-sm text-neutral-500 dark:text-neutral-400">Total Órdenes</p>
          <p className="text-3xl font-bold text-black dark:text-white mt-1">{stats.total}</p>
        </div>
        <div className="bg-yellow-50 dark:bg-yellow-900/20 rounded-lg shadow p-6">
          <p className="text-sm text-yellow-600 dark:text-yellow-400">Pendientes</p>
          <p className="text-3xl font-bold text-yellow-700 dark:text-yellow-300 mt-1">{stats.pending}</p>
        </div>
        <div className="bg-blue-50 dark:bg-blue-900/20 rounded-lg shadow p-6">
          <p className="text-sm text-blue-600 dark:text-blue-400">En Proceso</p>
          <p className="text-3xl font-bold text-blue-700 dark:text-blue-300 mt-1">{stats.processing}</p>
        </div>
        <div className="bg-green-50 dark:bg-green-900/20 rounded-lg shadow p-6">
          <p className="text-sm text-green-600 dark:text-green-400">Entregadas</p>
          <p className="text-3xl font-bold text-green-700 dark:text-green-300 mt-1">{stats.delivered}</p>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-6">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
              Buscar
            </label>
            <input
              type="text"
              placeholder="Código o email..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
              Estado
            </label>
            <select
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Todos</option>
              {ORDER_STATUSES.map(status => (
                <option key={status} value={status}>{status}</option>
              ))}
            </select>
          </div>
          <div className="flex items-end">
            <button
              onClick={() => { setSearchTerm(''); setFilterStatus(''); }}
              className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg text-neutral-700 dark:text-neutral-300 hover:bg-neutral-100 dark:hover:bg-neutral-700 transition"
            >
              Limpiar Filtros
            </button>
          </div>
        </div>
      </div>

      {/* Orders Table */}
      <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-neutral-50 dark:bg-neutral-900">
              <tr>
                <th className="px-6 py-4 text-left text-xs font-semibold text-neutral-500 dark:text-neutral-400 uppercase">
                  Código
                </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-neutral-500 dark:text-neutral-400 uppercase">
                  Cliente
                </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-neutral-500 dark:text-neutral-400 uppercase">
                  Total
                </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-neutral-500 dark:text-neutral-400 uppercase">
                  Estado
                </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-neutral-500 dark:text-neutral-400 uppercase">
                  Fecha
                </th>
                <th className="px-6 py-4 text-right text-xs font-semibold text-neutral-500 dark:text-neutral-400 uppercase">
                  Acciones
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-neutral-200 dark:divide-neutral-700">
              {filteredOrders.map((order) => (
                <tr key={order.id} className="hover:bg-neutral-50 dark:hover:bg-neutral-700/50 transition">
                  <td className="px-6 py-4">
                    <p className="font-mono font-medium text-black dark:text-white">
                      #{order.orderCode}
                    </p>
                  </td>
                  <td className="px-6 py-4">
                    <p className="text-black dark:text-white">{order.emailCustomer}</p>
                    <p className="text-sm text-neutral-500 dark:text-neutral-400">
                      {order.city}
                    </p>
                  </td>
                  <td className="px-6 py-4">
                    <p className="font-medium text-black dark:text-white">
                      {formatCurrency(order.total)}
                    </p>
                  </td>
                  <td className="px-6 py-4">
                    <select
                      value={order.status}
                      onChange={(e) => handleStatusChange(order.id, e.target.value)}
                      className={`px-3 py-1 rounded-full text-xs font-medium border-0 cursor-pointer ${getStatusColor(order.status)}`}
                    >
                      {ORDER_STATUSES.map(status => (
                        <option key={status} value={status}>{status}</option>
                      ))}
                    </select>
                  </td>
                  <td className="px-6 py-4">
                    <p className="text-neutral-500 dark:text-neutral-400">
                      {new Date(order.createdAt).toLocaleDateString()}
                    </p>
                  </td>
                  <td className="px-6 py-4 text-right">
                    <button
                      onClick={() => setSelectedOrder(order)}
                      className="p-2 text-blue-600 hover:bg-blue-100 dark:hover:bg-blue-900/30 rounded-lg transition"
                      title="Ver detalles"
                    >
                      👁️
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {filteredOrders.length === 0 && (
            <div className="text-center py-12">
              <p className="text-4xl mb-4">📋</p>
              <p className="text-neutral-500 dark:text-neutral-400">No se encontraron órdenes</p>
            </div>
          )}
        </div>
      </div>

      {/* Order Detail Modal */}
      {selectedOrder && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-2xl shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
            <div className="p-6 border-b dark:border-neutral-700 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-black dark:text-white">
                Orden #{selectedOrder.orderCode}
              </h2>
              <button
                onClick={() => setSelectedOrder(null)}
                className="p-2 hover:bg-neutral-100 dark:hover:bg-neutral-700 rounded-lg"
              >
                ✕
              </button>
            </div>
            <div className="p-6 space-y-6">
              {/* Order Info */}
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-sm text-neutral-500 dark:text-neutral-400">Cliente</p>
                  <p className="font-medium text-black dark:text-white">{selectedOrder.emailCustomer}</p>
                </div>
                <div>
                  <p className="text-sm text-neutral-500 dark:text-neutral-400">Estado</p>
                  <span className={`inline-block px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(selectedOrder.status)}`}>
                    {selectedOrder.status}
                  </span>
                </div>
                <div>
                  <p className="text-sm text-neutral-500 dark:text-neutral-400">Ciudad</p>
                  <p className="font-medium text-black dark:text-white">{selectedOrder.city}</p>
                </div>
                <div>
                  <p className="text-sm text-neutral-500 dark:text-neutral-400">Método de Pago</p>
                  <p className="font-medium text-black dark:text-white">{selectedOrder.paymentMethod}</p>
                </div>
                <div className="col-span-2">
                  <p className="text-sm text-neutral-500 dark:text-neutral-400">Dirección</p>
                  <p className="font-medium text-black dark:text-white">{selectedOrder.address}</p>
                </div>
                {selectedOrder.description && (
                  <div className="col-span-2">
                    <p className="text-sm text-neutral-500 dark:text-neutral-400">Descripción</p>
                    <p className="font-medium text-black dark:text-white">{selectedOrder.description}</p>
                  </div>
                )}
              </div>

              {/* Products */}
              {selectedOrder.products && selectedOrder.products.length > 0 && (
                <div>
                  <p className="text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-3">Productos</p>
                  <div className="space-y-3">
                    {selectedOrder.products.map((item) => (
                      <div key={item.id} className="flex items-center gap-4 p-3 bg-neutral-50 dark:bg-neutral-900 rounded-lg">
                        <div className="w-12 h-12 bg-neutral-200 dark:bg-neutral-600 rounded-lg flex items-center justify-center">
                          📦
                        </div>
                        <div className="flex-1">
                          <p className="font-medium text-black dark:text-white">
                            {item.name || 'Producto'}
                          </p>
                          <p className="text-sm text-neutral-500 dark:text-neutral-400">
                            Cantidad: {item.quantity} × {formatCurrency(item.unitPrice)}
                            {item.size && ` | Talla: ${item.size}`}
                            {item.color && ` | Color: ${item.color}`}
                          </p>
                        </div>
                        <p className="font-medium text-black dark:text-white">
                          {formatCurrency(item.total)}
                        </p>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Total */}
              <div className="flex justify-between items-center p-4 bg-blue-50 dark:bg-blue-900/20 rounded-lg">
                <p className="text-lg font-medium text-black dark:text-white">Total</p>
                <p className="text-2xl font-bold text-blue-600 dark:text-blue-400">
                  {formatCurrency(selectedOrder.total)}
                </p>
              </div>

              {/* Actions */}
              <div className="flex justify-end gap-4 pt-4 border-t dark:border-neutral-700">
                <button
                  onClick={() => setSelectedOrder(null)}
                  className="px-6 py-3 border border-neutral-300 dark:border-neutral-600 rounded-lg text-neutral-700 dark:text-neutral-300 hover:bg-neutral-100 dark:hover:bg-neutral-700 transition"
                >
                  Cerrar
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

