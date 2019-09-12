package com.example.mike.mp3player.service.library.search;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDao {

    @Insert
    void insert(Song song);

    @Query("SELECT COUNT(*) FROM songs")
    int getCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Song> songs);

    @Query("SELECT * FROM songs WHERE title like '%' || :title || '%'")
    List<Song> query(String title);
}
