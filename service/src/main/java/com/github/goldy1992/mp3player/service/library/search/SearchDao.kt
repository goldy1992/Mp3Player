package com.github.goldy1992.mp3player.service.library.search

interface SearchDao<T : SearchEntity> {


    fun insert(t: T)
    fun count() : Int
    fun insertAll(items: List<@JvmSuppressWildcards T>)
    fun deleteOld(ids: List<String?>?)
    fun query(query: String?): List<T>?
}