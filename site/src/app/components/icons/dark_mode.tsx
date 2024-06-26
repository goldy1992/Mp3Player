import React from "react"

export interface IconProps extends React.ComponentProps<any> {
    width?: number,
    height?: number,
    fill?: string
}


const DarkModeIcon = ({width=48, height=48, fill="#212121"}: IconProps) => {
    return (

        <svg width={width} height={height} viewBox="0 0 24 24" version="1.1" xmlns="http://www.w3.org/2000/svg">
            <title>Dark and Light Mode Switch</title>
    <desc>Created with Sketch.</desc>
    <g id="🔍-Product-Icons" stroke="none" strokeWidth="1" fill={fill} fillRule="evenodd">
        <g id="dark_mode" fillRule="nonzero">
            <path d="M12,22 C17.5228475,22 22,17.5228475 22,12 C22,6.4771525 17.5228475,2 12,2 C6.4771525,2 2,6.4771525 2,12 C2,17.5228475 6.4771525,22 12,22 Z M12,20.5 L12,3.5 C16.6944204,3.5 20.5,7.30557963 20.5,12 C20.5,16.6944204 16.6944204,20.5 12,20.5 Z" id="🎨-Color">

</path>
        </g>
    </g>
</svg>

    )
}

export default DarkModeIcon