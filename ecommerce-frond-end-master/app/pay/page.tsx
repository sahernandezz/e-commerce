'use client'

import {useEffect, useState} from 'react';
import {useCart} from '@/context/cart';
import {createOrder} from "@/lib/graphql/mutation";
import {OrderInput, ProductInput} from "@/lib/types";
import {currencyFormatter} from "@/lib/currencyFormatter";

export default function PayPage() {
    const {cart, total} = useCart();

    const [email, setEmail] = useState('');
    const [address, setAddress] = useState('');
    const [city, setCity] = useState('');
    const [description, setDescription] = useState('');
    const [paymentMethod, setPaymentMethod] = useState('CASH');

    const add = async () => {
        const products: ProductInput[] = cart.map(product => ({
            quantity: product.quantity,
            productId: product.id.toString(),
            size: product.size
        }));

        const orderData: OrderInput = {
            emailCustomer: email,
            address,
            city,
            description,
            paymentMethod,
            products
        };

        const res = await createOrder(orderData);
     location.href = res === 0 ? '/pay/success' : '/pay/error';
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
                            <label className="block text-sm font-medium text-black dark:text-white">Email</label>
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="w-full p-2 mt-2 border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-black dark:text-white"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-black dark:text-white">Address</label>
                            <input
                                type="text"
                                value={address}
                                onChange={(e) => setAddress(e.target.value)}
                                className="w-full p-2 mt-2 border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-black dark:text-white"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-black dark:text-white">City</label>
                            <input
                                type="text"
                                value={city}
                                onChange={(e) => setCity(e.target.value)}
                                className="w-full p-2 mt-2 border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-black dark:text-white"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-black dark:text-white">Description</label>
                            <input
                                type="text"
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                className="w-full p-2 mt-2 border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-black dark:text-white"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-black dark:text-white">Payment
                                Method</label>
                            <select
                                value={paymentMethod}
                                onChange={(e) => setPaymentMethod(e.target.value)}
                                className="w-full p-2 mt-2  border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-black dark:text-white"
                            >
                                <option value="CASH">Cash</option>
                                <option value="CREDIT_CARD">Credit Card</option>
                                <option value="DEBIT_CARD">Debit Card</option>
                                <option value="PAYPAL">PayPal</option>
                            </select>
                        </div>
                        <button onClick={add} disabled={!cart.length || !email || !address || !city}
                                className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition-all duration-300 disabled:opacity-50">
                            Continue to shipping
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
};