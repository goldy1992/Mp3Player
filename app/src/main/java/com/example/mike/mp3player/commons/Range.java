package com.example.mike.mp3player.commons;

import android.os.Parcel;
import android.os.Parcelable;

public final class Range implements Parcelable {

    private final int lower;
    private final int upper;

    public Range(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public Range(Parcel in) {
        this.lower = in.readInt();
        this.upper = in.readInt();
    }

    public static final Creator<Range> CREATOR = new Creator<Range>() {
        @Override
        public Range createFromParcel(Parcel in) {
            return new Range(in);
        }

        @Override
        public Range[] newArray(int size) {
            return new Range[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getLower());
        dest.writeInt(getUpper());
    }

    public static Range create(int lower, int upper) {
        return new Range(lower, upper);
    }

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }

}
