package com.example.mike.mp3player.service.library.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FolderDao {
    @Query("SELECT * FROM folder")
    List<Folder> getAll();

    @Query("SELECT * FROM folder WHERE path IN (:userIds)")
    List<Folder> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Folder WHERE name LIKE :title LIMIT 1")
    List<Folder> findByName(String title);

    @Insert
    void insertAll(Folder... folders);

    @Delete
    void delete(Folder folder);
}
