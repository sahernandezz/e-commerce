'use client'

import {useEffect, useState} from "react";
import {getAllProductsActive, getHomepageConfig, HomepageConfig} from "@/lib/graphql/query";
import {Product} from "@/lib/types";
import {currencyFormatter} from "@/lib/currencyFormatter";
import Link from "next/link";

// Helper function to safely get image URL
const getImageUrl = (product: Product | undefined, index: number = 0): string | undefined => {
    return product?.imagesUrl?.[index] || undefined;
};

// Componente para card de producto
const ProductCard = ({ product, sizes = "(min-width: 768px) 33vw, 100vw", className = "" }: {
    product: Product | undefined,
    sizes?: string,
    className?: string
}) => {
    if (!product) return null;

    return (
        <Link href={`/product/${product.id?.toString() || ""}`} className={`relative block aspect-square h-full w-full ${className}`}>
            <div className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black relative border-neutral-200 dark:border-neutral-800">
                {getImageUrl(product) && (
                    <img
                        sizes={sizes}
                        className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-105"
                        src={getImageUrl(product)}
                        alt={product.name || ""}
                    />
                )}
                <div className="absolute bottom-0 left-0 flex w-full px-4 pb-4 @container/label">
                    <div className="flex items-center rounded-full border bg-white/70 p-1 text-xs font-semibold text-black backdrop-blur-md dark:border-neutral-800 dark:bg-black/70 dark:text-white">
                        <h3 className="mr-4 line-clamp-2 flex-grow pl-2 leading-none tracking-tight">{product.name}</h3>
                        <p className="flex-none rounded-full bg-blue-600 p-2 text-white">{currencyFormatter(product.price)}</p>
                    </div>
                </div>
            </div>
        </Link>
    );
};

export default function Home() {
    const [items, setItems] = useState<Product[]>([]);
    const [config, setConfig] = useState<HomepageConfig | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [productsData, configData] = await Promise.all([
                    getAllProductsActive(),
                    getHomepageConfig()
                ]);
                setItems(productsData);
                setConfig(configData);
            } catch (error) {
                console.error('Error fetching data:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) {
        return (
            <div className="mx-auto max-w-screen-2xl px-4 py-8">
                <div className="flex items-center justify-center h-[50vh]">
                    <div className="text-center">
                        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
                        <p className="text-neutral-500">Cargando productos...</p>
                    </div>
                </div>
            </div>
        );
    }

    if (items.length === 0) {
        return (
            <div className="mx-auto max-w-screen-2xl px-4 py-8">
                <div className="flex items-center justify-center h-[50vh]">
                    <div className="text-center">
                        <p className="text-neutral-500">No hay productos disponibles</p>
                    </div>
                </div>
            </div>
        );
    }

    // Helper para convertir HomepageConfigProduct a Product
    const convertToProduct = (configProduct: any): Product | undefined => {
        if (!configProduct) return undefined;
        return {
            ...configProduct,
            id: typeof configProduct.id === 'string' ? parseInt(configProduct.id, 10) : configProduct.id
        };
    };

    // Productos destacados desde la configuración o fallback a los primeros productos
    const featuredMain = config?.featuredProductMain ? convertToProduct(config.featuredProductMain) : items[0];
    const featuredSecondary1 = config?.featuredProductSecondary1 ? convertToProduct(config.featuredProductSecondary1) : (items[1] || items[0]);
    const featuredSecondary2 = config?.featuredProductSecondary2 ? convertToProduct(config.featuredProductSecondary2) : (items[2] || items[0]);

    // Productos del carrusel desde la configuración o fallback
    const carouselProducts = config?.carouselProducts?.length
        ? config.carouselProducts.map(convertToProduct).filter((p): p is Product => p !== undefined)
        : items.slice(0, 8);

    const showCarousel = config?.showCarousel ?? true;
    const carouselTitle = config?.carouselTitle || 'Productos Destacados';

    return (
        <>
            {/* Banner (si está habilitado) */}
            {config?.bannerEnabled && config?.bannerImageUrl && (
                <section className="mx-auto max-w-screen-2xl px-4 mb-4">
                    <Link href={config.bannerLink || '/search'} className="block">
                        <div className="relative rounded-lg overflow-hidden h-48 md:h-64">
                            <img
                                src={config.bannerImageUrl}
                                alt={config.bannerTitle || 'Banner'}
                                className="w-full h-full object-cover"
                            />
                            <div className="absolute inset-0 bg-black/40 flex flex-col justify-center items-center text-white">
                                {config.bannerTitle && (
                                    <h2 className="text-2xl md:text-4xl font-bold">{config.bannerTitle}</h2>
                                )}
                                {config.bannerSubtitle && (
                                    <p className="text-sm md:text-lg mt-2">{config.bannerSubtitle}</p>
                                )}
                            </div>
                        </div>
                    </Link>
                </section>
            )}

            {/* Productos Destacados */}
            <section className="mx-auto grid max-w-screen-2xl gap-4 px-4 pb-4 md:grid-cols-6 md:grid-rows-2 lg:max-h-[calc(100vh-200px)]">
                {/* Producto Principal */}
                <div className="md:col-span-4 md:row-span-2">
                    <ProductCard
                        product={featuredMain}
                        sizes="(min-width: 768px) 66vw, 100vw"
                    />
                </div>

                {/* Producto Secundario 1 */}
                <div className="md:col-span-2 md:row-span-1">
                    <ProductCard product={featuredSecondary1} />
                </div>

                {/* Producto Secundario 2 */}
                <div className="md:col-span-2 md:row-span-1">
                    <ProductCard product={featuredSecondary2} />
                </div>
            </section>

            {/* Carrusel de Productos */}
            {showCarousel && carouselProducts.length > 0 && (
                <section className="w-full pb-6 pt-1">
                    {carouselTitle && (
                        <h2 className="mx-auto max-w-screen-2xl px-4 mb-4 text-lg font-semibold text-black dark:text-white">
                            {carouselTitle}
                        </h2>
                    )}
                    <div className="overflow-x-auto">
                        <ul className="flex animate-carousel gap-4 px-4">
                            {carouselProducts.map((product, index) => (
                                <li
                                    key={product.id || index}
                                    className="relative aspect-square h-[30vh] max-h-[275px] w-2/3 max-w-[475px] flex-none md:w-1/3"
                                >
                                    <ProductCard
                                        product={product}
                                        sizes="(min-width: 1024px) 25vw, (min-width: 768px) 33vw, 50vw"
                                    />
                                </li>
                            ))}
                        </ul>
                    </div>
                </section>
            )}
        </>
    );
}
