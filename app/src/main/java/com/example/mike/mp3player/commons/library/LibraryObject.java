package com.example.mike.mp3player.commons.library;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class LibraryObject implements Parcelable {

    private final Category category;
    private final String id;
    private String title;
    private HashMap<String, String> extras;


    public LibraryObject(Category category, @NonNull String id) {
        this.category = category;
        this.id = id;
        this.setTitle(null);
        this.extras = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    protected LibraryObject(Parcel in) {
        this.category = Category.values()[in.readInt()];
        this.id = in.readString();
        this.setTitle(in.readString());
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
        dest.writeString(getTitle());
        dest.writeSerializable(extras);
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
