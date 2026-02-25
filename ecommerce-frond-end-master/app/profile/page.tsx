'use client';

import { useEffect, useState } from 'react';
import { useAuth } from '@/context/auth';
import { useRouter } from 'next/navigation';
import { EditIcon, ClipboardIcon, SettingsIcon, UserIcon, LockIcon, ShieldIcon } from '@/components/icons';
import { getRoleDisplayName } from '@/lib/roles';

export default function ProfilePage() {
    const { user, isAuthenticated, isAdmin } = useAuth();
    const router = useRouter();
    const [isEditing, setIsEditing] = useState(false);
    const [name, setName] = useState('');
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    useEffect(() => {
        if (!isAuthenticated) {
            router.push('/');
            return;
        }
        if (user) {
            setName(user.name || '');
            setLoading(false);
        }
    }, [isAuthenticated, user, router]);

    const handleUpdateProfile = async (e: React.FormEvent) => {
        e.preventDefault();
        setMessage('');
        setError('');
        setMessage('Función de actualización próximamente disponible');
        setIsEditing(false);
    };

    const handleChangePassword = async (e: React.FormEvent) => {
        e.preventDefault();
        setMessage('');
        setError('');
        setMessage('Función de cambio de contraseña próximamente disponible');
        setCurrentPassword('');
        setNewPassword('');
    };

    if (loading) {
        return (
            <div className="flex justify-center items-center min-h-[60vh]">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    return (
        <div className="max-w-4xl mx-auto px-4 py-8">
            {/* Header */}
            <div className="mb-8">
                <h1 className="text-2xl font-bold text-black dark:text-white">Mi Perfil</h1>
                <p className="text-neutral-500 dark:text-neutral-400 mt-1">
                    Gestiona tu información personal y preferencias
                </p>
            </div>

            {/* Alertas */}
            {message && (
                <div className="mb-6 p-4 bg-green-50 dark:bg-green-900/20 border border-green-200 dark:border-green-800 text-green-700 dark:text-green-400 rounded-lg flex items-center gap-3">
                    <svg className="w-5 h-5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <span>{message}</span>
                </div>
            )}

            {error && (
                <div className="mb-6 p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-400 rounded-lg flex items-center gap-3">
                    <svg className="w-5 h-5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <span>{error}</span>
                </div>
            )}

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Columna izquierda - Información del perfil */}
                <div className="lg:col-span-1">
                    <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-6">
                        {/* Avatar y nombre */}
                        <div className="text-center">
                            <div className="w-24 h-24 mx-auto bg-gradient-to-br from-blue-500 to-blue-600 rounded-full flex items-center justify-center text-white text-3xl font-bold shadow-lg">
                                {user?.name?.charAt(0).toUpperCase() || user?.email?.charAt(0).toUpperCase() || '?'}
                            </div>
                            <h2 className="mt-4 text-lg font-semibold text-black dark:text-white">
                                {user?.name || 'Sin nombre'}
                            </h2>
                            <p className="text-sm text-neutral-500 dark:text-neutral-400">
                                {user?.email}
                            </p>

                            {/* Badges de estado y rol */}
                            <div className="mt-4 flex flex-wrap justify-center gap-2">
                                <span className="inline-flex items-center gap-1 px-3 py-1 text-xs font-medium rounded-full bg-green-100 dark:bg-green-900/30 text-green-700 dark:text-green-400">
                                    <svg className="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
                                        <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                                    </svg>
                                    Cuenta activa
                                </span>
                                <span className={`inline-flex items-center gap-1 px-3 py-1 text-xs font-medium rounded-full ${
                                    isAdmin 
                                        ? 'bg-purple-100 dark:bg-purple-900/30 text-purple-700 dark:text-purple-400' 
                                        : 'bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-400'
                                }`}>
                                    {isAdmin ? <ShieldIcon className="w-3 h-3" /> : <UserIcon className="w-3 h-3" />}
                                    {getRoleDisplayName(user?.role)}
                                </span>
                            </div>
                        </div>

                        {/* Acciones rápidas */}
                        <div className="mt-6 pt-6 border-t border-neutral-200 dark:border-neutral-800 space-y-3">
                            <button
                                onClick={() => router.push('/my-orders')}
                                className="w-full flex items-center gap-3 px-4 py-3 text-left text-sm font-medium text-black dark:text-white bg-neutral-50 dark:bg-neutral-900 hover:bg-neutral-100 dark:hover:bg-neutral-800 rounded-lg transition"
                            >
                                <ClipboardIcon className="w-5 h-5 text-neutral-500" />
                                <div>
                                    <p>Mis Órdenes</p>
                                    <p className="text-xs text-neutral-500 dark:text-neutral-400">Ver historial de compras</p>
                                </div>
                            </button>

                            {isAdmin && (
                                <button
                                    onClick={() => router.push('/admin')}
                                    className="w-full flex items-center gap-3 px-4 py-3 text-left text-sm font-medium text-purple-700 dark:text-purple-400 bg-purple-50 dark:bg-purple-900/20 hover:bg-purple-100 dark:hover:bg-purple-900/30 rounded-lg transition"
                                >
                                    <SettingsIcon className="w-5 h-5" />
                                    <div>
                                        <p>Panel de Admin</p>
                                        <p className="text-xs text-purple-600 dark:text-purple-500">Gestionar la tienda</p>
                                    </div>
                                </button>
                            )}
                        </div>
                    </div>
                </div>

                {/* Columna derecha - Formularios */}
                <div className="lg:col-span-2 space-y-6">
                    {/* Información Personal */}
                    <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg">
                        <div className="flex justify-between items-center p-6 border-b border-neutral-200 dark:border-neutral-800">
                            <div>
                                <h2 className="text-lg font-semibold text-black dark:text-white">
                                    Información Personal
                                </h2>
                                <p className="text-sm text-neutral-500 dark:text-neutral-400">
                                    Actualiza tu información de perfil
                                </p>
                            </div>
                            <button
                                onClick={() => setIsEditing(!isEditing)}
                                className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition ${
                                    isEditing 
                                        ? 'bg-neutral-100 dark:bg-neutral-800 text-neutral-700 dark:text-neutral-300' 
                                        : 'bg-blue-600 text-white hover:bg-blue-700'
                                }`}
                            >
                                {isEditing ? (
                                    <>
                                        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                                        </svg>
                                        Cancelar
                                    </>
                                ) : (
                                    <>
                                        <EditIcon className="w-4 h-4" />
                                        Editar
                                    </>
                                )}
                            </button>
                        </div>

                        <div className="p-6">
                            {isEditing ? (
                                <form onSubmit={handleUpdateProfile} className="space-y-4">
                                    <div>
                                        <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                            Nombre
                                        </label>
                                        <input
                                            type="text"
                                            value={name}
                                            onChange={(e) => setName(e.target.value)}
                                            className="w-full px-4 py-2.5 border border-neutral-300 dark:border-neutral-700 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
                                            placeholder="Tu nombre"
                                            required
                                        />
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                            Email
                                        </label>
                                        <input
                                            type="email"
                                            value={user?.email || ''}
                                            disabled
                                            className="w-full px-4 py-2.5 border border-neutral-200 dark:border-neutral-800 rounded-lg bg-neutral-50 dark:bg-neutral-900 text-neutral-500 dark:text-neutral-500 cursor-not-allowed"
                                        />
                                        <p className="mt-1.5 text-xs text-neutral-500">El email no puede ser modificado</p>
                                    </div>
                                    <div className="pt-2">
                                        <button
                                            type="submit"
                                            className="w-full py-2.5 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition font-medium"
                                        >
                                            Guardar Cambios
                                        </button>
                                    </div>
                                </form>
                            ) : (
                                <div className="space-y-4">
                                    <div className="flex items-center justify-between py-3 border-b border-neutral-100 dark:border-neutral-800">
                                        <div>
                                            <p className="text-sm text-neutral-500 dark:text-neutral-400">Nombre</p>
                                            <p className="font-medium text-black dark:text-white">{user?.name || 'Sin nombre'}</p>
                                        </div>
                                    </div>
                                    <div className="flex items-center justify-between py-3">
                                        <div>
                                            <p className="text-sm text-neutral-500 dark:text-neutral-400">Email</p>
                                            <p className="font-medium text-black dark:text-white">{user?.email}</p>
                                        </div>
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>

                    {/* Cambiar Contraseña */}
                    <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg">
                        <div className="p-6 border-b border-neutral-200 dark:border-neutral-800">
                            <h2 className="text-lg font-semibold text-black dark:text-white">
                                Seguridad
                            </h2>
                            <p className="text-sm text-neutral-500 dark:text-neutral-400">
                                Actualiza tu contraseña para mantener tu cuenta segura
                            </p>
                        </div>

                        <div className="p-6">
                            <form onSubmit={handleChangePassword} className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                        Contraseña Actual
                                    </label>
                                    <input
                                        type="password"
                                        value={currentPassword}
                                        onChange={(e) => setCurrentPassword(e.target.value)}
                                        className="w-full px-4 py-2.5 border border-neutral-300 dark:border-neutral-700 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
                                        placeholder="••••••••"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                        Nueva Contraseña
                                    </label>
                                    <input
                                        type="password"
                                        value={newPassword}
                                        onChange={(e) => setNewPassword(e.target.value)}
                                        className="w-full px-4 py-2.5 border border-neutral-300 dark:border-neutral-700 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
                                        placeholder="••••••••"
                                        required
                                    />
                                    <p className="mt-1.5 text-xs text-neutral-500">Mínimo 8 caracteres</p>
                                </div>
                                <div className="pt-2">
                                    <button
                                        type="submit"
                                        className="w-full py-2.5 bg-neutral-900 dark:bg-white text-white dark:text-black rounded-lg hover:bg-neutral-800 dark:hover:bg-neutral-100 transition font-medium"
                                    >
                                        Cambiar Contraseña
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
