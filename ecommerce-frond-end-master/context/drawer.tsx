'use client'


import { createContext, useContext, useState, ReactNode } from 'react';

interface DrawerContextType {
    open: boolean;
    setOpen: (open: boolean) => void;
}

const DrawerContext = createContext<DrawerContextType | undefined>(undefined);

export const DrawerProvider = ({ children }: { children: ReactNode }) => {
    const [open, setOpen] = useState(false);

    return (
        <DrawerContext.Provider value={{ open, setOpen }}>
    {children}
    </DrawerContext.Provider>
);
};

export const useDrawer = () => {
    const context = useContext(DrawerContext);
    if (!context) {
        throw new Error('useDrawer must be used within a DrawerProvider');
    }
    return context;
};