'use client';

import { useEffect, useState } from 'react';
import { useAuth } from '@/context/auth';
import {
    getDashboardStats,
    getTopSellingProducts,
    getRecentOrders,
    DashboardStats,
    TopSellingProduct,
    Order,
    setAuthToken
} from '@/lib/graphql/admin';
import { currencyFormatter as formatCurrency } from '@/lib/currencyFormatter';

export default function AdminDashboard() {
    const { token } = useAuth();
    const [stats, setStats] = useState<DashboardStats | null>(null);
    const [topProducts, setTopProducts] = useState<TopSellingProduct[]>([]);
    const [recentOrders, setRecentOrders] = useState<Order[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (token) {
            setAuthToken(token);
            fetchDashboardData();
        }
    }, [token]);

    const fetchDashboardData = async () => {
        try {
            setLoading(true);
            const [statsData, topProductsData, recentOrdersData] = await Promise.all([
                getDashboardStats(),
                getTopSellingProducts(5),
                getRecentOrders(5)
            ]);

            setStats(statsData);
            setTopProducts(topProductsData);
            setRecentOrders(recentOrdersData);
        } catch (err) {
            console.error('Error fetching dashboard data:', err);
            // Set empty stats when API fails
            setStats({
                totalProducts: 0,
                totalOrders: 0,
                totalUsers: 0,
                pendingOrders: 0,
                totalRevenue: 0,
                averageOrderValue: 0,
                ordersThisMonth: 0,
                newUsersThisMonth: 0
            });
            setTopProducts([]);
            setRecentOrders([]);
        } finally {
            setLoading(false);
        }
    };

    const getStatusColor = (status: string) => {
        switch (status?.toUpperCase()) {
            case 'COMPLETED':
            case 'DELIVERED':
                return 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400';
            case 'PENDING':
                return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/30 dark:text-yellow-400';
            case 'PROCESSING':
                return 'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400';
            case 'CANCELLED':
                return 'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-400';
            default:
                return 'bg-neutral-100 text-neutral-800 dark:bg-neutral-800 dark:text-neutral-300';
        }
    };

    if (loading) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    const statCards = [
        {
            label: 'Productos',
            value: stats?.totalProducts || 0,
            icon: (
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                </svg>
            ),
            bgColor: 'bg-blue-600'
        },
        {
            label: 'Órdenes',
            value: stats?.totalOrders || 0,
            icon: (
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                </svg>
            ),
            bgColor: 'bg-green-600'
        },
        {
            label: 'Usuarios',
            value: stats?.totalUsers || 0,
            icon: (
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                </svg>
            ),
            bgColor: 'bg-purple-600'
        },
        {
            label: 'Pendientes',
            value: stats?.pendingOrders || 0,
            icon: (
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
            ),
            bgColor: 'bg-amber-600'
        },
    ];

    return (
        <div className="space-y-6">
            {/* Stats Grid */}
            <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
                {statCards.map((stat) => (
                    <div
                        key={stat.label}
                        className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-5"
                    >
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-neutral-500 dark:text-neutral-400">{stat.label}</p>
                                <p className="text-2xl font-bold text-black dark:text-white mt-1">
                                    {stat.value.toLocaleString()}
                                </p>
                            </div>
                            <div className={`w-12 h-12 ${stat.bgColor} rounded-lg flex items-center justify-center text-white`}>
                                {stat.icon}
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {/* Revenue Stats */}
            <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
                <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-5">
                    <p className="text-sm text-neutral-500 dark:text-neutral-400">Ingresos Totales</p>
                    <p className="text-xl font-bold text-black dark:text-white mt-1">
                        {formatCurrency(stats?.totalRevenue || 0)}
                    </p>
                </div>
                <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-5">
                    <p className="text-sm text-neutral-500 dark:text-neutral-400">Valor Promedio</p>
                    <p className="text-xl font-bold text-black dark:text-white mt-1">
                        {formatCurrency(stats?.averageOrderValue || 0)}
                    </p>
                </div>
                <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-5">
                    <p className="text-sm text-neutral-500 dark:text-neutral-400">Órdenes Este Mes</p>
                    <p className="text-xl font-bold text-black dark:text-white mt-1">
                        {stats?.ordersThisMonth || 0}
                    </p>
                </div>
                <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-5">
                    <p className="text-sm text-neutral-500 dark:text-neutral-400">Nuevos Usuarios</p>
                    <p className="text-xl font-bold text-black dark:text-white mt-1">
                        {stats?.newUsersThisMonth || 0}
                    </p>
                </div>
            </div>

            {/* Content Grid */}
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Top Selling Products */}
                <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-5">
                    <h2 className="text-base font-semibold text-black dark:text-white mb-4">
                        Productos Más Vendidos
                    </h2>
                    <div className="space-y-3">
                        {topProducts.length > 0 ? topProducts.map((item, index) => (
                            <div key={item.product.id} className="flex items-center gap-3 p-2 bg-neutral-50 dark:bg-neutral-900 rounded-lg">
                                <span className={`w-7 h-7 rounded-full flex items-center justify-center text-white text-xs font-bold ${
                                    index === 0 ? 'bg-yellow-500' : index === 1 ? 'bg-neutral-400' : index === 2 ? 'bg-amber-600' : 'bg-neutral-300 dark:bg-neutral-700 text-neutral-600 dark:text-neutral-300'
                                }`}>
                                    {index + 1}
                                </span>
                                <div className="flex-1 min-w-0">
                                    <p className="font-medium text-black dark:text-white text-sm truncate">
                                        {item.product.name}
                                    </p>
                                    <p className="text-xs text-neutral-500 dark:text-neutral-400">
                                        {item.totalSold} vendidos · {formatCurrency(item.revenue)}
                                    </p>
                                </div>
                            </div>
                        )) : (
                            <div className="text-center text-neutral-500 dark:text-neutral-400 py-8">
                                <p className="text-sm">No hay datos disponibles</p>
                            </div>
                        )}
                    </div>
                </div>

                {/* Recent Orders */}
                <div className="lg:col-span-2 bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-5">
                    <div className="flex justify-between items-center mb-4">
                        <h2 className="text-base font-semibold text-black dark:text-white">
                            Órdenes Recientes
                        </h2>
                        <a href="/admin/orders" className="text-blue-600 hover:text-blue-700 text-sm font-medium">
                            Ver todas →
                        </a>
                    </div>
                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead>
                                <tr className="text-left text-neutral-500 dark:text-neutral-400 text-xs border-b border-neutral-200 dark:border-neutral-800">
                                    <th className="pb-3 font-medium">Código</th>
                                    <th className="pb-3 font-medium">Cliente</th>
                                    <th className="pb-3 font-medium">Total</th>
                                    <th className="pb-3 font-medium">Estado</th>
                                    <th className="pb-3 font-medium">Fecha</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-neutral-100 dark:divide-neutral-800">
                                {recentOrders.length > 0 ? recentOrders.map((order) => (
                                    <tr key={order.id} className="text-sm">
                                        <td className="py-3 font-medium text-black dark:text-white">
                                            #{order.orderCode?.slice(0, 8)}...
                                        </td>
                                        <td className="py-3 text-neutral-600 dark:text-neutral-400">
                                            {order.emailCustomer}
                                        </td>
                                        <td className="py-3 font-medium text-black dark:text-white">
                                            {formatCurrency(order.total)}
                                        </td>
                                        <td className="py-3">
                                            <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
                                                {order.status}
                                            </span>
                                        </td>
                                        <td className="py-3 text-neutral-500 dark:text-neutral-400">
                                            {new Date(order.createdAt).toLocaleDateString()}
                                        </td>
                                    </tr>
                                )) : (
                                    <tr>
                                        <td colSpan={5} className="py-8 text-center text-neutral-500 dark:text-neutral-400">
                                            <p className="text-sm">No hay órdenes recientes</p>
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
}
