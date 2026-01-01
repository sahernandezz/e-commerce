'use client'

import { useState, FormEvent } from 'react';
import { useAuth } from '@/context/auth';
import { useLoginModal } from '@/context/login-modal';

type AuthMode = 'login' | 'register';

export const Auth = () => {
    const { login, register } = useAuth();
    const { setOpen } = useLoginModal();
    const [mode, setMode] = useState<AuthMode>('login');
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    const resetForm = () => {
        setName('');
        setEmail('');
        setPassword('');
        setConfirmPassword('');
        setError(null);
    };

    const handleModeChange = (newMode: AuthMode) => {
        setMode(newMode);
        resetForm();
    };

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        setError(null);

        if (mode === 'register') {
            if (password !== confirmPassword) {
                setError('Las contraseñas no coinciden');
                return;
            }
            if (password.length < 6) {
                setError('La contraseña debe tener al menos 6 caracteres');
                return;
            }
        }

        setLoading(true);
        try {
            if (mode === 'login') {
                await login(email, password);
            } else {
                await register(name, email, password);
            }
            setOpen(false);
        } catch (err: any) {
            const message = err?.response?.errors?.[0]?.message ||
                           (mode === 'login' ? 'Usuario o contraseña incorrectos' : 'Error al crear la cuenta');
            setError(message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex flex-col gap-4 max-w-sm mx-auto">
            {/* Tabs */}
            <div className="flex border-b border-neutral-200 dark:border-neutral-700">
                <button
                    type="button"
                    onClick={() => handleModeChange('login')}
                    className={`flex-1 py-2 text-sm font-medium transition-colors ${
                        mode === 'login'
                            ? 'text-blue-600 border-b-2 border-blue-600'
                            : 'text-neutral-500 hover:text-neutral-700 dark:text-neutral-400 dark:hover:text-neutral-300'
                    }`}
                >
                    Iniciar Sesión
                </button>
                <button
                    type="button"
                    onClick={() => handleModeChange('register')}
                    className={`flex-1 py-2 text-sm font-medium transition-colors ${
                        mode === 'register'
                            ? 'text-blue-600 border-b-2 border-blue-600'
                            : 'text-neutral-500 hover:text-neutral-700 dark:text-neutral-400 dark:hover:text-neutral-300'
                    }`}
                >
                    Crear Cuenta
                </button>
            </div>

            {/* Form */}
            <form onSubmit={handleSubmit} className="flex flex-col gap-4 p-4">
                <h2 className="text-xl font-semibold text-center mb-2">
                    {mode === 'login' ? 'Iniciar Sesión' : 'Crear Cuenta'}
                </h2>

                {mode === 'register' && (
                    <input
                        type="text"
                        placeholder="Nombre completo"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        className="border rounded p-2 dark:bg-neutral-800 dark:border-neutral-700"
                        required
                        disabled={loading}
                    />
                )}

                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="border rounded p-2 dark:bg-neutral-800 dark:border-neutral-700"
                    required
                    disabled={loading}
                />

                <input
                    type="password"
                    placeholder="Contraseña"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="border rounded p-2 dark:bg-neutral-800 dark:border-neutral-700"
                    required
                    disabled={loading}
                />

                {mode === 'register' && (
                    <input
                        type="password"
                        placeholder="Confirmar contraseña"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        className="border rounded p-2 dark:bg-neutral-800 dark:border-neutral-700"
                        required
                        disabled={loading}
                    />
                )}

                {error && <span className="text-red-500 text-sm text-center">{error}</span>}

                <button
                    type="submit"
                    className="bg-blue-600 text-white p-2 rounded hover:bg-blue-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                    disabled={loading}
                >
                    {loading
                        ? 'Cargando...'
                        : mode === 'login'
                            ? 'Iniciar sesión'
                            : 'Crear cuenta'
                    }
                </button>

                <p className="text-xs text-center text-neutral-500 dark:text-neutral-400">
                    {mode === 'login'
                        ? '¿No tienes cuenta? '
                        : '¿Ya tienes cuenta? '
                    }
                    <button
                        type="button"
                        onClick={() => handleModeChange(mode === 'login' ? 'register' : 'login')}
                        className="text-blue-600 hover:underline"
                    >
                        {mode === 'login' ? 'Regístrate' : 'Inicia sesión'}
                    </button>
                </p>
            </form>
        </div>
    );
};
