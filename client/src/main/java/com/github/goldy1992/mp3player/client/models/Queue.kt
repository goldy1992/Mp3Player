package com.github.goldy1992.mp3player.client.models

data class Queue(
    val items : List<Song>,
    val currentIndex : Int)
{
    companion object {
        val EMPTY = Queue(emptyList(), 0)
    }
}
