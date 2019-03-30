package com.example.mike.mp3player.commons.library;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class LibraryObject implements Parcelable {
    private final Category category;
    private final String id;
    private HashMap<String, String> extras;

    public LibraryObject(@NonNull Category category, @NonNull String id) {
        this.category = category;
        this.id = id;
        this.extras = new HashMap<>();
    }


    protected LibraryObject(Parcel in) {
        this.category = Category.values()[in.readInt()];
        this.id = in.readString();
        this.extras = (HashMap) in.readSerializable();
    }

    public static final Creator<LibraryObject> CREATOR = new Creator<LibraryObject>() {
        @Override
        public LibraryObject createFromParcel(Parcel in) {
            return new LibraryObject(in);
        }

        @Override
        public LibraryObject[] newArray(int size) {
            return new LibraryObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(category.ordinal());
        dest.writeString(id);
        dest.writeSerializable(extras);
    }

    public Category getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(id).append(", category: ").append(category);
        return sb.toString();
    }

    public void putExtra(String key, String value) {
        extras.put(key, value);
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
}
