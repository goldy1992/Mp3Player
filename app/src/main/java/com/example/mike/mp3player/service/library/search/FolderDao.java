package com.example.mike.mp3player.service.library.search;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FolderDao {

    @Insert
    void insert(Folder folder);

    @Query("SELECT COUNT(*) FROM folders")
    int getCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Folder> folders);

    @Query("SELECT * FROM folders WHERE name like '%' || :name || '%'")
    List<Folder> query(String name);
}
