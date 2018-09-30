package com.example.mike.mp3player.service.library;

import android.os.Environment;

import com.example.mike.mp3player.service.library.utils.IsDirectoryFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MediaLibrary {

    private boolean playlistRecursInSubDirectory = false;
    private Collection<String> library;
    private IsDirectoryFilter isDirectoryFilter = new IsDirectoryFilter();

    public void buildMediaLibrary(){
        File rootDirectory = Environment.getRootDirectory();
        String path = rootDirectory.getPath();

        File currentDirectory = rootDirectory;

        List<File> directories = new ArrayList<>();
        List<File> files = new ArrayList<>();

        for (String currentFileString : currentDirectory.list()) {
            if(isDirectoryFilter.accept(currentDirectory, currentFileString)) {
                directories.add(new File(currentDirectory, currentFileString));
            }
            else {
                files.add(new File(currentDirectory, currentFileString));
            }
        }
    }

    public void buildDirectoryPlaylist(File directory)
    {
      //  directory.
    }
}
