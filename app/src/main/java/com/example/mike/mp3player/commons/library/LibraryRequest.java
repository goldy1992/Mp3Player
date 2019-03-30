package com.example.mike.mp3player.commons.library;

import android.os.Parcel;

import com.example.mike.mp3player.commons.Range;

import androidx.annotation.NonNull;

import static com.example.mike.mp3player.commons.Constants.DEFAULT_RANGE;

public class LibraryRequest extends LibraryObject {

    private Range range = DEFAULT_RANGE;

    public LibraryRequest(Category category, @NonNull String id) {
        super(category, id);
    }

    @SuppressWarnings("unchecked")
    protected LibraryRequest(Parcel in) {
        super(in);
        this.range = in.readParcelable(Range.class.getClassLoader());
    }

    public void setNext() {
        setRange(range.getNextRange());
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
        dest.writeParcelable(range, range.describeContents());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }
}