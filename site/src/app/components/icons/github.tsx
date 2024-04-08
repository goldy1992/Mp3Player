'use client'

import { IsDarkModeContext } from "@/app/components/dark_mode/dark_mode_context";
import React, { useContext } from "react";
import Image from 'next/image'
import { IconProps } from "./dark_mode";

// import GitHubLight from "assets/github-mark/github-mark-white.svg"
// import GitHubDark from "assets/github-mark/github-mark.svg"

const ICON_ALT = "MP3 Player GitHub Repository"

const GithubIcon: React.FC = ({width=48, height=48} : IconProps) => {
    const dm = useContext(IsDarkModeContext);
    const GitHubImage = dm.enabled ? (
        // <GitHubLight width={width} height={height} />
        <Image
        priority
        src="github-mark/github-mark-white.svg"
        alt={ICON_ALT}
        width={width}
        height={height}
        style={{width: 'auto', height: 'auto'}}
  />
    ) : (
        <Image
        priority
        src="github-mark/github-mark.svg"
        alt={ICON_ALT}
        width={width}
        height={height}
        style={{width: 'auto', height: 'auto'}}
  />
        )
  
    return (
        <a href="https://github.com/goldy1992/Mp3Player" title="Github Repository">
    <div className="hover:cursor-pointer">  
    { GitHubImage}
    </div>
    </a>)
    
}

export default GithubIcon