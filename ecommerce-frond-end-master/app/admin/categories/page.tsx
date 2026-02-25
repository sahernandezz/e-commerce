'use client';

import { useEffect, useState } from 'react';
import { useAuth } from '@/context/auth';
import {
    getCategoriesPaginated,
    createCategory,
    updateCategory,
    updateCategoryStatus,
    getCategoryStatuses,
    setAuthToken,
    Category,
    PageResponse,
    StatusOption
} from '@/lib/graphql/admin';
import { ConfirmModal } from '@/components/ConfirmModal';

export default function CategoriesPage() {
    const { token } = useAuth();
    const [categories, setCategories] = useState<Category[]>([]);
    const [pageInfo, setPageInfo] = useState<PageResponse<Category> | null>(null);
    const [statuses, setStatuses] = useState<StatusOption[]>([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [searchTerm, setSearchTerm] = useState('');
    const [filterStatus, setFilterStatus] = useState('');
    const [showModal, setShowModal] = useState(false);
    const [editingCategory, setEditingCategory] = useState<Category | null>(null);
    const [formData, setFormData] = useState({ name: '' });
    const [showToggleModal, setShowToggleModal] = useState(false);
    const [togglingCategory, setTogglingCategory] = useState<Category | null>(null);
    const [toggleLoading, setToggleLoading] = useState(false);

    useEffect(() => {
        if (token) {
            setAuthToken(token);
            fetchData();
            fetchStatuses();
        }
    }, [token, page]);

    const fetchData = async () => {
        try {
            setLoading(true);
            const data = await getCategoriesPaginated(
                page,
                10,
                filterStatus || undefined,
                searchTerm || undefined
            );
            setCategories(data.content);
            setPageInfo(data);
        } catch (error) {
            console.error('Error fetching categories:', error);
        } finally {
            setLoading(false);
        }
    };

    const fetchStatuses = async () => {
        try {
            const data = await getCategoryStatuses();
            setStatuses(data);
        } catch (error) {
            console.error('Error fetching statuses:', error);
        }
    };

    const handleSearch = () => {
        setPage(0);
        fetchData();
    };

    const handleCreate = () => {
        setEditingCategory(null);
        setFormData({ name: '' });
        setShowModal(true);
    };

    const handleEdit = (category: Category) => {
        setEditingCategory(category);
        setFormData({ name: category.name });
        setShowModal(true);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        let success;
        if (editingCategory) {
            success = await updateCategory(editingCategory.id, formData);
        } else {
            success = await createCategory(formData);
        }
        if (success) {
            setShowModal(false);
            fetchData();
        }
    };

    const handleToggleStatus = (category: Category) => {
        setTogglingCategory(category);
        setShowToggleModal(true);
    };

    const confirmToggleStatus = async () => {
        if (!togglingCategory) return;

        setToggleLoading(true);
        try {
            const newStatus = togglingCategory.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
            const success = await updateCategoryStatus(togglingCategory.id, newStatus);
            if (success) {
                fetchData();
            }
        } finally {
            setToggleLoading(false);
            setShowToggleModal(false);
            setTogglingCategory(null);
        }
    };

    const getStatusBadge = (status: string) => {
        switch (status) {
            case 'ACTIVE':
                return 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400';
            case 'INACTIVE':
                return 'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-400';
            default:
                return 'bg-neutral-100 text-neutral-800 dark:bg-neutral-800 dark:text-neutral-300';
        }
    };

    const getStatusLabel = (status: string) => {
        const found = statuses.find(s => s.value === status);
        return found?.label || status;
    };

    if (loading && categories.length === 0) {
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
                    <h1 className="text-2xl font-bold text-black dark:text-white">Categorías</h1>
                    <p className="text-neutral-500 dark:text-neutral-400 mt-1">
                        {pageInfo?.totalElements || 0} categorías en total
                    </p>
                </div>
                <button
                    onClick={handleCreate}
                    className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition flex items-center gap-2"
                >
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                    </svg>
                    Nueva Categoría
                </button>
            </div>

            {/* Filters */}
            <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-4">
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                        <input
                            type="text"
                            placeholder="Buscar por nombre..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                            className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-700 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white"
                        />
                    </div>
                    <div>
                        <select
                            value={filterStatus}
                            onChange={(e) => setFilterStatus(e.target.value)}
                            className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-700 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white"
                        >
                            <option value="">Todos los estados</option>
                            {statuses.map(s => (
                                <option key={s.value} value={s.value}>{s.label}</option>
                            ))}
                        </select>
                    </div>
                    <div className="flex gap-2">
                        <button
                            onClick={handleSearch}
                            className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
                        >
                            Buscar
                        </button>
                        <button
                            onClick={() => { setSearchTerm(''); setFilterStatus(''); setPage(0); fetchData(); }}
                            className="px-4 py-2 border border-neutral-300 dark:border-neutral-700 rounded-lg hover:bg-neutral-100 dark:hover:bg-neutral-800 transition"
                        >
                            Limpiar
                        </button>
                    </div>
                </div>
            </div>

            {/* Table */}
            <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg overflow-hidden">
                <table className="w-full">
                    <thead className="bg-neutral-50 dark:bg-neutral-900">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase">ID</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase">Nombre</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase">Estado</th>
                            <th className="px-6 py-3 text-right text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase">Acciones</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-neutral-200 dark:divide-neutral-800">
                        {categories.map((category) => (
                            <tr key={category.id} className="hover:bg-neutral-50 dark:hover:bg-neutral-900">
                                <td className="px-6 py-4 text-sm text-neutral-500 dark:text-neutral-400">
                                    #{category.id}
                                </td>
                                <td className="px-6 py-4 text-sm font-medium text-black dark:text-white">
                                    {category.name}
                                </td>
                                <td className="px-6 py-4">
                                    <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusBadge(category.status)}`}>
                                        {getStatusLabel(category.status)}
                                    </span>
                                </td>
                                <td className="px-6 py-4 text-right space-x-2">
                                    <button
                                        onClick={() => handleEdit(category)}
                                        className="text-blue-600 hover:text-blue-700"
                                    >
                                        Editar
                                    </button>
                                    <button
                                        onClick={() => handleToggleStatus(category)}
                                        className={category.status === 'ACTIVE' ? 'text-orange-600 hover:text-orange-700' : 'text-green-600 hover:text-green-700'}
                                    >
                                        {category.status === 'ACTIVE' ? 'Desactivar' : 'Activar'}
                                    </button>
                                </td>
                            </tr>
                        ))}
                        {categories.length === 0 && (
                            <tr>
                                <td colSpan={4} className="px-6 py-8 text-center text-neutral-500 dark:text-neutral-400">
                                    No se encontraron categorías
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            {/* Pagination */}
            {pageInfo && pageInfo.totalPages > 1 && (
                <div className="flex justify-between items-center">
                    <p className="text-sm text-neutral-500 dark:text-neutral-400">
                        Página {pageInfo.page + 1} de {pageInfo.totalPages}
                    </p>
                    <div className="flex gap-2">
                        <button
                            onClick={() => setPage(p => Math.max(0, p - 1))}
                            disabled={pageInfo.first}
                            className="px-4 py-2 border border-neutral-300 dark:border-neutral-700 rounded-lg disabled:opacity-50 hover:bg-neutral-100 dark:hover:bg-neutral-800 transition"
                        >
                            Anterior
                        </button>
                        <button
                            onClick={() => setPage(p => p + 1)}
                            disabled={pageInfo.last}
                            className="px-4 py-2 border border-neutral-300 dark:border-neutral-700 rounded-lg disabled:opacity-50 hover:bg-neutral-100 dark:hover:bg-neutral-800 transition"
                        >
                            Siguiente
                        </button>
                    </div>
                </div>
            )}

            {/* Modal */}
            {showModal && (
                <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
                    <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-6 w-full max-w-md">
                        <h2 className="text-xl font-bold text-black dark:text-white mb-4">
                            {editingCategory ? 'Editar Categoría' : 'Nueva Categoría'}
                        </h2>
                        <form onSubmit={handleSubmit}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                    Nombre
                                </label>
                                <input
                                    type="text"
                                    value={formData.name}
                                    onChange={(e) => setFormData({ name: e.target.value })}
                                    required
                                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-700 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white"
                                />
                            </div>
                            <div className="flex gap-4">
                                <button
                                    type="button"
                                    onClick={() => setShowModal(false)}
                                    className="flex-1 px-4 py-2 border border-neutral-300 dark:border-neutral-700 rounded-lg hover:bg-neutral-100 dark:hover:bg-neutral-800 transition"
                                >
                                    Cancelar
                                </button>
                                <button
                                    type="submit"
                                    className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
                                >
                                    {editingCategory ? 'Actualizar' : 'Crear'}
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
                    setTogglingCategory(null);
                }}
                onConfirm={confirmToggleStatus}
                title={togglingCategory?.status === 'ACTIVE' ? 'Desactivar categoría' : 'Activar categoría'}
                message={togglingCategory?.status === 'ACTIVE'
                    ? `¿Estás seguro de que deseas desactivar la categoría "${togglingCategory?.name}"?`
                    : `¿Estás seguro de que deseas activar la categoría "${togglingCategory?.name}"?`
                }
                confirmText={togglingCategory?.status === 'ACTIVE' ? 'Desactivar' : 'Activar'}
                cancelText="Cancelar"
                variant={togglingCategory?.status === 'ACTIVE' ? 'warning' : 'info'}
                loading={toggleLoading}
            />
        </div>
    );
}

