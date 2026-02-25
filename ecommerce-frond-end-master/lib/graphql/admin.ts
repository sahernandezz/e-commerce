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

// =============================================
// Types
// =============================================

export interface DashboardStats {
    totalProducts: number;
    totalOrders: number;
    totalUsers: number;
    pendingOrders: number;
    totalRevenue: number;
    averageOrderValue: number;
    ordersThisMonth: number;
    newUsersThisMonth: number;
}

export interface TopSellingProduct {
    product: {
        id: string;
        name: string;
        price: number;
        imagesUrl?: string[];
    };
    totalSold: number;
    revenue: number;
}

export interface Order {
    id: string;
    orderCode: string;
    emailCustomer: string;
    address: string;
    city: string;
    description?: string;
    paymentMethod: string;
    status: string;
    total: number;
    createdAt?: string;
    updatedAt?: string;
    products?: OrderProduct[];
}

export interface OrderProduct {
    id: string;
    quantity: number;
    unitPrice: number;
    total: number;
    name?: string;
    size?: string;
    color?: string;
}

export interface AdminUser {
    id: string;
    email: string;
    name?: string;
    role?: string;
    status: string;
    createdAt?: string;
}

export interface Category {
    id: string;
    name: string;
    status: string;
}

export interface Product {
    id: string;
    name: string;
    price: number;
    description?: string;
    status: string;
    createdAt?: string;
    updatedAt?: string;
    discount?: number;
    category?: Category;
    imagesUrl?: string[];
    colors?: string[];
    sizes?: string[];
}

