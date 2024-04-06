import * as React from "react"
import LegacyPrivacyPolicy from "../components/privacy_policy/legacy"
import AppBar from "components/app_bar/app_bar"
import Layout from "components/layout"



export const PrivacyPolicy : React.FC = () => {
    return (
        <Layout>
        <AppBar />
        <div>
            <LegacyPrivacyPolicy />
        </div>
        </Layout>
    )
}

export default PrivacyPolicy