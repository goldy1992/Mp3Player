'use client'
import React from "react";


import GithubIcon from "@/app/components/icons/github";
import AppBar from "@/app/components/app_bar/app_bar";
import Link from "next/link";
const Home: React.FC = () => {
  return (
    <div className="flex flex-col min-h-screen">
      <div className="flex-none">
        <AppBar />
      </div>
      <div className="flex flex-col flex-grow items-center justify-between px-24 pt-10 pb-[10px]">
        <div className="flex-1 text-center">An <span className="font-bold italic">open source</span> <span className="text-3xl font-extrabold text-nowrap">MP3 Player</span> for <span className="font-bold">Android</span></div>

        <div className="flex-1"><GithubIcon /></div>
        <div className="flex flex-1 items-center space-x-4 text-sm">
          <div>© 2024 <a className="hover:cursor-pointer" href="https://github.com/goldy1992">goldy1992</a></div>
          <div>|</div>
          <div className="font-semibold"><Link href="/privacy">Privacy</Link></div>
        </div>
      </div>
    </div>

  )
}

export default Home