package com.example.mike.mp3player.service.library.db;

import android.support.v4.media.MediaBrowserCompat;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class RootDao implements CategoryDao {

    @Override
    @Query("SELECT * FROM root where category = 1 or category = 2")
    public abstract List<Root> getAll();

    @Query("SELECT * FROM root WHERE category IN (:categories)")
    abstract List<Root> loadAllByIds(int[] categories);

    @Query("SELECT * FROM Root WHERE category LIKE :title LIMIT 1")
    abstract List<Root> findByName(int title);

    @Insert
    public abstract void insertAll(Root... roots);

    @Delete
    abstract void delete(Root root);
}
