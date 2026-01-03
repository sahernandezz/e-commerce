'use client'

import { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { loginUser, registerUser } from '@/lib/graphql/mutation';
import { setAuthToken as setQueryAuthToken } from '@/lib/graphql/query';
import { setAuthToken as setMutationAuthToken } from '@/lib/graphql/mutation';
import { isAdmin } from '@/lib/roles';

interface User {
    id: string;
    email: string;
    name: string | null;
    role?: string | null;
}

interface AuthContextType {
    token: string | null;
    user: User | null;
    isAuthenticated: boolean;
    isAdmin: boolean;
    login: (email: string, password: string) => Promise<void>;
    register: (name: string, email: string, password: string) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Helper para normalizar el usuario y extraer el rol como string
const normalizeUser = (userData: any): User | null => {
    if (!userData) return null;

    // Extraer el rol como string (puede venir como objeto {id, name, status} o como string)
    let roleString: string | null = null;
    if (userData.role) {
        if (typeof userData.role === 'object' && userData.role.name) {
            roleString = userData.role.name;
        } else if (typeof userData.role === 'string') {
            roleString = userData.role;
        }
    }

    return {
        id: userData.id,
        email: userData.email?.toLowerCase() || '',
        name: userData.name,
        role: roleString
    };
};

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
                    const parsedUser = JSON.parse(storedUser);
                    setUser(normalizeUser(parsedUser));
                } catch (e) {
                    console.error('Error parsing stored user:', e);
                }
            }
        }
    }, []);

    const login = async (email: string, password: string) => {
        // Normalizar email a minúsculas
        const normalizedEmail = email.toLowerCase().trim();
        const response = await loginUser(normalizedEmail, password);
        const normalizedUser = normalizeUser(response.user);
        setToken(response.token);
        setUser(normalizedUser);
        setQueryAuthToken(response.token);
        setMutationAuthToken(response.token);
        if (typeof window !== 'undefined') {
            localStorage.setItem('token', response.token);
            localStorage.setItem('user', JSON.stringify(normalizedUser));
        }
    };

    const register = async (name: string, email: string, password: string) => {
        // Normalizar email a minúsculas
        const normalizedEmail = email.toLowerCase().trim();
        const response = await registerUser(name, normalizedEmail, password);
        const normalizedUser = normalizeUser(response.user);
        setToken(response.token);
        setUser(normalizedUser);
        setQueryAuthToken(response.token);
        setMutationAuthToken(response.token);
        if (typeof window !== 'undefined') {
            localStorage.setItem('token', response.token);
            localStorage.setItem('user', JSON.stringify(normalizedUser));
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

    // Obtener el rol como string para isAdmin
    const userRole = user?.role || null;

    return (
        <AuthContext.Provider value={{
            token,
            user,
            isAuthenticated: !!token,
            isAdmin: isAdmin(userRole),
            login,
            register,
            logout
        }}>
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
