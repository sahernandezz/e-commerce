import type { Metadata } from "next";
import "./globals.css";
import { Navbar } from "@/components/navbar/index";
import { Footer } from "@/components/footer";
import { ThemeProvider } from "next-themes";
import { CartProvider } from "@/context/cart";
import { DrawerProvider } from "@/context/drawer";
import { AuthProvider } from "@/context/auth";
import { LoginModalProvider } from "@/context/login-modal";
import { LoginModal } from "@/components/LoginModal";
import { Drawer } from "@/components/drawer";

export const metadata: Metadata = {
  title: "Ecommerce App",
  description: "Modern ecommerce application built with Next.js 15 and React 19",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className="bg-neutral-50 text-black selection:bg-teal-300 dark:bg-neutral-900 dark:text-white dark:selection:bg-pink-500 dark:selection:text-white">
        <ThemeProvider attribute="class" defaultTheme="system" enableSystem>
          <AuthProvider>
            <LoginModalProvider>
              <CartProvider>
                <DrawerProvider>
                  <Navbar />
                  <LoginModal />
                  <Drawer />
                  <main>{children}</main>
                  <Footer />
                </DrawerProvider>
              </CartProvider>
            </LoginModalProvider>
          </AuthProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}

