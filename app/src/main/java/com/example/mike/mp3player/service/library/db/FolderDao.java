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
public abstract class FolderDao extends LibraryCollection {

    public FolderDao(String id, String title, String description, Comparator keyComparator, Comparator valueComparator) {
        super(id, title, description, keyComparator, valueComparator);
    }

    @Query("SELECT * FROM folder")
    abstract List<Folder> getAll();

    @Query("SELECT * FROM folder WHERE path IN (:userIds)")
    abstract List<Folder> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Folder WHERE name LIKE :title LIMIT 1")
    abstract List<Folder> findByName(String title);

    @Insert
    public abstract void insertAll(Folder... folders);

    @Delete
    abstract void delete(Folder folder);
}
