import { describe, it, expect } from 'vitest';
import { cn } from '@/lib/utils';

describe('cn (className utility)', () => {
    it('should merge single class', () => {
        expect(cn('text-red-500')).toBe('text-red-500');
    });

    it('should merge multiple classes', () => {
        const result = cn('px-4', 'py-2');
        expect(result).toContain('px-4');
        expect(result).toContain('py-2');
    });

    it('should handle conditional classes', () => {
        const isActive = true;
        const result = cn('base', isActive && 'active');
        expect(result).toContain('base');
        expect(result).toContain('active');
    });

    it('should filter falsy values', () => {
        const result = cn('base', false, null, undefined, 'extra');
        expect(result).toContain('base');
        expect(result).toContain('extra');
        expect(result).not.toContain('false');
        expect(result).not.toContain('null');
    });

    it('should resolve tailwind conflicts (last wins)', () => {
        const result = cn('px-4', 'px-8');
        expect(result).toBe('px-8');
    });

    it('should resolve color conflicts', () => {
        const result = cn('text-red-500', 'text-blue-500');
        expect(result).toBe('text-blue-500');
    });

    it('should return empty string for no args', () => {
        expect(cn()).toBe('');
    });
});

