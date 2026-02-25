import { GraphQLClient, gql } from 'graphql-request';

const endpoint = process.env.NEXT_PUBLIC_API_URL
    ? `${process.env.NEXT_PUBLIC_API_URL}/graphql`
    : 'http://localhost:8080/graphql';

let authToken: string | null = null;
export const setAuthToken = (token: string | null) => {
    authToken = token;
};

const getClient = () => new GraphQLClient(endpoint, {
    headers: authToken ? { Authorization: `Bearer ${authToken}` } : {},
});

const GET_ALL_PRODUCTS_ACTIVE = gql`
  query {
    getAllProductsActive {
       id,
       name,
       price,
       imagesUrl
    }
  }
`;
export const getAllProductsActive = async () => {
    try {
        const data: any = await getClient().request(GET_ALL_PRODUCTS_ACTIVE);
        return data.getAllProductsActive;
    } catch (error) {
        console.error('Error fetching items:', error);
        throw error;
    }
};

const GET_ALL_PRODUCTS_ACTIVE_BY_CATEGORY_ID = gql`
  query getAllProductsActiveByCategoryId($categoryId: String!) {
    getAllProductsActiveByCategoryId(categoryId: $categoryId) {
      id,
      name,
      price,
      imagesUrl
    }
  }
`;

export const getAllProductsActiveByCategoryId = async (categoryId: string) => {
    try {
        const data: any = await getClient().request(GET_ALL_PRODUCTS_ACTIVE_BY_CATEGORY_ID, {categoryId});
        return data.getAllProductsActiveByCategoryId;
    } catch (error) {
        console.error('Error fetching items:', error);
        throw error;
    }
};

const GET_ALL_CATEGORIES_ACTIVE = gql`
    query {
        getAllCategoriesActive {
            id,
            name,
            status
        }
    }
    `;

export const getAllCategoriesActive = async () => {
    try {
        const data: any = await getClient().request(GET_ALL_CATEGORIES_ACTIVE);
        // Filtrar solo categorías activas (doble verificación)
        const categories = data.getAllCategoriesActive || [];
        return categories.filter((cat: any) => cat.status === 'ACTIVE');
    } catch (error) {
        console.error('Error fetching categories:', error);
        throw error;
    }
};

//getProductById
const GET_PRODUCT_BY_ID = gql`
  query getProductById($id: String!) {
    getProductById(id: $id) {
      id,
      name,
      price,
      description,
      imagesUrl,
      category {
      name
      },
      colors,
      sizes
    }
  }
`;
export const getProductById = async (id: string) => {
    try {
        const data: any = await getClient().request(GET_PRODUCT_BY_ID, {id});
        return data.getProductById;
    } catch (error) {
        console.error('Error fetching items:', error);
        throw error;
    }
};

//getAllProductsActiveByName
const GET_ALL_PRODUCTS_ACTIVE_BY_NAME = gql`
  query getAllProductsActiveByName($name: String!) {
    getAllProductsActiveByName(name: $name) {
      id,
      name,
      price,
      imagesUrl
    }
  }
`;
export const getAllProductsActiveByName = async (name: string) => {
    try {
        const data: any = await getClient().request(GET_ALL_PRODUCTS_ACTIVE_BY_NAME, {name});
        return data.getAllProductsActiveByName;
    } catch (error) {
        console.error('Error fetching items:', error);
        throw error;
    }
};

// Get My Orders (for authenticated users)
const GET_MY_ORDERS = gql`
    query getMyOrders {
        getMyOrders {
            id
            orderCode
            emailCustomer
            address
            city
            description
            paymentMethod
            status
            total
            createdAt
            updatedAt
            products {
                id
                quantity
                price
                size
                color
                product {
                    id
                    name
                    imagesUrl
                }
            }
        }
    }
`;

export interface OrderProduct {
    id: string;
    quantity: number;
    price: number;
    size?: string;
    color?: string;
    product?: {
        id: string;
        name: string;
        imagesUrl: string[];
    };
}

export interface UserOrder {
    id: string;
    orderCode: string;
    emailCustomer: string;
    address: string;
    city: string;
    description?: string;
    paymentMethod: string;
    status: string;
    total: number;
    createdAt: string;
    updatedAt?: string;
    products?: OrderProduct[];
}

export const getMyOrders = async (): Promise<UserOrder[]> => {
    try {
        const data: any = await getClient().request(GET_MY_ORDERS);
        return data.getMyOrders || [];
    } catch (error) {
        console.error('Error fetching my orders:', error);
        return [];
    }
};

// Payment Methods
export interface PaymentMethodOption {
    value: string;
    label: string;
}

const GET_PAYMENT_METHODS = gql`
    query {
        getPaymentMethods {
            value
            label
        }
    }
`;

export const getPaymentMethods = async (): Promise<PaymentMethodOption[]> => {
    try {
        const data: any = await getClient().request(GET_PAYMENT_METHODS);
        return data.getPaymentMethods || [];
    } catch (error) {
        console.error('Error fetching payment methods:', error);
        // Fallback en caso de error (debe coincidir con PaymentMethod.java)
        return [
            { value: 'CASH', label: 'Efectivo' },
            { value: 'CREDIT_CARD', label: 'Tarjeta de crédito' },
            { value: 'DEBIT_CARD', label: 'Tarjeta de débito' },
            { value: 'PAYPAL', label: 'PayPal' },
            { value: 'TRANSFER', label: 'Transferencia' }
        ];
    }
};

// Homepage Config
export interface HomepageConfigProduct {
    id: string;
    name: string;
    price: number;
    imagesUrl?: string[];
}

export interface HomepageConfig {
    id: string;
    featuredProductMain?: HomepageConfigProduct;
    featuredProductSecondary1?: HomepageConfigProduct;
    featuredProductSecondary2?: HomepageConfigProduct;
    carouselProducts?: HomepageConfigProduct[];
    showCarousel?: boolean;
    carouselTitle?: string;
    bannerTitle?: string;
    bannerSubtitle?: string;
    bannerImageUrl?: string;
    bannerLink?: string;
    bannerEnabled?: boolean;
    updatedAt?: string;
}

const GET_HOMEPAGE_CONFIG = gql`
    query getHomepageConfig {
        getHomepageConfig {
            id
            featuredProductMain {
                id
                name
                price
                imagesUrl
            }
            featuredProductSecondary1 {
                id
                name
                price
                imagesUrl
            }
            featuredProductSecondary2 {
                id
                name
                price
                imagesUrl
            }
            carouselProducts {
                id
                name
                price
                imagesUrl
            }
            showCarousel
            carouselTitle
            bannerTitle
            bannerSubtitle
            bannerImageUrl
            bannerLink
            bannerEnabled
            updatedAt
        }
    }
`;

export const getHomepageConfig = async (): Promise<HomepageConfig | null> => {
    try {
        const data: any = await getClient().request(GET_HOMEPAGE_CONFIG);
        return data.getHomepageConfig;
    } catch (error) {
        console.error('Error fetching homepage config:', error);
        return null;
    }
};

