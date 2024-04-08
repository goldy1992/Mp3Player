import { createContext, useState } from 'react';
import React from 'react'


export const IsDarkModeContext = createContext({
    enabled: true,
    setIsDarkMode: (dm:boolean) => {}});

export function DarkModeProvider(props:any) {
    const [darkMode, setDarkMode] = useState(true);
    return <IsDarkModeContext.Provider 
    value={{ enabled: darkMode, setIsDarkMode: setDarkMode }}>
        {props.children}
        </IsDarkModeContext.Provider>;
  }

