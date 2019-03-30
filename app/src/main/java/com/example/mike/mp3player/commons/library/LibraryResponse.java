package com.example.mike.mp3player.commons.library;

import android.os.Parcel;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.Range;

import androidx.annotation.NonNull;

public class LibraryResponse extends LibraryRequest {
    public static final int RESULT_SIZE_NOT_SET = -1;
    public static final int UNKNOWN = -1;

    private int resultSize = RESULT_SIZE_NOT_SET;
    private int totalNumberOfChildren = UNKNOWN;
    private MediaItem mediaItem;

    public LibraryResponse(@NonNull LibraryRequest libraryRequest) {
        super(libraryRequest.getCategory(), libraryRequest.getId());
    }

    @SuppressWarnings("unchecked")
    protected LibraryResponse(Parcel in) {
        super(in);
        this.resultSize = in.readInt();
        this.totalNumberOfChildren = in.readInt();
        this.setMediaItem(in.readParcelable(MediaItem.class.getClassLoader()));
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
        dest.writeInt(resultSize);
        dest.writeInt(totalNumberOfChildren);
        dest.writeParcelable(getMediaItem(), getMediaItem().getFlags());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    public LibraryRequest getNextRequest() {
        Range nextRange = getRange().getNextRange();
        return new LibraryRequest(this, nextRange);
    }

    public boolean hasMoreChildren() {
        return resultSize < totalNumberOfChildren;
    }

    public int getResultSize() {
        return resultSize;
    }

    public void setResultSize(int resultSize) {
        this.resultSize = resultSize;
    }

    public int getTotalNumberOfChildren() {
        return totalNumberOfChildren;
    }

    public void setTotalNumberOfChildren(int totalNumberOfChildren) {
        this.totalNumberOfChildren = totalNumberOfChildren;
    }

    public MediaItem getMediaItem() {
        return mediaItem;
    }

    public void setMediaItem(MediaItem mediaItem) {
        this.mediaItem = mediaItem;
    }
}