export interface PageResponse<T> {
    content: T[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
    first: boolean;
    last: boolean;
    hasNext: boolean;
    hasPrevious: boolean;
}

export interface StatusOption {
    value: string;
    label: string;
}

// =============================================
// Dashboard Queries
// =============================================

const GET_DASHBOARD_STATS = gql`
    query getDashboardStats {
        getDashboardStats {
            totalProducts
            totalOrders
            totalUsers
            pendingOrders
            totalRevenue
            averageOrderValue
            ordersThisMonth
            newUsersThisMonth
        }
    }
`;

const GET_TOP_SELLING_PRODUCTS = gql`
    query getTopSellingProducts($limit: Int) {
        getTopSellingProducts(limit: $limit) {
            product {
                id
                name
                price
                imagesUrl
            }
            totalSold
            revenue
        }
    }
`;

const GET_RECENT_ORDERS = gql`
    query getRecentOrders($limit: Int) {
        getRecentOrders(limit: $limit) {
            id
            orderCode
            emailCustomer
            status
            total
            createdAt
        }
    }
`;

// =============================================
// Paginated Queries
// =============================================

const GET_PRODUCTS_PAGINATED = gql`
    query getProductsPaginated($page: Int!, $size: Int!, $status: String, $search: String) {
        getProductsPaginated(page: $page, size: $size, status: $status, search: $search) {
            content {
                id
                name
                price
                description
                status
                createdAt
                updatedAt
                discount
                category {
                    id
                    name
                }
                imagesUrl
                colors
                sizes
            }
            page
            size
            totalElements
            totalPages
            first
            last
            hasNext
            hasPrevious
        }
    }
`;

const GET_CATEGORIES_PAGINATED = gql`
    query getCategoriesPaginated($page: Int!, $size: Int!, $status: String, $search: String) {
        getCategoriesPaginated(page: $page, size: $size, status: $status, search: $search) {
            content {
                id
                name
                status
            }
            page
            size
            totalElements
            totalPages
            first
            last
            hasNext
            hasPrevious
        }
    }
`;

const GET_ORDERS_PAGINATED = gql`
    query getOrdersPaginated($page: Int!, $size: Int!, $status: String, $customerEmail: String) {
        getOrdersPaginated(page: $page, size: $size, status: $status, customerEmail: $customerEmail) {
            content {
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
                    unitPrice
                    total
                    name
                    size
                    color
                }
            }
            page
            size
            totalElements
            totalPages
            first
            last
            hasNext
            hasPrevious
        }
    }
`;

const GET_USERS_PAGINATED = gql`
    query getUsersPaginated($page: Int!, $size: Int!, $status: String, $search: String) {
        getUsersPaginated(page: $page, size: $size, status: $status, search: $search) {
            content {
                id
                email
                name
                role
                status
                createdAt
            }
            page
            size
            totalElements
            totalPages
            first
            last
            hasNext
            hasPrevious
        }
    }
`;

// =============================================
// Status Queries
// =============================================

const GET_PRODUCT_STATUSES = gql`
    query getProductStatuses {
        getProductStatuses {
            value
            label
        }
    }
`;

const GET_CATEGORY_STATUSES = gql`
    query getCategoryStatuses {
        getCategoryStatuses {
            value
            label
        }
    }
`;

const GET_ORDER_STATUSES = gql`
    query getOrderStatuses {
        getOrderStatuses {
            value
            label
        }
    }
`;

const GET_USER_STATUSES = gql`
    query getUserStatuses {
        getUserStatuses {
            value
            label
        }
    }
`;

const GET_ALL_CATEGORIES_ACTIVE = gql`
    query getAllCategoriesActive {
        getAllCategoriesActive {
            id
            name
            status
        }
    }
`;

// =============================================
// Mutations
// =============================================

const UPDATE_ORDER_STATUS = gql`
    mutation updateOrderStatus($id: ID!, $status: String!) {
        updateOrderStatus(id: $id, status: $status) {
            id
            status
        }
    }
`;

const UPDATE_USER_STATUS = gql`
    mutation updateUserStatus($id: ID!, $status: String!) {
        updateUserStatus(id: $id, status: $status) {
            id
            status
        }
    }
`;

const UPDATE_PRODUCT_STATUS = gql`
    mutation updateProductStatus($id: ID!, $status: String!) {
        updateProductStatus(id: $id, status: $status) {
            id
            status
        }
    }
`;

const UPDATE_CATEGORY_STATUS = gql`
    mutation updateCategoryStatus($id: ID!, $status: String!) {
        updateCategoryStatus(id: $id, status: $status) {
            id
            status
        }
    }
`;

const CREATE_PRODUCT = gql`
    mutation addProduct($input: InputProductRequest!) {
        addProduct(input: $input) {
            id
            name
        }
    }
`;

const UPDATE_PRODUCT = gql`
    mutation updateProduct($id: ID!, $input: InputProductRequest!) {
        updateProduct(id: $id, input: $input) {
            id
            name
        }
    }
`;

const DELETE_PRODUCT = gql`
    mutation deleteProduct($id: ID!) {
        deleteProduct(id: $id)
    }
`;

const CREATE_CATEGORY = gql`
    mutation createCategory($input: InputCategoryRequest!) {
        createCategory(input: $input) {
            id
            name
        }
    }
`;

const UPDATE_CATEGORY = gql`
    mutation updateCategory($id: ID!, $input: InputCategoryRequest!) {
        updateCategory(id: $id, input: $input) {
            id
            name
        }
    }
`;

const DELETE_CATEGORY = gql`
    mutation deleteCategory($id: ID!) {
        deleteCategory(id: $id)
    }
`;

// =============================================
// API Functions
// =============================================

// Dashboard
export const getDashboardStats = async (): Promise<DashboardStats> => {
    const data: any = await getClient().request(GET_DASHBOARD_STATS);
    return data.getDashboardStats;
};

export const getTopSellingProducts = async (limit: number = 5): Promise<TopSellingProduct[]> => {
    const data: any = await getClient().request(GET_TOP_SELLING_PRODUCTS, { limit });
    return data.getTopSellingProducts || [];
};

export const getRecentOrders = async (limit: number = 5): Promise<Order[]> => {
    const data: any = await getClient().request(GET_RECENT_ORDERS, { limit });
    return data.getRecentOrders || [];
};

// Products
export const getProductsPaginated = async (
    page: number = 0,
    size: number = 10,
    status?: string,
    search?: string
): Promise<PageResponse<Product>> => {
    const data: any = await getClient().request(GET_PRODUCTS_PAGINATED, {
        page,
        size,
        status: status || null,
        search: search || null
    });
    return data.getProductsPaginated;
};

export const createProduct = async (input: any): Promise<boolean> => {
    try {
        await getClient().request(CREATE_PRODUCT, { input });
        return true;
    } catch (error) {
        console.error('Error creating product:', error);
        return false;
    }
};

export const updateProduct = async (id: string, input: any): Promise<boolean> => {
    try {
        await getClient().request(UPDATE_PRODUCT, { id, input });
        return true;
    } catch (error) {
        console.error('Error updating product:', error);
        return false;
    }
};

export const updateProductStatus = async (id: string, status: string): Promise<boolean> => {
    try {
        await getClient().request(UPDATE_PRODUCT_STATUS, { id, status });
        return true;
    } catch (error) {
        console.error('Error updating product status:', error);
        return false;
    }
};

export const deleteProduct = async (id: string): Promise<boolean> => {
    try {
        await getClient().request(DELETE_PRODUCT, { id });
        return true;
    } catch (error) {
        console.error('Error deleting product:', error);
        return false;
    }
};

// Categories
export const getCategoriesPaginated = async (
    page: number = 0,
    size: number = 10,
    status?: string,
    search?: string
): Promise<PageResponse<Category>> => {
    const data: any = await getClient().request(GET_CATEGORIES_PAGINATED, {
        page,
        size,
        status: status || null,
        search: search || null
    });
    return data.getCategoriesPaginated;
};

export const getAllCategoriesActive = async (): Promise<Category[]> => {
    const data: any = await getClient().request(GET_ALL_CATEGORIES_ACTIVE);
    return data.getAllCategoriesActive || [];
};

export const createCategory = async (input: { name: string }): Promise<boolean> => {
    try {
        await getClient().request(CREATE_CATEGORY, { input });
        return true;
    } catch (error) {
        console.error('Error creating category:', error);
        return false;
    }
};

export const updateCategory = async (id: string, input: { name: string }): Promise<boolean> => {
    try {
        await getClient().request(UPDATE_CATEGORY, { id, input });
        return true;
    } catch (error) {
        console.error('Error updating category:', error);
        return false;
    }
};

export const updateCategoryStatus = async (id: string, status: string): Promise<boolean> => {
    try {
        await getClient().request(UPDATE_CATEGORY_STATUS, { id, status });
        return true;
    } catch (error) {
        console.error('Error updating category status:', error);
        return false;
    }
};

export const deleteCategory = async (id: string): Promise<boolean> => {
    try {
        await getClient().request(DELETE_CATEGORY, { id });
        return true;
    } catch (error) {
        console.error('Error deleting category:', error);
        return false;
    }
};

// Orders
export const getOrdersPaginated = async (
    page: number = 0,
    size: number = 10,
    status?: string,
    customerEmail?: string
): Promise<PageResponse<Order>> => {
    const data: any = await getClient().request(GET_ORDERS_PAGINATED, {
        page,
        size,
        status: status || null,
        customerEmail: customerEmail || null
    });
    return data.getOrdersPaginated;
};

export const getAllOrders = async (): Promise<Order[]> => {
    try {
        const response = await getOrdersPaginated(0, 1000);
        return response?.content || [];
    } catch (error) {
        console.error('Error fetching all orders:', error);
        return [];
    }
};

export const updateOrderStatus = async (id: string, status: string): Promise<boolean> => {
    try {
        await getClient().request(UPDATE_ORDER_STATUS, { id, status });
        return true;
    } catch (error) {
        console.error('Error updating order status:', error);
        return false;
    }
};

// Users
export const getUsersPaginated = async (
    page: number = 0,
    size: number = 10,
    status?: string,
    search?: string
): Promise<PageResponse<AdminUser>> => {
    const data: any = await getClient().request(GET_USERS_PAGINATED, {
        page,
        size,
        status: status || null,
        search: search || null
    });
    return data.getUsersPaginated;
};

export const updateUserStatus = async (id: string, status: string): Promise<boolean> => {
    try {
        await getClient().request(UPDATE_USER_STATUS, { id, status });
        return true;
    } catch (error) {
        console.error('Error updating user status:', error);
        return false;
    }
};

// Status Options
export const getProductStatuses = async (): Promise<StatusOption[]> => {
    const data: any = await getClient().request(GET_PRODUCT_STATUSES);
    return data.getProductStatuses || [];
};

export const getCategoryStatuses = async (): Promise<StatusOption[]> => {
    const data: any = await getClient().request(GET_CATEGORY_STATUSES);
    return data.getCategoryStatuses || [];
};

export const getOrderStatuses = async (): Promise<StatusOption[]> => {
    const data: any = await getClient().request(GET_ORDER_STATUSES);
    return data.getOrderStatuses || [];
};

export const getUserStatuses = async (): Promise<StatusOption[]> => {
    const data: any = await getClient().request(GET_USER_STATUSES);
    return data.getUserStatuses || [];
};

// =============================================
// Homepage Config
// =============================================

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

export interface HomepageConfigInput {
    featuredProductMainId?: number;
    featuredProductSecondary1Id?: number;
    featuredProductSecondary2Id?: number;
    carouselProductIds?: number[];
    showCarousel?: boolean;
    carouselTitle?: string;
    bannerTitle?: string;
    bannerSubtitle?: string;
    bannerImageUrl?: string;
    bannerLink?: string;
    bannerEnabled?: boolean;
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

const UPDATE_HOMEPAGE_CONFIG = gql`
    mutation updateHomepageConfig($input: HomepageConfigInput!) {
        updateHomepageConfig(input: $input) {
            id
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

export const updateHomepageConfig = async (input: HomepageConfigInput): Promise<HomepageConfig | null> => {
    try {
        const data: any = await getClient().request(UPDATE_HOMEPAGE_CONFIG, { input });
        return data.updateHomepageConfig;
    } catch (error) {
        console.error('Error updating homepage config:', error);
        return null;
    }
};

