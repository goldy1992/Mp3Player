package com.github.goldy1992.mp3player.client.ui.components.smoothline

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.ui.components.equalizer.cards.SmoothLineCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import org.apache.commons.lang3.tuple.MutablePair

val scope = CoroutineScope(Dispatchers.Default)
val testAudio =  flow<List<Float>> {

    val pairs : MutableList<MutablePair<Float, Boolean>> = mutableListOf()
    pairs.add(MutablePair(0f, true))
    pairs.add(MutablePair(23f, true))
    pairs.add(MutablePair(94f, false))
    pairs.add(MutablePair(125f, true))

    while(true) {
        for (p in pairs)  {
            val increasing = p.right
            if (increasing) {
                p.left += 15
                if (p.left > 200f) {
                    p.right = !p.right
                }
            } else {
                p.left -= 23
                if (p.left <= 0f) {
                    p.right = !p.right
                }
            }
        }

        val newList = pairs.map { p -> p.left }.toList()
        emit(newList)
        delay(200L)
    }
}.shareIn(
    scope,
    replay = 1,
    started = SharingStarted.WhileSubscribed()
)

@RequiresApi(Build.VERSION_CODES.N)
@Preview
@Composable
fun DisplaySmoothLineEqualizer() {
   val testAudio by testAudio.collectAsState(listOf(0f, 0f, 0f, 0f))

    SmoothLineCard(
        modifier = Modifier.fillMaxSize(),
        frequencyPhases =  {testAudio},
  //  isPlayingProvider = {true},
    )
}