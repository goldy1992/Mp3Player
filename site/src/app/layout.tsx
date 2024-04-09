'use client'
import { Inter } from "next/font/google";
import "./globals.css";
import { DarkModeProvider, IsDarkModeContext } from "./components/dark_mode/dark_mode_context";
import { useContext } from "react";
import { TitleContext, TitleProvider } from "./components/title_context";

const inter = Inter({ subsets: ["latin"] });


const RootLayout = ({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) => {
  return (
    <DarkModeProvider>
      <TitleProvider>
        <RootChild>
          {children}
       </RootChild>
      </TitleProvider>
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
  const titleContext = useContext(TitleContext)
  const title = titleContext.title

  return (
    <html lang="en" className={isDarkMode}>
      <head>
        <title>{title}</title>
      </head>
      <body className="bg-neutral-100 dark:bg-neutral-900 text-gray-950 dark:text-gray-50">{children}</body>
    </html>

  )
}
