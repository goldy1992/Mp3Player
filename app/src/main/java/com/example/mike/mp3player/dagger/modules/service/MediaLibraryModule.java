package com.example.mike.mp3player.dagger.modules.service;

import android.content.ContentResolver;
import android.content.Context;

import com.example.mike.mp3player.service.library.Category;
import com.example.mike.mp3player.service.library.FolderLibraryCollection;
import com.example.mike.mp3player.service.library.LibraryCollection;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.RootLibraryCollection;
import com.example.mike.mp3player.service.library.SongCollection;
import com.example.mike.mp3player.service.library.db.AppDatabase;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaLibraryModule {

    @Provides
    @Singleton
    public MediaLibrary provideMediaLibrary(Map<Category, LibraryCollection>categoryLibraryCollectionMap) {
        return new MediaLibrary(categoryLibraryCollectionMap);
    }

    @Provides
    @Singleton
    public ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    @Provides
    @Singleton
    public Map<Category, LibraryCollection> provideCategortiesToLibraryCollectionMap(ContentResolver contentResolver) {
        Map<Category, LibraryCollection> map = new EnumMap<>(Category.class);
        RootLibraryCollection rootLibraryCollection = new RootLibraryCollection(contentResolver);
        SongCollection songCollection = new SongCollection(contentResolver);
        FolderLibraryCollection folderLibraryCollection = new FolderLibraryCollection(contentResolver);
        map.put(Category.ROOT, rootLibraryCollection);
        map.put(Category.FOLDERS, folderLibraryCollection);
        map.put(Category.SONGS, songCollection);
        return map;
    }
}
