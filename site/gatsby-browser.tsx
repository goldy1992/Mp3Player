import "./src/styles/global.css"


import { useContext } from "react";
import React from "react";
import { DarkModeProvider, IsDarkModeContext } from "./src/components/dark_mode/dark_mode_context";
import { AppProps } from "./src/components/app_props";



const LayoutInner = ({ children } :  AppProps) => {
    const darkModeContext = useContext(IsDarkModeContext)
    var darkMode = darkModeContext.enabled
    const isDarkMode = darkMode ? "dark" : ""
    console.log("dark mode: " + darkMode)
    return (
  
        <div className={isDarkMode}>
            <div className="bg-neutral-100 dark:bg-neutral-900 text-gray-950 dark:text-gray-50">

               {children}
    
            </div>
        </div>
  
    )
}


export const wrapRootElement = ({ element }) => {
    return (
        <DarkModeProvider>
           <LayoutInner>
                {element}
            </LayoutInner>
        </DarkModeProvider>
    )
}
