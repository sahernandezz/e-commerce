'use client'

import {useEffect, useState} from 'react';
import {useCart} from '@/context/cart';
import {useAuth} from '@/context/auth';
import {createOrder} from "@/lib/graphql/mutation";
import {OrderInput, ProductInput} from "@/lib/types";
import {currencyFormatter} from "@/lib/currencyFormatter";

export default function PayPage() {
    const {cart, total, clearCart} = useCart();
    const {user, isAuthenticated} = useAuth();

    const [email, setEmail] = useState('');
    const [emailError, setEmailError] = useState('');
    const [address, setAddress] = useState('');
    const [city, setCity] = useState('');
    const [description, setDescription] = useState('');
    const [paymentMethod, setPaymentMethod] = useState('CASH');
    const [isProcessing, setIsProcessing] = useState(false);
    const [isEmailFromUser, setIsEmailFromUser] = useState(false);

    // Cargar el email del usuario si está logueado
    useEffect(() => {
        if (isAuthenticated && user?.email) {
            setEmail(user.email.toLowerCase());
            setIsEmailFromUser(true);
            setEmailError('');
        }
    }, [isAuthenticated, user]);

    // Validar email en tiempo real
    const validateEmail = (email: string) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!email) {
            setEmailError('El correo electrónico es requerido');
            return false;
        }
        if (!emailRegex.test(email)) {
            setEmailError('El correo electrónico debe ser válido');
            return false;
        }
        setEmailError('');
        return true;
    };

    const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const newEmail = e.target.value.toLowerCase().trim();
        setEmail(newEmail);
        validateEmail(newEmail);
    };

    const add = async () => {
        // Validar email antes de enviar
        if (!validateEmail(email)) {
            return;
        }

        if (!address || !city) {
            alert('Por favor complete todos los campos requeridos');
            return;
        }

        setIsProcessing(true);

        try {
            const products: ProductInput[] = cart.map(product => ({
                quantity: product.quantity,
                productId: product.id.toString(),
                size: product.size
            }));

            const orderData: OrderInput = {
                emailCustomer: email.toLowerCase().trim(),
                address,
                city,
                description,
                paymentMethod,
                products
            };

            const res = await createOrder(orderData);

            if (res === 0) {
                // Orden exitosa - vaciar el carrito
                clearCart();
                location.href = '/pay/success';
            } else {
                location.href = '/pay/error';
            }
        } catch (error) {
            console.error('Error al crear la orden:', error);
            location.href = '/pay/error';
        } finally {
            setIsProcessing(false);
        }
    }

    useEffect(() => {
        if (!cart.length) {
            location.href = '/';
        }
    }, []);

    return (
        <div className="w-full flex justify-center p-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="text-white p-8 w-full">
                    <div className="space-y-6">
                        <div>
                            <label className="block text-sm font-medium text-black dark:text-white">
                                Email <span className="text-red-500">*</span>
                            </label>
                            <input
                                type="email"
                                value={email}
                                onChange={handleEmailChange}
                                required
                                disabled={isEmailFromUser}
                                className={`w-full p-2 mt-2 border rounded focus:outline-none focus:ring-2 text-black dark:text-white ${
                                    emailError 
                                        ? 'border-red-500 focus:ring-red-500' 
                                        : 'border-gray-600 focus:ring-blue-500'
                                } ${isEmailFromUser ? 'bg-gray-100 dark:bg-gray-700 cursor-not-allowed' : ''}`}
                                placeholder="tu@email.com"
                            />
                            {emailError && (
                                <p className="mt-1 text-sm text-red-500">{emailError}</p>
                            )}
                            {isEmailFromUser && (
                                <p className="mt-1 text-xs text-gray-500 dark:text-gray-400">
                                    Usando el email de tu cuenta
                                </p>
                            )}
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-black dark:text-white">
                                Dirección <span className="text-red-500">*</span>
                            </label>
                            <input
                                type="text"
                                value={address}
                                onChange={(e) => setAddress(e.target.value)}
                                required
                                className="w-full p-2 mt-2 border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-black dark:text-white"
                                placeholder="Calle 123, Apartamento 4B"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-black dark:text-white">
                                Ciudad <span className="text-red-500">*</span>
                            </label>
                            <input
                                type="text"
                                value={city}
                                onChange={(e) => setCity(e.target.value)}
                                required
                                className="w-full p-2 mt-2 border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-black dark:text-white"
                                placeholder="Tu ciudad"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-black dark:text-white">Descripción</label>
                            <input
                                type="text"
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                className="w-full p-2 mt-2 border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-black dark:text-white"
                                placeholder="Instrucciones adicionales (opcional)"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-black dark:text-white">Método de Pago</label>
                            <select
                                value={paymentMethod}
                                onChange={(e) => setPaymentMethod(e.target.value)}
                                className="w-full p-2 mt-2  border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-black dark:text-white"
                            >
                                <option value="CASH">Efectivo</option>
                                <option value="CREDIT_CARD">Tarjeta de Crédito</option>
                                <option value="DEBIT_CARD">Tarjeta de Débito</option>
                                <option value="PAYPAL">PayPal</option>
                            </select>
                        </div>
                        <button
                            onClick={add}
                            disabled={!cart.length || !email || !address || !city || !!emailError || isProcessing}
                            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                            {isProcessing ? 'Procesando...' : 'Continuar con el envío'}
                        </button>
                    </div>
                </div>

                <div className="p-8 w-96 mt-8 md:mt-0">
                    {
                        cart.map(product => (
                            <div key={product.id}
                                 className="flex w-full flex-col border-b border-neutral-300 dark:border-neutral-700">
                                <div className="relative flex w-full flex-row justify-between px-1 py-4">
                                    <div className="flex flex-row">
                                        <div
                                            className="relative h-16 w-16 overflow-hidden rounded-md border border-neutral-300 bg-neutral-300 dark:border-neutral-700 dark:bg-neutral-900 dark:hover:bg-neutral-800">
                                            <img alt="Acme Slip-On Shoes" loading="lazy" width="64" height="64"
                                                 decoding="async" data-nimg="1"
                                                 className="h-full w-full object-cover"
                                                 src={product.imagesUrl[0]}/>
                                        </div>
                                    </div>

                                    <div className="flex flex-col justify-between ml-4">
                                        <p className="text-xs text-gray-500 dark:text-neutral-400">{product.size}</p>
                                        <p className="text-xs text-gray-500 dark:text-neutral-400">{product.color}</p>
                                        <p className="text-sm font-semibold text-black dark:text-white">{product.name} ({product.quantity})</p>
                                    </div>

                                    <div className="flex h-16 flex-col justify-between"><p
                                        className="flex justify-end space-y-2 text-right text-sm">$&nbsp;{currencyFormatter(product.price * product.quantity)}</p>
                                    </div>
                                </div>
                            </div>
                        ))
                    }
                </div>
            </div>
        </div>
    );
}

