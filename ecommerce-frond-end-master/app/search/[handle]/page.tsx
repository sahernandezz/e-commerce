'use client'

import {useEffect, useState} from "react";
import {getAllProductsActive, getAllProductsActiveByCategoryId} from "@/lib/graphql/query";
import {Product} from "@/lib/types";
import {Card} from "@/components/card";

export default function SearchByPage ({params}: { params: { handle: string } }) {

    const [items, setItems] = useState([]);

    useEffect(() => {
        const getItems = async () => {
            try {
                const itemsData = await getAllProductsActiveByCategoryId(params.handle);
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