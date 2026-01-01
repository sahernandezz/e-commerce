'use client'

import Link from "next/link";
import {useEffect, useState} from "react";
import {getAllCategoriesActive} from "@/lib/graphql/query";
import {Category} from "@/lib/types";

export default function RootLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>) {

    const [items, setItems] = useState([]);

    useEffect(() => {
        const getItems = async () => {
            try {
                const itemsData = await getAllCategoriesActive();
                setItems(itemsData as any);
            } catch (error) {
                console.error('Error fetching items:', error);
            }
        };

        getItems();
    }, []);

    return (
        <div className="mx-auto flex max-w-screen-2xl flex-col gap-8 px-4 pb-4 text-black md:flex-row dark:text-white">
            <div className="order-first w-full flex-none md:max-w-[125px]">
                <nav><h3 className="hidden text-xs text-neutral-500 md:block dark:text-neutral-400">Category</h3>
                    <ul className="hidden md:block">
                        <li key="Todo" className="mt-2 flex text-black dark:text-white">
                            <Link href="/search"
                                  className="w-full text-sm underline-offset-4 hover:underline dark:hover:text-neutral-100">Todo</Link>
                        </li>

                        {
                            items.map((item: Category, index: number) => (
                                <li key={index} className="mt-2 flex text-black dark:text-white">
                                    <Link href={`/search/${item.id.toString()}`}
                                          className="w-full text-sm underline-offset-4 hover:underline dark:hover:text-neutral-100">{item.name}</Link>
                                </li>
                            ))
                        }
                    </ul>

                    <ul className="md:hidden">
                        <div className="relative">
                            <div
                                className="flex w-full items-center justify-between rounded border border-black/30 px-4 py-2 text-sm dark:border-white/30">
                                <div>All</div>
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                     strokeWidth="1.5"
                                     stroke="currentColor" aria-hidden="true" data-slot="icon" className="h-4">
                                    <path strokeLinecap="round" strokeLinejoin="round"
                                          d="m19.5 8.25-7.5 7.5-7.5-7.5"></path>
                                </svg>
                            </div>
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
                <nav><h3 className="hidden text-xs text-neutral-500 md:block dark:text-neutral-400">Sort by</h3>
                    <ul className="hidden md:block">
                        <li className="mt-2 flex text-sm text-black dark:text-white"><a
                            className="w-full hover:underline hover:underline-offset-4"
                            href="/search/headwear?sort=price-asc">Price: Low to high</a></li>
                        <li className="mt-2 flex text-sm text-black dark:text-white"><a
                            className="w-full hover:underline hover:underline-offset-4"
                            href="/search/headwear?sort=price-desc">Price: High to low</a></li>
                    </ul>

                    <ul className="md:hidden">
                        <div className="relative">
                            <div
                                className="flex w-full items-center justify-between rounded border border-black/30 px-4 py-2 text-sm dark:border-white/30">
                                <div>Relevance</div>
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                     strokeWidth="1.5" stroke="currentColor" aria-hidden="true" data-slot="icon"
                                     className="h-4">
                                    <path strokeLinecap="round" strokeLinejoin="round"
                                          d="m19.5 8.25-7.5 7.5-7.5-7.5"></path>
                                </svg>
                            </div>
                        </div>
                    </ul>
                </nav>
            </div>
        </div>
    );
}