package com.example.mike.mp3player.service.library.db;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import androidx.room.TypeConverter;

import com.example.mike.mp3player.commons.library.Category;

import java.util.List;
import java.util.TreeSet;

public class CategoryConverter {
    @TypeConverter
    public static Category intToCategory(int categoryCode) {
        for (Category category : Category.values()) {
            if (categoryCode == category.getRank()) {
                return category;
            }
        }
        return null;
    }

    @TypeConverter
    public static Root categoryToRoot(Category category) {
        Root root = new Root();
        root.category = category;
        return root;
    }

    @TypeConverter
    public static MediaBrowserCompat.MediaItem rootToMediaItem(Root root) {
        String categoryName = root.category.name();
        MediaDescriptionCompat foldersDescription = new MediaDescriptionCompat.Builder()
                .setDescription(categoryName)
                .setTitle(categoryName)
                .setMediaId(categoryName)
                .build();
        return new MediaBrowserCompat.MediaItem(foldersDescription, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);

    }

    @TypeConverter
    public static int categoryToInt(Category category) {
        return category.getRank();
    }

}
