import { createContext, useState } from 'react';
import React from 'react'

export const DEFAULT_TITLE = 'MP3 Player'

export const TitleContext = createContext({
    title: 'MP3 Player',
    setTitle: (title: string) => {}});

export function TitleProvider(props:any) {
    const [title, setTitle] = useState(DEFAULT_TITLE);
    return <TitleContext.Provider 
    value={{ title: title, setTitle: setTitle }}>
        {props.children}
        </TitleContext.Provider>;
  }

