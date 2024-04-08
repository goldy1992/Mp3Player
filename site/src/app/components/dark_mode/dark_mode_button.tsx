'use client'
import { IsDarkModeContext } from "@/app/components/dark_mode/dark_mode_context";
import React, { useContext } from "react";
import DarkModeIcon, { IconProps } from "@/app/components/icons/dark_mode"

const DARK_FILL = "#212121"
const LIGHT_FILL = "#DEDEDE"

 const DarkModeButton = ({width=48, height=48}: IconProps) => {
    const dm = useContext(IsDarkModeContext);
    const fill = dm.enabled ? LIGHT_FILL : DARK_FILL
    
    const icon =   ( 
      <div className="hover:cursor-pointer" onClick={() => {dm.setIsDarkMode(!dm.enabled)}} >
            
            
          <DarkModeIcon width={width} height={height} fill={fill} />
        </div>
    );

    
  
    return (
      <IsDarkModeContext.Provider value={{enabled: dm.enabled, setIsDarkMode: dm.setIsDarkMode}}>
      { icon      }

    </IsDarkModeContext.Provider>
    );
}

export default DarkModeButton