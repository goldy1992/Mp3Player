package com.github.goldy1992.mp3player.service.library.data.search

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AlbumDao : SearchDao<Album> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(entity: Album)

    @Query("SELECT COUNT(*) FROM albums")
    override fun count() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertAll(entities: List<Album>)

    @Query("DELETE FROM albums WHERE id NOT IN ( :ids )")
    override fun deleteOld(ids: List<String?>?)

    @Query("SELECT * FROM albums WHERE value like '%' || :value || '%'")
    override fun query(value: String?): List<Album>
}