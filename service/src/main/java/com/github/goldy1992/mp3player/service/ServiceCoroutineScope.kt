package com.github.goldy1992.mp3player.service

import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class ServiceCoroutineScope

    @Inject
    constructor(override val coroutineContext: CoroutineContext) : CoroutineScope {

}