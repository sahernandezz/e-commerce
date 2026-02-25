'use client';

import { useEffect, useState } from 'react';
import { useAuth } from '@/context/auth';
import { useRouter } from 'next/navigation';
import { getMyOrders, UserOrder, setAuthToken } from '@/lib/graphql/query';
import { currencyFormatter } from '@/lib/currencyFormatter';
import { getOrderStatusLabel, getPaymentMethodLabel } from '@/lib/status';
import { PackageIcon, ClipboardIcon, UserIcon } from '@/components/icons';

const statusColors: Record<string, string> = {
    PENDING: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/30 dark:text-yellow-400',
    CONFIRMED: 'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400',
    PROCESSING: 'bg-indigo-100 text-indigo-800 dark:bg-indigo-900/30 dark:text-indigo-400',
    SHIPPED: 'bg-purple-100 text-purple-800 dark:bg-purple-900/30 dark:text-purple-400',
    DELIVERED: 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400',
    CANCELED: 'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-400',
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
            <div className="flex justify-center items-center min-h-[60vh]">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    return (
        <div className="max-w-5xl mx-auto px-4 py-8">
            {/* Header */}
            <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center gap-4 mb-8">
                <div>
                    <h1 className="text-2xl font-bold text-black dark:text-white">Mis Órdenes</h1>
                    <p className="text-neutral-500 dark:text-neutral-400 mt-1">
                        {orders.length > 0
                            ? `${orders.length} ${orders.length === 1 ? 'orden' : 'órdenes'} en tu historial`
                            : 'Historial de tus compras'
                        }
                    </p>
                </div>
                <button
                    onClick={() => router.push('/profile')}
                    className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-neutral-700 dark:text-neutral-300 bg-neutral-100 dark:bg-neutral-800 hover:bg-neutral-200 dark:hover:bg-neutral-700 rounded-lg transition"
                >
                    <UserIcon className="w-4 h-4" />
                    Mi Perfil
                </button>
            </div>

            {orders.length === 0 ? (
                /* Estado vacío */
                <div className="text-center py-16 bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg">
                    <div className="w-20 h-20 mx-auto mb-6 bg-neutral-100 dark:bg-neutral-900 rounded-full flex items-center justify-center">
                        <PackageIcon className="w-10 h-10 text-neutral-400" />
                    </div>
                    <h2 className="text-xl font-semibold text-black dark:text-white mb-2">
                        No tienes órdenes aún
                    </h2>
                    <p className="text-neutral-500 dark:text-neutral-400 mb-6 max-w-sm mx-auto">
                        ¡Explora nuestra tienda y haz tu primera compra!
                    </p>
                    <button
                        onClick={() => router.push('/search')}
                        className="px-6 py-2.5 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition font-medium"
                    >
                        Explorar Productos
                    </button>
                </div>
            ) : (
                /* Lista de órdenes */
                <div className="space-y-4">
                    {orders.map((order) => (
                        <div
                            key={order.id}
                            className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg overflow-hidden transition hover:border-neutral-300 dark:hover:border-neutral-700"
                        >
                            {/* Header de la orden */}
                            <div
                                className="p-5 cursor-pointer"
                                onClick={() => setSelectedOrder(selectedOrder?.id === order.id ? null : order)}
                            >
                                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                                    <div className="flex items-start sm:items-center gap-4">
                                        <div className="w-12 h-12 bg-blue-100 dark:bg-blue-900/30 rounded-lg flex items-center justify-center flex-shrink-0">
                                            <ClipboardIcon className="w-6 h-6 text-blue-600 dark:text-blue-400" />
                                        </div>
                                        <div>
                                            <p className="font-mono text-sm font-semibold text-black dark:text-white">
                                                #{order.orderCode}
                                            </p>
                                            <p className="text-sm text-neutral-500 dark:text-neutral-400 mt-0.5">
                                                {new Date(order.createdAt).toLocaleDateString('es-ES', {
                                                    year: 'numeric',
                                                    month: 'long',
                                                    day: 'numeric'
                                                })}
                                            </p>
                                        </div>
                                    </div>
                                    <div className="flex flex-wrap items-center gap-3 sm:gap-4">
                                        <span className={`px-3 py-1 rounded-full text-xs font-medium ${statusColors[order.status] || 'bg-neutral-100 text-neutral-800 dark:bg-neutral-800 dark:text-neutral-200'}`}>
                                            {getOrderStatusLabel(order.status)}
                                        </span>
                                        <p className="text-lg font-bold text-black dark:text-white">
                                            {currencyFormatter(order.total)}
                                        </p>
                                        <svg
                                            className={`w-5 h-5 text-neutral-400 transition-transform duration-200 ${selectedOrder?.id === order.id ? 'rotate-180' : ''}`}
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
                                <div className="border-t border-neutral-200 dark:border-neutral-800 p-5 bg-neutral-50 dark:bg-neutral-900/50">
                                    {/* Info de envío y pago */}
                                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-6">
                                        <div className="p-4 bg-white dark:bg-black rounded-lg border border-neutral-200 dark:border-neutral-800">
                                            <div className="flex items-center gap-2 mb-2">
                                                <svg className="w-4 h-4 text-neutral-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                                                </svg>
                                                <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase tracking-wide">Dirección de Envío</p>
                                            </div>
                                            <p className="font-medium text-black dark:text-white">{order.address}</p>
                                            <p className="text-sm text-neutral-600 dark:text-neutral-400">{order.city}</p>
                                        </div>
                                        <div className="p-4 bg-white dark:bg-black rounded-lg border border-neutral-200 dark:border-neutral-800">
                                            <div className="flex items-center gap-2 mb-2">
                                                <svg className="w-4 h-4 text-neutral-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                                                </svg>
                                                <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase tracking-wide">Método de Pago</p>
                                            </div>
                                            <p className="font-medium text-black dark:text-white">{getPaymentMethodLabel(order.paymentMethod)}</p>
                                        </div>
                                    </div>

                                    {/* Notas */}
                                    {order.description && (
                                        <div className="mb-6 p-4 bg-white dark:bg-black rounded-lg border border-neutral-200 dark:border-neutral-800">
                                            <div className="flex items-center gap-2 mb-2">
                                                <svg className="w-4 h-4 text-neutral-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z" />
                                                </svg>
                                                <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase tracking-wide">Notas</p>
                                            </div>
                                            <p className="text-sm text-neutral-600 dark:text-neutral-400">{order.description}</p>
                                        </div>
                                    )}

                                    {/* Productos */}
                                    {order.products && order.products.length > 0 && (
                                        <div>
                                            <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase tracking-wide mb-3">
                                                Productos ({order.products.length})
                                            </p>
                                            <div className="space-y-2">
                                                {order.products.map((item) => (
                                                    <div key={item.id} className="flex items-center gap-4 p-3 bg-white dark:bg-black rounded-lg border border-neutral-200 dark:border-neutral-800">
                                                        <div className="w-14 h-14 bg-neutral-100 dark:bg-neutral-900 rounded-lg overflow-hidden flex-shrink-0">
                                                            {item.product?.imagesUrl?.[0] ? (
                                                                <img
                                                                    src={item.product.imagesUrl[0]}
                                                                    alt={item.product.name}
                                                                    className="w-full h-full object-cover"
                                                                />
                                                            ) : (
                                                                <div className="w-full h-full flex items-center justify-center text-neutral-400">
                                                                    <PackageIcon className="w-6 h-6" />
                                                                </div>
                                                            )}
                                                        </div>
                                                        <div className="flex-1 min-w-0">
                                                            <p className="font-medium text-black dark:text-white truncate">
                                                                {item.product?.name || 'Producto'}
                                                            </p>
                                                            <div className="flex flex-wrap gap-2 mt-1">
                                                                <span className="text-xs text-neutral-500 dark:text-neutral-400">
                                                                    Cant: {item.quantity}
                                                                </span>
                                                                {item.size && (
                                                                    <span className="text-xs text-neutral-500 dark:text-neutral-400">
                                                                        Talla: {item.size}
                                                                    </span>
                                                                )}
                                                                {item.color && (
                                                                    <span className="text-xs text-neutral-500 dark:text-neutral-400">
                                                                        Color: {item.color}
                                                                    </span>
                                                                )}
                                                            </div>
                                                        </div>
                                                        <p className="font-medium text-black dark:text-white whitespace-nowrap">
                                                            {currencyFormatter(item.price * item.quantity)}
                                                        </p>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    )}

                                    {/* Total */}
                                    <div className="flex justify-between items-center mt-6 pt-4 border-t border-neutral-200 dark:border-neutral-800">
                                        <p className="text-sm font-medium text-neutral-500 dark:text-neutral-400">Total del pedido</p>
                                        <p className="text-xl font-bold text-blue-600 dark:text-blue-400">
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
