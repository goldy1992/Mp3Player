package com.example.mike.mp3player.service.library.db;

import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mike.mp3player.commons.library.Category;

@Entity
public class Root {

    @PrimaryKey
    @NonNull
    @TypeConverters(CategoryConverter.class)
    public Category category;

}
