import { IsDarkModeContext } from "components/dark_mode/dark_mode_context";
import React, { useContext } from "react";
import GitHubLight from "assets/github-mark/github-mark-white.svg"
import GitHubDark from "assets/github-mark/github-mark.svg"


const GithubIcon: React.FC = ({width=48, height=48} : {width?: Number, height?: Number}) => {
    const dm = useContext(IsDarkModeContext);
    const GitHubImage = dm.enabled ? (
        <GitHubLight width={width} height={height} />
    ) : (
        <GitHubDark width={width} height={height} />
        )
  
    return (
    <div className="hover:cursor-pointer">  
    { GitHubImage}
    </div>)
}

export default GithubIcon