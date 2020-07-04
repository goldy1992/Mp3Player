package com.github.goldy1992.mp3player.service

import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ServiceScoped
class ServiceCoroutineScope

    @Inject
    constructor(override val coroutineContext: CoroutineContext) : CoroutineScope {

}