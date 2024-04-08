'use client'
import { Inter } from "next/font/google";
import "./globals.css";
import { DarkModeProvider, IsDarkModeContext } from "./components/dark_mode/dark_mode_context";
import { useContext } from "react";

const inter = Inter({ subsets: ["latin"] });


const RootLayout = ({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) => {
  return (
    <DarkModeProvider>
      <RootChild>
        {children}
      </RootChild>
    </DarkModeProvider>
  );
}
export default RootLayout


const RootChild = ({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) => {
  const darkModeContext = useContext(IsDarkModeContext)
  var darkMode = darkModeContext.enabled
    const isDarkMode = darkMode ? "dark" : ""

  return (
    <html lang="en" className={isDarkMode}>
      <body className="bg-neutral-100 dark:bg-neutral-900 text-gray-950 dark:text-gray-50">{children}</body>
    </html>

  )
}
