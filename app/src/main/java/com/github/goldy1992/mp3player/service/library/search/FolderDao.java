package com.github.goldy1992.mp3player.service.library.search;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FolderDao extends SearchDao<Folder> {

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Folder folder);

    @Override
    @Query("SELECT COUNT(*) FROM folders")
    int getCount();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Folder> folders);

    @Override
    @Query("DELETE FROM folders WHERE id NOT IN ( :ids )")
    void deleteOld(List<String> ids);


    @Override
    @Query("SELECT * FROM folders WHERE value like '%' || :value || '%'")
    List<Folder> query(String value);
}
