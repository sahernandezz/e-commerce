'use client'

import {Suspense, use, useEffect, useState} from "react";
import {useSearchParams} from "next/navigation";
import {getAllProductsActiveByCategoryId} from "@/lib/graphql/query";
import {Product} from "@/lib/types";
import {Card} from "@/components/card";

function SearchByContent({handle}: {handle: string}) {
    const searchParams = useSearchParams();
    const sort = searchParams.get('sort');
    const [items, setItems] = useState<Product[]>([]);

    useEffect(() => {
        const getItems = async () => {
            try {
                const itemsData = await getAllProductsActiveByCategoryId(handle);
                setItems(itemsData);
            } catch (error) {
                console.error('Error fetching items:', error);
            }
        };

        getItems();
    }, [handle]);

    // Ordenar productos según el parámetro sort
    const sortedItems = [...items].sort((a, b) => {
        if (sort === 'price-asc') {
            return a.price - b.price;
        } else if (sort === 'price-desc') {
            return b.price - a.price;
        }
        return 0; // Sin ordenamiento específico
    });

    return (
        <>
            {
                sortedItems.map((item: Product) => <Card key={item.id} product={item}/>)
            }
        </>
    );
}

export default function SearchByPage ({params}: { params: Promise<{ handle: string }> }) {
    const { handle } = use(params);

    return (
        <Suspense fallback={<div className="col-span-full flex justify-center py-8"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div></div>}>
            <SearchByContent handle={handle} />
        </Suspense>
    );
}