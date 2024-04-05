import { IsDarkModeContext } from "components/dark_mode/dark_mode_context";
import React, { useContext } from "react";
import DarkModeIcon from 'assets/dark_mode.svg'

 const DarkModeButton : React.FC = () => {
    const dm = useContext(IsDarkModeContext);
    const icon =   ( 
      <div onClick={() => {dm.setIsDarkMode(!dm.enabled)
          console.log("setting dm false")
           }} > 
          <DarkModeIcon className="stroke-sky-800 hover:cursor-pointer dark:stroke-sky-100 w-6 h-6 mr-2" />
        </div>
    );

    
  
    return (
      <IsDarkModeContext.Provider value={{enabled: dm.enabled, setIsDarkMode: dm.setIsDarkMode}}>
      { icon      }

    </IsDarkModeContext.Provider>
    );
}

export default DarkModeButton