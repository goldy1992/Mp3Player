export declare interface AppProps {
    children?: React.ReactNode; // best, accepts everything React can render
    childrenElement?: React.JSX.Element; // A single React element
    style?: React.CSSProperties; // to pass through style props
    onChange?: React.FormEventHandler<HTMLInputElement>; // form events! the generic parameter is the type of event.target
    //  more info: https://react-typescript-cheatsheet.netlify.app/docs/advanced/patterns_by_usecase/#wrappingmirroring
   }