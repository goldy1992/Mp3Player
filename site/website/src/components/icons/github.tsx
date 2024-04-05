import { IsDarkModeContext } from "components/dark_mode/dark_mode_context";
import { StaticImage } from "gatsby-plugin-image";
import React, { useContext } from "react";
import GitHubLight from "assets/github-mark/github-mark-white.svg"
import GitHubDark from "assets/github-mark/github-mark.svg"


const GithubIcon: React.FC = () => {
    const dm = useContext(IsDarkModeContext);
    const GitHubImage = dm.enabled ? (<GitHubDark />) : (<GitHubLight />)
  
    return (
    <>    
    { GitHubImage}
    </>)
}

export default GithubIcon