package com.example.mike.mp3player.commons.library;

import android.os.Parcel;
import android.os.Parcelable;

public class LibraryId implements Parcelable {

    private final Category category;
    private final String id;

    public LibraryId(Category category, String id) {
        this.category = category;
        this.id = id;
    }

    protected LibraryId(Parcel in) {
        this.id = in.readString();
        this.category = Category.values()[in.readInt()];
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
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(id).append(", category: ").append(category);
        return sb.toString();
    }
}
