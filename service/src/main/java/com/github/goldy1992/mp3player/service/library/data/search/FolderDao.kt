package com.github.goldy1992.mp3player.service.library.data.search

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FolderDao : SearchDao<Folder> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(entity: Folder)

    @Query("SELECT COUNT(*) FROM folders")
    override fun count() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertAll(entities: List<Folder>)

    @Query("DELETE FROM folders WHERE id NOT IN ( :ids )")
    override fun deleteOld(ids: List<String?>?)

    @Query("SELECT * FROM folders WHERE value like '%' || :value || '%'")
    override fun query(value: String?): List<Folder>?
}