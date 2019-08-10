package com.example.mike.mp3player.commons.library;

import android.os.Parcel;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

public class LibraryResponse extends LibraryRequest {
    public static final int RESULT_SIZE_NOT_SET = -1;
    public static final int UNKNOWN = -1;

    private int resultSize = RESULT_SIZE_NOT_SET;
    private int totalNumberOfChildren = UNKNOWN;
    private MediaItem mediaItem;

    public LibraryResponse(@NonNull LibraryRequest libraryRequest) {
        super(libraryRequest.getParentType(), libraryRequest.getId());
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    public boolean hasMoreChildren() {
        return resultSize < totalNumberOfChildren;
    }

    public int getResultSize() {
        return resultSize;
    }

    public void setResultSize(int resultSize) {
        this.resultSize = resultSize;
    }

    public int getTotalNumberOfChildren() {
        return totalNumberOfChildren;
    }

    public void setTotalNumberOfChildren(int totalNumberOfChildren) {
        this.totalNumberOfChildren = totalNumberOfChildren;
    }

    public MediaItem getMediaItem() {
        return mediaItem;
    }

    public void setMediaItem(MediaItem mediaItem) {
        this.mediaItem = mediaItem;
    }
}
