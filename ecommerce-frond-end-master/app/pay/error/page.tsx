import Link from "next/link";

export default function PayErrorPage() {

    return (
        <div className="w-full flex justify-center">
            <div className="p-12">
                <div className="p-6 md:mx-auto">
                    <svg viewBox="0 0 24 24" className="text-red-600 w-16 h-16 mx-auto my-6">
                        <path fill="currentColor"
                              d="M11.001,10H13V7h-2V10zM11,14h2v-2h-2V14z M12,0C5.373,0,0,5.373,0,12s5.373,12,12,12s12-5.373,12-12S18.627,0,12,0z M12,22C6.486,22,2,17.514,2,12S6.486,2,12,2s10,4.486,10,10S17.514,22,12,22z">
                        </path>
                    </svg>
                    <div className="text-center">
                        <h3 className="md:text-2xl text-base text-gray-900 dark:text-gray-100 font-semibold text-center">Payment
                            Error!</h3>
                        <p className="text-gray-600 dark:text-gray-400 my-2">There was an issue processing your
                            payment.</p>
                        <p className="dark:text-gray-300"> Please try again later. </p>
                        <div className="py-10 text-center">
                            <Link href="/"
                                  className="px-12 bg-red-600 hover:bg-red-500 dark:bg-red-700 dark:hover:bg-red-600 text-white font-semibold py-3">
                                GO BACK
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    )
}