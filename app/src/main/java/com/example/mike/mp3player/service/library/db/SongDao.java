package com.example.mike.mp3player.service.library.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.service.library.LibraryCollection;

import java.util.Comparator;
import java.util.List;

@Dao
public abstract class SongDao extends LibraryCollection {

    @Query("SELECT * FROM song")
    abstract List<Song> getAll();

    @Query("SELECT * FROM song WHERE uri IN (:userIds)")
    abstract List<Song> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Song WHERE title LIKE :title LIMIT 1")
    abstract List<Song> findByName(String title);

//    @Query("SELECT * FROM Song INNER JOIN folder ON folder.path WHERE folder.path = :path")
//    List<Song> getChildren(String path);


    @Insert
    public abstract void insertAll(Song... songs);

    @Delete
    abstract void delete(Song song);

    @Override
    public Category getRootId() {
        return Category.SONGS;
    }
}
