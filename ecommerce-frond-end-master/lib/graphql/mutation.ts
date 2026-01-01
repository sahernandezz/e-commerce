import { GraphQLClient, gql } from 'graphql-request';
import { OrderInput } from '@/lib/types';

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

const CREATE_ORDER = gql`
    mutation createOrder($input: InputOrderRequest!) {
        createOrder(input: $input)
    }
`;

const LOGIN = gql`
    mutation login($email: String!, $password: String!) {
        login(input: { email: $email, password: $password }) {
            token
            message
            user {
                id
                email
                name
            }
        }
    }
`;

const REGISTER = gql`
    mutation register($name: String!, $email: String!, $password: String!) {
        register(input: { name: $name, email: $email, password: $password }) {
            token
            message
            user {
                id
                email
                name
            }
        }
    }
`;

export const createOrder = async (input: OrderInput) => {
    try {
        await getClient().request(CREATE_ORDER, { input });
        return 0;
    } catch (error) {
        return 1;
    }
};

interface AuthResponse {
    token: string;
    user: {
        id: string;
        email: string;
        name: string | null;
    };
}

export const loginUser = async (email: string, password: string): Promise<AuthResponse> => {
    const data: any = await getClient().request(LOGIN, { email, password });
    return {
        token: data.login.token,
        user: data.login.user
    };
};

export const registerUser = async (name: string, email: string, password: string): Promise<AuthResponse> => {
    const data: any = await getClient().request(REGISTER, { name, email, password });
    return {
        token: data.register.token,
        user: data.register.user
    };
};

