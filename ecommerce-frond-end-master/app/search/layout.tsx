'use client'

import Link from "next/link";
import {Suspense, useEffect, useState} from "react";
import {usePathname, useSearchParams} from "next/navigation";
import {getAllCategoriesActive} from "@/lib/graphql/query";
import {Category} from "@/lib/types";

function SearchLayoutContent({children}: {children: React.ReactNode}) {
    const [items, setItems] = useState<Category[]>([]);
    const pathname = usePathname();
    const searchParams = useSearchParams();
    const currentSort = searchParams.get('sort') || '';

    useEffect(() => {
        const getItems = async () => {
            try {
                const itemsData = await getAllCategoriesActive();
                setItems(itemsData as Category[]);
            } catch (error) {
                console.error('Error fetching items:', error);
            }
        };

        getItems();
    }, []);

    const getSortUrl = (sort: string) => {
        const params = new URLSearchParams(searchParams.toString());
        if (sort) {
            params.set('sort', sort);
        } else {
            params.delete('sort');
        }
        return `${pathname}?${params.toString()}`;
    };

    return (
        <div className="mx-auto flex max-w-screen-2xl flex-col gap-8 px-4 pb-4 text-black md:flex-row dark:text-white">
            <div className="order-first w-full flex-none md:max-w-[125px]">
                <nav><h3 className="hidden text-xs text-neutral-500 md:block dark:text-neutral-400">Categoría</h3>
                    <ul className="hidden md:block">
                        <li key="Todo" className="mt-2 flex text-black dark:text-white">
                            <Link href="/search"
                                  className={`w-full text-sm underline-offset-4 hover:underline dark:hover:text-neutral-100 ${pathname === '/search' ? 'underline' : ''}`}>
                                Todo
                            </Link>
                        </li>

                        {
                            items.map((item: Category) => (
                                <li key={item.id} className="mt-2 flex text-black dark:text-white">
                                    <Link href={`/search/${item.id.toString()}`}
                                          className={`w-full text-sm underline-offset-4 hover:underline dark:hover:text-neutral-100 ${pathname === `/search/${item.id}` ? 'underline' : ''}`}>
                                        {item.name}
                                    </Link>
                                </li>
                            ))
                        }
                    </ul>

                    <ul className="md:hidden">
                        <div className="relative">
                            <select
                                className="w-full rounded border border-black/30 px-4 py-2 text-sm dark:border-white/30 bg-white dark:bg-black"
                                onChange={(e) => window.location.href = e.target.value}
                                value={pathname}
                            >
                                <option value="/search">Todo</option>
                                {items.map((item: Category) => (
                                    <option key={item.id} value={`/search/${item.id}`}>
                                        {item.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </ul>
                </nav>
            </div>

            <div className="order-last min-h-screen w-full md:order-none">
                <ul className="grid grid-flow-row gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3">
                    {children}
                </ul>
            </div>

            <div className="order-none flex-none md:order-last md:w-[125px]">
                <nav><h3 className="hidden text-xs text-neutral-500 md:block dark:text-neutral-400">Ordenar por</h3>
                    <ul className="hidden md:block">
                        <li className="mt-2 flex text-sm text-black dark:text-white">
                            <Link
                                href={getSortUrl('price-asc')}
                                className={`w-full hover:underline hover:underline-offset-4 ${currentSort === 'price-asc' ? 'underline underline-offset-4' : ''}`}
                            >
                                Precio: Menor a mayor
                            </Link>
                        </li>
                        <li className="mt-2 flex text-sm text-black dark:text-white">
                            <Link
                                href={getSortUrl('price-desc')}
                                className={`w-full hover:underline hover:underline-offset-4 ${currentSort === 'price-desc' ? 'underline underline-offset-4' : ''}`}
                            >
                                Precio: Mayor a menor
                            </Link>
                        </li>
                    </ul>

                    <ul className="md:hidden">
                        <div className="relative">
                            <select
                                className="w-full rounded border border-black/30 px-4 py-2 text-sm dark:border-white/30 bg-white dark:bg-black"
                                onChange={(e) => window.location.href = getSortUrl(e.target.value)}
                                value={currentSort}
                            >
                                <option value="">Relevancia</option>
                                <option value="price-asc">Precio: Menor a mayor</option>
                                <option value="price-desc">Precio: Mayor a menor</option>
                            </select>
                        </div>
                    </ul>
                </nav>
            </div>
        </div>
    );
}

export default function RootLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <Suspense fallback={
            <div className="mx-auto flex max-w-screen-2xl flex-col gap-8 px-4 pb-4 text-black md:flex-row dark:text-white">
                <div className="flex justify-center items-center w-full h-64">
                    <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
                </div>
            </div>
        }>
            <SearchLayoutContent>{children}</SearchLayoutContent>
        </Suspense>
    );
}
