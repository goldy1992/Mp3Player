import Image from 'next/image'

const Logo: React.FC = ({width=48, height=48} : {width?: number, height?: number}) => {
    return (
    
    <Image
        priority
        src="logo_inkscape.svg"
        alt="MP3 Player Logo"
        width={width}
        height={height}
        style={{width: width, height: height}}
  />)
}

export default Logo