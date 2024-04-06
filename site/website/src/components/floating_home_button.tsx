import React from 'react'
import ArrowUpIcon from 'src/assets/arrow_top.svg'

export default function FloatingHomeButton(
    {onClick} : 
    {onClick? : ()=> void}) {
    return (
        <div className="fixed bottom-3 end-3 dark:border-white border-black border rounded-full bg-neutral-100 dark:bg-neutral-900 items-center p-4 dark:hover:bg-neutral-700 hover:drow-shadow-md hover:bg-neutral-300 hover:cursor-pointer" onClick={() => {if (onClick != null) onClick()}}>
       <svg className="fill-neutral-900 dark:fill-neutral-100" xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M440-160v-487L216-423l-56-57 320-320 320 320-56 57-224-224v487h-80Z"/></svg>
        </div>
    )
}