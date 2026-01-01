import {Product} from "@/lib/types";
import Link from "next/link";
import {currencyFormatter} from "@/lib/currencyFormatter";

export const Card = ({product}: { product: Product }) => {

    return (
        <li className="aspect-square transition-opacity animate-fadeIn">
            <Link href={`/product/${product.id.toString()}`}
                  className="relative inline-block h-full w-full">
                <div
                    className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black relative border-neutral-200 dark:border-neutral-800">
                    {product.imagesUrl?.[0] && (
                        <img src={product.imagesUrl[0]} alt={product.name}
                             width={0} height={0}
                             sizes="(min-width: 768px) 33vw, (min-width: 640px) 50vw, 100vw"
                             className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-105"/>
                    )}
                    <div className="absolute bottom-0 left-0 flex w-full px-4 pb-4 @container/label">
                        <div
                            className="flex items-center rounded-full border bg-white/70 p-1 text-xs font-semibold text-black backdrop-blur-md dark:border-neutral-800 dark:bg-black/70 dark:text-white">
                            <h3 className="mr-4 line-clamp-2 flex-grow pl-2 leading-none tracking-tight">{product.name}</h3>
                            <p
                                className="flex-none rounded-full bg-blue-600 p-2 text-white">{currencyFormatter(product.price)}<span
                                className="ml-1 inline hidden @[275px]/label:inline"></span></p></div>
                    </div>
                </div>
            </Link>
        </li>
    )
}