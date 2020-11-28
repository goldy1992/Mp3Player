package com.github.goldy1992.mp3player.service.library.search

interface SearchDao<T : SearchEntity> {


    fun insert(entity: T)
    fun count() : Int
    fun insertAll(entities: List<@JvmSuppressWildcards T>)
    fun deleteOld(ids: List<String?>?)
    fun query(value: String?): List<T>?
}