import Link from "next/link";

export const Footer = () => {

    return (
        <footer className="text-sm text-neutral-500 dark:text-neutral-400">
            <div
                className="mx-auto flex w-full max-w-7xl flex-col gap-6 border-t border-neutral-200 px-6 py-12 text-sm md:flex-row md:gap-12 md:px-4 min-[1320px]:px-0 dark:border-neutral-700">
                <div>
                    <Link href="/" className="flex items-center gap-2 text-black md:pt-1 dark:text-white">
                        <span className="uppercase">Ecommerce</span>
                    </Link>
                </div>
                <nav>
                    <ul>
                        <li><Link
                            className="block p-2 text-lg underline-offset-4 hover:text-black hover:underline md:inline-block md:text-sm dark:hover:text-neutral-300"
                            href="/">Home</Link></li>
                    </ul>
                </nav>

                <div className="md:ml-auto">
                    <a
                        className="flex h-8 w-max flex-none items-center justify-center rounded-md border border-neutral-200 bg-white text-xs text-black dark:border-neutral-700 dark:bg-black dark:text-white"
                        aria-label="Deploy on Vercel" href="https://github.com/sahernandezz">
                        <hr className="h-full border-r border-neutral-200 dark:border-neutral-700"/>
                        <span className="px-3">GitHub</span></a></div>
            </div>
            <div className="border-t border-neutral-200 py-6 text-sm dark:border-neutral-700">
                <div
                    className="mx-auto flex w-full max-w-7xl flex-col items-center gap-1 px-4 md:flex-row md:gap-0 md:px-4 min-[1320px]:px-0">
                    <p>sahernandezz</p>
                </div>
            </div>
        </footer>
    )
}