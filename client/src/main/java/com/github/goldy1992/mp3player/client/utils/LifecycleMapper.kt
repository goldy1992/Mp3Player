package com.github.goldy1992.mp3player.client.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Utility extension function to start collecting a flow when the lifecycle is started,
 * and *cancel* the collection on stop, with a custom collector.
 * This is different from `lifecycleScope.launchWhenStarted{ flow.collect{..} }`, in which case the
 * coroutine is just suspended on stop.
 */
inline fun <T, R> Flow<T>.mapWhileResumed(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (value: T) -> R
) {
    ObserverWhileResumedImpl(lifecycleOwner, this, collector)
}

/**
 * Utility extension function on [Flow] to start collecting a flow when the lifecycle is started,
 * and *cancel* the collection on stop.
 */
inline fun <reified T> Flow<T>.collectWhileStartedIn(
    lifecycleOwner: LifecycleOwner
) {
    ObserverWhileResumedImpl(lifecycleOwner, this, {})
}

@PublishedApi
internal class ObserverWhileResumedImpl<T, R>(
    lifecycleOwner: LifecycleOwner,
    private val flow: Flow<T>,
    private val mapper: suspend (value: T) -> R
) : DefaultLifecycleObserver {

    private var job: Job? = null

    override fun onResume(owner: LifecycleOwner) {
        job = owner.lifecycleScope.launchWhenResumed {
            flow.map {
                mapper(it)
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        job?.cancel()
        job = null
    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }
}