package com.example.mike.mp3player.service.library;


import android.os.Environment;

import com.example.mike.mp3player.service.library.utils.IsDirectoryFilter;
import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;
import com.example.mike.mp3player.service.library.utils.MusicFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaLibrary {

    private boolean playlistRecursInSubDirectory = false;
    private Map<File, List<File>> library;
    private MusicFileFilter musicFileFilter = new MusicFileFilter();
    private IsDirectoryFilter isDirectoryFilter = new IsDirectoryFilter();

    public void init() {
        library = new HashMap<>();
    }

    public void buildMediaLibrary(){
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        buildDirectoryPlaylist(externalStorageDirectory);
    }

    private void buildDirectoryPlaylist(File directory)
    {
        List<File> directories = new ArrayList<>();
        List<File> files = new ArrayList<>();

        if (directory.list() != null) {
            for (String currentFileString : directory.list()) {
                if (isDirectoryFilter.accept(directory, currentFileString)) {
                    directories.add(new File(directory, currentFileString));
                } else if (musicFileFilter.accept(directory, currentFileString)) {
                    files.add(new File(directory, currentFileString));
                }
            } // for
        }

        if (!files.isEmpty()) {
            getLibrary().put(directory, files);
        }

        if (!directories.isEmpty())
        {
            for (File directoryToBrowse : directories) {
                buildDirectoryPlaylist(directoryToBrowse);
            }
        }
    }

    public Map<File, List<File>> getLibrary() {
        return library;
    }
}
