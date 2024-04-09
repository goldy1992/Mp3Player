'use client'
import "./globals.css";
import { DarkModeProvider, IsDarkModeContext } from "./components/dark_mode/dark_mode_context";
import { useContext } from "react";
import { MetadataContext, MetadataProvider } from "./components/metadata_context";


const RootLayout = ({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) => {
  return (
    <DarkModeProvider>
      <MetadataProvider>
        <RootChild>
          {children}
       </RootChild>
      </MetadataProvider>
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
  const metadataContext = useContext(MetadataContext)
  const metadata = metadataContext.metadata
  const url = "https://goldy1992.github.io/Mp3Player" + (!metadata.path ? "" : metadata.path) 
  return (
    <html lang="en" className={isDarkMode}>
      <head>
        <title>{metadata.title}</title>
        <meta property="og:image" content="logo_inkscape.svg" />
        <meta property="og:image:type" content="image/svg+xml" />
        <meta property="og:image:alt" content="MP3 Player" />
        <meta property="og:site_name" content="MP3 Player" />
        <meta property="og:title" content={metadata.title} />
        <meta property="og:type" content="website" />
        <meta property="og:description" content={metadata.description} />
        <meta property="og:url" content={url} />
      </head>
      <body className="bg-neutral-100 dark:bg-neutral-900 text-gray-950 dark:text-gray-50">{children}</body>
    </html>

  )
}
