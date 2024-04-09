import { createContext, useState } from 'react';
import React from 'react'
import { DEFAULT_METADATA, DEFAULT_TITLE, IMetadata } from '../constants';


export const MetadataContext = createContext({
    metadata: DEFAULT_METADATA,
    setMetadata: (metadata: IMetadata) => {}});

export function MetadataProvider(props:any) {
    const [metadata, setMetadata] = useState(DEFAULT_METADATA);
    return <MetadataContext.Provider 
    value={{ metadata: metadata, setMetadata: setMetadata }}>
        {props.children}
        </MetadataContext.Provider>;
  }

