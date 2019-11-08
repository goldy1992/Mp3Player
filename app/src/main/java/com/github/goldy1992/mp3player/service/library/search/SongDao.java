package com.github.goldy1992.mp3player.service.library.search;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDao extends SearchDao<Song> {

    @Override
    @Insert
    void insert(Song song);

    @Override
    @Query("SELECT COUNT(*) FROM songs")
    int getCount();

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Song> songs);

    @Override
    @Query("SELECT * FROM songs WHERE value like '%' || :value || '%'")
    List<Song> query(String value);
}
