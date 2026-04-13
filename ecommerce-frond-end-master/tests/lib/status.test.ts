import { describe, it, expect } from 'vitest';
import {
    ORDER_STATUS_MAP,
    PRODUCT_STATUS_MAP,
    CATEGORY_STATUS_MAP,
    USER_STATUS_MAP,
    PAYMENT_METHOD_MAP,
    getOrderStatusLabel,
    getProductStatusLabel,
    getCategoryStatusLabel,
    getUserStatusLabel,
    getPaymentMethodLabel,
    ORDER_STATUSES,
    PRODUCT_STATUSES,
    CATEGORY_STATUSES,
    USER_STATUSES,
    PAYMENT_METHODS,
} from '@/lib/status';

// =============================================
// Status Maps
// =============================================

describe('ORDER_STATUS_MAP', () => {
    it('should map all order statuses', () => {
        expect(ORDER_STATUS_MAP.PENDING).toBe('Pendiente');
        expect(ORDER_STATUS_MAP.CONFIRMED).toBe('Confirmado');
        expect(ORDER_STATUS_MAP.PROCESSING).toBe('En proceso');
        expect(ORDER_STATUS_MAP.SHIPPED).toBe('Enviado');
        expect(ORDER_STATUS_MAP.DELIVERED).toBe('Entregado');
        expect(ORDER_STATUS_MAP.CANCELED).toBe('Cancelado');
    });

    it('should have exactly 6 statuses', () => {
        expect(Object.keys(ORDER_STATUS_MAP)).toHaveLength(6);
    });
});

describe('PRODUCT_STATUS_MAP', () => {
    it('should map ACTIVE and INACTIVE', () => {
        expect(PRODUCT_STATUS_MAP.ACTIVE).toBe('Activo');
        expect(PRODUCT_STATUS_MAP.INACTIVE).toBe('Inactivo');
    });
});

describe('CATEGORY_STATUS_MAP', () => {
    it('should map ACTIVE and INACTIVE', () => {
        expect(CATEGORY_STATUS_MAP.ACTIVE).toBe('Activo');
        expect(CATEGORY_STATUS_MAP.INACTIVE).toBe('Inactivo');
    });
});

describe('USER_STATUS_MAP', () => {
    it('should map ACTIVE, INACTIVE, and SUSPENDED', () => {
        expect(USER_STATUS_MAP.ACTIVE).toBe('Activo');
        expect(USER_STATUS_MAP.INACTIVE).toBe('Inactivo');
        expect(USER_STATUS_MAP.SUSPENDED).toBe('Suspendido');
    });
});

describe('PAYMENT_METHOD_MAP', () => {
    it('should map all payment methods', () => {
        expect(PAYMENT_METHOD_MAP.CASH).toBe('Efectivo');
        expect(PAYMENT_METHOD_MAP.CREDIT_CARD).toBe('Tarjeta de crédito');
        expect(PAYMENT_METHOD_MAP.DEBIT_CARD).toBe('Tarjeta de débito');
        expect(PAYMENT_METHOD_MAP.PAYPAL).toBe('PayPal');
        expect(PAYMENT_METHOD_MAP.TRANSFER).toBe('Transferencia');
    });

    it('should have exactly 5 methods', () => {
        expect(Object.keys(PAYMENT_METHOD_MAP)).toHaveLength(5);
    });
});

// =============================================
// Label helper functions
// =============================================

describe('getOrderStatusLabel', () => {
    it('should return the label for a known status', () => {
        expect(getOrderStatusLabel('PENDING')).toBe('Pendiente');
        expect(getOrderStatusLabel('SHIPPED')).toBe('Enviado');
    });

    it('should return the raw string for an unknown status', () => {
        expect(getOrderStatusLabel('UNKNOWN')).toBe('UNKNOWN');
    });
});

describe('getProductStatusLabel', () => {
    it('should return "Activo" for ACTIVE', () => {
        expect(getProductStatusLabel('ACTIVE')).toBe('Activo');
    });

    it('should return raw string for unknown status', () => {
        expect(getProductStatusLabel('DRAFT')).toBe('DRAFT');
    });
});

describe('getCategoryStatusLabel', () => {
    it('should return "Inactivo" for INACTIVE', () => {
        expect(getCategoryStatusLabel('INACTIVE')).toBe('Inactivo');
    });
});

describe('getUserStatusLabel', () => {
    it('should return "Suspendido" for SUSPENDED', () => {
        expect(getUserStatusLabel('SUSPENDED')).toBe('Suspendido');
    });

    it('should return raw string for unknown status', () => {
        expect(getUserStatusLabel('BANNED')).toBe('BANNED');
    });
});

describe('getPaymentMethodLabel', () => {
    it('should return "PayPal" for PAYPAL', () => {
        expect(getPaymentMethodLabel('PAYPAL')).toBe('PayPal');
    });

    it('should return raw string for unknown method', () => {
        expect(getPaymentMethodLabel('BITCOIN')).toBe('BITCOIN');
    });
});

// =============================================
// Selector arrays
// =============================================

describe('ORDER_STATUSES array', () => {
    it('should have 6 entries', () => {
        expect(ORDER_STATUSES).toHaveLength(6);
    });

    it('each entry should have value and label', () => {
        ORDER_STATUSES.forEach((entry) => {
            expect(entry).toHaveProperty('value');
            expect(entry).toHaveProperty('label');
            expect(typeof entry.value).toBe('string');
            expect(typeof entry.label).toBe('string');
        });
    });
});

describe('PRODUCT_STATUSES array', () => {
    it('should have 2 entries', () => {
        expect(PRODUCT_STATUSES).toHaveLength(2);
    });
});

describe('CATEGORY_STATUSES array', () => {
    it('should have 2 entries', () => {
        expect(CATEGORY_STATUSES).toHaveLength(2);
    });
});

describe('USER_STATUSES array', () => {
    it('should have 3 entries', () => {
        expect(USER_STATUSES).toHaveLength(3);
    });
});

describe('PAYMENT_METHODS array', () => {
    it('should have 5 entries', () => {
        expect(PAYMENT_METHODS).toHaveLength(5);
    });

    it('should include CASH with label Efectivo', () => {
        const cash = PAYMENT_METHODS.find((m) => m.value === 'CASH');
        expect(cash).toBeDefined();
        expect(cash!.label).toBe('Efectivo');
    });
});

