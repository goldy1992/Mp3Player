package com.example.mike.mp3player.service.library.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mike.mp3player.commons.library.Category;

@Entity
public class Root extends CategoryEntity {

    @PrimaryKey
    @NonNull
    @TypeConverters(CategoryConverter.class)
    public Category category;

}
