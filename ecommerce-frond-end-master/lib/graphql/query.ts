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
            name
        }
    }
    `;
let categoriesCache: { id: string, name: string }[] | null = null;

export const getAllCategoriesActive = async () => {
    if (categoriesCache) {
        return categoriesCache;
    }

    try {
        const data: any = await getClient().request(GET_ALL_CATEGORIES_ACTIVE);
        categoriesCache = data.getAllCategoriesActive;
        return categoriesCache;
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
