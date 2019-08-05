package com.example.mike.mp3player.service.library.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mike.mp3player.commons.library.Category;

import java.util.List;

@Dao
public interface RootDao {

    @Query("SELECT * FROM root")
    List<Root> getAll();

    @Query("SELECT * FROM root WHERE category IN (:categories)")
    List<Root> loadAllByIds(int[] categories);

    @Query("SELECT * FROM Root WHERE category LIKE :title LIMIT 1")
    List<Root> findByName(int title);

    @Insert
    void insertAll(Root... roots);

    @Delete
    void delete(Root root);
}
