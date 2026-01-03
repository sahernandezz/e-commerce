'use client';

import { useEffect, useState } from 'react';
import { useAuth } from '@/context/auth';
import { useRouter } from 'next/navigation';
import { getMyOrders, UserOrder, setAuthToken } from '@/lib/graphql/query';
import { currencyFormatter } from '@/lib/currencyFormatter';

const statusColors: Record<string, string> = {
    PENDING: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200',
    PROCESSING: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200',
    SHIPPED: 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200',
    DELIVERED: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
    CANCELLED: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200',
};

const statusLabels: Record<string, string> = {
    PENDING: 'Pendiente',
    PROCESSING: 'En Proceso',
    SHIPPED: 'Enviado',
    DELIVERED: 'Entregado',
    CANCELLED: 'Cancelado',
};

export default function MyOrdersPage() {
    const { token, isAuthenticated, user } = useAuth();
    const router = useRouter();
    const [orders, setOrders] = useState<UserOrder[]>([]);
    const [loading, setLoading] = useState(true);
    const [selectedOrder, setSelectedOrder] = useState<UserOrder | null>(null);

    useEffect(() => {
        if (!isAuthenticated) {
            router.push('/');
            return;
        }
        if (token) {
            setAuthToken(token);
            fetchOrders();
        }
    }, [isAuthenticated, token, router]);

    const fetchOrders = async () => {
        try {
            setLoading(true);
            const data = await getMyOrders();
            setOrders(data);
        } catch (err) {
            console.error('Error fetching orders:', err);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="flex justify-center items-center min-h-screen">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    return (
        <div className="max-w-6xl mx-auto p-6">
            <div className="flex justify-between items-center mb-8">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Mis Órdenes</h1>
                    <p className="text-gray-500 dark:text-gray-400 mt-1">
                        Historial de tus compras
                    </p>
                </div>
                <button
                    onClick={() => router.push('/profile')}
                    className="px-4 py-2 text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-lg transition flex items-center gap-2"
                >
                    ← Volver al Perfil
                </button>
            </div>

            {orders.length === 0 ? (
                <div className="text-center py-16 bg-white dark:bg-gray-800 rounded-lg shadow-lg">
                    <div className="text-6xl mb-4">📦</div>
                    <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-2">
                        No tienes órdenes aún
                    </h2>
                    <p className="text-gray-500 dark:text-gray-400 mb-6">
                        ¡Explora nuestra tienda y haz tu primera compra!
                    </p>
                    <button
                        onClick={() => router.push('/')}
                        className="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
                    >
                        Ir a la Tienda
                    </button>
                </div>
            ) : (
                <div className="space-y-4">
                    {orders.map((order) => (
                        <div
                            key={order.id}
                            className="bg-white dark:bg-gray-800 rounded-lg shadow-lg overflow-hidden hover:shadow-xl transition"
                        >
                            <div
                                className="p-6 cursor-pointer"
                                onClick={() => setSelectedOrder(selectedOrder?.id === order.id ? null : order)}
                            >
                                <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                                    <div className="flex items-center gap-4">
                                        <div className="w-12 h-12 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full flex items-center justify-center text-white text-xl">
                                            📋
                                        </div>
                                        <div>
                                            <p className="font-mono font-semibold text-gray-900 dark:text-white">
                                                #{order.orderCode}
                                            </p>
                                            <p className="text-sm text-gray-500 dark:text-gray-400">
                                                {new Date(order.createdAt).toLocaleDateString('es-ES', {
                                                    year: 'numeric',
                                                    month: 'long',
                                                    day: 'numeric'
                                                })}
                                            </p>
                                        </div>
                                    </div>
                                    <div className="flex flex-wrap items-center gap-4">
                                        <span className={`px-3 py-1 rounded-full text-sm font-medium ${statusColors[order.status] || 'bg-gray-100 text-gray-800'}`}>
                                            {statusLabels[order.status] || order.status}
                                        </span>
                                        <p className="text-lg font-bold text-gray-900 dark:text-white">
                                            {currencyFormatter(order.total)}
                                        </p>
                                        <svg
                                            className={`w-5 h-5 text-gray-400 transition-transform ${selectedOrder?.id === order.id ? 'rotate-180' : ''}`}
                                            fill="none"
                                            stroke="currentColor"
                                            viewBox="0 0 24 24"
                                        >
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                                        </svg>
                                    </div>
                                </div>
                            </div>

                            {/* Detalles expandibles */}
                            {selectedOrder?.id === order.id && (
                                <div className="border-t dark:border-gray-700 p-6 bg-gray-50 dark:bg-gray-900/50">
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                                        <div>
                                            <p className="text-sm text-gray-500 dark:text-gray-400">Dirección de Envío</p>
                                            <p className="font-medium text-gray-900 dark:text-white">{order.address}</p>
                                            <p className="text-gray-600 dark:text-gray-300">{order.city}</p>
                                        </div>
                                        <div>
                                            <p className="text-sm text-gray-500 dark:text-gray-400">Método de Pago</p>
                                            <p className="font-medium text-gray-900 dark:text-white">{order.paymentMethod}</p>
                                        </div>
                                        {order.description && (
                                            <div className="col-span-2">
                                                <p className="text-sm text-gray-500 dark:text-gray-400">Notas</p>
                                                <p className="text-gray-600 dark:text-gray-300">{order.description}</p>
                                            </div>
                                        )}
                                    </div>

                                    {/* Productos */}
                                    {order.products && order.products.length > 0 && (
                                        <div>
                                            <p className="text-sm font-medium text-gray-500 dark:text-gray-400 mb-3">Productos</p>
                                            <div className="space-y-3">
                                                {order.products.map((item) => (
                                                    <div key={item.id} className="flex items-center gap-4 p-3 bg-white dark:bg-gray-800 rounded-lg">
                                                        <div className="w-16 h-16 bg-gray-100 dark:bg-gray-700 rounded-lg overflow-hidden flex-shrink-0">
                                                            {item.product?.imagesUrl?.[0] ? (
                                                                <img
                                                                    src={item.product.imagesUrl[0]}
                                                                    alt={item.product.name}
                                                                    className="w-full h-full object-cover"
                                                                />
                                                            ) : (
                                                                <div className="w-full h-full flex items-center justify-center text-gray-400">
                                                                    📦
                                                                </div>
                                                            )}
                                                        </div>
                                                        <div className="flex-1">
                                                            <p className="font-medium text-gray-900 dark:text-white">
                                                                {item.product?.name || 'Producto'}
                                                            </p>
                                                            <p className="text-sm text-gray-500 dark:text-gray-400">
                                                                Cantidad: {item.quantity}
                                                                {item.size && ` | Talla: ${item.size}`}
                                                                {item.color && ` | Color: ${item.color}`}
                                                            </p>
                                                        </div>
                                                        <p className="font-medium text-gray-900 dark:text-white">
                                                            {currencyFormatter(item.price * item.quantity)}
                                                        </p>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    )}

                                    {/* Total */}
                                    <div className="flex justify-between items-center mt-6 pt-4 border-t dark:border-gray-700">
                                        <p className="text-lg font-medium text-gray-900 dark:text-white">Total</p>
                                        <p className="text-2xl font-bold text-blue-600 dark:text-blue-400">
                                            {currencyFormatter(order.total)}
                                        </p>
                                    </div>
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

