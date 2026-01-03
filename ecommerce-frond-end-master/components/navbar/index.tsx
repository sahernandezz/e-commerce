'use client'

import Link from "next/link";
import { useTheme } from "next-themes";
import { useDrawer } from "@/context/drawer";
import { useCart } from "@/context/cart";
import { useLoginModal } from "@/context/login-modal";
import { useAuth } from "@/context/auth";
import { useState, useEffect } from "react";

// Icono del sol (light mode)
const SunIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
         strokeWidth="1.5" stroke="currentColor" aria-hidden="true" className="h-4">
        <path strokeLinecap="round" strokeLinejoin="round"
              d="M12 3v1.5M12 19.5V21M4.22 4.22l1.06 1.06M18.72 18.72l1.06 1.06M3 12h1.5M19.5 12H21M4.22 19.78l1.06-1.06M18.72 5.28l1.06-1.06M12 7.5a4.5 4.5 0 1 1 0 9 4.5 4.5 0 0 1 0-9z"/>
    </svg>
);

// Icono de la luna (dark mode)
const MoonIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
         strokeWidth="1.5" stroke="currentColor" aria-hidden="true" className="h-4">
        <path strokeLinecap="round" strokeLinejoin="round"
              d="M21.752 15.002A9.72 9.72 0 0 1 18 15.75c-5.385 0-9.75-4.365-9.75-9.75 0-1.33.266-2.597.748-3.752A9.753 9.753 0 0 0 3 11.25C3 16.635 7.365 21 12.75 21a9.753 9.753 0 0 0 9.002-5.998Z"/>
    </svg>
);

