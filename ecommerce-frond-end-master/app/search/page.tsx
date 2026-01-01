'use client'

import {useEffect, useState} from "react";
import {getAllProductsActive} from "@/lib/graphql/query";
import {Product} from "@/lib/types";
import {Card} from "@/components/card";

export default function SearchPage() {

    const [items, setItems] = useState<Product[]>([]);

    useEffect(() => {
        const getItems = async () => {
            try {
                const itemsData = await getAllProductsActive();
                setItems(itemsData);
            } catch (error) {
                console.error('Error fetching items:', error);
            }
        };

        getItems();
    }, []);

    return (
        <>
            {
                items.map((item: Product) => <Card key={item.id} product={item}/>)
            }
        </>
    );

}