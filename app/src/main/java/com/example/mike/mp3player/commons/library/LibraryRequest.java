package com.example.mike.mp3player.commons.library;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class LibraryRequest extends LibraryObject {


    public LibraryRequest(@NonNull Category category, @NonNull String id) {
        super(category, id);
    }

    public LibraryRequest(@NonNull LibraryRequest libraryRequest) {
        super(libraryRequest.getCategory(), libraryRequest.getId());
    }


    @SuppressWarnings("unchecked")
    protected LibraryRequest(Parcel in) {
        super(in);

    }

    public static final Creator<LibraryRequest> CREATOR = new Creator<LibraryRequest>() {
        @Override
        public LibraryRequest createFromParcel(Parcel in) {
            return new LibraryRequest(in);
        }

        @Override
        public LibraryRequest[] newArray(int size) {
            return new LibraryRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

}