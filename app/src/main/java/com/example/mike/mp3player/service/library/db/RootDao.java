package com.example.mike.mp3player.service.library.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mike.mp3player.service.library.LibraryCollection;

import java.util.Comparator;
import java.util.List;

@Dao
public abstract class RootDao extends LibraryCollection {

    public RootDao(String id, String title, String description, Comparator keyComparator, Comparator valueComparator) {
        super(id, title, description, keyComparator, valueComparator);
    }

    @Query("SELECT * FROM :tableName")
    public abstract List<Root> getAll(String tableName);

    @Query("SELECT * FROM root WHERE category IN (:categories)")
    abstract List<Root> loadAllByIds(int[] categories);

    @Query("SELECT * FROM Root WHERE category LIKE :title LIMIT 1")
    abstract List<Root> findByName(int title);

    @Insert
    public abstract void insertAll(Root... roots);

    @Delete
    abstract void delete(Root root);
}
