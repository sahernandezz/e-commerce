'use client'

import {Dialog, DialogBackdrop, DialogPanel, TransitionChild} from '@headlessui/react';
import {useDrawer} from '@/context/drawer';
import {useCart} from '@/context/cart';
import {Cart} from '@/components/drawer/Card';
import {currencyFormatter} from "@/lib/currencyFormatter";
import Link from "next/link";

export const Drawer = () => {
    const {open, setOpen} = useDrawer();
    const {cart, total} = useCart();

    return (
        <Dialog open={open} onClose={setOpen} className="relative z-10">
            <DialogBackdrop
                transition
                className="fixed inset-0 bg-black bg-opacity-20 transition-opacity duration-500 ease-in-out data-[closed]:opacity-0"
            />

            <div className="fixed inset-0 overflow-hidden">
                <div className="absolute inset-0 overflow-hidden">
                    <div className="pointer-events-none fixed inset-y-0 right-0 flex max-w-full sm:pl-10">
                        <DialogPanel
                            transition
                            className="pointer-events-auto relative w-screen max-w-md transform transition duration-500 ease-in-out data-[closed]:translate-x-full sm:duration-700"
                        >
                            <TransitionChild>
                                <div
                                    className="absolute left-0 top-0 -ml-8 flex pr-2 pt-4 duration-500 ease-in-out data-[closed]:opacity-0 sm:-ml-10 sm:pr-4">
                                    <button
                                        type="button"
                                        onClick={() => setOpen(false)}
                                        className="relative rounded-md text-gray-300 hover:text-white focus:outline-none focus:ring-2 focus:ring-white"
                                    >
                                        <span className="absolute -inset-2.5"/>
                                        <span className="sr-only">Close panel</span>
                                        <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            fill="none"
                                            viewBox="0 0 24 24"
                                            strokeWidth="1.5"
                                            stroke="currentColor"
                                            aria-hidden="true"
                                            className="h-6"
                                        >
                                            <path
                                                strokeLinecap="round"
                                                strokeLinejoin="round"
                                                d="M6 18 18 6M6 6l12 12"
                                            />
                                        </svg>
                                    </button>
                                </div>
                            </TransitionChild>

                            <div
                                className="fixed bottom-0 right-0 top-0 flex h-full w-full flex-col overflow-y-auto border-l border-neutral-200 bg-white/80 p-6 text-black backdrop-blur-xl md:w-[450px] dark:border-neutral-700 dark:bg-black/80 dark:text-white">

                                <div className="mb-4 flex items-center justify-between">
                                    <p className="text-lg font-semibold">My Cart</p>
                                    <button aria-label="Close cart" onClick={() => setOpen(false)}>
                                        <div
                                            className="relative flex h-11 w-11 items-center justify-center rounded-md border border-neutral-200 text-black transition-colors dark:border-neutral-700 dark:text-white">
                                            <svg
                                                xmlns="http://www.w3.org/2000/svg"
                                                fill="none"
                                                viewBox="0 0 24 24"
                                                strokeWidth="1.5"
                                                stroke="currentColor"
                                                aria-hidden="true"
                                                data-slot="icon"
                                                className="h-6 transition-all ease-in-out hover:scale-110"
                                            >
                                                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18 18 6M6 6l12 12"/>
                                            </svg>
                                        </div>
                                    </button>
                                </div>

                                {
                                    cart.length === 0 && (
                                        <>

                                            <div
                                                className="mt-20 flex w-full flex-col items-center justify-center overflow-hidden">
                                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                     strokeWidth="1.5" stroke="currentColor" aria-hidden="true"
                                                     data-slot="icon"
                                                     className="h-16">
                                                    <path strokeLinecap="round" strokeLinejoin="round"
                                                          d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437M7.5 14.25a3 3 0 0 0-3 3h15.75m-12.75-3h11.218c1.121-2.3 2.1-4.684 2.924-7.138a60.114 60.114 0 0 0-16.536-1.84M7.5 14.25 5.106 5.272M6 20.25a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0Zm12.75 0a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0Z"></path>
                                                </svg>
                                                <p className="mt-6 text-center text-2xl font-bold">Your cart is empty.</p>
                                            </div>
                                        </>
                                    )
                                }

                                {
                                    cart.length > 0 && (
                                        <>
                                            <div className="flex h-full flex-col justify-between overflow-y-auto p-1">
                                                <ul className="flex-grow overflow-auto py-4">
                                                    {
                                                        cart.map((product) => (
                                                            <Cart key={product.id} productSlice={product}/>
                                                        ))
                                                    }
                                                </ul>
                                            </div>

                                            <div className="py-4 text-sm text-neutral-500 dark:text-neutral-400">
                                                <div
                                                    className="mb-3 flex items-center justify-between border-b border-neutral-200 pb-1 pt-1 dark:border-neutral-700">
                                                    <p>Shipping</p><p className="text-right">Calculated at checkout</p>
                                                </div>
                                                <div
                                                    className="mb-3 flex items-center justify-between border-b border-neutral-200 pb-1 pt-1 dark:border-neutral-700">
                                                    <p>Total</p><p
                                                    className="text-right text-base text-black dark:text-white">$&nbsp;{currencyFormatter(total)}</p>
                                                </div>
                                            </div>

                                            <div>
                                                <Link href="/pay" onClick={() => setOpen(false)}
                                                    className="block w-full rounded-full bg-blue-600 p-3 text-center text-sm font-medium text-white opacity-90 hover:opacity-100"
                                                    type="submit">Proceed to Checkout
                                                </Link>
                                            </div>
                                        </>
                                    )
                                }
                            </div>
                        </DialogPanel>
                    </div>
                </div>
            </div>
        </Dialog>
    );
}
