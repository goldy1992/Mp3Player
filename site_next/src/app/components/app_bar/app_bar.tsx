'use client'
import React from "react";
import Logo from "@/app/components/icons/logo"
import DarkModeButton from "@/app/components/dark_mode/dark_mode_button";
import Link from "next/link";

const ICON_WIDTH = 24
const ICON_HEIGHT = 24

const AppBar : React.FC =() => {
    return (
        <div className="p-4 flex">
            <div className="flex-none">
            <Link href="/">
                <Logo/>
                </Link>
            </div>
            <div className="flex-grow" />
            {/* <div className="flex-none">
                <GithubIcon width={ICON_WIDTH} height={ICON_HEIGHT}/>
            </div> */}
            <div className="flex-none flex items-center">
                <DarkModeButton width={ICON_WIDTH} height={ICON_HEIGHT}/>
            </div>
            {/* <div className="flex-none flex items-center bg-blue-500">
                <span className="bg-red-100 flex-grow align-middle">MP3 Player</span>
            </div> */}
        </div>
    )
}

export default AppBar