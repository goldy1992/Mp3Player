import { ReactNode, useContext } from "react";
import React from "react";
import { DarkModeProvider, IsDarkModeContext } from "./dark_mode/dark_mode_context";
import { AppProps } from "./app_props";


const LayoutInner = ({ children } :  AppProps) => {
    const darkModeContext = useContext(IsDarkModeContext)
    var darkMode = darkModeContext.enabled
    const isDarkMode = darkMode ? "dark" : ""
    console.log("dark mode: " + darkMode)
    const rootClassName = "scroll-smooth bg-neutral-100 dark:bg-neutral-900"
    return (
  
        <div className={isDarkMode}>
            <div className={rootClassName}>
               {children}
            </div>
        </div>
  
    )
}

const Layout = ({ children } :  AppProps) => {
    return (
        <DarkModeProvider>
           <LayoutInner>
                {children}
            </LayoutInner>
        </DarkModeProvider>
    )
}
export default Layout