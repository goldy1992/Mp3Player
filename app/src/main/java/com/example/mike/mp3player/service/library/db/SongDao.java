package com.example.mike.mp3player.service.library.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDao {
    @Query("SELECT * FROM song")
    List<Song> getAll();

    @Query("SELECT * FROM song WHERE uri IN (:userIds)")
    List<Song> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Song WHERE title LIKE :title LIMIT 1")
    List<Song> findByName(String title);

//    @Query("SELECT * FROM Song INNER JOIN folder ON folder.path WHERE folder.path = :path")
//    List<Song> getChildren(String path);


    @Insert
    void insertAll(Song... songs);

    @Delete
    void delete(Song song);
}
