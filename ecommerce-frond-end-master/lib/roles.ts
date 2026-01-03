/**
 * Enum centralizado para los roles del sistema.
 * Mantiene consistencia entre backend y frontend.
 */
export enum RoleName {
    ADMIN = 'ADMIN',
    USER = 'USER'
}

/**
 * Información completa de un rol
 */
export interface RoleInfo {
    code: RoleName;
    displayName: string;
    description: string;
}

/**
 * Mapa de roles con su información completa
 */
export const ROLES: Record<RoleName, RoleInfo> = {
    [RoleName.ADMIN]: {
        code: RoleName.ADMIN,
        displayName: 'Administrador',
        description: 'Usuario con acceso completo al sistema'
    },
    [RoleName.USER]: {
        code: RoleName.USER,
        displayName: 'Cliente',
        description: 'Usuario regular del sistema'
    }
};

/**
 * Obtiene el nombre para mostrar de un rol
 * @param role Código del rol
 * @returns Nombre para mostrar
 */
export function getRoleDisplayName(role: string | undefined | null): string {
    if (!role) return 'Desconocido';
    const roleInfo = ROLES[role as RoleName];
    return roleInfo?.displayName || role;
}

/**
 * Verifica si un rol es válido
 * @param role Código del rol a verificar
 * @returns true si es válido
 */
export function isValidRole(role: string | undefined | null): boolean {
    return role !== null && role !== undefined && Object.values(RoleName).includes(role as RoleName);
}

/**
 * Verifica si el rol es administrador
 * @param role Código del rol
 * @returns true si es ADMIN
 */
export function isAdmin(role: string | undefined | null): boolean {
    return role === RoleName.ADMIN;
}

/**
 * Verifica si el rol es usuario regular
 * @param role Código del rol
 * @returns true si es USER
 */
export function isUser(role: string | undefined | null): boolean {
    return role === RoleName.USER;
}

/**
 * Obtiene la lista de roles disponibles para selección
 * @returns Array de opciones de roles
 */
export function getRoleOptions(): { value: string; label: string }[] {
    return Object.values(RoleName).map(role => ({
        value: role,
        label: ROLES[role].displayName
    }));
}

