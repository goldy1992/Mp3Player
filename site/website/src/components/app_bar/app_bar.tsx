import React from "react";
import Logo from "assets/logo.svg"
import GithubIcon from "components/icons/github";
import DarkModeButton from "components/dark_mode/dark_mode_button";

const ICON_WIDTH = 24
const ICON_HEIGHT = 24

const AppBar : React.FC =() => {
    return (
        <div className="p-4 flex">
            <div className="flex-none">
                <Logo width={48} height={48}/>
            </div>
            <div className="flex-grow" />
            {/* <div className="flex-none">
                <GithubIcon width={ICON_WIDTH} height={ICON_HEIGHT}/>
            </div> */}
            <div className="flex-none">
                <DarkModeButton width={ICON_WIDTH} height={ICON_HEIGHT}/>
            </div>
            {/* <div className="flex-none flex items-center bg-blue-500">
                <span className="bg-red-100 flex-grow align-middle">MP3 Player</span>
            </div> */}
        </div>
    )
}

export default AppBar