package com.github.goldy1992.mp3player.client.media.flows

import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class FlowBase<F>(
    protected  val scope: CoroutineScope,
    protected val onCollect: suspend (F) -> Unit
) : LogTagger

{

    protected fun initFlow(flow : Flow<F>) {
        scope.launch {
            flow.collect {
                onCollect(it)
            }
        }
    }

    abstract fun getFlow() : Flow<F>


}