'use client'

import {useEffect, useState} from "react";
import {getAllProductsActive} from "@/lib/graphql/query";
import {Product} from "@/lib/types";
import {currencyFormatter} from "@/lib/currencyFormatter";
import Link from "next/link";

// Helper function to safely get image URL
const getImageUrl = (product: Product | undefined, index: number = 0): string | undefined => {
    return product?.imagesUrl?.[index] || undefined;
};

export default function Home() {

    const [items, setItems] = useState<Product[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const getItems = async () => {
            try {
                const itemsData = await getAllProductsActive();
                setItems(itemsData);
            } catch (error) {
                console.error('Error fetching items:', error);
            } finally {
                setLoading(false);
            }
        };

        getItems();
    }, []);

    if (loading || items.length === 0) {
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

    return (
        <>
            <section
                className="mx-auto grid max-w-screen-2xl gap-4 px-4 pb-4 md:grid-cols-6 md:grid-rows-2 lg:max-h-[calc(100vh-200px)]">
                <div className="md:col-span-4 md:row-span-2">
                    <Link href={`/product/${items[0]?.id?.toString() || ""}`} className="relative block aspect-square h-full w-full">
                        <div
                            className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black relative border-neutral-200 dark:border-neutral-800">
                            {getImageUrl(items[0]) && (
                                <img sizes="(min-width: 768px) 66vw, 100vw"
                                     className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-105"
                                     src={getImageUrl(items[0])} alt={items[0]?.name || ""}/>
                            )}
                            <div
                                className="absolute bottom-0 left-0 flex w-full px-4 pb-4 @container/label lg:px-20 lg:pb-[35%]">
                                <div
                                    className="flex items-center rounded-full border bg-white/70 p-1 text-xs font-semibold text-black backdrop-blur-md dark:border-neutral-800 dark:bg-black/70 dark:text-white">
                                    <h3 className="mr-4 line-clamp-2 flex-grow pl-2 leading-none tracking-tight">{items[0]?.name}</h3>
                                    <p
                                        className="flex-none rounded-full bg-blue-600 p-2 text-white">{currencyFormatter(items[0].price)}</p>
                                </div>
                            </div>
                        </div>
                    </Link>
                </div>

                <div className="md:col-span-2 md:row-span-1">
                    <Link href={`/product/${items[0]?.id?.toString() || ""}`} className="relative block aspect-square h-full w-full">
                        <div
                            className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black relative border-neutral-200 dark:border-neutral-800">
                            <img sizes="(min-width: 768px) 33vw, 100vw"
                                 className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-105"
                                 src={getImageUrl(items[0])} alt={items[0]?.name || ""}/>
                            <div className="absolute bottom-0 left-0 flex w-full px-4 pb-4 @container/label">
                                <div
                                    className="flex items-center rounded-full border bg-white/70 p-1 text-xs font-semibold text-black backdrop-blur-md dark:border-neutral-800 dark:bg-black/70 dark:text-white">
                                    <h3 className="mr-4 line-clamp-2 flex-grow pl-2 leading-none tracking-tight">{items[0]?.name}</h3>
                                    <p
                                        className="flex-none rounded-full bg-blue-600 p-2 text-white">{currencyFormatter(items[0].price)}</p>
                                </div>
                            </div>
                        </div>
                    </Link>
                </div>

                <div className="md:col-span-2 md:row-span-1">
                    <Link href={`/product/${items[0]?.id?.toString() || ""}`} className="relative block aspect-square h-full w-full">
                        <div
                            className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black relative border-neutral-200 dark:border-neutral-800">
                            <img src={getImageUrl(items[0])} alt={items[0]?.name || ""} sizes="(min-width: 768px) 33vw, 100vw"
                                 className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-105"/>
                            <div className="absolute bottom-0 left-0 flex w-full px-4 pb-4 @container/label">
                                <div
                                    className="flex items-center rounded-full border bg-white/70 p-1 text-xs font-semibold text-black backdrop-blur-md dark:border-neutral-800 dark:bg-black/70 dark:text-white">
                                    <h3 className="mr-4 line-clamp-2 flex-grow pl-2 leading-none tracking-tight">{items[0]?.name}</h3>
                                    <p
                                        className="flex-none rounded-full bg-blue-600 p-2 text-white">{currencyFormatter(items[0].price)}</p>
                                </div>
                            </div>
                        </div>
                    </Link>
                </div>
            </section>

            <div className="w-full overflow-x-auto pb-6 pt-1">
                <ul className="flex animate-carousel gap-4">
                    <li className="relative aspect-square h-[30vh] max-h-[275px] w-2/3 max-w-[475px] flex-none md:w-1/3">
                        <Link href={`/product/${items[0]?.id?.toString() || ""}`} className="relative h-full w-full">
                            <div
                                className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black relative border-neutral-200 dark:border-neutral-800">
                                <img src={getImageUrl(items[0])} alt={items[0]?.name || ""}
                                     sizes="(min-width: 1024px) 25vw, (min-width: 768px) 33vw, 50vw"
                                     className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-10"/>

                                <div className="absolute bottom-0 left-0 flex w-full px-4 pb-4 @container/label">
                                    <div
                                        className="flex items-center rounded-full border bg-white/70 p-1 text-xs font-semibold text-black backdrop-blur-md dark:border-neutral-800 dark:bg-black/70 dark:text-white">
                                        <h3 className="mr-4 line-clamp-2 flex-grow pl-2 leading-none tracking-tight">{items[0]?.name}</h3>
                                        <p
                                            className="flex-none rounded-full bg-blue-600 p-2 text-white">{currencyFormatter(items[0].price)}</p>
                                    </div>
                                </div>
                            </div>
                        </Link>
                    </li>
                    <li className="relative aspect-square h-[30vh] max-h-[275px] w-2/3 max-w-[475px] flex-none md:w-1/3">
                        <Link href={`/product/${items[0]?.id?.toString() || ""}`} className="relative h-full w-full">
                            <div
                                className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black relative border-neutral-200 dark:border-neutral-800">
                                <img src={getImageUrl(items[0])} alt={items[0]?.name || ""}
                                     sizes="(min-width: 1024px) 25vw, (min-width: 768px) 33vw, 50vw"
                                     className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-10"/>

                                <div className="absolute bottom-0 left-0 flex w-full px-4 pb-4 @container/label">
                                    <div
                                        className="flex items-center rounded-full border bg-white/70 p-1 text-xs font-semibold text-black backdrop-blur-md dark:border-neutral-800 dark:bg-black/70 dark:text-white">
                                        <h3 className="mr-4 line-clamp-2 flex-grow pl-2 leading-none tracking-tight">{items[0]?.name}</h3>
                                        <p
                                            className="flex-none rounded-full bg-blue-600 p-2 text-white">{currencyFormatter(items[0].price)}</p>
                                    </div>
                                </div>
                            </div>
                        </Link>
                    </li>
                    <li className="relative aspect-square h-[30vh] max-h-[275px] w-2/3 max-w-[475px] flex-none md:w-1/3">
                        <Link href={`/product/${items[0]?.id?.toString() || ""}`} className="relative h-full w-full">
                            <div
                                className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black relative border-neutral-200 dark:border-neutral-800">
                                <img src={getImageUrl(items[0])} alt={items[0]?.name || ""}
                                     sizes="(min-width: 1024px) 25vw, (min-width: 768px) 33vw, 50vw"
                                     className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-10"/>

                                <div className="absolute bottom-0 left-0 flex w-full px-4 pb-4 @container/label">
                                    <div
                                        className="flex items-center rounded-full border bg-white/70 p-1 text-xs font-semibold text-black backdrop-blur-md dark:border-neutral-800 dark:bg-black/70 dark:text-white">
                                        <h3 className="mr-4 line-clamp-2 flex-grow pl-2 leading-none tracking-tight">{items[0]?.name}</h3>
                                        <p
                                            className="flex-none rounded-full bg-blue-600 p-2 text-white">{currencyFormatter(items[0].price)}</p>
                                    </div>
                                </div>
                            </div>
                        </Link>
                    </li>
                    <li className="relative aspect-square h-[30vh] max-h-[275px] w-2/3 max-w-[475px] flex-none md:w-1/3">
                        <Link href={`/product/${items[0]?.id?.toString() || ""}`} className="relative h-full w-full">
                            <div
                                className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black relative border-neutral-200 dark:border-neutral-800">
                                <img src={getImageUrl(items[0])} alt={items[0]?.name || ""}
                                     sizes="(min-width: 1024px) 25vw, (min-width: 768px) 33vw, 50vw"
                                     className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-10"/>

                                <div className="absolute bottom-0 left-0 flex w-full px-4 pb-4 @container/label">
                                    <div
                                        className="flex items-center rounded-full border bg-white/70 p-1 text-xs font-semibold text-black backdrop-blur-md dark:border-neutral-800 dark:bg-black/70 dark:text-white">
                                        <h3 className="mr-4 line-clamp-2 flex-grow pl-2 leading-none tracking-tight">{items[0]?.name}</h3>
                                        <p
                                            className="flex-none rounded-full bg-blue-600 p-2 text-white">{currencyFormatter(items[0].price)}</p>
                                    </div>
                                </div>
                            </div>
                        </Link>
                    </li>
                    <li className="relative aspect-square h-[30vh] max-h-[275px] w-2/3 max-w-[475px] flex-none md:w-1/3">
                        <Link href={`/product/${items[0]?.id?.toString() || ""}`} className="relative h-full w-full">
                            <div
                                className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black relative border-neutral-200 dark:border-neutral-800">
                                <img src={getImageUrl(items[0])} alt={items[0]?.name || ""}
                                     sizes="(min-width: 1024px) 25vw, (min-width: 768px) 33vw, 50vw"
                                     className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-10"/>

                                <div className="absolute bottom-0 left-0 flex w-full px-4 pb-4 @container/label">
                                    <div
                                        className="flex items-center rounded-full border bg-white/70 p-1 text-xs font-semibold text-black backdrop-blur-md dark:border-neutral-800 dark:bg-black/70 dark:text-white">
                                        <h3 className="mr-4 line-clamp-2 flex-grow pl-2 leading-none tracking-tight">{items[0]?.name}</h3>
                                        <p
                                            className="flex-none rounded-full bg-blue-600 p-2 text-white">{currencyFormatter(items[0].price)}</p>
                                    </div>
                                </div>
                            </div>
                        </Link>
                    </li>
                    <li className="relative aspect-square h-[30vh] max-h-[275px] w-2/3 max-w-[475px] flex-none md:w-1/3">
                        <Link href={`/product/${items[0]?.id?.toString() || ""}`} className="relative h-full w-full">
                            <div
                                className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black relative border-neutral-200 dark:border-neutral-800">
                                <img src={getImageUrl(items[0])} alt={items[0]?.name || ""}
                                     sizes="(min-width: 1024px) 25vw, (min-width: 768px) 33vw, 50vw"
                                     className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-10"/>

                                <div className="absolute bottom-0 left-0 flex w-full px-4 pb-4 @container/label">
                                    <div
                                        className="flex items-center rounded-full border bg-white/70 p-1 text-xs font-semibold text-black backdrop-blur-md dark:border-neutral-800 dark:bg-black/70 dark:text-white">
                                        <h3 className="mr-4 line-clamp-2 flex-grow pl-2 leading-none tracking-tight">{items[0]?.name}</h3>
                                        <p
                                            className="flex-none rounded-full bg-blue-600 p-2 text-white">{currencyFormatter(items[0].price)}</p>
                                    </div>
                                </div>
                            </div>
                        </Link>
                    </li>
                    <li className="relative aspect-square h-[30vh] max-h-[275px] w-2/3 max-w-[475px] flex-none md:w-1/3">
                        <Link href={`/product/${items[0]?.id?.toString() || ""}`} className="relative h-full w-full">
                            <div
                                className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black relative border-neutral-200 dark:border-neutral-800">
                                <img src={getImageUrl(items[0])} alt={items[0]?.name || ""}
                                     sizes="(min-width: 1024px) 25vw, (min-width: 768px) 33vw, 50vw"
                                     className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-10"/>

                                <div className="absolute bottom-0 left-0 flex w-full px-4 pb-4 @container/label">
                                    <div
                                        className="flex items-center rounded-full border bg-white/70 p-1 text-xs font-semibold text-black backdrop-blur-md dark:border-neutral-800 dark:bg-black/70 dark:text-white">
                                        <h3 className="mr-4 line-clamp-2 flex-grow pl-2 leading-none tracking-tight">{items[0]?.name}</h3>
                                        <p
                                            className="flex-none rounded-full bg-blue-600 p-2 text-white">{currencyFormatter(items[0].price)}</p>
                                    </div>
                                </div>
                            </div>
                        </Link>
                    </li>
                </ul>
            </div>
        </>
    );
}
