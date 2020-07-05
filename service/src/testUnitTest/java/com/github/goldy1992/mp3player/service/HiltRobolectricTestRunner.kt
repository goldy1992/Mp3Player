package com.github.goldy1992.mp3player.service

import org.robolectric.RobolectricTestRunner

class HiltRobolectricTestRunner
    constructor(testClass : Class<*>)
    : RobolectricTestRunner(testClass) {

    //override fun newApplication()
}