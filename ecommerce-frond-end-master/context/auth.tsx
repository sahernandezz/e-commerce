'use client'

import { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { loginUser, registerUser } from '@/lib/graphql/mutation';
import { setAuthToken as setQueryAuthToken } from '@/lib/graphql/query';
import { setAuthToken as setMutationAuthToken } from '@/lib/graphql/mutation';

interface User {
    id: string;
    email: string;
    name: string | null;
}

interface AuthContextType {
    token: string | null;
    user: User | null;
    isAuthenticated: boolean;
    login: (email: string, password: string) => Promise<void>;
    register: (name: string, email: string, password: string) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [token, setToken] = useState<string | null>(null);
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {
        if (typeof window !== 'undefined') {
            const storedToken = localStorage.getItem('token');
            const storedUser = localStorage.getItem('user');
            if (storedToken) {
                setToken(storedToken);
                setQueryAuthToken(storedToken);
                setMutationAuthToken(storedToken);
            }
            if (storedUser) {
                try {
                    setUser(JSON.parse(storedUser));
                } catch (e) {
                    console.error('Error parsing stored user:', e);
                }
            }
        }
    }, []);

    const login = async (email: string, password: string) => {
        const response = await loginUser(email, password);
        setToken(response.token);
        setUser(response.user);
        setQueryAuthToken(response.token);
        setMutationAuthToken(response.token);
        if (typeof window !== 'undefined') {
            localStorage.setItem('token', response.token);
            localStorage.setItem('user', JSON.stringify(response.user));
        }
    };

    const register = async (name: string, email: string, password: string) => {
        const response = await registerUser(name, email, password);
        setToken(response.token);
        setUser(response.user);
        setQueryAuthToken(response.token);
        setMutationAuthToken(response.token);
        if (typeof window !== 'undefined') {
            localStorage.setItem('token', response.token);
            localStorage.setItem('user', JSON.stringify(response.user));
        }
    };

    const logout = () => {
        setToken(null);
        setUser(null);
        setQueryAuthToken(null);
        setMutationAuthToken(null);
        if (typeof window !== 'undefined') {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
        }
    };

    return (
        <AuthContext.Provider value={{ token, user, isAuthenticated: !!token, login, register, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
