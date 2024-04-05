import { IsDarkModeContext } from "./dark_mode_context"
import React, { useContext } from "react";
import { DarkModeIcon, LightModeIcon } from "./icons/icons";

export default function DarkModeButton() {
    const dm = useContext(IsDarkModeContext);
    const lightModeIcon =   ( 
      <div onClick={() => {dm.setIsDarkMode(false)
          console.log("setting dm false")
           }} > 
          <LightModeIcon className="stroke-sky-800 hover:cursor-pointer dark:stroke-sky-100 w-6 h-6 mr-2" />
        </div>
    );

    
    const darkModeIcon =   ( 
      <div onClick={() => {dm.setIsDarkMode(true) }} > 
          <DarkModeIcon className="stroke-sky-800 hover:cursor-pointer dark:stroke-sky-100 w-6 h-6 mr-2"/>
        </div>
    );
    return (
      <IsDarkModeContext.Provider value={{enabled: dm.enabled, setIsDarkMode: dm.setIsDarkMode}}>
      { dm.enabled ? lightModeIcon : darkModeIcon      }

    </IsDarkModeContext.Provider>
    );
}