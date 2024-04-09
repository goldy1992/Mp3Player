'use client'
import * as React from "react"
import LegacyPrivacyPolicy from "../components/privacy_policy/legacy"
import AppBar from "@/app/components/app_bar/app_bar"
import FloatingHomeButton from "@/app/components/floating_home_button"
import { MetadataContext } from "../components/metadata_context"
import { DEFAULT_TITLE } from "../constants"

const TITLE = DEFAULT_TITLE + ": Privacy"
const DESCRIPTION = "Privacy Policy for MP3 Player Android App."

const PrivacyPolicy: React.FC = () => {
  const metadataContext = React.useContext(MetadataContext)
  metadataContext.setMetadata({
    title: TITLE,
    description: DESCRIPTION,
    path: "/privacy"
  })
    return (
        <div className="pb-24">
            <div id="home" />
            <AppBar />
            <div >
                <LegacyPrivacyPolicy />
            </div>
            <FloatingHomeButton onClick={() =>
          scrollToFn("home")
        }
         /> 
        </div>
    )
}


const scrollToFn : (arg?: string) => void = (arg) => {
    if (arg != null) {
      let el = document.getElementById(arg);
      if (el != null) {
        el.scrollIntoView(true);
      }
    }
  }



export default PrivacyPolicy