'use client'

import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { ProductAddCart } from '@/lib/types';

interface CartContextType {
    cart: ProductAddCart[];
    addToCart: (product: ProductAddCart) => void;
    removeFromCart: (productId: string) => void;
    addQuantity: (productId: string) => void;
    subtractQuantity: (productId: string) => void;
    total: number;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider = ({ children }: { children: ReactNode }) => {
    const [cart, setCart] = useState<ProductAddCart[]>([]);
    const [initialized, setInitialized] = useState(false);
    const total = cart.reduce((acc, product) => acc + product.price * product.quantity, 0);

    useEffect(() => {
        const storedCart = localStorage.getItem('cart');
        if (storedCart) {
            try {
                setCart(JSON.parse(storedCart) as ProductAddCart[]);
            } catch (err) {
                console.error('Error parsing cart from localStorage', err);
            }
        }
        setInitialized(true);
    }, []);

    useEffect(() => {
        if (initialized) {
            localStorage.setItem('cart', JSON.stringify(cart));
        }
    }, [cart, initialized]);

    const addToCart = (product: ProductAddCart) => {
        setCart((prevCart) => {
            const found = prevCart.find((p) => p.id === product.id);
            if (found) {
                return prevCart.map((p) =>
                    p.id === product.id ? { ...p, quantity: p.quantity + 1 } : p
                );
            }
            return [...prevCart, { ...product, quantity: 1 }];
        });
    };

    const removeFromCart = (productId: string) => {
        setCart((prevCart) => prevCart.filter((product) => product.id.toString() !== productId));
    };

    const addQuantity = (productId: string) => {
        setCart((prevCart) =>
            prevCart.map((p) =>
                p.id.toString() === productId ? { ...p, quantity: p.quantity + 1 } : p
            )
        );
    };

    const subtractQuantity = (productId: string) => {
        setCart((prevCart) => {
            const product = prevCart.find((p) => p.id.toString() === productId);
            if (!product) return prevCart;
            if (product.quantity === 1) {
                return prevCart.filter((p) => p.id.toString() !== productId);
            }
            return prevCart.map((p) =>
                p.id.toString() === productId ? { ...p, quantity: p.quantity - 1 } : p
            );
        });
    };

    return (
        <CartContext.Provider value={{ cart, total, addToCart, removeFromCart, addQuantity, subtractQuantity }}>
            {children}
        </CartContext.Provider>
    );
};

export const useCart = () => {
    const context = useContext(CartContext);
    if (!context) {
        throw new Error('useCart must be used within a CartProvider');
    }
    return context;
};