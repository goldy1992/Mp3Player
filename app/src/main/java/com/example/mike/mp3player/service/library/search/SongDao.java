package com.example.mike.mp3player.service.library.search;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SongDao {

    @Insert
    void insert(Song song);

    @Query("SELECT * FROM songs WHERE title like '%:title%'")
    Song[] query(String title);
}
