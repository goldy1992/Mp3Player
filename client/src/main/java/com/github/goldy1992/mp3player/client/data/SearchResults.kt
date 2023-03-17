package com.github.goldy1992.mp3player.client.data

import com.github.goldy1992.mp3player.client.SearchResult
import com.github.goldy1992.mp3player.client.ui.states.State

data class SearchResults(
    val state : State = State.NOT_LOADED,
    val resultsMap : List<SearchResult> = emptyList()
){
    companion object {
        val NO_RESULTS = SearchResults(State.NO_RESULTS)
    }

    fun hasResults() : Boolean {
        return resultsMap.isNotEmpty()
    }

    fun getResult(index : Int) : SearchResult {
        if (index < 0 || index >= resultsMap.size) {
            return SearchResult.EMPTY
        }
        return resultsMap[index]
    }
}