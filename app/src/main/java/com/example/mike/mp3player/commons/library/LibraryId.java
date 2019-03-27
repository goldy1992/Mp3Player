package com.example.mike.mp3player.commons.library;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mike.mp3player.commons.Range;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public class LibraryId implements Parcelable {

    public static final int RESULT_SIZE_NOT_SET = -1;
    private final Category category;
    private final String id;
    private HashMap<String, String> extras;
    private int resultSize = RESULT_SIZE_NOT_SET;
    private Range range;
    private MediaItem mediaItem;
    private List<MediaItem> children;

    public LibraryId(Category category, @NonNull String id) {
        this.category = category;
        this.id = id;
        this.extras = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    protected LibraryId(Parcel in) {
        this.category = Category.values()[in.readInt()];
        this.id = in.readString();
        this.extras = (HashMap) in.readSerializable();
        this.resultSize = in.readInt();
        this.mediaItem = in.readParcelable(MediaItem.class.getClassLoader());
        this.setChildren(in.createTypedArrayList(MediaItem.CREATOR));
    }

    public void putExtra(String key, String value) {
        extras.put(key, value);
    }

    public static final Creator<LibraryId> CREATOR = new Creator<LibraryId>() {
        @Override
        public LibraryId createFromParcel(Parcel in) {
            return new LibraryId(in);
        }

        @Override
        public LibraryId[] newArray(int size) {
            return new LibraryId[size];
        }
    };

    public Category getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(category.ordinal());
        dest.writeString(id);
        dest.writeSerializable(extras);
        dest.writeInt(resultSize);
        dest.writeParcelable(mediaItem, mediaItem.getFlags());
        dest.writeTypedList(children);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(id).append(", category: ").append(category);
        return sb.toString();
    }

    public HashMap<String, String> getExtras() {
        return extras;
    }

    public boolean hasExtra(String key) {
        return extras.containsKey(key);
    }

    public String getExtra(String key) {
        return extras.get(key);
    }

    public int getResultSize() {
        return resultSize;
    }

    public void setResultSize(int resultSize) {
        this.resultSize = resultSize;
    }

    public List<MediaItem> getChildren() {
        return children;
    }

    public void setChildren(List<MediaItem> children) {
        this.children = children;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }
}
