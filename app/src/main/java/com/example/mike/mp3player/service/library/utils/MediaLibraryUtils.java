package com.example.mike.mp3player.service.library.utils;

import android.os.Environment;

import java.io.File;

public final class MediaLibraryUtils {

    public static String appendToPath(String path, String toAppend) {
        return path + File.separator + toAppend;
    }

    public static File appendToFilePath(File file, String toAPpend) {
        return new File(file.getPath() +  File.separator + toAPpend);
    }

    public static File getRootDirectory(){
        return Environment.getRootDirectory();
    }
}
