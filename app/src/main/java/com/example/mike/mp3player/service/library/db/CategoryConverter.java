package com.example.mike.mp3player.service.library.db;

import androidx.room.TypeConverter;

import com.example.mike.mp3player.commons.library.Category;

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
    public static int categoryToInt(Category category) {
        return category.getRank();
    }

}
