package com.example.mike.mp3player.service.library.search;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FolderDao extends SearchDao<Folder> {

    @Override
    @Insert
    void insert(Folder folder);

    @Override
    @Query("SELECT COUNT(*) FROM folders")
    int getCount();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Folder> folders);

    @Override
    @Query("SELECT * FROM folders WHERE name like '%' || :name || '%'")
    List<Folder> query(String name);
}