export const Navbar = () => {
    const { theme, setTheme } = useTheme();
    const { open, setOpen } = useDrawer();
    const { cart } = useCart();
    const { setOpen: setLoginOpen } = useLoginModal();
    const { isAuthenticated, user, logout, isAdmin } = useAuth();
    const [menuOpen, setMenuOpen] = useState(false);
    const [userMenuOpen, setUserMenuOpen] = useState(false);
    const [mounted, setMounted] = useState(false);

    // Solo renderizar el icono del tema después de que el componente se monte
    // para evitar errores de hidratación
    useEffect(() => {
        setMounted(true);
    }, []);

    const toggleTheme = () => {
        setTheme(theme === "dark" ? "light" : "dark");
    };

    return (
        <>
            <nav className="relative flex items-center justify-between p-4 lg:px-6">
                <div className="block flex-none md:hidden">
                    <button
                        onClick={() => setMenuOpen(!menuOpen)}
                        className="flex h-11 w-11 items-center justify-center rounded-md border border-neutral-200 text-black transition-colors md:hidden dark:border-neutral-700 dark:text-white">
                        {menuOpen ? (
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 24 24"
                                strokeWidth="1.5"
                                stroke="currentColor"
                                aria-hidden="true"
                                className="h-4"
                            >
                                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18 18 6M6 6l12 12"/>
                            </svg>
                        ) : (
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5"
                                 stroke="currentColor" aria-hidden="true" className="h-4">
                                <path strokeLinecap="round" strokeLinejoin="round"
                                      d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"/>
                            </svg>
                        )}
                    </button>
                </div>

                <div className="flex w-full items-center">
                    <div className="flex w-full md:w-1/3">
                        <Link className="mr-2 flex w-full items-center justify-center md:w-auto lg:mr-6" href="/">
                            <div className="ml-2 flex-none text-sm font-medium uppercase md:hidden lg:block">Ecommerce
                            </div>
                        </Link>
                        <ul className="hidden gap-6 text-sm md:flex md:items-center">
                            <li><Link
                                className="text-neutral-500 underline-offset-4 hover:text-black hover:underline dark:text-neutral-400 dark:hover:text-neutral-300"
                                href="/search">All</Link></li>
                        </ul>
                    </div>

                    <div className="hidden justify-center md:flex md:w-1/3">
                    </div>

                    <div className="flex gap-3 justify-end md:w-1/3">
                        {/* Botón de tema - solo renderiza el icono correcto después del mount */}
                        <button
                            aria-label="Toggle theme"
                            onClick={toggleTheme}
                            className="relative flex h-11 w-11 items-center justify-center rounded-md border border-neutral-200 text-black transition-colors dark:border-neutral-700 dark:text-white"
                        >
                            {mounted ? (
                                theme === "dark" ? <SunIcon /> : <MoonIcon />
                            ) : (
                                // Placeholder mientras se monta para evitar layout shift
                                <div className="h-4 w-4" />
                            )}
                        </button>

                        {/* Botón de usuario / login */}
                        <div className="relative">
                            {isAuthenticated ? (
                                <>
                                    <button
                                        aria-label="User menu"
                                        onClick={() => setUserMenuOpen(!userMenuOpen)}
                                        className="relative flex h-11 items-center justify-center gap-2 rounded-md border border-neutral-200 px-3 text-black transition-colors dark:border-neutral-700 dark:text-white"
                                    >
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                             strokeWidth="1.5" stroke="currentColor" className="h-4 w-4">
                                            <path strokeLinecap="round" strokeLinejoin="round"
                                                  d="M12 12c2.485 0 4.5-2.015 4.5-4.5S14.485 3 12 3 7.5 5.015 7.5 7.5 9.515 12 12 12zm0 1.5c-3.315 0-6 2.685-6 6v1.5h12V19.5c0-3.315-2.685-6-6-6z"/>
                                        </svg>
                                        <span className="hidden md:inline text-sm">{user?.name || user?.email}</span>
                                    </button>
                                    {userMenuOpen && (
                                        <>
                                            <div
                                                className="fixed inset-0 z-40"
                                                onClick={() => setUserMenuOpen(false)}
                                            />
                                            <div className="absolute right-0 top-12 z-50 w-48 rounded-md border border-neutral-200 bg-white py-1 shadow-lg dark:border-neutral-700 dark:bg-neutral-900">
                                                <div className="border-b border-neutral-200 px-4 py-2 dark:border-neutral-700">
                                                    <p className="text-sm font-medium">{user?.name || 'Usuario'}</p>
                                                    <p className="text-xs text-neutral-500">{user?.email}</p>
                                                    {isAdmin && (
                                                        <span className="inline-block mt-1 px-2 py-0.5 text-xs bg-purple-100 text-purple-800 rounded-full dark:bg-purple-900 dark:text-purple-200">
                                                            Admin
                                                        </span>
                                                    )}
                                                </div>
                                                <Link
                                                    href="/profile"
                                                    onClick={() => setUserMenuOpen(false)}
                                                    className="block w-full px-4 py-2 text-left text-sm text-neutral-700 hover:bg-neutral-100 dark:text-neutral-300 dark:hover:bg-neutral-800"
                                                >
                                                    👤 Mi Perfil
                                                </Link>
                                                <Link
                                                    href="/my-orders"
                                                    onClick={() => setUserMenuOpen(false)}
                                                    className="block w-full px-4 py-2 text-left text-sm text-neutral-700 hover:bg-neutral-100 dark:text-neutral-300 dark:hover:bg-neutral-800"
                                                >
                                                    📋 Mis Órdenes
                                                </Link>
                                                {isAdmin && (
                                                    <Link
                                                        href="/admin"
                                                        onClick={() => setUserMenuOpen(false)}
                                                        className="block w-full px-4 py-2 text-left text-sm text-purple-600 hover:bg-neutral-100 dark:text-purple-400 dark:hover:bg-neutral-800"
                                                    >
                                                        ⚙️ Panel Admin
                                                    </Link>
                                                )}
                                                <div className="border-t border-neutral-200 dark:border-neutral-700 mt-1 pt-1">
                                                    <button
                                                        onClick={() => {
                                                            logout();
                                                            setUserMenuOpen(false);
                                                        }}
                                                        className="w-full px-4 py-2 text-left text-sm text-red-600 hover:bg-neutral-100 dark:hover:bg-neutral-800"
                                                    >
                                                        🚪 Cerrar sesión
                                                    </button>
                                                </div>
                                            </div>
                                        </>
                                    )}
                                </>
                            ) : (
                                <button
                                    aria-label="Login"
                                    onClick={() => setLoginOpen(true)}
                                    className="relative flex h-11 items-center justify-center gap-2 rounded-md border border-neutral-200 px-3 text-black transition-colors dark:border-neutral-700 dark:text-white"
                                >
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                         strokeWidth="1.5" stroke="currentColor" className="h-4 w-4">
                                        <path strokeLinecap="round" strokeLinejoin="round"
                                              d="M12 12c2.485 0 4.5-2.015 4.5-4.5S14.485 3 12 3 7.5 5.015 7.5 7.5 9.515 12 12 12zm0 1.5c-3.315 0-6 2.685-6 6v1.5h12V19.5c0-3.315-2.685-6-6-6z"/>
                                    </svg>
                                    <span className="hidden md:inline text-sm">Iniciar sesión</span>
                                </button>
                            )}
                        </div>

                        <button aria-label="Open cart" onClick={() => setOpen(!open)}>
                            <div
                                className="relative flex h-11 w-11 items-center justify-center rounded-md border border-neutral-200 text-black transition-colors dark:border-neutral-700 dark:text-white">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                     strokeWidth="1.5" stroke="currentColor" aria-hidden="true"
                                     className="h-4 transition-all ease-in-out hover:scale-110">
                                    <path strokeLinecap="round" strokeLinejoin="round"
                                          d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437M7.5 14.25a3 3 0 0 0-3 3h15.75m-12.75-3h11.218c1.121-2.3 2.1-4.684 2.924-7.138a60.114 60.114 0 0 0-16.536-1.84M7.5 14.25 5.106 5.272M6 20.25a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0Zm12.75 0a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0Z"/>
                                </svg>
                                {cart && cart.length > 0 && (
                                    <div
                                        className="absolute right-0 top-0 -mr-2 -mt-2 h-4 w-4 rounded bg-blue-600 text-[11px] font-medium text-white">
                                        {cart.length}
                                    </div>
                                )}
                            </div>
                        </button>
                    </div>
                </div>
            </nav>

            {/* Menú móvil */}
            {menuOpen && (
                <div className="fixed inset-0 z-50 bg-black/50 md:hidden" onClick={() => setMenuOpen(false)}>
                    <div
                        className="absolute left-0 top-0 h-full w-64 bg-white p-4 dark:bg-neutral-900"
                        onClick={(e) => e.stopPropagation()}
                    >
                        <div className="flex justify-between items-center mb-4">
                            <span className="font-medium">Menú</span>
                            <button onClick={() => setMenuOpen(false)}>
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                     strokeWidth="1.5" stroke="currentColor" className="h-6 w-6">
                                    <path strokeLinecap="round" strokeLinejoin="round" d="M6 18 18 6M6 6l12 12"/>
                                </svg>
                            </button>
                        </div>
                        <ul className="space-y-4">
                            <li>
                                <Link
                                    href="/"
                                    className="block text-neutral-500 hover:text-black dark:hover:text-white"
                                    onClick={() => setMenuOpen(false)}
                                >
                                    Home
                                </Link>
                            </li>
                            <li>
                                <Link
                                    href="/search"
                                    className="block text-neutral-500 hover:text-black dark:hover:text-white"
                                    onClick={() => setMenuOpen(false)}
                                >
                                    All Products
                                </Link>
                            </li>
                        </ul>
                    </div>
                </div>
            )}
        </>
    );
};

