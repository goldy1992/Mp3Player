'use client'
import { IsDarkModeContext } from "@/app/components/dark_mode/dark_mode_context";
import React, { useContext } from "react";
import DarkModeIcon from "@/app/components/icons/dark_mode"

const DARK_FILL = "#212121"
const LIGHT_FILL = "#DEDEDE"

 const DarkModeButton : React.FC = ({width=48, height=48} : {width?: Number, height?: Number}) => {
    const dm = useContext(IsDarkModeContext);
    const icon =   ( 
      <div className="hover:cursor-pointer" onClick={() => {dm.setIsDarkMode(!dm.enabled)
          console.log("setting dm false")
           }} > 
          <DarkModeIcon width={width} height={height} fill={dm.enabled ? LIGHT_FILL : DARK_FILL} />
        </div>
    );

    
  
    return (
      <IsDarkModeContext.Provider value={{enabled: dm.enabled, setIsDarkMode: dm.setIsDarkMode}}>
      { icon      }

    </IsDarkModeContext.Provider>
    );
}

export default DarkModeButton