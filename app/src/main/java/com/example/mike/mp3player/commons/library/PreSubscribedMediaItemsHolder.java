package com.example.mike.mp3player.commons.library;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PreSubscribedMediaItemsHolder implements Parcelable {

    private HashMap<LibraryResponse, List<MediaItem>> itemMap;
    private static final ClassLoader KEY_CLASS_LOADER = LibraryResponse.class.getClassLoader();

    public PreSubscribedMediaItemsHolder() {
        itemMap = new HashMap<>();
    }

    protected PreSubscribedMediaItemsHolder(Parcel in) {
        int mapSize = in.readInt();
        itemMap = new HashMap<>();
        for (int i = 0; i < mapSize; i++) {
            LibraryResponse key = in.readParcelable(KEY_CLASS_LOADER);
            List<MediaItem> value = new ArrayList<>();
            in.readTypedList(value, MediaItem.CREATOR);
            itemMap.put(key, value);
            }
        //in.r
    }

    public void addItem(LibraryResponse libraryResponse, List<MediaItem> children) {
        itemMap.put(libraryResponse, children);
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
        int mapSize = itemMap.size();
        dest.writeInt(mapSize);
        for (Map.Entry<LibraryResponse, List<MediaItem>> e : itemMap.entrySet()) {
            LibraryResponse key = e.getKey();
            dest.writeParcelable(key, key.describeContents());
            dest.writeTypedList(e.getValue());
        }
    }

    public Set<LibraryResponse> getKeySet() {
        return itemMap.keySet();
    }

    public HashMap<LibraryResponse, List<MediaItem>> getItemMap() {
        return itemMap;
    }
}
