package com.github.goldy1992.mp3player.service.library.search

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SongDao : SearchDao<Song> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(entity: Song)

    @Query("SELECT COUNT(*) FROM songs")
    override fun count() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertAll(entities: List<Song>)

    @Query("DELETE FROM songs WHERE id NOT IN ( :ids )")
    override fun deleteOld(ids: List<String?>?)

    @Query("SELECT * FROM songs WHERE value like '%' || :value || '%'")
    override fun query(value: String?): List<Song>?
}