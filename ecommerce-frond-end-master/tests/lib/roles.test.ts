import { describe, it, expect } from 'vitest';
import {
    RoleName,
    ROLES,
    getRoleDisplayName,
    isValidRole,
    isAdmin,
    isUser,
    getRoleOptions,
} from '@/lib/roles';

describe('RoleName enum', () => {
    it('should have ADMIN and USER values', () => {
        expect(RoleName.ADMIN).toBe('ADMIN');
        expect(RoleName.USER).toBe('USER');
    });
});

describe('ROLES map', () => {
    it('should contain info for ADMIN role', () => {
        const admin = ROLES[RoleName.ADMIN];
        expect(admin.code).toBe(RoleName.ADMIN);
        expect(admin.displayName).toBe('Administrador');
        expect(admin.description).toBe('Usuario con acceso completo al sistema');
    });

    it('should contain info for USER role', () => {
        const user = ROLES[RoleName.USER];
        expect(user.code).toBe(RoleName.USER);
        expect(user.displayName).toBe('Cliente');
        expect(user.description).toBe('Usuario regular del sistema');
    });
});

describe('getRoleDisplayName', () => {
    it('should return "Administrador" for ADMIN', () => {
        expect(getRoleDisplayName('ADMIN')).toBe('Administrador');
    });

    it('should return "Cliente" for USER', () => {
        expect(getRoleDisplayName('USER')).toBe('Cliente');
    });

    it('should return "Desconocido" for null', () => {
        expect(getRoleDisplayName(null)).toBe('Desconocido');
    });

    it('should return "Desconocido" for undefined', () => {
        expect(getRoleDisplayName(undefined)).toBe('Desconocido');
    });

    it('should return the raw string for an unknown role', () => {
        expect(getRoleDisplayName('SUPERADMIN')).toBe('SUPERADMIN');
    });
});

describe('isValidRole', () => {
    it('should return true for ADMIN', () => {
        expect(isValidRole('ADMIN')).toBe(true);
    });

    it('should return true for USER', () => {
        expect(isValidRole('USER')).toBe(true);
    });

    it('should return false for an unknown role', () => {
        expect(isValidRole('MODERATOR')).toBe(false);
    });

    it('should return false for null', () => {
        expect(isValidRole(null)).toBe(false);
    });

    it('should return false for undefined', () => {
        expect(isValidRole(undefined)).toBe(false);
    });

    it('should return false for empty string', () => {
        expect(isValidRole('')).toBe(false);
    });
});

describe('isAdmin', () => {
    it('should return true for ADMIN', () => {
        expect(isAdmin('ADMIN')).toBe(true);
    });

    it('should return false for USER', () => {
        expect(isAdmin('USER')).toBe(false);
    });

    it('should return false for null', () => {
        expect(isAdmin(null)).toBe(false);
    });

    it('should return false for undefined', () => {
        expect(isAdmin(undefined)).toBe(false);
    });
});

describe('isUser', () => {
    it('should return true for USER', () => {
        expect(isUser('USER')).toBe(true);
    });

    it('should return false for ADMIN', () => {
        expect(isUser('ADMIN')).toBe(false);
    });

    it('should return false for null', () => {
        expect(isUser(null)).toBe(false);
    });
});

describe('getRoleOptions', () => {
    it('should return an array of role options', () => {
        const options = getRoleOptions();
        expect(options).toHaveLength(2);
    });

    it('should include ADMIN option with label Administrador', () => {
        const options = getRoleOptions();
        const adminOption = options.find((o) => o.value === 'ADMIN');
        expect(adminOption).toBeDefined();
        expect(adminOption!.label).toBe('Administrador');
    });

    it('should include USER option with label Cliente', () => {
        const options = getRoleOptions();
        const userOption = options.find((o) => o.value === 'USER');
        expect(userOption).toBeDefined();
        expect(userOption!.label).toBe('Cliente');
    });
});

