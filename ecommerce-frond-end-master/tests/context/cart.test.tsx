import { describe, it, expect, beforeEach, vi } from 'vitest';
import { renderHook, act } from '@testing-library/react';
import React from 'react';
import { CartProvider, useCart } from '@/context/cart';

const localStorageMock = (() => {
    let store: Record<string, string> = {};
    return {
        getItem: vi.fn((key: string) => store[key] || null),
        setItem: vi.fn((key: string, value: string) => { store[key] = value; }),
        removeItem: vi.fn((key: string) => { delete store[key]; }),
        clear: vi.fn(() => { store = {}; }),
    };
})();
Object.defineProperty(window, 'localStorage', { value: localStorageMock });

const wrapper = ({ children }: { children: React.ReactNode }) =>
    React.createElement(CartProvider, null, children);

const sampleProduct = {
    id: 1, name: 'Test Product', price: 10000,
    imagesUrl: ['https://example.com/img.jpg'],
    quantity: 1, size: 'M', color: 'Rojo',
};

describe('useCart hook', () => {
    beforeEach(() => { localStorageMock.clear(); vi.clearAllMocks(); });

    it('should throw when used outside CartProvider', () => {
        expect(() => renderHook(() => useCart())).toThrow('useCart must be used within a CartProvider');
    });

    it('should start with an empty cart', () => {
        const { result } = renderHook(() => useCart(), { wrapper });
        expect(result.current.cart).toEqual([]);
        expect(result.current.total).toBe(0);
    });

    it('should add a product to the cart', () => {
        const { result } = renderHook(() => useCart(), { wrapper });
        act(() => { result.current.addToCart(sampleProduct); });
        expect(result.current.cart).toHaveLength(1);
        expect(result.current.cart[0].name).toBe('Test Product');
    });

    it('should increment quantity when adding existing product', () => {
        const { result } = renderHook(() => useCart(), { wrapper });
        act(() => { result.current.addToCart(sampleProduct); });
        act(() => { result.current.addToCart(sampleProduct); });
        expect(result.current.cart).toHaveLength(1);
        expect(result.current.cart[0].quantity).toBe(2);
    });

    it('should calculate total correctly', () => {
        const { result } = renderHook(() => useCart(), { wrapper });
        act(() => { result.current.addToCart(sampleProduct); });
        expect(result.current.total).toBe(10000);
        act(() => { result.current.addToCart(sampleProduct); });
        expect(result.current.total).toBe(20000);
    });

    it('should remove a product from the cart', () => {
        const { result } = renderHook(() => useCart(), { wrapper });
        act(() => { result.current.addToCart(sampleProduct); });
        act(() => { result.current.removeFromCart('1'); });
        expect(result.current.cart).toHaveLength(0);
    });

    it('should add quantity to a product', () => {
        const { result } = renderHook(() => useCart(), { wrapper });
        act(() => { result.current.addToCart(sampleProduct); });
        act(() => { result.current.addQuantity('1'); });
        expect(result.current.cart[0].quantity).toBe(2);
    });

    it('should subtract quantity from a product', () => {
        const { result } = renderHook(() => useCart(), { wrapper });
        act(() => { result.current.addToCart(sampleProduct); });
        act(() => { result.current.addQuantity('1'); });
        act(() => { result.current.subtractQuantity('1'); });
        expect(result.current.cart[0].quantity).toBe(1);
    });

    it('should remove product when subtracting to zero', () => {
        const { result } = renderHook(() => useCart(), { wrapper });
        act(() => { result.current.addToCart(sampleProduct); });
        act(() => { result.current.subtractQuantity('1'); });
        expect(result.current.cart).toHaveLength(0);
    });

    it('should clear the cart', () => {
        const { result } = renderHook(() => useCart(), { wrapper });
        act(() => { result.current.addToCart(sampleProduct); });
        act(() => { result.current.clearCart(); });
        expect(result.current.cart).toHaveLength(0);
        expect(result.current.total).toBe(0);
    });

    it('should handle multiple different products', () => {
        const { result } = renderHook(() => useCart(), { wrapper });
        const product2 = { ...sampleProduct, id: 2, name: 'Product 2', price: 5000 };
        act(() => { result.current.addToCart(sampleProduct); });
        act(() => { result.current.addToCart(product2); });
        expect(result.current.cart).toHaveLength(2);
        expect(result.current.total).toBe(15000);
    });
});

