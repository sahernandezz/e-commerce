
export type Product = {
    id: number;
    name: string;
    price: number;
    imagesUrl: string[];
}

export interface ProductInput {
    quantity: number;
    productId: string;
    size: string;
}

export interface OrderInput {
    emailCustomer: string;
    address: string;
    city: string;
    description?: string;
    paymentMethod: string;
    products: ProductInput[];
}

export type ProductAddCart = {
    id: number;
    name: string;
    price: number;
    imagesUrl: string[];
    quantity: number;
    size: string;
    color: string;
}

export type Category = {
    id: number;
    name: string;
}