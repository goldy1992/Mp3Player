package com.github.goldy1992.mp3player.service

import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ComponentScope
class ServiceCoroutineScope

    @Inject
    constructor(override val coroutineContext: CoroutineContext) : CoroutineScope {

}