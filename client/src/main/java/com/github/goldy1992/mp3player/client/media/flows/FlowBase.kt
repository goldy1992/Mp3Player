package com.github.goldy1992.mp3player.client.media.flows

import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Base class for which all Media Flows Should Extend
 * @constructor Creates an instance of [FlowBase]
 * @param scope The coroutine scope to operate the flow on
 * @param onCollect The lambda to invoke when the flow is collected from.
 */
abstract class FlowBase<F>(
        protected val scope: CoroutineScope,
        protected val onCollect: suspend (F) -> Unit
    ) : LogTagger

{

    /**
     * This method is called to initialise the flow collection post-constructor.
     * @param flow The flow that should be returned from [FlowBase.getFlow].
     */
    protected fun initFlow(flow : Flow<F>) {
        scope.launch {
            flow.collect {
                onCollect(it)
            }
        }
    }

    /**
     * @return The [Flow] which should be implemented in the subclass.
     */
    abstract fun getFlow() : Flow<F>


}