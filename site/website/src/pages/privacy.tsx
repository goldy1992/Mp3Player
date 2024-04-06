import * as React from "react"
import LegacyPrivacyPolicy from "../components/privacy_policy/legacy"
import AppBar from "components/app_bar/app_bar"



export const PrivacyPolicy: React.FC = () => {
    return (
        <div className="pb-24">
            <AppBar />
            <div >
                <LegacyPrivacyPolicy />
            </div>
        </div>

    )
}

export default PrivacyPolicy