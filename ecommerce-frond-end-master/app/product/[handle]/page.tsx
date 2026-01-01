'use client'

import {useEffect, useState} from "react";
import {getProductById} from "@/lib/graphql/query";
import {ProductAddCart} from "@/lib/types";
import {currencyFormatter} from "@/lib/currencyFormatter";
import {useCart} from "@/context/cart";
import {useDrawer} from "@/context/drawer";

export default function ProductPage({params}: { params: { handle: string } }) {
    const { addToCart } = useCart();
    const { setOpen } = useDrawer();

    const [item, setItem] = useState<{
        imagesUrl: string[],
        colors: string[],
        sizes: string[],
        id: number,
        name: string,
        description: string,
        price: number
    }>({
        imagesUrl: [],
        colors: [],
        sizes: [],
        id: 0,
        name: '',
        description: '',
        price: 0
    });
    const [image, setImage] = useState<{ image: string, id: number }>({
        image: '',
        id: 0
    });
    const [selectedColor, setSelectedColor] = useState<string>('');
    const [selectedSize, setSelectedSize] = useState<string>('');

    useEffect(() => {
        const getItems = async () => {
            try {
                const itemData = await getProductById(params.handle);
                setItem(itemData);
                setImage({image: itemData.imagesUrl[0], id: 0});
            } catch (error) {
                console.error('Error fetching items:', error);
            }
        };

        getItems();
    }, []);

    useEffect(() => {
        setItem({...item, colors: item?.colors.filter(color => color)});
    }, [selectedColor]);

    useEffect(() => {
        setItem({...item, sizes: item?.sizes.filter(size => size)});
    }, [selectedSize]);

    const addProductToCart = async () => {
        const product: ProductAddCart = {
            id: item.id,
            name: item?.name,
            price: item?.price,
            imagesUrl: item.imagesUrl,
            quantity: 1,
            size: selectedSize,
            color: selectedColor
        };
        addToCart(product);
        setOpen(true);
    };

    return (
        <div className="mx-auto max-w-screen-2xl px-4">
            <div
                className="flex flex-col rounded-lg border border-neutral-200 bg-white p-8 md:p-12 lg:flex-row lg:gap-8 dark:border-neutral-800 dark:bg-black">
                <div className="h-full w-full basis-full lg:basis-4/6">
                    <div>
                        <div className="relative aspect-square h-full max-h-[550px] w-full overflow-hidden">
                            {image?.image && (
                                <img src={image.image} alt={item?.name || ''} width={0} height={0} className="h-full w-full object-contain"
                                     sizes="(min-width: 1024px) 66vw, 100vw"/>
                            )}

                            <div className="absolute bottom-[15%] flex w-full justify-center">
                                {
                                    item?.imagesUrl.length > 1 && (
                                        <div
                                            className="mx-auto flex h-11 items-center rounded-full border border-white bg-neutral-50/80 text-neutral-500 backdrop-blur dark:border-black dark:bg-neutral-900/80">

                                            <button onClick={() => setImage({
                                                image: image?.id - 1 < 0 ? item?.imagesUrl[item?.imagesUrl.length - 1] : item?.imagesUrl[image?.id - 1],
                                                id: image?.id - 1 < 0 ? item?.imagesUrl.length - 1 : image?.id - 1
                                            })}
                                                    aria-label="Previous product image"
                                                    className="h-full px-6 transition-all ease-in-out hover:scale-110 hover:text-black dark:hover:text-white flex items-center justify-center">
                                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                     strokeWidth="1.5" stroke="currentColor" aria-hidden="true"
                                                     data-slot="icon" className="h-5">
                                                    <path strokeLinecap="round" strokeLinejoin="round"
                                                          d="M10.5 19.5 3 12m0 0 7.5-7.5M3 12h18"></path>
                                                </svg>
                                            </button>

                                            <div className="mx-1 h-6 w-px bg-neutral-500"></div>

                                            <button onClick={() => setImage({
                                                image: image?.id + 1 === item?.imagesUrl.length ? item?.imagesUrl[0] : item?.imagesUrl[image?.id + 1],
                                                id: image?.id + 1 === item?.imagesUrl.length ? 0 : image?.id + 1
                                            })}
                                                    aria-label="Next product image"
                                                    className="h-full px-6 transition-all ease-in-out hover:scale-110 hover:text-black dark:hover:text-white flex items-center justify-center">
                                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                                     strokeWidth="1.5" stroke="currentColor" aria-hidden="true"
                                                     data-slot="icon" className="h-5">
                                                    <path strokeLinecap="round" strokeLinejoin="round"
                                                          d="M13.5 4.5 21 12m0 0-7.5 7.5M21 12H3"></path>
                                                </svg>
                                            </button>
                                        </div>)
                                }
                            </div>
                        </div>

                        <ul className="my-12 flex items-center justify-center gap-2 overflow-auto py-1 lg:mb-0">
                            {
                                item?.imagesUrl.map((imageUrl, index) => (
                                    // eslint-disable-next-line react/jsx-key
                                    <li className="h-20 w-20">
                                        <button onClick={() => setImage({image: imageUrl, id: index})}
                                                aria-label="Select product image" className="h-full w-full">
                                            <div
                                                className="group flex h-full w-full items-center justify-center overflow-hidden rounded-lg border bg-white hover:border-blue-600 dark:bg-black border-2 border-blue-600">
                                                {imageUrl && (
                                                    <img alt={item?.name || 'Product image'} loading="lazy" width="80"
                                                         height="80"
                                                         decoding="async" data-nimg="1"
                                                         src={imageUrl}
                                                         className="relative h-full w-full object-contain transition duration-300 ease-in-out group-hover:scale-105"/>
                                                )}
                                            </div>
                                        </button>
                                    </li>
                                ))
                            }
                        </ul>
                    </div>
                </div>

                <div className="basis-full lg:basis-2/6">
                    <div className="mb-6 flex flex-col border-b pb-6 dark:border-neutral-700"><h1
                        className="mb-2 text-5xl font-medium">{item?.name}</h1>
                        <div className="mr-auto w-auto rounded-full bg-blue-600 p-2 text-sm text-white">
                            <p>{currencyFormatter(item?.price)}</p></div>
                    </div>
                    <div>
                        {
                            item?.colors.length > 0 && (
                                <dl className="mb-8">
                                    <dt className="mb-4 text-sm uppercase tracking-wide">Color</dt>
                                    <dd className="flex flex-wrap gap-3">
                                        {
                                            item?.colors.map((color) => (
                                                // eslint-disable-next-line react/jsx-key
                                                <button onClick={() => setSelectedColor(color)}
                                                        title="Size 1"
                                                        disabled={selectedColor === color}
                                                        className="flex min-w-[48px] items-center justify-center ch-full border bg-neutral-100 px-2 py-1 text-sm dark:border-neutral-800 dark:bg-neutral-900 ring-1 ring-transparent transition duration-300 ease-in-out hover:ring-blue-600 disabled:ring-blue-600 disabled:cursor-default">{color}
                                                </button>
                                            ))
                                        }
                                    </dd>
                                </dl>
                            )
                        }

                        {
                            item?.sizes.length > 0 && (
                                <dl className="mb-8">
                                    <dt className="mb-4 text-sm uppercase tracking-wide">Size</dt>
                                    <dd className="flex flex-wrap gap-3">
                                        {
                                            item?.sizes.map((size) => (
                                                // eslint-disable-next-line react/jsx-key
                                                <button onClick={() => setSelectedSize(size)}
                                                        aria-disabled="false"
                                                        title="Size 1"
                                                        disabled={selectedSize === size}
                                                        className="flex min-w-[48px] items-center justify-center rounded-full border bg-neutral-100 px-2 py-1 text-sm dark:border-neutral-800 dark:bg-neutral-900 ring-1 ring-transparent transition duration-300 ease-in-out hover:ring-blue-600 disabled:ring-blue-600 disabled:cursor-default">{size}
                                                </button>
                                            ))
                                        }
                                    </dd>
                                </dl>
                            )
                        }
                    </div>
                    <div
                        className="prose mx-auto max-w-6xl text-base leading-7 text-black prose-headings:mt-8 prose-headings:font-semibold prose-headings:tracking-wide prose-headings:text-black prose-h1:text-5xl prose-h2:text-4xl prose-h3:text-3xl prose-h4:text-2xl prose-h5:text-xl prose-h6:text-lg prose-a:text-black prose-a:underline hover:prose-a:text-neutral-300 prose-strong:text-black prose-ol:mt-8 prose-ol:list-decimal prose-ol:pl-6 prose-ul:mt-8 prose-ul:list-disc prose-ul:pl-6 dark:text-white dark:prose-headings:text-white dark:prose-a:text-white dark:prose-strong:text-white mb-6 text-sm leading-tight dark:text-white/[60%]">
                        <p>{item?.description}</p>
                    </div>
                    <div>
                        <button aria-label="Please select an option" onClick={addProductToCart} disabled={item?.colors.length > 0 && selectedColor === '' || item?.sizes.length > 0 && selectedSize === ''}
                                className="relative flex w-full items-center justify-center rounded-full bg-blue-600 p-4 tracking-wide text-white hover:bg-blue-700 disabled:opacity-90 disabled:hover:bg-blue-400 disabled:bg-blue-400">
                            <div className="absolute left-0 ml-4">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                     strokeWidth="1.5" stroke="currentColor" aria-hidden="true" data-slot="icon"
                                     className="h-5">
                                    <path strokeLinecap="round" strokeLinejoin="round"
                                          d="M12 4.5v15m7.5-7.5h-15"></path>
                                </svg>
                            </div>
                            Add To Cart
                        </button>
                        <p aria-live="polite" className="sr-only" role="status"></p></div>
                </div>
            </div>
        </div>
    );
}