/**
 * Mapeo de estados sincronizado con los enums del backend
 * Los valores (keys) corresponden a los nombres de los enums en Java
 * Los labels corresponden a displayName de cada enum
 */

// Estados de Órdenes (OrderStatus.java)
export const ORDER_STATUS_MAP: Record<string, string> = {
    PENDING: 'Pendiente',
    CONFIRMED: 'Confirmado',
    PROCESSING: 'En proceso',
    SHIPPED: 'Enviado',
    DELIVERED: 'Entregado',
    CANCELED: 'Cancelado',
};

// Estados de Productos (ProductStatus.java)
export const PRODUCT_STATUS_MAP: Record<string, string> = {
    ACTIVE: 'Activo',
    INACTIVE: 'Inactivo',
};

// Estados de Categorías (CategoryStatus.java)
export const CATEGORY_STATUS_MAP: Record<string, string> = {
    ACTIVE: 'Activo',
    INACTIVE: 'Inactivo',
};

// Estados de Usuarios (UserStatus.java)
export const USER_STATUS_MAP: Record<string, string> = {
    ACTIVE: 'Activo',
    INACTIVE: 'Inactivo',
    SUSPENDED: 'Suspendido',
};

// Métodos de Pago (PaymentMethod.java)
export const PAYMENT_METHOD_MAP: Record<string, string> = {
    CASH: 'Efectivo',
    CREDIT_CARD: 'Tarjeta de crédito',
    DEBIT_CARD: 'Tarjeta de débito',
    PAYPAL: 'PayPal',
    TRANSFER: 'Transferencia',
};

// Helper functions
export const getOrderStatusLabel = (status: string): string =>
    ORDER_STATUS_MAP[status] || status;

export const getProductStatusLabel = (status: string): string =>
    PRODUCT_STATUS_MAP[status] || status;

export const getCategoryStatusLabel = (status: string): string =>
    CATEGORY_STATUS_MAP[status] || status;

export const getUserStatusLabel = (status: string): string =>
    USER_STATUS_MAP[status] || status;

export const getPaymentMethodLabel = (method: string): string =>
    PAYMENT_METHOD_MAP[method] || method;

// Arrays para selectores (valor y label)
export const ORDER_STATUSES = Object.entries(ORDER_STATUS_MAP).map(([value, label]) => ({
    value,
    label,
}));

export const PRODUCT_STATUSES = Object.entries(PRODUCT_STATUS_MAP).map(([value, label]) => ({
    value,
    label,
}));

export const CATEGORY_STATUSES = Object.entries(CATEGORY_STATUS_MAP).map(([value, label]) => ({
    value,
    label,
}));

export const USER_STATUSES = Object.entries(USER_STATUS_MAP).map(([value, label]) => ({
    value,
    label,
}));

export const PAYMENT_METHODS = Object.entries(PAYMENT_METHOD_MAP).map(([value, label]) => ({
    value,
    label,
}));

