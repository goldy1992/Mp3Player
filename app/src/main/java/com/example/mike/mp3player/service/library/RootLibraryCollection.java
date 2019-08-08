package com.example.mike.mp3player.service.library;

import android.content.ContentResolver;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.library.LibraryObject;

import java.util.List;
import java.util.TreeSet;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByCategory;

public class RootLibraryCollection extends LibraryCollection {

    public static final String ID = Constants.CATEGORY_ROOT_ID;


    public RootLibraryCollection(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    public String[] getProjection() {
        return new String[0];
    }

    @Override
    public TreeSet<MediaItem> getChildren(LibraryObject id) {
        return null;
    }

    @Override
    public TreeSet<MediaItem> getAllChildren() {
        TreeSet<MediaItem> rootMediaItems = new TreeSet<>(compareRootMediaItemsByCategory
        );
        for (Category c : Category.values()) {
            if (c.equals(Category.SONGS) || c.equals(Category.FOLDERS)) {
                rootMediaItems.add(createRootItem(c));
            }
        }
        return rootMediaItems;
    }


    @Override
    public List<MediaItem> search(String query) {
        return null;
    }

    @Override
    public Category getRootId() {
        return null;
    }

    /**
     * @return a root category item
     */
    public static MediaItem createRootItem(Category category) {
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setDescription(category.getDescription())
                .setTitle(category.getTitle())
                .setMediaId(category.name())
                .build();
        return new MediaItem(mediaDescriptionCompat, 0);
    }

}
