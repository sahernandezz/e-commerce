export function currencyFormatter(value: number) {
    const formatter = new Intl.NumberFormat('en-US', {
        style: 'currency',
        minimumFractionDigits: 2,
        currency: 'COP'
    })
    return formatter.format(value)
}