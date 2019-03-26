package com.example.mike.mp3player.commons.library;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class LibraryId implements Parcelable {

    public static final int RESULT_SIZE_NOT_SET = -1;
    private final Category category;
    private final String id;
    private HashMap<String, String> extras;
    private int resultSize = RESULT_SIZE_NOT_SET;

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
}
