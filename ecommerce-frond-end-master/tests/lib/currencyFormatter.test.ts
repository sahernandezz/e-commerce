import { describe, it, expect } from 'vitest';
import { currencyFormatter } from '@/lib/currencyFormatter';

describe('currencyFormatter', () => {
    it('should format a positive integer value', () => {
        const result = currencyFormatter(50000);
        expect(result).toContain('50,000');
        expect(result).toContain('COP');
    });

    it('should format zero', () => {
        const result = currencyFormatter(0);
        expect(result).toContain('0');
    });

    it('should format decimal values with two fraction digits', () => {
        const result = currencyFormatter(1234.5);
        expect(result).toContain('1,234.50');
    });

    it('should format large numbers with proper grouping', () => {
        const result = currencyFormatter(1000000);
        expect(result).toContain('1,000,000');
    });

    it('should format negative values', () => {
        const result = currencyFormatter(-500);
        expect(result).toContain('500');
    });
});

