package com.example.mike.mp3player.commons.library;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PreSubscribedMediaItemsHolder implements Parcelable {

    private HashMap<LibraryId, List<MediaItem>> itemMap;

    public PreSubscribedMediaItemsHolder() {
        itemMap = new HashMap<>();
    }
    protected PreSubscribedMediaItemsHolder(Parcel in) {
        itemMap = (HashMap) in.readSerializable();
        //in.r
    }

    public void addItem(LibraryId libraryId, List<MediaItem> children) {
        itemMap.put(libraryId, children);
    }



    public static final Creator<PreSubscribedMediaItemsHolder> CREATOR = new Creator<PreSubscribedMediaItemsHolder>() {
        @Override
        public PreSubscribedMediaItemsHolder createFromParcel(Parcel in) {
            return new PreSubscribedMediaItemsHolder(in);
        }

        @Override
        public PreSubscribedMediaItemsHolder[] newArray(int size) {
            return new PreSubscribedMediaItemsHolder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeSerializable(itemMap);
    }
}
