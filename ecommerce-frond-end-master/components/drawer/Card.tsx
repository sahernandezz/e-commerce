'use client'

import {ProductAddCart} from "@/lib/types";
import {currencyFormatter} from "@/lib/currencyFormatter";
import {useCart} from "@/context/cart";
import Link from "next/link";

export const Cart = ({productSlice}: { productSlice: ProductAddCart }) => {
    const {subtractQuantity, removeFromCart, addQuantity} = useCart();

    return (
        <li className="flex w-full flex-col border-b border-neutral-300 dark:border-neutral-700">
            <div className="relative flex w-full flex-row justify-between px-1 py-4">
                <div className="absolute z-40 -ml-1 -mt-2">
                    <div>
                        <button onClick={() => removeFromCart(productSlice.id.toString())}
                                type="submit" aria-label="Remove cart item"
                                className="flex h-[24px] w-[24px] items-center justify-center rounded-full bg-neutral-500">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none"
                                 viewBox="0 0 24 24" strokeWidth="1.5"
                                 stroke="currentColor" aria-hidden="true"
                                 data-slot="icon"
                                 className="mx-[1px] h-4 w-4 text-white dark:text-black">
                                <path strokeLinecap="round" strokeLinejoin="round"
                                      d="M6 18 18 6M6 6l12 12"></path>
                            </svg>
                        </button>
                        <p aria-live="polite" className="sr-only" role="status"></p>
                    </div>
                </div>

                <div className="flex flex-row">
                    <div
                        className="relative h-16 w-16 overflow-hidden rounded-md border border-neutral-300 bg-neutral-300 dark:border-neutral-700 dark:bg-neutral-900 dark:hover:bg-neutral-800">
                        {productSlice.imagesUrl?.[0] && (
                            <img alt={productSlice.name} loading="lazy" width="64" height="64"
                                 decoding="async" data-nimg="1"
                                 className="h-full w-full object-cover"
                                 src={productSlice.imagesUrl[0]}/>
                        )}
                    </div>
                    <Link className="z-30 ml-2 flex flex-row space-x-4"
                          href={`/product/${productSlice.id}`}>
                        <div className="flex flex-1 flex-col text-base"><span
                            className="leading-tight">{productSlice.name}</span>
                            <p className="text-sm text-neutral-500 dark:text-neutral-400">{productSlice.color}</p>
                            <p className="text-sm text-neutral-500 dark:text-neutral-400">{productSlice.size}</p>
                        </div>
                    </Link>
                </div>

                <div className="flex h-16 flex-col justify-between"><p
                    className="flex justify-end space-y-2 text-right text-sm">$&nbsp;{currencyFormatter(productSlice.price * productSlice.quantity)}</p>
                    <div
                        className="ml-auto flex h-9 flex-row items-center rounded-full border border-neutral-200 dark:border-neutral-700">
                        <div>
                            <button onClick={() => subtractQuantity(productSlice.id.toString())}
                                    type="submit" aria-label="Reduce item quantity"
                                    className="ease flex h-full min-w-[36px] max-w-[36px] flex-none items-center justify-center rounded-full p-2 transition-all duration-200 hover:border-neutral-800 hover:opacity-80 ml-auto">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none"
                                     viewBox="0 0 24 24" strokeWidth="1.5"
                                     stroke="currentColor" aria-hidden="true"
                                     data-slot="icon"
                                     className="h-4 w-4 dark:text-neutral-500">
                                    <path strokeLinecap="round" strokeLinejoin="round"
                                          d="M5 12h14"></path>
                                </svg>
                            </button>
                            <p aria-live="polite" className="sr-only" role="status"></p>
                        </div>

                        <p className="w-6 text-center"><span
                            className="w-full text-sm">{productSlice.quantity}</span>
                        </p>

                        <div>
                            <button onClick={() => addQuantity(productSlice.id.toString())}
                                    type="submit" aria-label="Increase item quantity"
                                    className="ease flex h-full min-w-[36px] max-w-[36px] flex-none items-center justify-center rounded-full p-2 transition-all duration-200 hover:border-neutral-800 hover:opacity-80">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none"
                                     viewBox="0 0 24 24" strokeWidth="1.5"
                                     stroke="currentColor" aria-hidden="true"
                                     data-slot="icon"
                                     className="h-4 w-4 dark:text-neutral-500">
                                    <path strokeLinecap="round" strokeLinejoin="round"
                                          d="M12 4.5v15m7.5-7.5h-15"></path>
                                </svg>
                            </button>
                            <p aria-live="polite" className="sr-only" role="status"></p>
                        </div>
                    </div>
                </div>
            </div>
        </li>
    )
}