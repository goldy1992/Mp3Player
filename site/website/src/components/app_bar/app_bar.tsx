import React from "react";
import Icon from "assets/icon.svg"
import GithubIcon from "components/icons/github";
import DarkModeButton from "components/dark_mode/dark_mode_button";


const AppBar : React.FC =() => {
    return (
        <div className="p-4 flex">
            <div className="flex-none">
                <Icon width="48" height="48"/>
            </div>
            <div className="flex-none">
                <GithubIcon/>
            </div>
            <div className="flex-none">
                <DarkModeButton/>
            </div>
            <div className="flex-none flex items-center bg-blue-500">
                <span className="bg-red-100 flex-grow align-middle">MP3 Player</span>
            </div>
        </div>
    )
}

export default AppBar