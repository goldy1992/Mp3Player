import { ReactNode } from "react";
import React from "react";
import { DarkModeProvider } from "./dark_mode/dark_mode_context";
import { AppProps } from "./app_props";


const Layout = ({ children } :  AppProps) => {
    return (
        <DarkModeProvider>
            {children}
        </DarkModeProvider>
    )
}

export default Layout